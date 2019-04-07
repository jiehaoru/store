package com.jhr.controller.vo;

import java.io.Serializable;
import java.util.Date;

/**
 * 返厂表
 */
public class RefactoryVO implements Serializable {
    private String id;

    private String numstr; //自定义编号

    private Double refpri; //返厂价格

    private Long refnum; //返厂数量

    private Date refdate; //返厂日期

    private Integer flag;  //状态

    private String operator; //操作人

    private Date createtime; //创建时间

    private String filed1;

    private String filed2;

    private String filed3;

    private String filed4;

    private String filed5;

    private static final long serialVersionUID = 1L;


    // 操作框显示用
    public String xq="详情";
    public String bj="编辑";
    public String sc="删除";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumstr() {
        return numstr;
    }

    public void setNumstr(String numstr) {
        this.numstr = numstr;
    }

    public Double getRefpri() {
        return refpri;
    }

    public void setRefpri(Double refpri) {
        this.refpri = refpri;
    }

    public Long getRefnum() {
        return refnum;
    }

    public void setRefnum(Long refnum) {
        this.refnum = refnum;
    }

    public Date getRefdate() {
        return refdate;
    }

    public void setRefdate(Date refdate) {
        this.refdate = refdate;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getFiled1() {
        return filed1;
    }

    public void setFiled1(String filed1) {
        this.filed1 = filed1;
    }

    public String getFiled2() {
        return filed2;
    }

    public void setFiled2(String filed2) {
        this.filed2 = filed2;
    }

    public String getFiled3() {
        return filed3;
    }

    public void setFiled3(String filed3) {
        this.filed3 = filed3;
    }

    public String getFiled4() {
        return filed4;
    }

    public void setFiled4(String filed4) {
        this.filed4 = filed4;
    }

    public String getFiled5() {
        return filed5;
    }

    public void setFiled5(String filed5) {
        this.filed5 = filed5;
    }

    public String getXq() {
        return xq;
    }

    public void setXq(String xq) {
        this.xq = xq;
    }

    public String getBj() {
        return bj;
    }

    public void setBj(String bj) {
        this.bj = bj;
    }

    public String getSc() {
        return sc;
    }

    public void setSc(String sc) {
        this.sc = sc;
    }

    @Override
    public String toString() {
        return "RefactoryVO{" +
                "id='" + id + '\'' +
                ", numstr='" + numstr + '\'' +
                ", refpri=" + refpri +
                ", refnum=" + refnum +
                ", refdate=" + refdate +
                ", flag=" + flag +
                ", operator='" + operator + '\'' +
                ", createtime=" + createtime +
                ", filed1='" + filed1 + '\'' +
                ", filed2='" + filed2 + '\'' +
                ", filed3='" + filed3 + '\'' +
                ", filed4='" + filed4 + '\'' +
                ", filed5='" + filed5 + '\'' +
                ", xq='" + xq + '\'' +
                ", bj='" + bj + '\'' +
                ", sc='" + sc + '\'' +
                '}';
    }
}