package com.imooc.demoblog.service;

import com.imooc.demoblog.entity.Blog;
import com.imooc.demoblog.entity.Type;
import com.imooc.demoblog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BlogService {
    Blog getBlog(Long id);
    Page<Blog> listBlog(Pageable pageable, Blog blog);
    Blog saveBlog(Blog blog);
    Blog updateBlog(Long id, Blog blog);
    void deleteBlog(Long id);
    boolean isTypeUsedByBlog(Long typeId);
    Page<Blog> listRecommendBlogTop(Integer size, Blog blog);
    Page<Blog> listBlog(Long tagId,Pageable pageable);
    Page<Blog> listBlog(Type type, Pageable pageable);
    Page<Blog> listBlog(String query, Pageable pageable);
}
