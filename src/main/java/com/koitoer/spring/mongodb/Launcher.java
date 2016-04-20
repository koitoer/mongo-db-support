package com.koitoer.spring.mongodb;

import com.koitoer.spring.mongodb.domain.A;
import com.koitoer.spring.mongodb.domain.B;
import com.koitoer.spring.mongodb.repository.DocsARepository;
import com.koitoer.spring.mongodb.repository.DocsBRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;

/**
 * Created by mauricio.mena on 20/04/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "spring.xml")
public class Launcher {

    @Autowired
    DocsBRepository docsBRepository;
    @Autowired
    DocsARepository docsARepository;

   @Test
   public void test1(){

        B b = new B();
        b.setName("BName1");
        b = docsBRepository.save(b);

        A a1 = new A();
        a1.setName("AName1");
        a1.setB(b);
       docsARepository.save(a1); // stored

        A a2 = new A();
        a2.setName("AName2");
        a2.setB(b);
        docsARepository.save(a2); // dup key error
    }

}
