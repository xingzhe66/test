package com.dcits.comet.batch.util;

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import javax.xml.bind.annotation.*;
import java.util.List;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(
        name = "data"
)
@XmlAccessorType(XmlAccessType.FIELD)
class Data {
    @XmlAttribute(
            name = "templateType"
    )
    private TemplateType templateType;
    @XmlAttribute(
            name = "headLength"
    )
    private int headLength = 0;
    @XmlAttribute(
            name = "endingLength"
    )
    private int endingLength = 0;
    @XmlAttribute(
            name = "perRecordLength"
    )
    private int perRecordLength = 0;
    @XmlAttribute(
            name = "lengthType"
    )
    private LengthType lengthType;
    @XmlAttribute(
            name = "charSet"
    )
    private String charSet;
    @XmlAttribute(
            name = "split"
    )
    private String split;
    @XmlAttribute(
            name = "splitType"
    )
    private String splitType;
    @XmlAttribute(
            name = "clazz"
    )
    private String clazz;
    @XmlAttribute(
            name = "lineSeparator"
    )
    private String lineSeparator;
    @XmlElement(
            name = "column"
    )
    private List<Column> columnList;

    Data() {
        this.lengthType = LengthType.CHAR;
    }

    public TemplateType getTemplateType() {
        return this.templateType;
    }

    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }

    public int getHeadLength() {
        return this.headLength;
    }

    public void setHeadLength(int headLength) {
        this.headLength = headLength;
    }

    public int getEndingLength() {
        return this.endingLength;
    }

    public void setEndingLength(int endingLength) {
        this.endingLength = endingLength;
    }

    public int getPerRecordLength() {
        return this.perRecordLength;
    }

    public void setPerRecordLength(int perRecordLength) {
        this.perRecordLength = perRecordLength;
    }

    public LengthType getLengthType() {
        return this.lengthType;
    }

    public void setLengthType(LengthType lengthType) {
        this.lengthType = lengthType;
    }

    public String getCharSet() {
        return this.charSet;
    }

    public void setCharSet(String charSet) {
        this.charSet = charSet;
    }

    public String getSplit() {
        return this.split;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public String getClazz() {
        return this.clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getLineSeparator() {
        return this.lineSeparator;
    }

    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }

    public List<Column> getColumnList() {
        return this.columnList;
    }

    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    public boolean hasLineSeparator() {
        return this.getLineSeparator() != null && this.getLineSeparator().length() > 0;
    }

    public boolean hasSplit() {
        return this.getSplit() != null && this.getSplit().length() > 0;
    }

    public boolean isFixLengthFile() {
        return !this.hasLineSeparator() || !this.hasSplit();
    }

    public String getSplitType() {
        return this.splitType;
    }

    public void setSplitType(String splitType) {
        this.splitType = splitType;
    }
}
