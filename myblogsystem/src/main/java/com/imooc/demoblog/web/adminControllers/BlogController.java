package com.imooc.demoblog.web.adminControllers;

import Utils.CloudConnection;
import com.imooc.demoblog.dao.TypeRespository;
import com.imooc.demoblog.entity.Blog;
import com.imooc.demoblog.entity.Tag;
import com.imooc.demoblog.entity.Type;
import com.imooc.demoblog.entity.User;
import com.imooc.demoblog.service.BlogService;
import com.imooc.demoblog.service.TagService;
import com.imooc.demoblog.service.TypeService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/adminer")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;


    @GetMapping("/blogs")
    public String blogs(Model model,Blog blog, HttpSession session) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        //System.out.println("start = " + start);
        Pageable pageable = new PageRequest(0, 5, sort);
        User user = (User) session.getAttribute("currentUser");
        blog.setUser(user);
        Page page = blogService.listBlog(pageable, blog);
        model.addAttribute("page",page);
        model.addAttribute("types",typeService.findAll());
        return "admin/blogs";
    }


    @RequestMapping("/blogs/search")
    public String blogSearch(@RequestParam(value = "start", defaultValue = "0") Integer start, Blog blog,
                             HttpSession session ,@RequestParam(name = "typeId", defaultValue = "-1") Long typeId,  Model model) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        System.out.println("start = " + start);
        if(typeId == (long)-1)
            blog.setType(null);
        else {
            Type type = typeService.getType(typeId);
            blog.setType(type);
        }
        Pageable pageable = new PageRequest(start, 5, sort);
        User user = (User) session.getAttribute("currentUser");
        blog.setUser(user);
        Page page = blogService.listBlog(pageable, blog);
        model.addAttribute("page",page);
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String input(Model model) {
       setTypeTag(model);
        model.addAttribute("blog",new Blog());
        return "admin/blogs-input";
    }

    public void setTypeTag(Model model) {
        model.addAttribute("types",typeService.findAll());
        model.addAttribute("tags",tagService.findAll());
    }

    @GetMapping("/blogs/input/blogId")
    public String inputId(Model model, @RequestParam(name = "blogId") Long blogId) {
        setTypeTag(model);
        Blog blog = blogService.getBlog(blogId);
        model.addAttribute("blog",blog);
        return "admin/blogs-input";
    }


    @RequestMapping("/blogs/delete")
    public String blogDelete(@RequestParam(value = "blogId") Long blogId, RedirectAttributes attributes) {
        blogService.deleteBlog(blogId);
        attributes.addFlashAttribute("message","You delete the blog successfully!");
        return "redirect:/adminer/blogs";
    }

    @PostMapping("/blogs")
    public String post(Blog blog, @RequestParam(value = "typeId", defaultValue = "-1") Long typeId, HttpSession session, RedirectAttributes attributes) throws IOException {
        System.out.println("blog.id = " + blog.getId());
        blog.setUpdateTime(new Date());
        blog.setUser((User)session.getAttribute("currentUser"));
        blog.setTags(tagService.idToTags(blog.getTagIds()));
        blog.setTagIds(blog.getTagIds());
        blog.setType(typeService.getType(typeId));

        String firstPicture = blog.getFirstPicture();
        String newPicture = CloudConnection.uploadToCloud(firstPicture);
        if(newPicture != null)
            blog.setFirstPicture(newPicture);

        Long id = blog.getId();
        String message;
        if(id == (long)0) {
            blog.setCreateTime(new Date());
            blog.setViews(0);
            blogService.saveBlog(blog);
            message = "You add the blog successfully!";
        }

        else {
            blogService.updateBlog(id,blog);
            message = "You update the blog successfully!";
        }

        attributes.addFlashAttribute("message",message);
        return "redirect:/adminer/blogs";
    }


}