package com.jhr.utils.base;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.io.Serializable;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/07 10:26
 */
public class BaseRspHeader implements Serializable {


    private static final long serialVersionUID = 7768608691843981374L;

    private String respCode;
    private String respDesc;

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
