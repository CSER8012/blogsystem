package com.imooc.demoblog.web.adminControllers;

import com.imooc.demoblog.entity.Blog;
import com.imooc.demoblog.entity.Comment;
import com.imooc.demoblog.entity.User;
import com.imooc.demoblog.service.BlogService;
import com.imooc.demoblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable  Long blogId, Model model) {
        List<Comment> comments = commentService.listCommentByBlogId(blogId);
        model.addAttribute("comments",comments);
        return "index :: commentList";
    }

    @PostMapping("/commentsPost")
    public String post(Comment comment, HttpSession session) {

        Long blogId = comment.getBlog().getId();
        Blog blog = blogService.getBlog(blogId);
        List<Comment> commentList = blog.getComments();
        commentList.add(comment);
        blog.setComments(commentList);
        blogService.updateBlog(blogId,blog);
        comment.setBlog(blog);
        comment.setAvatar(avatar);
        User user = (User)session.getAttribute("currentUser");
        if(user != null) {
            comment.setAdminComment(true);
            comment.setAvatar(user.getAvatar());
            comment.setNickname(user.getNickname());
            comment.setEmail(user.getEmail());
        }
        else
            comment.setAvatar(avatar);

        commentService.saveComment(comment);
        return "redirect:/comments/" + comment.getBlog().getId();
    }
}
