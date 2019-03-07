package com.jhr.entity;

import java.io.Serializable;

/**
 * 中间表
 * 款式表-入库表
 */
public class Stylewar implements Serializable {
    private Long id;

    private Long styleid;

    private Long warehousingid;

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

    public Long getWarehousingid() {
        return warehousingid;
    }

    public void setWarehousingid(Long warehousingid) {
        this.warehousingid = warehousingid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", styleid=").append(styleid);
        sb.append(", warehousingid=").append(warehousingid);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}