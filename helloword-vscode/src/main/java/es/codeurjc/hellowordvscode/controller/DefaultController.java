package es.codeurjc.hellowordvscode.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.ui.Model;


@ControllerAdvice
public class DefaultController {
    
    @ModelAttribute("userType")
    public String userTipe(Model model, HttpServletRequest request){
        if (request.isUserInRole("ADMIN"))
            return "Admin";
        else if (request.isUserInRole("CLIENT"))
            return "Client";
        else
            return "Manager";
    }
}
