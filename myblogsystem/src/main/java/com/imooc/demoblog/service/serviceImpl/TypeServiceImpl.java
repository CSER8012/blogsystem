package com.imooc.demoblog.service.serviceImpl;

import com.imooc.demoblog.dao.TypeRespository;
import com.imooc.demoblog.entity.Blog;
import com.imooc.demoblog.entity.Type;
import com.imooc.demoblog.entity.User;
import com.imooc.demoblog.service.TypeService;
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
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TypeServiceImpl implements TypeService {


    @Autowired
    private TypeRespository typeRespository;


    @Transactional
    @Override
    public Type saveType(Type type) {
        return typeRespository.save(type);
    }


    @Override
    public Type getType(Long id) {
        if(typeRespository.existsById(id))
            return typeRespository.getOne(id);
        return null;
    }

    @Transactional
    @Override
    public Page<Type> typeList(Pageable pageable) {
        return typeRespository.findAll(pageable);
    }

    @Transactional
    @Override
    public Type update(Long id, Type type) {
        Type type1 = typeRespository.getOne(id);
        if(type1 == null) {
            try {
                throw new NotFoundException("no such id !");
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
        BeanUtils.copyProperties(type,type1);

        return typeRespository.save(type1);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        typeRespository.deleteById(id);
    }

    @Override
    public boolean findTypeByName(String name) {
        Type type = typeRespository.findByName(name);
        return type != null;
    }

    @Override
    public List<Type> findAll() {
        return typeRespository.findAll();
    }

    @Override
    public Page<Type> typeList(User user, Pageable pageable) {
        return typeRespository.findAll(new Specification<Type>() {
            @Override
            public Predicate toPredicate(Root<Type> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
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
    public Page<Type> listTypeTop(Integer size, Blog blog) {
        Sort sort = new Sort(Sort.Direction.DESC,"id");
        Pageable pageable = new PageRequest(0,size,sort);
        Page<Type> types = typeList(blog.getUser(),pageable);
        return types;
    }


}
