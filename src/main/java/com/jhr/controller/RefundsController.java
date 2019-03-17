package com.jhr.controller;

import com.jhr.entity.*;
import com.jhr.service.RefundsService;
import com.jhr.service.StyleService;
import com.jhr.service.StylefunService;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <br>
 * 标题： 退货表
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 21:24
 */
@Controller
@RequestMapping("/lecture")
public class RefundsController extends BaseController {
    public static final Logger LOGGER = LoggerFactory.getLogger(RefundsController.class);

    @Autowired
    private StyleService styleService;
    @Autowired
    private RefundsService refundsService;
    @Autowired
    private StylefunService stylefunService;

    /**
     * 插入 退货条目
     * @param refunds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertRefunds",method = RequestMethod.POST)
    public BaseRsp insertRefunds(@RequestBody Refunds refunds){
        BaseRsp baseRsp=new BaseRsp();
        try {
            List<Style> styles=new ArrayList<Style>();
            //
            String ss = refunds.getNumstr();
            if (null!=ss) {
                Style style=new Style();
                style.setNumstr(ss);
                styles = styleService.selectStyleBy(style);
                if (styles.size()==0) { //没有这个款式
                    baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                    baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",没有这个款式");
                    return baseRsp;
                }
            }
            // 返厂表插入
            refunds.setId(Sequence.getInstance().nextId());
            refunds.setFlag(1);//1 有效
            refunds.setCreatetime(new Date());
            int i = refundsService.insertRefunds(refunds);
            int ii=0;
            if (i>0){

                //中间表插入
               Stylefun stylefun=new Stylefun();
                stylefun.setId(Sequence.getInstance().nextId());
                stylefun.setRefundsid(refunds.getId());
                //有这款式
                if(styles.size()>0){
                    stylefun.setStyleid(styles.get(0).getId());
                }
                ii= stylefunService.insert(stylefun);

                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+i+","+ii);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+i+","+ii);
            }


        }catch (Exception e){
            LOGGER.error("RefundsController========>insertRefunds失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }


    /**
     * 查询全部条目(首页显示有效状态的)
     * 有效状态的
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectRefundsListByFlag",method = RequestMethod.GET)
    public BaseRsp<List<Refunds>> selectRefundsListByFlag(){
        BaseRsp<List<Refunds>> baseRsp=new BaseRsp<List<Refunds>>();
        Refunds refunds=new Refunds();
        List<Refunds> list =null;
        try {
            refunds.setFlag(1);
            list =refundsService.selectRefundsListBy(refunds);
            baseRsp.setData(list);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        }catch (Exception e){
            LOGGER.error("RefundsController========>selectRefundsListByFlag失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }


    /**
     * 根据+自定义编号查询有效的
     *(搜索框用)
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectRefundsListByNumStr",method = RequestMethod.POST)
    public BaseRsp<List<Refunds>> selectRefundsListByNumStr(@RequestBody Refunds refunds){
        BaseRsp<List<Refunds>> baseRsp=new BaseRsp<List<Refunds>>();
        List<Refunds> list =null;
        if (null==refunds.getNumstr()) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",Numstr 为空");
            return baseRsp;
        }

        try {
            refunds.setFlag(1);
            list =refundsService.selectRefundsListBy(refunds);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(list);
        }catch (Exception e){
            LOGGER.error("RefundsController========>selectRefundsListByNumStr失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     *  通过id查询
     * @param refunds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectRefunds",method = RequestMethod.POST)
    public BaseRsp<Refunds> selectRefunds(@RequestBody Refunds refunds){
        BaseRsp<Refunds> baseRsp =new BaseRsp<Refunds>();

        if(null==refunds.getId()){
            LOGGER.error("RefundsController========>selectRefunds失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",selectRefunds失败,id为空");
            return baseRsp;
        }
        try {
            List<Refunds> refunds1 = refundsService.selectRefundsListBy(refunds);
            Refunds rsp=new Refunds();
            if (refunds1.size()>0){
                rsp=refunds1.get(0);
            }
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(rsp);
        }catch (Exception e){
            LOGGER.error("RefundsController========>selectRefunds失败", e);
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
    @RequestMapping(value = "/updateRefunds",method = RequestMethod.POST)
    public BaseRsp updateRefunds(@RequestBody Refunds refunds) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==refunds.getId()) {
            LOGGER.error("RefundsController========>updateRefunds失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",updateRefunds失败,id为空");
            return baseRsp;
        }

        int re;
        try {
            re = refundsService.updateByPrimaryKey(refunds);
            if (re>0){
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",受影响行数"+re);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",修改影响行数"+re);
            }
        } catch (Exception e) {
            LOGGER.error("RefundsController========>updateRefunds失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 物理删除
     * @param refunds
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteRefunds",method = RequestMethod.POST)
    public BaseRsp deleteRefunds(@RequestBody Refunds refunds) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==refunds.getId()) {
            LOGGER.error("RefundsController========>deleteRefunds失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",deleteRefunds失败,id为空");
            return baseRsp;
        }

        int re;
        int ree=0;
        try {
            re=refundsService.deleteByPrimaryKey(refunds.getId());
            if (re>0){
                //删除中间表的全部相关信息
               Stylefun stylefun=new Stylefun();
                stylefun.setRefundsid(refunds.getId());
                ree= stylefunService.deleteBy(stylefun);
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespCode(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+re+","+ree);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+re+","+ree);
            }


        } catch (Exception e) {
            LOGGER.error("RefundsController========>deleteRefunds失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }


}
