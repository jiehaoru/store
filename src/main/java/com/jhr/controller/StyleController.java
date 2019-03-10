package com.jhr.controller;

import com.jhr.entity.Style;
import com.jhr.service.StyleService;
import com.jhr.utils.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;


/**
 * <br>
 * 标题： 款式
 * 描述：
 *
 * @author jhr
 * @create 2019/03/07 15:04
 */

@Controller
@RequestMapping("/style")
public class StyleController extends BaseController {

    public static final Logger LOGGER = LoggerFactory.getLogger(StyleController.class);

    @Autowired
    private StyleService styleService;

    /**
     * 插入 款式
     *
     * @param style
     * @return
     */
    @RequestMapping("/insertStyle")
    public String insertStyle(Style style) {
//        BaseRsp baseRsp=new BaseRsp();
        try {
            style.setId(Sequence.getInstance().nextId());
            style.setFlag(1);//1 有效
            style.setCreatetime(new Date());
            int i = styleService.insertStyle(style);

        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }
        //跳转tableYS页面
        return "tableYS";
    }

    /**
     * 查询全部
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectStyleList")
    public List<Style> selectStyleList() {
//        BaseRsp baseRsp=new BaseRsp();
        List<Style> list = null;
        try {
            list = styleService.selectStyleList();
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return list;
        }

        return list;
    }

    /**
     * 查询全部
     * 有效状态的
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectStyleListByFlag")
    public List<Style> selectStyleListByFlag() {
//        BaseRsp baseRsp=new BaseRsp();
        Style style = new Style();
        List<Style> list = null;
        try {
            style.setFlag(1);
            list = styleService.selectStyleBy(style);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return list;
        }

        return list;
    }

    /**
     * 通过主键查询
     *
     * @param id
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectStyleByPrimaryKey")
    public Style selectStyleByPrimaryKey(Long id) {
//        BaseRsp baseRsp=new BaseRsp();
        Style style = null;
        try {
            style = styleService.selectStyleByPrimaryKey(id);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return style;
        }

        return style;
    }


    /**
     * 模糊 查询 款名
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectStyleByName")
    public List<Style> selectStyleByName(String styleName) {
//        BaseRsp baseRsp=new BaseRsp();
        Style style = new Style();
        List<Style> list = null;
        try {
            style.setFlag(1);//有效
            style.setStylename(styleName);
            list = styleService.selectStyleBy(style);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return list;
        }

        return list;
    }


    /**
     * 通过 自定义编号查询
     * 提供给其他表调用
     * 有效无效都 返回
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectStyleBynumstr")
    public Style selectStyleBynumstr(String numstr) {
//        BaseRsp baseRsp=new BaseRsp();
        Style rspstyle = null;
        Style style = new Style();
        try {
            style.setNumstr(numstr);
            List<Style> styles = styleService.selectStyleBy(style);//返回单个
            if (null != styles && styles.size()>0) {
                rspstyle= styles.get(0);//
            }
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return rspstyle;
        }

        return rspstyle;
    }


    /**
     * 通过主键修改
     *
     * @return
     */

    @RequestMapping("/updateStyle")
    public String updateStyle(Style style) {
//        BaseRsp baseRsp=new BaseRsp();
        int re;
        try {
            re = styleService.updateByPrimaryKey(style);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        //跳转tableYS页面
        return "tableYS";
    }


    /**
     * 物理删除
     *
     * @param id
     * @return
     */
    @RequestMapping("/deleteByPrimaryKey")
    public String deleteByPrimaryKey(Long id) {
//        BaseRsp baseRsp=new BaseRsp();
        int re;
        try {
            re = styleService.deleteByPrimaryKey(id);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        //跳转tableYS页面
        return "tableYS";
    }


}