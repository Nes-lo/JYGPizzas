package com.jyg.nomina.controllers;


import com.jyg.nomina.models.DayJob;
import com.jyg.nomina.models.Employee;
import com.jyg.nomina.models.PaymentPeriod;
import com.jyg.nomina.models.User;
import com.jyg.nomina.services.DayJobService;
import com.jyg.nomina.services.EmployeeService;
import com.jyg.nomina.services.PaymentPeriodService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.jyg.nomina.util.globalvar.InitSessionAdmin.*;
import static com.jyg.nomina.util.globalvar.VarDayJob.STATUS_INIT;
import static com.jyg.nomina.util.globalvar.VarPaymentPeriod.P_ESTADO_INICIAL;


@AllArgsConstructor
@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private EmployeeService employeeService;
    private PaymentPeriodService paymentPeriodService;
    private DayJobService dayJobService;

    @GetMapping("")
    public String allEmployees(@ModelAttribute("employee")Employee employee, Model model, HttpSession session){

        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isGestor = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(USUARIO_GESTOR_ADMIN) ||
                            p.getRoleName().equals(USUARIO_GESTOR_USUAR));
            if (isGestor) {

                List<Employee> employees = employeeService.findAll();

           /*     double total=employees.stream()
                        .flatMap(p->p.getPaymentPeriods().stream())
                        .filter(q->q.getStatePeriod().equals(P_ESTADO_INICIAL))
                        .flatMap(o->o.getDayJobs().stream())
                        .mapToDouble(s->(s.getWorkingHour()* s.getPaymentPeriod().getEmployee().getValueHourWorked())).sum();


*/

                model.addAttribute("userLogger", userLogger);
                model.addAttribute("employees", employees);

               // model.addAttribute("paymentePeriod",paymentPeriodService.findStateEmployee())

                return "views/employees";
            }
        }
        return "redirect:/";

    }

    @GetMapping("/edit/{id}")
    public  String editEmployee(@ModelAttribute("employee")Employee employee,
                               @PathVariable("id") Long id,
                               Model model, HttpSession session, RedirectAttributes attribute){

        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isGestor = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(USUARIO_GESTOR_ADMIN) ||
                            p.getRoleName().equals(USUARIO_GESTOR_USUAR));
             if (isGestor) {
                List<Employee> employees = employeeService.findAll();

                model.addAttribute("userLogger", userLogger);
                model.addAttribute("employees", employees);
                employee=employeeService.findById(id);
                if(employee!=null){
                    model.addAttribute("employee",employee);
                }
                return "views/employees";
            }

        }
        return "redirect:/";
    }


    @GetMapping("/new")
    public String newEmployee(){

        return "redirect:/employees";
    }


    @PostMapping("/new")
    public String addEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result,
                              HttpSession session, Model model, RedirectAttributes attribute) {

        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isGestor = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(USUARIO_GESTOR_ADMIN));
            if (isGestor) {
                List<Employee> employees = employeeService.findAll();
                model.addAttribute("userLogger", userLogger);
                model.addAttribute("employees", employees);
                System.out.println( "A");
                if (result.hasErrors()) {

                    model.addAttribute("employee", employee);
                    return "views/employees";
                }
                System.out.println( "B");

                    Employee employee1 = employeeService.findById(employee.getId());
                    if (employee1 != null) {
                        employee1.setEmployeeName(employee.getEmployeeName());
                        employee1.setDocumentTypeEmployee(employee.getDocumentTypeEmployee());
                        employee1.setEmployeeEmail(employee.getEmployeeEmail());
                        employee1.setEmployeePhone(employee.getEmployeePhone());
                        employee1.setEmployeeAddress(employee.getEmployeeAddress());
                        employeeService.saveEmployee(employee1);
                        attribute.addFlashAttribute("success", "Edicion Exitosa");
                    } else {
                        employee.setAsset(true);
                        employeeService.saveEmployee(employee);
                        attribute.addFlashAttribute("success", "Registro Exitoso");

                    }

                return "redirect:/employees";
            }
        }
        return "redirect:/";
    }

    @PutMapping("/edit")
    public String addEditEmployee(@Valid @ModelAttribute("employee") Employee employee, BindingResult result,
                           HttpSession session, Model model, RedirectAttributes attribute){

        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isGestor = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(USUARIO_GESTOR_USUAR));
            if (isGestor) {
                List<Employee> employees = employeeService.findAll();
                model.addAttribute("userLogger", userLogger);
                model.addAttribute("employees", employees);
                if (result.hasErrors()) {
                    model.addAttribute("employee", employee);
                    return "views/employees";
                }
                Employee employee1=employeeService.findById(employee.getId());
                if(employee1!=null){
                    employee1.setValueHourWorked(employee.getValueHourWorked());
                    employee1.setAsset(employee.getAsset());
                    employeeService.saveEmployee(employee1);
                    attribute.addFlashAttribute("success", "Edición Exitosa");
                }
                return "redirect:/employees";
            }
        }
        return "redirect:/";
    }


}
