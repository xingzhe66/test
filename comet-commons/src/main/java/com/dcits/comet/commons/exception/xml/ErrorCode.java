package com.dcits.comet.commons.exception.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * 错误码xml定义对应的实体对象
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "errorCode")
public class ErrorCode {

    @XmlAttribute(required = true)
    private String code;
    @XmlAttribute(required = true)
    private String message;
    @XmlAttribute(required = true)
    private String outCode;
    @XmlAttribute
    private String outMessage; // 暂未用到，作保留...2016/12/06


    public String getCode() {
        return code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }


    public String getOutCode() {
        return outCode;
    }


    public void setOutCode(String outCode) {
        this.outCode = outCode;
    }


    public String getOutMessage() {
        return outMessage;
    }


    public void setOutMessage(String outMessage) {
        this.outMessage = outMessage;
    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ErrorCode other = (ErrorCode) obj;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        return true;
    }


    @Override
    public String toString() {
        return "ErrorCode [code=" + code + ", message=" + message + ", outCode=" + outCode + ", outMessage=" + outMessage + "]";
    }

}
