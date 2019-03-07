package com.jhr.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 返厂表
 */
public class Refactory implements Serializable {
    private Long id;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumstr() {
        return numstr;
    }

    public void setNumstr(String numstr) {
        this.numstr = numstr == null ? null : numstr.trim();
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
        this.operator = operator == null ? null : operator.trim();
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
        this.filed1 = filed1 == null ? null : filed1.trim();
    }

    public String getFiled2() {
        return filed2;
    }

    public void setFiled2(String filed2) {
        this.filed2 = filed2 == null ? null : filed2.trim();
    }

    public String getFiled3() {
        return filed3;
    }

    public void setFiled3(String filed3) {
        this.filed3 = filed3 == null ? null : filed3.trim();
    }

    public String getFiled4() {
        return filed4;
    }

    public void setFiled4(String filed4) {
        this.filed4 = filed4 == null ? null : filed4.trim();
    }

    public String getFiled5() {
        return filed5;
    }

    public void setFiled5(String filed5) {
        this.filed5 = filed5 == null ? null : filed5.trim();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", numstr=").append(numstr);
        sb.append(", refpri=").append(refpri);
        sb.append(", refnum=").append(refnum);
        sb.append(", refdate=").append(refdate);
        sb.append(", flag=").append(flag);
        sb.append(", operator=").append(operator);
        sb.append(", createtime=").append(createtime);
        sb.append(", filed1=").append(filed1);
        sb.append(", filed2=").append(filed2);
        sb.append(", filed3=").append(filed3);
        sb.append(", filed4=").append(filed4);
        sb.append(", filed5=").append(filed5);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}