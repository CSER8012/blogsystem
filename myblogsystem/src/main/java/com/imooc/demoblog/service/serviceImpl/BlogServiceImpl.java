package com.imooc.demoblog.service.serviceImpl;

import com.imooc.demoblog.dao.BlogRepository;
import com.imooc.demoblog.entity.Blog;
import com.imooc.demoblog.entity.Tag;
import com.imooc.demoblog.entity.Type;
import com.imooc.demoblog.entity.User;
import com.imooc.demoblog.service.BlogService;
import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.*;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Override
    public Blog getBlog(Long id) {
        return blogRepository.getOne(id);
    }

    @Override
    public Page<Blog> listBlog(Pageable pageable, Blog blog) {
        return blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(blog.getUser() != null)
                    predicates.add(cb.equal(root.<User>get("user").get("id"),blog.getUser().getId()));
                if(blog.getTitle() != null && !"".equals(blog.getTitle().replaceAll(" ",""))) {
                    predicates.add(cb.like(root.<String>get("title"),"%"+blog.getTitle()+"%"));
                }

                if(blog.getType() != null && blog.getType().getId() != null ) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),blog.getType().getId()));
                }

                if(blog.isRecommend()) {
                    predicates.add(cb.equal(root.<Boolean>get("recommend"),blog.isRecommend()));
                }


                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        }, pageable);
    }


    @Override
    public Blog saveBlog(Blog blog) {
        return blogRepository.save(blog);
    }

    @Override
    public Blog updateBlog(Long id, Blog blog) {
        Blog b = blogRepository.getOne(id);
        if(b == null) {
            try {
                throw new NotFoundException("no such id!");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        BeanUtils.copyProperties(blog,b);
        return blogRepository.save(b);
    }

    @Override
    public void deleteBlog(Long id) {
        blogRepository.deleteById(id);
    }

    @Override
    public boolean isTypeUsedByBlog(Long typeId) {
        List<Blog> blogs = blogRepository.findAll();
        for(Blog blog : blogs) {
            if(blog.getType().getId() == typeId)
                return true;
        }
        return false;
    }

    @Override
    public Page<Blog> listRecommendBlogTop(Integer size, Blog blog) {
        blog.setRecommend(true);
        blog.setType(null);
        blog.setTitle(null);
        Sort sort = new Sort(Sort.Direction.DESC,"updateTime");
        Pageable pageable = new PageRequest(0,size,sort);
        Page<Blog> blogs = listBlog(pageable,blog);
        return blogs;
    }

    @Override
    public Page<Blog> listBlog(Long tagId, Pageable pageable) {
        Page<Blog> page = blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
                Join join = root.join("tags");
                return cb.equal(join.get("id"),tagId);
            }
        },pageable);
        return page;
    }

    @Override
    public Page<Blog> listBlog(Type type, Pageable pageable) {
        Page<Blog> page = blogRepository.findAll(new Specification<Blog>() {
            @Override
            public Predicate toPredicate(Root<Blog> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                if(type.getId() != null ) {
                    predicates.add(cb.equal(root.<Type>get("type").get("id"),type.getId()));
                }
                cq.where(predicates.toArray(new Predicate[predicates.size()]));
                return null;
            }
        },pageable);
        return page;
    }


    @Override
    public Page<Blog> listBlog(String query, Pageable pageable) {
        return blogRepository.findByQuery("%"+query+"%",pageable);
    }


}
