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
         return request.isUserInRole("ADMIN");
    }
    @ModelAttribute("isManager")
    public Boolean isManager(HttpServletRequest request){
         return request.isUserInRole("MANAGER");
    }
    @ModelAttribute("isClient")
    public Boolean isClient(HttpServletRequest request){
         return request.isUserInRole("CLIENT");
    }
    @ModelAttribute("isUser")
    public Boolean isUser(HttpServletRequest request){
         return request.isUserInRole("USER");
    }

    @ModelAttribute("path")
    public String getPath(HttpServletRequest request){
         return request.getServletPath();
    }

}
