package com.jhr.entity;

import java.io.Serializable;

/**
 * 中间表
 * 款式表-返厂表
 */
public class Stylefac implements Serializable {
    private Long id;

    private Long styleid;

    private Long refactoryid;

    private static final long serialVersionUID = 1L;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStyleid() {
        return styleid;
    }

    public void setStyleid(Long styleid) {
        this.styleid = styleid;
    }

    public Long getRefactoryid() {
        return refactoryid;
    }

    public void setRefactoryid(Long refactoryid) {
        this.refactoryid = refactoryid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", styleid=").append(styleid);
        sb.append(", refactoryid=").append(refactoryid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}