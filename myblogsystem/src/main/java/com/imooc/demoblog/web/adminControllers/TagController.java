package com.imooc.demoblog.web.adminControllers;

import com.imooc.demoblog.entity.Blog;
import com.imooc.demoblog.entity.Tag;
import com.imooc.demoblog.entity.Type;
import com.imooc.demoblog.entity.User;
import com.imooc.demoblog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/adminer")
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String tags(@RequestParam(value = "start", defaultValue = "0") Integer start,
                       HttpSession session, Model model) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(start,5,sort);
        User user = (User)session.getAttribute("currentUser");
        Page page = tagService.tagList(user,pageable);
        model.addAttribute("page",page);
        return "admin/tags";
    }

    @GetMapping("/tags/input")
    public String input(Model model) {
        model.addAttribute("tag",null);
        return "admin/tagsInput";
    }

    @GetMapping("/tags/input/tagId")
    public String inputId(Model model,@RequestParam(value = "id") Long id,@RequestParam(value = "start") Integer start) {
        Tag tag = tagService.getTag(id);
        model.addAttribute("tag",tag);
        model.addAttribute("start",start);
        return "admin/tagsInput";
    }

    @PostMapping("/tags")
    public ModelAndView post(@RequestParam(value = "name") String name, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        String message;
        String viewName = "redirect:/adminer/tags/input";
        if(name == null || name.replaceAll(" ","").length() == 0)
            message = "The tag name can not be empty!";
        else if (tagService.findTagByName(name))
            message = "The tag name can not be empty!";
        else {
            Tag tag = new Tag();
            tag.setName(name);
            tagService.saveTag(tag);
            message = "You add the tag successfully!";
            viewName = "redirect:/adminer/tags";
        }

        attributes.addFlashAttribute("message",message);
        modelAndView.setViewName(viewName);
        return modelAndView;
    }

    @PostMapping("/tags/tagId")
    public ModelAndView postId(@RequestParam(value = "name") String name,@RequestParam(value = "id") Long id, @RequestParam(value = "start") Integer start, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        String message;
        String viewName = "redirect:/adminer/tags/input/tagId?id="+id+"&start="+start;
        Tag tag = tagService.getTag(id);
        if(name == null || name.replaceAll(" ","").length() == 0) {
            message = "The tag name can not be empty!";
        }

        else if(tagService.findTagByName(name)) {
            message = "This tag has existed!";
        }

        else {
            tag.setName(name);
            tagService.update(id,tag);
            message = "You update the tag successfully!";
            viewName = "redirect:/adminer/tags?start="+start;
        }

        attributes.addFlashAttribute("message",message);
        modelAndView.setViewName(viewName);
        return modelAndView;
    }

    @RequestMapping("/tags/delete")
    public ModelAndView delete(@RequestParam(value = "id") Long id, @RequestParam(value = "start") Integer start,RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView("redirect:/adminer/tags?start="+start);
        List<Blog> blogs = tagService.getTag(id).getBlogs();
        if(blogs != null && blogs.size() > 0) {
            attributes.addFlashAttribute("message","This tag has blogs! You can not delete it!");
            return modelAndView;
        }
        tagService.delete(id);
        attributes.addFlashAttribute("message","You delete the tag successfully!");
        return modelAndView;
    }

}
