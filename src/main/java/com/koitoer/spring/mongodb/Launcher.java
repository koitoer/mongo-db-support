package com.koitoer.spring.mongodb;

import com.koitoer.spring.mongodb.domain.A;
import com.koitoer.spring.mongodb.domain.B;
import com.koitoer.spring.mongodb.example.DataContent;
import com.koitoer.spring.mongodb.example.DataContentResult;
import com.koitoer.spring.mongodb.repository.DocsARepository;
import com.koitoer.spring.mongodb.repository.DocsBRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Arrays;
import java.util.List;

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

    @Autowired
    MongoTemplate mongoTemplate;

   @Test
   public void test1(){


       Aggregation aggregation = Aggregation.newAggregation(
               Aggregation.match(Criteria.where("publisherId").is(Integer.parseInt("10")))
               , Aggregation.group("publisherId").count().as("total").sum("name").as("name").sum("zip").as("zip")
       );

       AggregationResults<DataContentResult> result = mongoTemplate.aggregate(aggregation , DataContent.class, DataContentResult.class);
       List<DataContentResult> theResult = result.getMappedResults();

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
