package com.imooc.demoblog.service.serviceImpl;

import com.imooc.demoblog.dao.CommentRepository;
import com.imooc.demoblog.entity.Comment;
import com.imooc.demoblog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public List<Comment> listCommentByBlogId(Long blogId) {
        Sort sort = new Sort(Sort.Direction.DESC,"createTime");
        return commentRepository.findByBlogIdAndParentCommentNull(blogId,sort);
    }

    @Transactional
    @Override
    public Comment saveComment(Comment comment) {
        comment.setCreateTime(new Date());
        Long parentCommentId = comment.getParentComment().getId();
        if(parentCommentId != -1) {
            Comment parentComment = commentRepository.getOne(parentCommentId);
            Comment rootComment = findRootComment(parentComment);
            List<Comment> replyComments = rootComment.getReplyComments();
            replyComments.add(comment);
            rootComment.setReplyComments(replyComments);
            commentRepository.save(rootComment);
            comment.setParentComment(rootComment);
        }

        else
            comment.setParentComment(null);

        return commentRepository.save(comment);
    }

    public Comment findRootComment(Comment comment) {
        Comment parentComment = comment.getParentComment();
        if(parentComment == null)
            return comment;

        Comment c = parentComment;
        while(c.getParentComment() != null) {
            c = c.getParentComment();
        }

        return c;
    }
}
