package com.imooc.demoblog;

import com.imooc.demoblog.dao.UserRepository;
import com.imooc.demoblog.entity.Type;
import com.imooc.demoblog.entity.User;
import com.imooc.demoblog.service.TypeService;
import com.imooc.demoblog.service.UserService;
import com.imooc.demoblog.service.serviceImpl.TypeServiceImpl;
import com.imooc.demoblog.service.serviceImpl.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringRunner.class)

public class DemoblogApplicationTests {

    @Test
    public void contextLoads() {

    }

}
