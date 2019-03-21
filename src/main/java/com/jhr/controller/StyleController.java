package com.jhr.controller;

import com.jhr.entity.Style;
import com.jhr.service.StyleService;
import com.jhr.utils.Sequence;
import com.jhr.utils.base.BaseRsp;
import com.jhr.utils.base.BaseRspConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
@RequestMapping("/lecture")
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
    @ResponseBody
    @RequestMapping(value = "/insertStyle", method = RequestMethod.POST)
    public BaseRsp insertStyle(@RequestBody Style style) {

        BaseRsp baseRsp=new BaseRsp();
        try {
            style.setId(Sequence.getInstance().nextId());
            style.setFlag(1);//1 有效
            style.setCreatetime(new Date());
            int i = styleService.insertStyle(style);
            if(i>0){
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            }else{
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            }


        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }
        return baseRsp;
    }

    /**
     * 查询全部
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectStyleList",method = RequestMethod.GET)
    public BaseRsp<List<Style>> selectStyleList() {
        BaseRsp<List<Style>> baseRsp=new BaseRsp<List<Style>>();
        List<Style> list = null;
        try {
            list = styleService.selectStyleList();
            baseRsp.setData(list);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 查询全部
     * 有效状态的
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectStyleListByFlag",method = RequestMethod.GET)
    public BaseRsp<List<Style>> selectStyleListByFlag() {
        BaseRsp<List<Style>> baseRsp=new BaseRsp<List<Style>>();
        Style style = new Style();
        List<Style> list = null;
        try {
            style.setFlag(1);
            list = styleService.selectStyleBy(style);
            baseRsp.setData(list);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 通过主键查询
     *
     * @param style
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectStyleByPrimaryKey",method = RequestMethod.POST)
    public BaseRsp<Style> selectStyleByPrimaryKey(@RequestBody Style style) {
        BaseRsp<Style> baseRsp=new BaseRsp<Style>();
        Style styleReq = null;
        if(null==style.getId()){
            LOGGER.error("StyleServiceImpl========>selectStyleByPrimaryKey失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",selectStyleByPrimaryKey失败,id为空");
            return baseRsp;
        }

        try {
            style = styleService.selectStyleByPrimaryKey(style.getId());
            baseRsp.setData(style);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return baseRsp;
        }

        return baseRsp;
    }


    /**
     * 模糊 查询 款名
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectStyleByName",method = RequestMethod.POST)
    public BaseRsp<List<Style>> selectStyleByName(@RequestBody Style style) {
        BaseRsp<List<Style>> baseRsp=new BaseRsp<List<Style>>();
        List<Style> list = null;
        if (null ==style.getStylename()) {
            LOGGER.error("StyleServiceImpl========>selectStyleByName,款名为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",款名为空");
            return baseRsp;
        }
        try {
            style.setFlag(1);//有效
            list = styleService.selectStyleBy(style);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(list);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }


    /**
     * 通过 自定义编号查询
     * 提供给其他表调用
     * 有效无效都 返回
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectStyleBynumstr",method = RequestMethod.POST)
    public BaseRsp<Style> selectStyleBynumstr(@RequestBody Style style) {
        BaseRsp<Style> baseRsp=new BaseRsp<Style>();
        Style rspstyle = null;
        if (null==style.getNumstr()) {
            LOGGER.error("StyleServiceImpl========>selectStyleBynumstr,自定义编号为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",自定义编号为空");
            return baseRsp;
        }
        try {
            List<Style> styles = styleService.selectStyleBy(style);//返回单个
            if(styles.size()>0){
                rspstyle=styles.get(0);
            }
            baseRsp.setData(rspstyle);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>selectStyleBynumstr失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }


    /**
     * 通过主键修改
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateStyle",method = RequestMethod.POST)
    public BaseRsp updateStyle(@RequestBody Style style) {
       BaseRsp baseRsp=new BaseRsp();

       if(null==style.getId()){
           baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
           baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",主键id为空");
           return baseRsp;
       }

        int re;
        try {
            re = styleService.updateByPrimaryKey(style);
            if (re>0){
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",修改影响行数"+re);
            }
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 物理删除
     * @param style
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteByPrimaryKey",method = RequestMethod.GET)
    public BaseRsp deleteByPrimaryKey(@RequestBody Style style) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==style.getId()) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",id值为空");
            return baseRsp;
        }

        int re;
        try {

            re = styleService.deleteByPrimaryKey(style.getId());
            if (re>0){

                    baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                    baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);

                }else {
                if (null==style.getId()) {
                    baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                    baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+re);
                }
            }

        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }
        return baseRsp;
    }


}