package com.kay.service.impl;

import com.kay.common.ServerResponse;
import com.kay.service.ICategoryService;
import com.kay.service.IUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by kay on 2018/3/20.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-config.xml"})
public class ICategoryServiceImplTest {

    @Resource
    private ICategoryService iCategoryService;

    @Test
    public void getAllCategoryChildrenByParentId() throws Exception {
        ServerResponse response = iCategoryService.getAllCategoryChildrenByParentId(0);
    }

}