package com.jyg.nomina.controllers;

import com.jyg.nomina.models.DayJob;
import com.jyg.nomina.models.PaymentPeriod;
import com.jyg.nomina.models.User;
import com.jyg.nomina.services.DayJobService;
import com.jyg.nomina.services.PaymentPeriodService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;

import static com.jyg.nomina.util.globalvar.InitSessionAdmin.USUARIO_GESTOR_USUAR;
import static com.jyg.nomina.util.globalvar.VarDayJob.*;
import static com.jyg.nomina.util.globalvar.VarPaymentPeriod.*;
import static java.util.stream.Collectors.toList;

@AllArgsConstructor
@Controller
@RequestMapping("/dayjobs")
public class DayJobController {

    DayJobService dayJobService;
    PaymentPeriodService paymentPeriodService;

    @GetMapping("/dayJobsPayment/{id}")
    public String findPayment(@PathVariable("id") Long id,
                              Model model, HttpSession session) {
        DayJob dayJob = new DayJob();
        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isGestor = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(USUARIO_GESTOR_USUAR));
            if (isGestor) {
                PaymentPeriod paymentPeriod =paymentPeriodService.findPaymentPeriodId(id); //paymentPeriodService.findPaymentPeriodIdAndStatus(id, P_ESTADO_INICIAL);
                 if (paymentPeriod != null) {
                     double total=0;
                     if(paymentPeriod.getStatePeriod().equals(P_ESTADO_INICIAL)) {

                        total = dayJobService.findByPaymentAndStatus(paymentPeriod, STATUS_INIT)
                                 .stream().mapToDouble(p -> (p.getWorkingHour()
                                         * paymentPeriod.getEmployee().getValueHourWorked())).sum();

                     }
                     else if(paymentPeriod.getStatePeriod().equals(P_ESTADO_FINAL)) {

                         total = dayJobService.findByPaymentAndStatus(paymentPeriod, STATUS_FINAL)
                                 .stream().mapToDouble(p -> (p.getWorkingHour()
                                         * paymentPeriod.getEmployee().getValueHourWorked())).sum();


                     }
                     model.addAttribute("valPendiente", total);
                     dayJob.setPaymentPeriod(paymentPeriod);
                     List<DayJob> dayJobs = dayJobService.findByPayment(paymentPeriod);
                     model.addAttribute("paymentPeriod", paymentPeriod);
                     model.addAttribute("dayJobs", dayJobs);
                     model.addAttribute("userLogger", userLogger);
                     model.addAttribute("dayJob", dayJob);

                     return "views/dayJobs";
                }
                return "redirect:/employees";
            }

        }
        return "redirect:/";
    }

    @PostMapping("/newDayJob")
    public String createDayJob(@Valid @ModelAttribute("dayJob") DayJob dayJob, BindingResult result,
                               HttpSession session, Model model, RedirectAttributes attribute) {
        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            PaymentPeriod paymentPeriod = paymentPeriodService
                    .findPaymentPeriodIdAndStatus(dayJob.getPaymentPeriod().getId(), P_ESTADO_INICIAL);
            boolean isGestor = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(USUARIO_GESTOR_USUAR));
            if (isGestor) {
                if (result.hasErrors()) {
                    if (paymentPeriod != null) {
                        double total = dayJobService.findByPaymentAndStatus(paymentPeriod, STATUS_INIT)
                                .stream().mapToDouble(p -> (p.getWorkingHour()
                                        * paymentPeriod.getEmployee().getValueHourWorked())).sum();
                        model.addAttribute("valPendiente", total);
                        dayJob.setPaymentPeriod(paymentPeriod);
                        List<DayJob> dayJobs = dayJobService.findByPaymentAndStatus(paymentPeriod, STATUS_INIT);
                        model.addAttribute("paymentPeriod", paymentPeriod);
                        model.addAttribute("dayJobs", dayJobs);
                        model.addAttribute("userLogger", userLogger);
                        model.addAttribute("dayJob", dayJob);


                        return "views/dayJobs";
                    } else {
                        return "redirect:/employees";
                    }
                }
                if (paymentPeriod != null) {
                    dayJob.setPaymentPeriod(paymentPeriod);
                    dayJobService.saveDayJob(dayJob);
                    attribute.addFlashAttribute("success", "Horas Registradas Con Exito");
                }
                return "redirect:/dayjobs/dayJobsPayment/" + paymentPeriod.getId();
            }
        }
        return "redirect:/";
    }


    @GetMapping("/edit/{idPayment}/{id}")
    public String editDayJob(@PathVariable("idPayment") Long idPayment, @PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes attribute) {

        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isGestor = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(USUARIO_GESTOR_USUAR));
            if (isGestor) {
                PaymentPeriod paymentPeriod = paymentPeriodService.findPaymentPeriodIdAndStatus(idPayment, P_ESTADO_INICIAL);

                if (paymentPeriod != null) {
                    DayJob dayJob = dayJobService.findDayJobPaymentPeriod(id, paymentPeriod);
                    if (dayJob != null) {
                        double total = dayJobService.findByPaymentAndStatus(paymentPeriod, STATUS_INIT)
                                .stream().mapToDouble(p -> (p.getWorkingHour()
                                        * paymentPeriod.getEmployee().getValueHourWorked())).sum();
                        model.addAttribute("valPendiente", total);
                        List<DayJob> dayJobs = dayJobService.findByPayment(paymentPeriod);
                        model.addAttribute("paymentPeriod", paymentPeriod);
                        model.addAttribute("dayJobs", dayJobs);
                        model.addAttribute("userLogger", userLogger);
                        model.addAttribute("dayJob", dayJob);

                        return "views/dayJobs";
                    }
                }
                return "redirect:/employees";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/anular/{idPayment}/{id}")
    public String anularDayJob(@PathVariable("idPayment") Long idPayment, @PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes attribute) {

        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isGestor = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(USUARIO_GESTOR_USUAR));
            if (isGestor) {
                PaymentPeriod paymentPeriod = paymentPeriodService.findPaymentPeriodIdAndStatus(idPayment, P_ESTADO_INICIAL);

                if (paymentPeriod != null) {
                    DayJob dayJob = dayJobService.findDayJobPaymentPeriod(id, paymentPeriod);
                    if (dayJob != null) {
                        double total = dayJobService.findByPaymentAndStatus(paymentPeriod, STATUS_INIT)
                                .stream().mapToDouble(p -> (p.getWorkingHour()
                                        * paymentPeriod.getEmployee().getValueHourWorked())).sum();
                        model.addAttribute("valPendiente", total);
                        List<DayJob> dayJobs = dayJobService.findByPayment(paymentPeriod);
                        model.addAttribute("paymentPeriod", paymentPeriod);
                        model.addAttribute("dayJobs", dayJobs);
                        model.addAttribute("userLogger", userLogger);
                        model.addAttribute("dayJob", new DayJob());
                        dayJob.setLiquidationStatus(STATUS_ABORT);
                        dayJobService.saveDayJob(dayJob);
                        attribute.addFlashAttribute("error", "el dia " + dayJob.getWorkingDay() + " fue anulado.");
                        return "redirect:/dayjobs/dayJobsPayment/" + paymentPeriod.getId();
                    }
                }
                return "redirect:/employees";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/liquid/{id}")
    public String liquidPayment(@PathVariable("id") Long id, Model model, HttpSession session, RedirectAttributes attribute) {

        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isGestor = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(USUARIO_GESTOR_USUAR));
            if (isGestor) {
                PaymentPeriod paymentPeriod = paymentPeriodService.findPaymentPeriodIdAndStatus(id, P_ESTADO_INICIAL);
                System.out.println(paymentPeriod.getId());
                System.out.println(paymentPeriod.getPeriodName());
                if (paymentPeriod != null) {
                    List<DayJob> dayJobs = dayJobService.findByPaymentAndStatusNot(paymentPeriod, STATUS_ABORT);

                    if (dayJobs.size() > 0) {

                        double total = dayJobs
                                .stream().mapToDouble(p -> (p.getWorkingHour()
                                        * paymentPeriod.getEmployee().getValueHourWorked())).sum();

                        paymentPeriod.setTotalLiquidatedPeriod(total);
                        paymentPeriod.setFinalDate(new Date());
                        paymentPeriod.setStatePeriod(P_ESTADO_FINAL);
                        dayJobs = dayJobs.stream()
                                .filter(p -> p.getLiquidationStatus().equals(STATUS_INIT))
                                .peek(p -> p.setLiquidationStatus(STATUS_FINAL)).collect(toList());

                    } else {
                        paymentPeriod.setStatePeriod(P_ESTADO_CERRADO);
                    }
                    dayJobService.saveAll(dayJobs);
                    paymentPeriodService.savePayment(paymentPeriod);
                    attribute.addFlashAttribute("success", "Periodo Liquidado Con Exito");
                }
                return "redirect:/employees";
            }
        }
        return "redirect:/";
    }
}
