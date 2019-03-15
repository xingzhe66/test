package com.dcits;

import com.alibaba.fastjson.JSON;
import com.dcits.comet.dao.DaoSupport;
import com.dcits.comet.dao.model.Order;
import com.dcits.yunyun.entity.SysLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
//@EnableAutoConfiguration(exclude = WebMvcConfigurer.class)
@ComponentScan(basePackages = { "com.dcits", "com.dcits.yunyun"})
public class Appmain implements CommandLineRunner {

    protected static final Logger LOGGER = LoggerFactory.getLogger(Appmain.class);
   @Autowired
   private ApplicationContext applicationContext;

    public static void main(String[] args) {

        new SpringApplicationBuilder(Appmain.class)
                .web(WebApplicationType.NONE)
                .run(args);

    }

    @Override
    public void run(String... args) throws Exception {
        DaoSupport daoSupport= (DaoSupport) applicationContext.getBean("daoSupport");
        SysLog sysLog=new SysLog();
        sysLog.setId(1000000000001l);
     //   sysLog=daoSupport.selectByPrimaryKey(sysLog,1000000000001l);
        LOGGER.info(sysLog.toString());

        sysLog=new SysLog();
        sysLog.setId(2000000000002l);
     //   sysLog=daoSupport.selectByPrimaryKey(sysLog,2000000000002l);
        LOGGER.info(sysLog.toString());

        sysLog=new SysLog();
        sysLog.addOrder(Order.desc("id"));
        List<SysLog> list=daoSupport.selectList(sysLog.getClass().getName()+".extendSelect",sysLog);
        LOGGER.info(JSON.toJSONString(list));


        LOGGER.info(sysLog.toString());

    }
}
