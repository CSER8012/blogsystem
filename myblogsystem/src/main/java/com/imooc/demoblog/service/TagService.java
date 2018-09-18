package com.imooc.demoblog.service;

import com.imooc.demoblog.entity.Tag;
import com.imooc.demoblog.entity.Type;
import com.imooc.demoblog.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
    Tag saveTag(Tag tag);
    Tag getTag(Long id);
    Page<Tag> tagList(Pageable pageable);
    Page<Tag> tagList(User user, Pageable pageable);
    Tag update(Long id, Tag tag);
    void delete(Long id);
    boolean findTagByName(String name);
    List<Tag> findAll();
    List<Tag> idToTags(String ids);
    Page<Tag> listTagsTop(Integer size);
}
