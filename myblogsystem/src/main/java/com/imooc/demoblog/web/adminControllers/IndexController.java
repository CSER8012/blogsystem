package com.imooc.demoblog.web.adminControllers;

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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/blogList")
    public String list(@RequestParam(value = "start",defaultValue = "0") Integer start,
                       HttpSession session, Model model) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(start, 5, sort);
        User user = (User) session.getAttribute("currentUser");
        Blog blog = new Blog();
        blog.setUser(user);
        Page page = blogService.listBlog(pageable,blog);
        model.addAttribute("page",page);
        model.addAttribute("types",typeService.listTypeTop(6,blog));
        model.addAttribute("tags",tagService.listTagsTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8,blog));
        return "blog";
    }

    @GetMapping("/typeList")
    public String types(@RequestParam(value = "start",defaultValue = "0") Integer start,HttpSession session,
                        @RequestParam(value = "id",defaultValue = "-1") Long id, Model model) {
        User user = (User)session.getAttribute("currentUser");
        Page<Type> typeList = typeService.typeList(user,new PageRequest(0,100,new Sort(Sort.Direction.DESC,
                "id")));
        List<Type> types = new ArrayList<>();
        for(Type type : typeList) {
            types.add(type);
        }

        if(id == -1 && types.size() > 0)
            id = types.get(0).getId();

        Blog blog = new Blog();
        Type type = new Type();
        if(id == -1) {
            blog.setType(null);
        }
        else {
            type.setId(id);
            blog.setType(type);
        }
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(start, 5, sort);

        blog.setUser(user);
        Page page = blogService.listBlog(pageable,blog);
        model.addAttribute("page",page);
        model.addAttribute("types",types);
        model.addAttribute("activeTypeId",id);
        return "types";
    }

    @GetMapping("/tagList")
    public String tags(@RequestParam(value = "start",defaultValue = "0") Integer start,HttpSession session,
                        @RequestParam(value = "id",defaultValue = "-1") Long id, Model model) {
        User user = (User)session.getAttribute("currentUser");
        Page<Tag> tagList = tagService.tagList(user,new PageRequest(0,100,new Sort(Sort.Direction.DESC,
                "id")));
        List<Tag> tags = new ArrayList<>();
        for(Tag tag : tagList) {
            tags.add(tag);
        }
        if(id == -1 && tags.size() > 0)
            id = tags.get(0).getId();

        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(start, 5, sort);
        Page page = blogService.listBlog(id,pageable);
        model.addAttribute("page",page);
        model.addAttribute("tags",tags);
        model.addAttribute("activeTagId",id);
        return "tags";
    }

    @GetMapping("/blog")
    public String blogDetails(@RequestParam(value = "id") Long id,Model model ) {
        Blog blog = blogService.getBlog(id);
        if(blog.getViews() == null)
            blog.setViews(0);
        blog.setViews(blog.getViews()+1);
        blogService.updateBlog(id,blog);
        model.addAttribute("blog",blog);
        return "index";
    }

    @RequestMapping("/search")
    public String search(Model model, @RequestParam(value = "query") String query, @RequestParam(value = "start",defaultValue = "0") Integer start) {
        Sort sort = new Sort(Sort.Direction.DESC, "updateTime");
        Pageable pageable = new PageRequest(start, 4, sort);
        model.addAttribute("page",blogService.listBlog(query,pageable));
        model.addAttribute("query",query);
        return "search";
    }


}
