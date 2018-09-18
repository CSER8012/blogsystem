package com.imooc.demoblog.web.adminControllers;

import com.imooc.demoblog.entity.Type;
import com.imooc.demoblog.entity.User;
import com.imooc.demoblog.service.BlogService;
import com.imooc.demoblog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/adminer")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types")
    public String types(@RequestParam(value = "start", defaultValue = "0") Integer start,
                        HttpSession session, Model model){
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(start,5,sort);
        User user = (User)session.getAttribute("currentUser");
        Page page = typeService.typeList(user,pageable);
        model.addAttribute("page",page);
        return "admin/types";
    }

    @GetMapping("/types/input")
    public String input(Model model) {
        model.addAttribute("type",null);
        model.addAttribute("start",(long)0);
        return "admin/typesInput";
    }

    @GetMapping("/types/input/typeId")
    public String inputId(@RequestParam(value = "id") Long id, @RequestParam(value = "start") Integer start,Model model) {
        Type type = typeService.getType(id);
        model.addAttribute("type",type);
        model.addAttribute("start",start);
        return "admin/typesInput";
    }

    @PostMapping("/types")
    public ModelAndView post(@RequestParam(value = "name") String name, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        String message;
        String viewName = "redirect:/adminer/types/input";;
        if(name == null || name.replaceAll(" ","").length() == 0) {
            message = "The type name can not be empty!";
        }

        else if(typeService.findTypeByName(name)) {
            message = "This type has existed!";
        }

        else {
            Type type = new Type();
            type.setName(name);
            typeService.saveType(type);
            message = "You add the type successfully!";
            viewName = "redirect:/adminer/types";
        }

        attributes.addFlashAttribute("message",message);
        modelAndView.setViewName(viewName);
        return modelAndView;
    }

    @PostMapping("/types/typeId")
    public ModelAndView postId(@RequestParam(value = "id") Long id,@RequestParam(value = "name") String name,RedirectAttributes attributes,@RequestParam("start") Integer start) {
        ModelAndView modelAndView = new ModelAndView();
        String message;
        String viewName = "redirect:/adminer/types/input/typeId?id="+id+"&start="+start;
        Type type = typeService.getType(id);
        if(name == null || name.replaceAll(" ","").length() == 0) {
            message = "The type name can not be empty!";
        }

        else if(typeService.findTypeByName(name)) {
            message = "This type has existed!";
        }

        else {
            type.setName(name);
            typeService.update(id,type);
            message = "You update the type successfully!";
            viewName = "redirect:/adminer/types?start="+start;
        }

        attributes.addFlashAttribute("message",message);
        modelAndView.setViewName(viewName);
        return modelAndView;
    }

    @RequestMapping("/types/delete")
    public ModelAndView delete(@RequestParam(value = "id") Long id, @RequestParam(value = "start",defaultValue = "0") Long start,RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView("redirect:/adminer/types?start"+start);
        if(blogService.isTypeUsedByBlog(id)) {
            attributes.addFlashAttribute("message","This type has blogs! You can not delete!");
            return  modelAndView;
        }
        attributes.addFlashAttribute("message","You delete the type successfully!");
        typeService.delete(id);
        return  modelAndView;
    }


}
