package com.imooc.demoblog.web.adminControllers;

import com.imooc.demoblog.entity.User;
import com.imooc.demoblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/adminer")
public class RegistController {

    @Autowired
    private UserService userService;

    @GetMapping("/regist")
    public String toRegist() {
        return "admin/regist";
    }

    @PostMapping("/regist")
    public String regist(Model model, @RequestParam(value = "username") String username, @RequestParam(value = "password")
                         String password, RedirectAttributes attributes) {
        User u = userService.findByUsername(username);
        String message;
        if(u != null) {
            message = "This username has been used !";
            attributes.addFlashAttribute("message",message);
            return "redirect:/adminer/regist";
        }

        else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            userService.addUser(user);
            message = "You have signed up successfully!";
        }

        attributes.addFlashAttribute("message",message);
        return "redirect:/adminer";
    }
}
