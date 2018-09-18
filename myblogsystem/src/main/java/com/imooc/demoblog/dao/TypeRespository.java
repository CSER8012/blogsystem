package com.imooc.demoblog.dao;

import com.imooc.demoblog.entity.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TypeRespository extends JpaRepository<Type,Long> {
    Type findByName(String name);

    Page<Type> findAll(Specification<Type> specification, Pageable pageable);
}
