package com.jhr.entity;

import java.io.Serializable;

/**
 * 中间表
 * 款式表-退货表
 */

public class Stylefun implements Serializable {
    private Long id;

    private Long styleid;

    private Long refundsid;

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

    public Long getRefundsid() {
        return refundsid;
    }

    public void setRefundsid(Long refundsid) {
        this.refundsid = refundsid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", styleid=").append(styleid);
        sb.append(", refundsid=").append(refundsid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}