package com.jhr.controller;

import com.jhr.entity.*;
import com.jhr.service.RefactoryService;
import com.jhr.service.StyleService;
import com.jhr.service.StylefacService;
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
 * 标题： 返厂表
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 17:24
 */
@Controller
@RequestMapping("/lecture")
public class RefactoryController extends BaseController {

    public static final Logger LOGGER=LoggerFactory.getLogger(RefactoryController.class);

    @Autowired
    private StyleService styleService;
    @Autowired
    private RefactoryService refactoryService;
    @Autowired
    private StylefacService stylefacService;

    /**
     * 插入 返厂条目
     * @param refactory
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertRefac",method = RequestMethod.POST)
    public BaseRsp insertRefac(@RequestBody Refactory refactory){
        BaseRsp baseRsp=new BaseRsp();
        try {
            List<Style> styles=new ArrayList<Style>();
            //
            String ss = refactory.getNumstr();
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
            refactory.setId(Sequence.getInstance().nextId());
            refactory.setFlag(1);//1 有效
            refactory.setCreatetime(new Date());
            int i = refactoryService.insertRefactory(refactory);
            int ii=0;
            if (i>0){

                //中间表插入
                Stylefac stylefac=new Stylefac();
                stylefac.setId(Sequence.getInstance().nextId());
                stylefac.setRefactoryid(refactory.getId());
                //有这款式
                if(styles.size()>0){
                    stylefac.setStyleid(styles.get(0).getId());
                }
                ii= stylefacService.insert(stylefac);

                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+i+","+ii);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+i+","+ii);
            }


        }catch (Exception e){
            LOGGER.error("RefactoryController========>insertRefac失败", e);
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
    @RequestMapping(value = "/selectRefactoryListByFlag",method = RequestMethod.GET)
    public BaseRsp<List<Refactory>> selectRefactoryListByFlag(){
        BaseRsp<List<Refactory>> baseRsp=new BaseRsp<List<Refactory>>();
        Refactory refactory=new Refactory();
        List<Refactory> list =null;
        try {
            refactory.setFlag(1);
            list =refactoryService.selectRefactoryListBy(refactory);
            baseRsp.setData(list);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        }catch (Exception e){
            LOGGER.error("RefactoryController========>selectRefactoryListByFlag失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;    }


    /**
     * 根据+自定义编号查询有效的
     *(搜索框用)
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectRefactoryListByNumStr",method = RequestMethod.POST)
    public BaseRsp<List<Refactory>> selectRefactoryListByNumStr(@RequestBody Refactory refactory){
        BaseRsp<List<Refactory>> baseRsp=new BaseRsp<List<Refactory>>();
        List<Refactory> list =null;
        if (null==refactory.getNumstr()) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",Numstr 为空");
            return baseRsp;
        }

        try {
            refactory.setFlag(1);
            list =refactoryService.selectRefactoryListBy(refactory);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(list);
        }catch (Exception e){
            LOGGER.error("RefactoryController========>selectRefactoryListByNumStr失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 根据id查询
     * 详情页
     * @param refactory
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectRefactory",method = RequestMethod.POST)
    public BaseRsp<Refactory> selectRefactory(@RequestBody Refactory refactory){
        BaseRsp<Refactory> baseRsp =new BaseRsp<Refactory>();

        if(null==refactory.getId()){
            LOGGER.error("RefactoryController========>selectRefactory失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",selectRefactory失败,id为空");
            return baseRsp;
        }
        try {

            Refactory refactory1 = refactoryService.selectRefactoryListByPrimaryKey(refactory);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(refactory1);
        }catch (Exception e){
            LOGGER.error("RefactoryController========>selectRefactory失败", e);
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
    @RequestMapping(value = "/updateRefactory",method = RequestMethod.POST)
    public BaseRsp updateRefactory(@RequestBody Refactory refactory) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==refactory.getId()) {
            LOGGER.error("RefactoryController========>updateRefactory失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",updateRefactory失败,id为空");
            return baseRsp;
        }

        int re;
        try {
            re = refactoryService.updateByPrimaryKey(refactory);
            if (re>0){
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",受影响行数"+re);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",修改影响行数"+re);
            }
        } catch (Exception e) {
            LOGGER.error("RefactoryController========>updateRefactory失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 物理删除
     * @param refactory
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteRefactory",method = RequestMethod.POST)
    public BaseRsp deleteRefactory(@RequestBody Refactory refactory) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==refactory.getId()) {
            LOGGER.error("RefactoryController========>deleteRefactory失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",deleteRefactory失败,id为空");
            return baseRsp;
        }

        int re;
        int ree=0;
        try {
            re=refactoryService.deleteByPrimaryKey(refactory.getId());
            if (re>0){
                //删除中间表的全部相关信息
               Stylefac stylefac=new Stylefac();
                stylefac.setRefactoryid(refactory.getId());
                ree= stylefacService.deleteBy(stylefac);
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespCode(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+re+","+ree);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+re+","+ree);
            }


        } catch (Exception e) {
            LOGGER.error("RefactoryController========>deleteRefactory失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }
}
