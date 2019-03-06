package com.dcits.comet.cache.aspect;

import com.dcits.comet.cache.annotation.CacheTable;
import com.dcits.comet.commons.utils.BusiUtil;
import com.dcits.comet.dao.model.BasePo;
import com.dcits.comet.dao.param.CacheUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-03-05 9:07
 * @Version 1.0
 **/
//@Component
//@Aspect
public class RedisCacheAspect {
    private static final Logger logger = LoggerFactory.getLogger(RedisCacheAspect.class);

    private static final String UPDATE_METHOD = "update";


    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 定制一个环绕通知
     *
     * @param joinPoint
     */
    @Around(value = "execution(* com.dcits.comet.dao.mybatis.DaoSupportImpl.selectOne(*))")
    public Object around(ProceedingJoinPoint joinPoint) {
        logger.info("<====== 进入 RedisCacheAspect 环绕通知 ======>");
        Object result = null;

        //先获取目标方法参数
        Object[] args = joinPoint.getArgs();
        BasePo basePo = (BasePo) args[0];

        if (basePo.getClass().isAnnotationPresent(CacheTable.class)) {
            try {
                String redisKey = CacheUtil.getCacheKey(basePo);
                logger.info("<====== redisKey {}  ======> ", redisKey);
                // 查询操作
                result = redisTemplate.opsForValue().get(redisKey);
                if (null == result) {
                    // Redis 中不存在，则从数据库中查找，并保存到 Redis
                    logger.info("<====== Redis 中不存在该记录，从数据库查找 ======>");
                    try {
                        result = joinPoint.proceed();
                        if (result != null) {
                            redisTemplate.opsForValue().set(redisKey, result);
                        }
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                logger.debug("<====== redisValue {}  ======> ", result.toString());
                logger.info("环绕通知之结束");
                return result;
            } catch (Throwable ex) {
                logger.error("<====== RedisCache 执行异常: {} ======>", ex);
            }
            return result;
        }
        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return result;
    }

    /**
     * 定制一个环绕通知
     *
     * @param joinPoint
     */
    @Around(value = "execution(* com.dcits.comet.dao.mybatis.DaoSupportImpl.insert(com.dcits.comet.dao.model.BasePo)) " +
            "|| execution(* com.dcits.comet.dao.mybatis.DaoSupportImpl.update(com.dcits.comet.dao.model.BasePo,com.dcits.comet.dao.model.BasePo))")
    public Object aroundClear(ProceedingJoinPoint joinPoint) {
        logger.info("<====== 进入 RedisCacheClearAspect 环绕通知 ======>");
        Object result = null;
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //类路径名
        String classPathName = joinPoint.getTarget().getClass().getName();
        //类名
        String className = classPathName.substring(classPathName.lastIndexOf(".") + 1, classPathName.length());
        //获取方法名
        String methodName = signature.getMethod().getName();

        logger.debug("className {} ", className);
        logger.debug("methodName {}", methodName);

        //先获取目标方法参数
        Object[] args = joinPoint.getArgs();

        BasePo basePo = (BasePo) args[0];


        //update 使用where模型
        if (BusiUtil.isEquals(UPDATE_METHOD, methodName)) {
            basePo = (BasePo) args[1];
        }

        try {
            result = joinPoint.proceed();
            return result;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        if (basePo.getClass().isAnnotationPresent(CacheTable.class)) {
            try {
                String redisKey = CacheUtil.getCacheKey(basePo);
                logger.info("<====== redisKey {}  ======> ", redisKey);
                // 删除操作
                redisTemplate.delete(redisKey);
            } catch (Throwable ex) {
                logger.error("<====== RedisCacheClear 执行异常: {} ======>", ex);
            }
        }
        return result;
    }


}
