package com.jhr.controller;

import com.jhr.controller.vo.RefactoryVO;
import com.jhr.entity.*;
import com.jhr.service.RefactoryService;
import com.jhr.service.StockService;
import com.jhr.service.StyleService;
import com.jhr.service.StylefacService;
import com.jhr.utils.Sequence;
import com.jhr.utils.base.BaseRsp;
import com.jhr.utils.base.BaseRspConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
    @Autowired
    private StockService stockService;

    /**
     * 插入 返厂条目
     * @param refactoryVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertRefac",method = RequestMethod.POST)
    public BaseRsp insertRefac(@RequestBody RefactoryVO refactoryVO){
        BaseRsp baseRsp=new BaseRsp();
        Stock stock=new Stock();//修改库存
        try {
            List<Style> styles=new ArrayList<Style>();
            List<Stock> stocks =new ArrayList<Stock>();
            //
            String ss = refactoryVO.getNumstr();
            if (null!=ss) {
                Style style=new Style();
                style.setNumstr(ss);
                style.setFlag(1);//有效的
                styles = styleService.selectStyleBy(style);
                if (styles.size()==0) { //没有这个款式
                    baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                    baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",没有这个款式");
                    return baseRsp;
                }
                // 库存表查询
                stock.setNumstr(refactoryVO.getNumstr());
                stocks = stockService.selectStockListBy(stock);
                if (stocks.size()<=0) {
                    baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                    baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",库存表中没有这个款式");
                    return baseRsp;
                }
            }

            //返厂数不为空
            if(null==refactoryVO.getRefnum()){
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",返厂数量为空");
                return baseRsp;
            }

            //
            //返厂数＜=库存数
            Stock stock1 = stocks.get(0);
            if(refactoryVO.getRefnum()>stock1.getNownumber()){
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",返厂数量>库存数量");
                return baseRsp;
            }
            // 返厂表插入
            Refactory refactory=new Refactory();
            BeanUtils.copyProperties(refactoryVO,refactory);
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
                //
                //自动修改 现库存 数量
                stock1.setNownumber(stock1.getNownumber()-refactory.getRefnum());
                stockService.updateByPrimaryKey(stock1);
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
    public BaseRsp<List<RefactoryVO>> selectRefactoryListByFlag(){
        BaseRsp<List<RefactoryVO>> baseRsp=new BaseRsp<List<RefactoryVO>>();
        Refactory refactory=new Refactory();
        List<Refactory> list =null;
        List<RefactoryVO> listVO=new ArrayList<>();
        try {
            refactory.setFlag(1);
            list =refactoryService.selectRefactoryListBy(refactory);
            if (list.size()>0){
                for (Refactory refactory1 : list) {
                    RefactoryVO vo=new RefactoryVO();
                    BeanUtils.copyProperties(refactory1,vo);
                    vo.setId(String.valueOf(refactory1.getId()));
                    listVO.add(vo);
                }
            }
            baseRsp.setData(listVO);
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
    public BaseRsp<List<RefactoryVO>> selectRefactoryListByNumStr(@RequestBody RefactoryVO refactoryVO){
        BaseRsp<List<RefactoryVO>> baseRsp=new BaseRsp<List<RefactoryVO>>();
        List<Refactory> list =null;
        List<RefactoryVO> listVO=new ArrayList<>();
        if (null==refactoryVO.getNumstr()) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",Numstr 为空");
            return baseRsp;
        }

        try {
            Refactory refactory=new Refactory();
            BeanUtils.copyProperties(refactoryVO,refactory);
            refactory.setFlag(1);
            list =refactoryService.selectRefactoryListBy(refactory);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            if (list.size()>0){
                for (Refactory refactory1 : list) {
                    RefactoryVO vo=new RefactoryVO();
                    BeanUtils.copyProperties(refactory1,vo);
                    vo.setId(String.valueOf(refactory1.getId()));
                    listVO.add(vo);
                }
            }
            baseRsp.setData(listVO);
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
     * @param refactoryVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectRefactory",method = RequestMethod.POST)
    public BaseRsp<RefactoryVO> selectRefactory(@RequestBody RefactoryVO refactoryVO){
        BaseRsp<RefactoryVO> baseRsp =new BaseRsp<RefactoryVO>();

        if(null==refactoryVO.getId()){
            LOGGER.error("RefactoryController========>selectRefactory失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",selectRefactory失败,id为空");
            return baseRsp;
        }
        try {
            Refactory refactory=new Refactory();
            BeanUtils.copyProperties(refactoryVO,refactory);
            refactory.setId(Long.valueOf(refactoryVO.getId()));
            Refactory refactory1 = refactoryService.selectRefactoryListByPrimaryKey(refactory);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            RefactoryVO rspVO=new RefactoryVO();
            BeanUtils.copyProperties(refactory1,rspVO);
            rspVO.setId(String.valueOf(refactory1.getId()));
            baseRsp.setData(rspVO);
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
    public BaseRsp updateRefactory(@RequestBody RefactoryVO refactoryVO) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==refactoryVO.getId()) {
            LOGGER.error("RefactoryController========>updateRefactory失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",updateRefactory失败,id为空");
            return baseRsp;
        }

        int re;
        try {
            Refactory refactory=new Refactory();
            BeanUtils.copyProperties(refactoryVO,refactory);
            refactory.setId(Long.valueOf(refactoryVO.getId()));
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
     * @param refactoryVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteRefactory",method = RequestMethod.POST)
    public BaseRsp deleteRefactory(@RequestBody RefactoryVO refactoryVO) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==refactoryVO.getId()) {
            LOGGER.error("RefactoryController========>deleteRefactory失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",deleteRefactory失败,id为空");
            return baseRsp;
        }

        int re;
        int ree=0;
        try {
            re=refactoryService.deleteByPrimaryKey(Long.valueOf(refactoryVO.getId()));
            if (re>0){
                //删除中间表的全部相关信息
               Stylefac stylefac=new Stylefac();
                stylefac.setRefactoryid(Long.valueOf(refactoryVO.getId()));
                ree= stylefacService.deleteBy(stylefac);
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+re+","+ree);
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
