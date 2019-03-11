package com.dcits.comet.batch.util;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import javax.xml.bind.annotation.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

@XmlRootElement(
        name = "column"
)
@XmlAccessorType(XmlAccessType.FIELD)
class Column {
    @XmlAttribute(
            name = "name"
    )
    private String name;
    @XmlAttribute(
            name = "length"
    )
    private int length;
    @XmlAttribute(
            name = "regex"
    )
    private String regex;
    @XmlTransient
    private Pattern pattern;
    @XmlAttribute(
            name = "dateFormat"
    )
    private String dateFormat;
    @XmlAttribute(
            name = "numMultiple"
    )
    private String numMultiple;
    @XmlAttribute(
            name = "desc"
    )
    private String desc;
    @XmlAttribute(
            name = "tranSrc"
    )
    private String tranSrc;
    @XmlAttribute(
            name = "tranDest"
    )
    private String tranDest;
    @XmlTransient
    private Method readValueMethod;
    @XmlTransient
    private Method writeValueMethod;
    @XmlTransient
    private Map<String, String> transMap = new HashMap();

    Column() {
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLength() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getRegex() {
        return this.regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Pattern getPattern() {
        return this.pattern;
    }

    public void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public String getDateFormat() {
        return this.dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getNumMultiple() {
        return this.numMultiple != null && !"".equals(this.numMultiple.trim()) ? this.numMultiple : null;
    }

    public void setNumMultiple(String numMultiple) {
        this.numMultiple = numMultiple;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTranSrc() {
        return this.tranSrc;
    }

    public void setTranSrc(String tranSrc) {
        this.tranSrc = tranSrc;
    }

    public String getTranDest() {
        return this.tranDest;
    }

    public void setTranDest(String tranDest) {
        this.tranDest = tranDest;
    }

    public Method getReadValueMethod() {
        return this.readValueMethod;
    }

    public void setReadValueMethod(Method readValueMethod) {
        this.readValueMethod = readValueMethod;
    }

    public Method getWriteValueMethod() {
        return this.writeValueMethod;
    }

    public void setWriteValueMethod(Method writeValueMethod) {
        this.writeValueMethod = writeValueMethod;
    }

    public String trans(String source) {
        if (source != null) {
            String dest = (String)this.transMap.get(source);
            if (dest == null) {
                throw new RuntimeException("trans error:source [" + source + "] has no dest ");
            } else {
                return dest;
            }
        } else {
            return null;
        }
    }

    public void addTrans(String source, String dest) {
        this.transMap.put(source, dest);
    }

    public boolean isNeedTrans() {
        return this.transMap.size() > 0;
    }
}
