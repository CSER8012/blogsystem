package com.imooc.demoblog.web.adminControllers;

import com.imooc.demoblog.entity.User;
import com.imooc.demoblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

@Controller
@RequestMapping("/adminer")
public class LoginController{

    @Autowired
    private UserService userService;

    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    @PostMapping("/login")
    public ModelAndView loginPage(HttpServletRequest request, HttpServletResponse response, HttpSession session, RedirectAttributes attributes) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = userService.checkUser(username,password);
        ModelAndView modelAndView = new ModelAndView();
        if(user != null) {
            user.setPassword(null);
            session.setAttribute("currentUser",user);
            modelAndView.setViewName("admin/index");
        }

        else {
            attributes.addFlashAttribute("message","Username or Password is Incorrect!");
            modelAndView.setViewName("redirect:/adminer");
        }

       return modelAndView;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/adminer";
    }
}

