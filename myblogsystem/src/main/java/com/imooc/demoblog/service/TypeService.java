package com.imooc.demoblog.service;

import com.imooc.demoblog.entity.Blog;
import com.imooc.demoblog.entity.Type;
import com.imooc.demoblog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface TypeService {

    Type saveType(Type type);
    Type getType(Long id);
    Page<Type> typeList(Pageable pageable);

    Type update(Long id, Type type);
    void delete(Long id);
    boolean findTypeByName(String name);
    List<Type> findAll();
    Page<Type> typeList(User user,Pageable pageable);
    Page<Type> listTypeTop(Integer size, Blog blog);
}
