package com.dcits.comet.commons.utils;

import com.dcits.comet.commons.exception.BusinessException;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;


/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-03-06 15:30
 * @Version 1.0
 **/
public class ValidatorUtil {
    private static Validator validator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory().getValidator();

    public static Validator getValidator() {
        return validator;
    }

    /**
     * 实体校验
     *
     * @param obj
     * @throws BusinessException
     */
    public static <T> void validate(T obj, Class... groups) throws BusinessException {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj, groups);
        if (constraintViolations.size() > 0) {
            ConstraintViolation<T> validateInfo = constraintViolations.iterator().next();
            // validateInfo.getMessage() 校验不通过时的信息，即message对应的值
            throw new BusinessException("0001", validateInfo.getMessage());
        }
    }
}
