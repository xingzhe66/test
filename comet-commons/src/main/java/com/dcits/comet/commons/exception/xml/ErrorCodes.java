package com.dcits.comet.commons.exception.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * @author wangyun
 * @date 2019/3/21
 * @description
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "http://www.dcits.com/comet/", name = "errorCode")
public class ErrorCodes {
    @XmlAttribute(required = true)
    private String externalSysIndicate;

    private List<ErrorCode> errorCode;


    public String getExternalSysIndicate() {
        return externalSysIndicate;
    }


    public void setExternalSysIndicate(String externalSysIndicate) {
        this.externalSysIndicate = externalSysIndicate;
    }


    public List<ErrorCode> getErrorCode() {
        return errorCode;
    }


    public void setErrorCode(List<ErrorCode> errorCode) {
        this.errorCode = errorCode;
    }


    @Override
    public String toString() {
        return "ErrorCodes [externalSysIndicate=" + externalSysIndicate + ", errorCode=" + errorCode + "]";
    }

}
