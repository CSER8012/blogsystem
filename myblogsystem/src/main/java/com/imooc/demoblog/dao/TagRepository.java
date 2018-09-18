package com.imooc.demoblog.dao;

import com.imooc.demoblog.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag,Long> {
    Tag findByName(String name);
    Page<Tag> findAll(Specification<Tag> specification, Pageable pageable);
}
