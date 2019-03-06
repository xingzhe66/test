package com.dcits.comet.dao.exception;

import com.dcits.comet.commons.exception.CometPlatformException;

/**
 * @Author chengliang
 * @Description //TODO
 * @Date 2019-03-06 11:25
 * @Version 1.0
 **/
public class ParamException extends CometPlatformException {

    public ParamException(String errorCode) {
        super(errorCode);
    }
}
