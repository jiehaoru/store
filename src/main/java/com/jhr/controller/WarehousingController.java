package com.jhr.controller;

import com.jhr.entity.*;
import com.jhr.service.StyleService;
import com.jhr.service.StylewarService;
import com.jhr.service.WarehousingService;
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
 * 标题：入库表
 * * 描述：
 *
 * @author jhr
 * @create 2019/03/10 16:27
 */
@Controller
@RequestMapping("/lecture")
public class  WarehousingController extends  BaseController {

    public static final Logger LOGGER=LoggerFactory.getLogger(WarehousingController.class);

    @Autowired
    private StyleService styleService;
    @Autowired
    private WarehousingService warehousingService;
    @Autowired
    private StylewarService stylewarService;

    /**
     * 插入 入库条目
     * @param warehousing
     * @return
     */
    @RequestMapping(value = "/insertWare",method = RequestMethod.POST)
    @ResponseBody
    public BaseRsp insertWare(@RequestBody Warehousing warehousing){
        BaseRsp baseRsp=new BaseRsp();
        try {
            List<Style> styles=new ArrayList<Style>();
            //
            String ss = warehousing.getNumstr();
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
            // 入库表插入
            warehousing.setId(Sequence.getInstance().nextId());
            warehousing.setFlag(1);//1 有效
            warehousing.setCreatetime(new Date());
            int i = warehousingService.insertWarehousing(warehousing);
            int ii=0;
            if (i>0){

                //中间表插入
                Stylewar stylewar=new Stylewar();
                stylewar.setId(Sequence.getInstance().nextId());
                stylewar.setWarehousingid(warehousing.getId());
                //有这款式
                if(styles.size()>0){
                    stylewar.setStyleid(styles.get(0).getId());
                }
                 ii= stylewarService.insert(stylewar);

                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+i+","+ii);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+i+","+ii);
            }


        }catch (Exception e){
            LOGGER.error("WarehousingController========>insertWare失败", e);
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
    @RequestMapping(value = "/selectWarehousingListByFlag",method = RequestMethod.GET)
    public BaseRsp< List<Warehousing>> selectWarehousingListByFlag(){
        BaseRsp< List<Warehousing>> baseRsp=new BaseRsp< List<Warehousing>>();
        Warehousing warehousing=new Warehousing();
        List<Warehousing> list =null;
        try {
            warehousing.setFlag(1);
            list =warehousingService.selectWarehousingListBy(warehousing);
            baseRsp.setData(list);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        }catch (Exception e){
            LOGGER.error("WarehousingController========>selectWarehousingListByFlag失败", e);
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
    @RequestMapping(value = "/selectWarehousingListByNumStr",method = RequestMethod.POST)
    public BaseRsp<List<Warehousing>> selectWarehousingListByNumStr(@RequestBody Warehousing warehousing){
        BaseRsp<List<Warehousing>> baseRsp=new BaseRsp<List<Warehousing>>();
        List<Warehousing> list =null;
        if (null==warehousing.getNumstr()) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",Numstr 为空");
            return baseRsp;
        }

        try {
            warehousing.setFlag(1);
            list =warehousingService.selectWarehousingListBy(warehousing);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(list);
        }catch (Exception e){
            LOGGER.error("WarehousingController========>selectWarehousingListByNumStr失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 根据id查询
     * 详情页
     * @param warehousing
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectWarehousing",method = RequestMethod.POST)
    public BaseRsp<Warehousing> selectWarehousing(@RequestBody Warehousing warehousing){
        BaseRsp<Warehousing> baseRsp =new BaseRsp<Warehousing>();

        if(null==warehousing.getId()){
            LOGGER.error("WarehousingController========>selectWarehousing失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",selectWarehousing失败,id为空");
            return baseRsp;
        }
        try {
            Warehousing rsp=new Warehousing();
            rsp=warehousingService .selectWarehousingListByPrimaryKey(warehousing);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(rsp);
        }catch (Exception e){
            LOGGER.error("WarehousingController========>selectWarehousing失败", e);
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
    @RequestMapping(value = "/updateWarehousing",method = RequestMethod.POST)
    public BaseRsp updateWarehousing(@RequestBody Warehousing warehousing) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==warehousing.getId()) {
            LOGGER.error("WarehousingController========>updateWarehousing失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",updateWarehousing失败,id为空");
            return baseRsp;
        }

        int re;
        try {
            re = warehousingService.updateByPrimaryKey(warehousing);
            if (re>0){
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",受影响行数"+re);
            }
        } catch (Exception e) {
            LOGGER.error("WarehousingController========>updateWarehousing失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 物理删除
     * @param warehousing
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteWarehousing",method = RequestMethod.POST)
    public BaseRsp deleteWarehousing(@RequestBody Warehousing warehousing) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==warehousing.getId()) {
            LOGGER.error("WarehousingController========>deleteWarehousing失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",deleteWarehousing失败,id为空");
            return baseRsp;
        }

        int re;
        int ree=0;
        try {
            re=warehousingService.deleteByPrimaryKey(warehousing.getId());
            if (re>0){
                //删除中间表的全部相关信息
                Stylewar stylewar =new Stylewar();
                stylewar.setWarehousingid(warehousing.getId());
                 ree= stylewarService.deleteBy(stylewar);
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespCode(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+re+","+ree);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+re+","+ree);
            }


        } catch (Exception e) {
            LOGGER.error("WarehousingController========>deleteWarehousing失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }
}
