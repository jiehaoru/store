package com.jhr.utils.base;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/07 14:30
 */
public class BaseRspConstants {
    public static final String USER_PREFIX = "user";
    public static final String ORDER_PREFIX = "order";
    public static final String COMMODITY_PREFIX = "commodity";
    public static final String PAY_PREFIX = "pay";
    public static final String WORKFLOW_PREFIX = "workflow";
    /** @deprecated */
    @Deprecated
    public static final String RSP_CODE_SUCCESS = "0000";
    public static final String CODE_SUCCESS = "0";
    public static final String RSP_DESC_SUCCESS = "成功";
    /** @deprecated */
    @Deprecated
    public static final String RSP_CODE_FAILUR = "8888";
    public static final String CODE_FAILUR = "1";
    public static final String RSP_DESC_FAILUR = "失败";
    public static final String RSP_CODE_PARAMETERS_ERROR = "7777";
    public static final String RSP_DESC_PARAMETERS_ERROR = "入参校验失败";
    public static final String RSP_CODE_BATCHDETAIL_IS_SAME = "T001";
    public static final String RSP_CODE_FILE_FORMAT_ERROR = "S100";
    public static final String RSP_DESC_ERROR = "系统异常";

    public BaseRspConstants() {
    }
}
