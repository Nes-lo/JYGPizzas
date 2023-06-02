package com.jyg.nomina.controllers;

import com.jyg.nomina.models.Role;
import com.jyg.nomina.models.User;
import com.jyg.nomina.services.RoleService;
import com.jyg.nomina.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.jyg.nomina.util.globalvar.GlobalVariables.ROLE_ADMIN;
import static com.jyg.nomina.util.globalvar.InitSessionAdmin.SALT_AUTOMATICO;

@AllArgsConstructor
@Controller
@RequestMapping("/usrs")
public class UserController {


    private UserService userService;
    private RoleService roleService;

    @GetMapping("")
    public String allUsuarios(@ModelAttribute("user") User user, Model model, HttpSession session) {

        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isAdmin = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(ROLE_ADMIN));
            if (isAdmin) {
                List<User> users = userService.findAll();
                //  List<Role> roles=roleService.findAll();
                model.addAttribute("userLogger", userLogger);
                model.addAttribute("users", users);

                return "views/usuarios";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/new")
    public String register(){
        return "redirect:/usrs";
    }

    @PostMapping("/new")
    public String addUsr(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session, Model model, RedirectAttributes attribute) {

        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isAdmin = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(ROLE_ADMIN));
            if (isAdmin) {
                List<User> users = userService.findAll();
                model.addAttribute("userLogger", userLogger);
                model.addAttribute("users", users);

                user.setPassword(user.getUserName() + SALT_AUTOMATICO);
                user.setConfirmPassword(user.getUserName() + SALT_AUTOMATICO);
                System.out.println(user.getPassword());
                userService.save(user, result);
                if (result.hasErrors()) {
                    return "views/usuarios";
                }
                attribute.addFlashAttribute("success", "Registro Exitoso");

                return "redirect:/usrs";
            }
        }
        return "redirect:/";
    }

    @GetMapping("/editRoles/{id}")
    public String editRoles(@PathVariable("id") Long id, HttpSession session, Model model){
        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isAdmin = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(ROLE_ADMIN));
            if (isAdmin) {

                model.addAttribute("roles", roleService.findAll());
                model.addAttribute("user", userService.findById(id));
                model.addAttribute("userLogger", userLogger);
                return "views/editRoles";

            }
        }
        return "redirect:/";
    }

    @PostMapping("/editRoles")
    public String editRoles(@Valid @ModelAttribute("User")User user, BindingResult result, HttpSession session, Model model, RedirectAttributes attribute){
        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            boolean isAdmin = userLogger.getRoles().stream()
                    .anyMatch(p -> p.getRoleName().equals(ROLE_ADMIN));
            if (isAdmin) {
                User user1=userService.findById(user.getId());
                if(user1==null){
                    return "redirect:/usrs";
                }
              /* List<Role> roles=new ArrayList<>();
                for(int i=0; i<user.getRoles().size();i++){
                    Role role=roleService.findByName(user.getRoles().get(i).getRoleName());
                    if(role!=null){
                      roles.add(role);
                    }
                }*/

                List<Role> roles = user.getRoles().stream()
                        .map(Role::getRoleName)
                        .map(roleService::findByName)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList());

                user1.setRoles(roles);
                user1.setConfirmPassword(user1.getPassword());


                user.getRoles().forEach(System.out::println);

                userService.save(user1);

                return "redirect:/usrs";

            }
        }
        return "redirect:/";
    }

    @GetMapping("/editPass")
    public String editPass(HttpSession session,Model model){
        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {

            User user=new User();
            System.out.println("pasa por");
            model.addAttribute("user",user);
            model.addAttribute("userLogger", userLogger);
            return "views/editPass";
        }
        return "redirect:/";

    }

    @PostMapping("/editPass")
    public String saveEditPass(@Valid @ModelAttribute("user")User user,BindingResult result ,@RequestParam("passAnt") String passAnt,  HttpSession session, Model model,  RedirectAttributes attribute){
        User userLogger = (User) session.getAttribute("user");
        if (userLogger != null) {
            User user1=userService.findById(userLogger.getId());
            if(user1!=null){
                System.out.println("A");
                if(user.getPassword().equals(user.getConfirmPassword())){
                    System.out.println("B");
                    System.out.println("pre"+user1.getPassword());
                    User user2=new User();
                    user2.setId(user1.getId());
                    user2.setPassword(passAnt);

                    Boolean valid=userService.validPass(user2);
                    System.out.println("C");
                    System.out.println(valid);
                    if(valid){
                        user1.setPassword(user.getPassword());
                        user1.setConfirmPassword(user.getConfirmPassword());
                        System.out.println("D");
                        userService.saveEncrypt(user1);
                        System.out.println("C");

                        attribute.addFlashAttribute("success", "La contraseña se cambio con exito.");
                        return "redirect:/";

                    }

                }
                attribute.addFlashAttribute("error", "No se pudieron validar los datos con exito.");
                return "redirect:/usrs/editPass";
            }

        }
        return "redirect:/";

    }



}
