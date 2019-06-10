package com.dcits.comet.common.test;

import com.dcits.comet.commons.exception.ExceptionContainer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
public class ErrorCodeTest {
    static final Logger LOGGER = LoggerFactory.getLogger(ErrorCodeTest.class);

    @Resource
    ExceptionContainer errorCodeContainer;


    @Before
    public void testBefore() {
    }


    @Test
    public void testPrint() {
        LOGGER.info("==========={}", ExceptionContainer.getErrorMessage("AAA", ""));
        LOGGER.info("==========={}", ExceptionContainer.getExternalCode("AAA", null));
        LOGGER.info("==========={}", ExceptionContainer.getInnerCode("AAA_OUT", null));

    }

    @Bean
    public ExceptionContainer exceptionContainer() {
        return new ExceptionContainer();
    }


}
