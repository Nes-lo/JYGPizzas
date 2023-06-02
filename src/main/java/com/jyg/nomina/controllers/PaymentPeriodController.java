package com.jyg.nomina.controllers;

import com.jyg.nomina.models.Employee;
import com.jyg.nomina.models.PaymentPeriod;
import com.jyg.nomina.models.User;
import com.jyg.nomina.services.EmployeeService;
import com.jyg.nomina.services.PaymentPeriodService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.jyg.nomina.util.globalvar.InitSessionAdmin.USUARIO_GESTOR_ADMIN;
import static com.jyg.nomina.util.globalvar.InitSessionAdmin.USUARIO_GESTOR_USUAR;
import static com.jyg.nomina.util.globalvar.VarPaymentPeriod.P_ESTADO_INICIAL;

@AllArgsConstructor
@Controller
@RequestMapping("/payments")
public class PaymentPeriodController {

    PaymentPeriodService paymentPeriodService;
    EmployeeService employeeService;

    @GetMapping("/{id}")
    public String listPeriod(@PathVariable("id") Long id,
                             Model model, HttpSession session){

        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isGestor = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(USUARIO_GESTOR_USUAR));
            if (isGestor) {

                Employee employee=employeeService.findById(id);
                if(employee!=null){
                    List<PaymentPeriod> paymentPeriods=paymentPeriodService.findByEmployee(employee);
                    Collections.reverse(paymentPeriods);
                    model.addAttribute("userLogger", userLogger);
                    model.addAttribute("paymentPeriods",paymentPeriods);
                    model.addAttribute("employee",employee);

                    return "views/paymentsPeriods";

                }
                return "redirect:/employees";
            }
        }

        return "redirect:/";

    }



    @GetMapping("/createPayment/{id}")
    public String createPayment(@PathVariable("id") Long id, HttpSession session, RedirectAttributes attribute){

        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isGestor = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(USUARIO_GESTOR_ADMIN) ||
                            p.getRoleName().equals(USUARIO_GESTOR_USUAR));
            if (isGestor) {
                Employee employee=employeeService.findByIdAndState(id,true);
                if(employee!=null){
                    PaymentPeriod paymentPeriod=paymentPeriodService.findStateEmployee(employee,P_ESTADO_INICIAL);
                    if(paymentPeriod==null){
                        paymentPeriod=new PaymentPeriod();
                        paymentPeriod.setEmployee(employee);
                        paymentPeriod.setStartDate(new Date());
                        paymentPeriod.setStatePeriod(P_ESTADO_INICIAL);
                        LocalDate fecha = LocalDate.now();
                        if(fecha.getDayOfMonth()<15){
                            paymentPeriod.setPeriodName("1Q "+
                                    fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"))
                            + " " + fecha.getYear());
                        }
                        else{
                            paymentPeriod.setPeriodName("2Q "+
                                    fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es", "ES"))
                                    + " " + fecha.getYear());
                        }
                        paymentPeriodService.savePayment(paymentPeriod);
                        attribute.addFlashAttribute("success", "Periodo Creado Con Exito");
                    }
                    else {
                        attribute.addFlashAttribute("warning", "Existe Un Periodo Abierto");
                    }
                }else {
                    attribute.addFlashAttribute("warning", "El Empleado No Existe o Esta Inactivo");
                }
                return "redirect:/employees";
            }
        }
        return "redirect:/";
    }

}
