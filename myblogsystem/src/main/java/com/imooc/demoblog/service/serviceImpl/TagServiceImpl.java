package com.imooc.demoblog.service.serviceImpl;

import com.imooc.demoblog.dao.TagRepository;
import com.imooc.demoblog.entity.Tag;
import com.imooc.demoblog.entity.User;
import com.imooc.demoblog.service.TagService;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagRepository tagRepository;


    @Override
    public Tag saveTag(Tag tag) {
        Tag t = tagRepository.save(tag);
        return t;
    }

    @Override
    public Tag getTag(Long id) {
        return tagRepository.getOne(id);
    }

    @Override
    public Page<Tag> tagList(Pageable pageable) {
        return tagRepository.findAll(pageable);
    }

    @Override
    public Page<Tag> tagList(User user, Pageable pageable) {
        return tagRepository.findAll(new Specification<Tag>() {
            @Override
            public Predicate toPredicate(Root<Tag> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                Join join = root.join("blogs");
                if(join != null)
                    predicates.add(cb.equal(join.get("user").get("id"),user.getId()));
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
    }

    @Override
    public Tag update(Long id, Tag tag) {
        Tag tag1 = tagRepository.getOne(id);
        if(tag1 == null) {
            try {
                throw new NotFoundException("no such id!");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }

        BeanUtils.copyProperties(tag,tag1);
        return tagRepository.save(tag1);
    }

    @Override
    public void delete(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public boolean findTagByName(String name) {
        if(tagRepository.findByName(name) != null)
            return true;
        return false;
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public List<Tag> idToTags(String ids) {
        List<Tag> tags = new ArrayList<>();
        if(ids.equals(""))
            return tags;
        String[] a = ids.split(",");
        for(int i = 0; i < a.length; i++) {
            Long index = (long)(Long.parseLong(a[i]));
            if(tagRepository.existsById(index))
                tags.add(tagRepository.getOne(index));

        }
        return tags;
    }

    @Override
    public Page<Tag> listTagsTop(Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(0,size,sort);
        Page<Tag> tags = tagRepository.findAll(pageable);
        return tags;
    }
}
