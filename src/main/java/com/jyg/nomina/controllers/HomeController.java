package com.jyg.nomina.controllers;

import com.jyg.nomina.models.LoginUser;
import com.jyg.nomina.models.Role;
import com.jyg.nomina.models.User;
import com.jyg.nomina.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.jyg.nomina.util.globalvar.GlobalVariables.ROLE_ADMIN;
import static com.jyg.nomina.util.globalvar.InitSessionAdmin.PASS;
import static com.jyg.nomina.util.globalvar.InitSessionAdmin.USUARIO_ADMIN;

@AllArgsConstructor
@Controller
public class HomeController {

    private UserService userService;


    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        User user= (User) session.getAttribute("user");
        if(user==null){
            // model.addAttribute("newUser", new User());
            model.addAttribute("newLogin", new LoginUser());
            return "views/login";
        }

        model.addAttribute("userLogger",user);
        return "views/index";
    }

    @GetMapping("/login")
    public String login(){

        return "redirect:/";
    }

    @PostMapping("/login")
    public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin,
                        BindingResult result, Model model, HttpSession session) {
        User user;

        if(newLogin.getUserName().equals(USUARIO_ADMIN) && newLogin.getPassword().equals(PASS)){
            user=new User();
            List<Role> roles=new ArrayList<>();
            Role adminRol=new Role();
            adminRol.setRoleName(ROLE_ADMIN);
            roles.add(adminRol);
            user.setUserName(newLogin.getUserName());
            user.setRoles(roles);
        }else{
            user=userService.login(newLogin, result);
        }
        if(result.hasErrors()) {
            model.addAttribute("newUser", new User());
            return "views/login";
        }

        user.setPassword("");
        session.setAttribute("user",user);
        return "redirect:/";


    }

    @GetMapping("/signOff")
    public String signOff(HttpSession session){
        session.setAttribute("user",null);

        return "redirect:/";
    }
}
