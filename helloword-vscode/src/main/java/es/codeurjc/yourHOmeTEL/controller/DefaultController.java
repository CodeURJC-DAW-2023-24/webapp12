package es.codeurjc.yourHOmeTEL.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import es.codeurjc.yourHOmeTEL.model.UserE;
import es.codeurjc.yourHOmeTEL.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;


@ControllerAdvice
public class DefaultController {

    @Autowired
    UserRepository userRepository;
    
    @ModelAttribute("userType")
    public String userType(HttpServletRequest request){
        if (request.isUserInRole("ADMIN"))
            return "Admin";
        else if (request.isUserInRole("CLIENT"))
            return "Client";
        else
            return "Manager";
    }

    @ModelAttribute("isAdmin")
    public Boolean isAdmin(HttpServletRequest request){
        boolean isAdmin = false;
        if (request.isUserInRole("ADMIN")){
            isAdmin = true;
         }
         return isAdmin;
    }
    @ModelAttribute("isManager")
    public Boolean isManager(HttpServletRequest request){
        boolean isManager = false;
        if (request.isUserInRole("MANAGER")){
            isManager= true;
         }
         return isManager;
    }
    @ModelAttribute("isClient")
    public Boolean isClient(HttpServletRequest request){
        boolean isClient= false;
        if (request.isUserInRole("CLIENT")){
            isClient = true;         
         }
         return isClient;
    }
    @ModelAttribute("isUser")
    public Boolean isUser(HttpServletRequest request){
        boolean isUser= false;
        if (request.isUserInRole("USER")){
            isUser = true;         
         }
         return isUser;
    }
}
