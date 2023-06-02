package com.jyg.nomina.services;

import com.jyg.nomina.models.LoginUser;
import com.jyg.nomina.models.User;
import com.jyg.nomina.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    public UserRepository userRepository;

    public User findById(Long id){
        return userRepository.findById(id).orElse(null);

    }

    public List<User> findAll(){
        return userRepository.findAll();
    }




    public User save(User user, BindingResult result){

        System.out.println(1);
        if(userRepository.findByUserName(user.getUserName()).isPresent()){
            result.rejectValue("userName", "Unique", "Existing User Name!");
            System.out.println(2);
            return null;

        } else if (!Pattern
                .compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^.,&+=])(?=\\S+$).{8,16}$")
                .matcher(user.getPassword()).matches()) {
            result.rejectValue("password", "ValidationError", "The password must have alphanumeric characters, uppercase, lowercase and special character!");
            System.out.println(3);
            return null;
        } else if(!user.getPassword().equals(user.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "Matches", "The Confirm Password must match Password!");
            System.out.println(4);
            return null;
        } else{
            user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
            return userRepository.save(user);
        }


    }
    public User save(User user){
            return userRepository.save(user);
    }
    public void saveEncrypt(User user){
        user.setPassword(BCrypt.hashpw(user.getPassword(),BCrypt.gensalt()));
       userRepository.save(user);
    }
    public User login(LoginUser newLogin, BindingResult result){

        User user=userRepository.findByUserName(newLogin.getUserName()).orElse(null);

        if(user==null) {
            result.rejectValue("userName", "Unique", "Invalid User Name!");
            return null;
        } else if(Boolean.FALSE.equals(BCrypt.checkpw(newLogin.getPassword(),user.getPassword()))
        ){
            result.rejectValue("password", "Matches", "Invalid Password!");
            return null;
        }
        return user;
    }


    public Boolean validPass(User editPass){

        User user1=userRepository.findById(editPass.getId()).orElse(null);

        System.out.println("edit"+editPass.getPassword());
        System.out.println("ORIG"+user1.getPassword());
        System.out.println("edit"+editPass.getUserName());
        System.out.println("ORIG"+user1.getUserName());

        if(user1==null){
            return false;
        }
        else if(Boolean.FALSE.equals(BCrypt.checkpw(editPass.getPassword(),user1.getPassword()))){
            return false;
        }
        return true;
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

}