package com.jhr.controller;

import com.jhr.entity.*;
import com.jhr.service.StyleService;
import com.jhr.service.StylewarService;
import com.jhr.service.WarehousingService;
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
 * 标题：入库表
 * * 描述：
 *
 * @author jhr
 * @create 2019/03/10 16:27
 */
@Controller
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
    @RequestMapping("/insertWare")
    public String insertWare(Warehousing warehousing){
//        BaseRsp baseRsp=new BaseRsp();
        try {
            String ss = warehousing.getNumstr();
            Style style=new Style();
            style.setNumstr(ss);
            List<Style> styles = styleService.selectStyleBy(style);
            if (styles == null) { //没有这个款式
                return "error";
            }
            // 入库表插入
            warehousing.setId(Sequence.getInstance().nextId());
            warehousing.setFlag(1);//1 有效
            warehousing.setCreatetime(new Date());
            int i = warehousingService.insertWarehousing(warehousing);

            //中间表插入
            Stylewar stylewar=new Stylewar();
            stylewar.setId(Sequence.getInstance().nextId());
            stylewar.setWarehousingid(warehousing.getId());
            stylewar.setStyleid(styles.get(0).getId());
           int ii= stylewarService.insert(stylewar);
        }catch (Exception e){
            LOGGER.error("WarehousingController========>insertWare失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }
        //跳转tableKC页面
        return "tableRK";
    }


    /**
     * 查询全部条目(首页显示有效状态的)
     * 有效状态的
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectWarehousingListByFlag")
    public List<Warehousing> selectWarehousingListByFlag(){
//        BaseRsp baseRsp=new BaseRsp();
        Warehousing warehousing=new Warehousing();
        List<Warehousing> list =null;
        try {
            warehousing.setFlag(1);
            list =warehousingService.selectWarehousingListBy(warehousing);
        }catch (Exception e){
            LOGGER.error("WarehousingController========>selectWarehousingListByFlag失败", e);
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
    @RequestMapping("/selectWarehousingListByNumStr")
    public List<Warehousing> selectWarehousingListByNumStr(String numstr){
//        BaseRsp baseRsp=new BaseRsp();
        List<Warehousing> list =null;
        Warehousing warehousing=new Warehousing();
        try {
            warehousing.setFlag(1);
            warehousing.setNumstr(numstr);
            list =warehousingService.selectWarehousingListBy(warehousing);
        }catch (Exception e){
            LOGGER.error("WarehousingController========>selectWarehousingListByNumStr失败", e);
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
    @RequestMapping("/selectWarehousing")
    public Warehousing selectWarehousing(String id){
        Warehousing warehousing=null;

        try {
            Warehousing req=new Warehousing();
            if (null!=id){
                req.setId(Long.valueOf(id));
            }
            warehousing=warehousingService .selectWarehousingListByPrimaryKey(req);
        }catch (Exception e){
            LOGGER.error("WarehousingController========>selectWarehousing失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return warehousing;
        }
        return warehousing;
    }

    /**
     * 通过主键修改
     *
     * @return
     */

    @RequestMapping("/updateWarehousing")
    public String updateWarehousing(Warehousing warehousing) {
//        BaseRsp baseRsp=new BaseRsp();
        int re;
        try {
            re = warehousingService.updateByPrimaryKey(warehousing);
        } catch (Exception e) {
            LOGGER.error("WarehousingController========>updateWarehousing失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        //跳转tableYS页面
        return "tableRK";
    }

    /**
     * 物理删除
     * @param key
     * @return
     */
    @RequestMapping("/deleteWarehousing")
    public String deleteWarehousing(Long key) {
//        BaseRsp baseRsp=new BaseRsp();
        int re;
        try {
            re=warehousingService.deleteByPrimaryKey(key);
            //删除中间表的全部相关信息
            Stylewar stylewar =new Stylewar();
            stylewar.setWarehousingid(key);
           int ree= stylewarService.deleteBy(stylewar);

        } catch (Exception e) {
            LOGGER.error("WarehousingController========>deleteWarehousing失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        //跳转页面
        return "tableRK";
    }
}
