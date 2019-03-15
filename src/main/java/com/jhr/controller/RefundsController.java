package com.jhr.controller;

import com.jhr.entity.*;
import com.jhr.service.RefundsService;
import com.jhr.service.StyleService;
import com.jhr.service.StylefunService;
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
     * 插入 出售条目
     * @param refunds
     * @return
     */
    @RequestMapping("/insertRefunds")
    public String insertRefunds(Refunds refunds){
//        BaseRsp baseRsp=new BaseRsp();
        try {
            String ss = refunds.getNumstr();
            Style style=new Style();
            style.setNumstr(ss);
            List<Style> styles = styleService.selectStyleBy(style);
            if (styles == null) { //没有这个款式
                return "error";
            }
            // 出售表插入
            refunds.setId(Sequence.getInstance().nextId());
            refunds.setFlag(1);//1 有效
            refunds.setCreatetime(new Date());
            int i = refundsService.insertRefunds(refunds);

            //中间表插入
            Stylefun stylefun=new Stylefun();
            stylefun.setId(Sequence.getInstance().nextId());
            stylefun.setRefundsid(refunds.getId());
            stylefun.setStyleid(styles.get(0).getId());
            int ii=stylefunService.insert(stylefun);
        }catch (Exception e){
            LOGGER.error("RefundsController========>insertRefunds失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }
        //跳转tableKC页面
        return "tableTH";
    }


    /**
     * 查询全部条目(首页显示有效状态的)
     * 有效状态的
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectRefundsListByFlag")
    public List<Refunds> selectRefundsListByFlag(){
//        BaseRsp baseRsp=new BaseRsp();
        Refunds refunds=new Refunds();
        List<Refunds> list =null;
        try {
            refunds.setFlag(1);
            list =refundsService.selectRefundsListBy(refunds);
        }catch (Exception e){
            LOGGER.error("RefundsController========>selectRefundsListByFlag失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return list;
        }

        return list;
    }


    /**
     * 根据+自定义编号查询有效的
     *(搜索框用)
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectRefundsListByNumStr")
    public List<Refunds> selectRefundsListByNumStr(String numstr){
//        BaseRsp baseRsp=new BaseRsp();
        List<Refunds> list =null;
        Refunds refunds=new Refunds();
        try {
            refunds.setFlag(1);
            refunds.setNumstr(numstr);
            list =refundsService.selectRefundsListBy(refunds);
        }catch (Exception e){
            LOGGER.error("RefundsController========>selectRefundsListByNumStr失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return list;
        }

        return list;
    }

    /**
     *  通过id查询
     * @param id
     * @return
     */
    @RequestMapping("/selectRefunds")
    public Refunds selectRefunds(String id){
        Refunds refunds=null;

        try {
            Refunds req=new Refunds();
            if (null!=id){
                req.setId(Long.valueOf(id));
            }
            List<Refunds> refundss = refundsService.selectRefundsListBy(req);
            if (null !=refundss && refundss.size()>0) {
                refunds=refundss.get(0);
            }
        }catch (Exception e){
            LOGGER.error("RefundsController========>selectRefunds失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return refunds;
        }
        return refunds;
    }

    /**
     * 通过主键修改
     *
     * @return
     */

    @RequestMapping("/updateRefunds")
    public String updateRefunds(Refunds refunds) {
//        BaseRsp baseRsp=new BaseRsp();
        int re;
        try {
            re = refundsService.updateByPrimaryKey(refunds);
        } catch (Exception e) {
            LOGGER.error("RefundsController========>updateRefunds失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        //跳转页面
        return "tableTH";
    }

    /**
     * 物理删除
     * @param key
     * @return
     */
    @RequestMapping("/deleteRefunds")
    public String deleteRefunds(Long key) {
//        BaseRsp baseRsp=new BaseRsp();
        int re;
        try {
            re=refundsService.deleteByPrimaryKey(key);
            //删除中间表的全部相关信息
           Stylefun stylefun=new Stylefun();
           stylefun.setRefundsid(key);
          int i= stylefunService.deleteBy(stylefun);
        } catch (Exception e) {
            LOGGER.error("RefundsController========>deleteRefunds失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        //跳转tableYS页面
        return "tableTH";
    }


}
