package com.jhr.controller;

import com.jhr.entity.*;
import com.jhr.service.RefactoryService;
import com.jhr.service.StyleService;
import com.jhr.service.StylefacService;
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
 * 标题： 返厂表
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 17:24
 */
@Controller
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
    @RequestMapping("/insertRefac")
    public String insertRefac(Refactory refactory){
//        BaseRsp baseRsp=new BaseRsp();
        try {
            String ss = refactory.getNumstr();
            Style style=new Style();
            style.setNumstr(ss);
            List<Style> styles = styleService.selectStyleBy(style);
            if (styles == null) { //没有这个款式
                return "error";
            }
            // 入库表插入
            refactory.setId(Sequence.getInstance().nextId());
            refactory.setFlag(1);//1 有效
            refactory.setCreatetime(new Date());
            int i = refactoryService.insertRefactory(refactory);

            //中间表插入
            Stylefac stylefac =new Stylefac();
            stylefac.setId(Sequence.getInstance().nextId());
            stylefac.setRefactoryid(refactory.getId());
            stylefac.setStyleid(styles.get(0).getId());
            int ii=stylefacService.insert(stylefac);
        }catch (Exception e){
            LOGGER.error("RefactoryController========>insertRefac失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }
        //跳转tableKC页面
        return "tableFC";
    }


    /**
     * 查询全部条目(首页显示有效状态的)
     * 有效状态的
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectRefactoryListByFlag")
    public List<Refactory> selectRefactoryListByFlag(){
//        BaseRsp baseRsp=new BaseRsp();
        Refactory refactory=new Refactory();
        List<Refactory> list =null;
        try {
            refactory.setFlag(1);
            list =refactoryService.selectRefactoryListBy(refactory);
        }catch (Exception e){
            LOGGER.error("RefactoryController========>selectRefactoryListByFlag失败", e);
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
    @RequestMapping("/selectRefactoryListByNumStr")
    public List<Refactory> selectRefactoryListByNumStr(String numstr){
//        BaseRsp baseRsp=new BaseRsp();
        List<Refactory> list =null;
        Refactory refactory=new Refactory();
        try {
            refactory.setFlag(1);
            refactory.setNumstr(numstr);
            list =refactoryService.selectRefactoryListBy(refactory);
        }catch (Exception e){
            LOGGER.error("RefactoryController========>selectRefactoryListByNumStr失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return list;
        }

        return list;
    }

    /**
     * 根据id查询
     * 详情页
     * @param id
     * @return
     */
    @RequestMapping("/selectRefactory")
    public Refactory selectRefactory(String id){
        Refactory refactory=null;

        try {
            Refactory req=new Refactory();
            if (null!=id){
                req.setId(Long.valueOf(id));
            }
            refactory=refactoryService .selectRefactoryListByPrimaryKey(req);
        }catch (Exception e){
            LOGGER.error("RefactoryController========>selectRefactory失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return refactory;
        }
        return refactory;
    }

    /**
     * 通过主键修改
     *
     * @return
     */

    @RequestMapping("/updateRefactory")
    public String updateRefactory(Refactory refactory) {
//        BaseRsp baseRsp=new BaseRsp();
        int re;
        try {
            re = refactoryService.updateByPrimaryKey(refactory);
        } catch (Exception e) {
            LOGGER.error("RefactoryController========>updateRefactory失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        //跳转tableYS页面
        return "tableFC";
    }

    /**
     * 物理删除
     * @param key
     * @return
     */
    @RequestMapping("/deleteRefactory")
    public String deleteRefactory(Long key) {
//        BaseRsp baseRsp=new BaseRsp();
        int re;
        try {
            re=refactoryService.deleteByPrimaryKey(key);
            //删除中间表的全部相关信息
           Stylefac stylefac =new Stylefac();
            stylefac.setRefactoryid(key);
            int ree= stylefacService.deleteBy(stylefac);

        } catch (Exception e) {
            LOGGER.error("RefactoryController========>deleteRefactory失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        //跳转页面
        return "tableFC";
    }
}
