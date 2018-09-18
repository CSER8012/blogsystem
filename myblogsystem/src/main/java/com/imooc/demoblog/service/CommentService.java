package com.imooc.demoblog.service;

import com.imooc.demoblog.entity.Comment;

import java.util.List;

public interface CommentService {
    List<Comment> listCommentByBlogId(Long blogId);
    Comment saveComment(Comment comment);
}
