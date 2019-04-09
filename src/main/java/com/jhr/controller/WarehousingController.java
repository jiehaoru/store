package com.jhr.controller;

import com.jhr.controller.vo.StockVO;
import com.jhr.controller.vo.WarehousingVO;
import com.jhr.entity.*;
import com.jhr.service.StyleService;
import com.jhr.service.StylewarService;
import com.jhr.service.WarehousingService;
import com.jhr.utils.Sequence;
import com.jhr.utils.base.BaseRsp;
import com.jhr.utils.base.BaseRspConstants;
import com.mchange.io.impl.LazyReadOnlyMemoryFileImpl;
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
    @Autowired
    private  StockController stockController;

    /**
     * 插入 入库条目
     * @param warehousingVO
     * @return
     */
    @RequestMapping(value = "/insertWare",method = RequestMethod.POST)
    @ResponseBody
    public BaseRsp insertWare(@RequestBody WarehousingVO warehousingVO){
        BaseRsp baseRsp=new BaseRsp();
        StockVO stock=new StockVO();//自动插入库存表
        try {
            List<Style> styles=new ArrayList<Style>();
            //
            String ss = warehousingVO.getNumstr();

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
            }else {// 为空
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",Numstr 为空");
                return baseRsp;
            }
            // 入库表插入
            Warehousing warehousing=new Warehousing();
            BeanUtils.copyProperties(warehousingVO,warehousing);
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

                //
                //自动 在库存表中记录
                stock.setNumstr(warehousing.getNumstr());
                stock.setInnumber(warehousing.getNumber());
                stock.setNownumber(warehousing.getNumber());
                stockController.insertStock(stock);

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
    public BaseRsp< List<WarehousingVO>> selectWarehousingListByFlag(){
        BaseRsp< List<WarehousingVO>> baseRsp=new BaseRsp< List<WarehousingVO>>();
        Warehousing warehousing=new Warehousing();
        List<Warehousing> list =null;
        List<WarehousingVO> listvo=new ArrayList<>();
        try {
            warehousing.setFlag(1);
            list =warehousingService.selectWarehousingListBy(warehousing);
            if (list.size()>0) {
                for (Warehousing warehousing1 : list) {
                    WarehousingVO vo=new WarehousingVO();
                    BeanUtils.copyProperties(warehousing1,vo);
                    vo.setId(String.valueOf(warehousing1.getId()));
                    listvo.add(vo);
                }
            }
            baseRsp.setData(listvo);
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
    public BaseRsp<List<WarehousingVO>> selectWarehousingListByNumStr(@RequestBody WarehousingVO warehousingVO){
        BaseRsp<List<WarehousingVO>> baseRsp=new BaseRsp<List<WarehousingVO>>();
        List<Warehousing> list =null;
        List<WarehousingVO> listvo=new ArrayList<>();
        if (null==warehousingVO.getNumstr()) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",Numstr 为空");
            return baseRsp;
        }

        try {
            Warehousing warehousing=new Warehousing();
            BeanUtils.copyProperties(warehousingVO,warehousing);
            warehousing.setFlag(1);
            list =warehousingService.selectWarehousingListBy(warehousing);
            if (list.size()>0) {
                for (Warehousing warehousing1 : list) {
                    WarehousingVO vo=new WarehousingVO();
                    BeanUtils.copyProperties(warehousing1,vo);
                    vo.setId(String.valueOf(warehousing1.getId()));
                    listvo.add(vo);
                }
            }
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(listvo);
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
     * @param warehousingVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectWarehousing",method = RequestMethod.POST)
    public BaseRsp<WarehousingVO> selectWarehousing(@RequestBody WarehousingVO warehousingVO){
        BaseRsp<WarehousingVO> baseRsp =new BaseRsp<WarehousingVO>();

        if(null==warehousingVO.getId()){
            LOGGER.error("WarehousingController========>selectWarehousing失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",selectWarehousing失败,id为空");
            return baseRsp;
        }
        try {
            Warehousing rsp=new Warehousing();
            WarehousingVO rspVO=new WarehousingVO();
            Warehousing warehousing=new Warehousing();
            BeanUtils.copyProperties(warehousingVO,warehousing);
            warehousing.setId(Long.valueOf(warehousingVO.getId()));
            rsp=warehousingService .selectWarehousingListByPrimaryKey(warehousing);

            if (null!=rsp){
                BeanUtils.copyProperties(rsp,rspVO);
                rspVO.setId(String.valueOf(rsp.getId()));
            }
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(rspVO);
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
    public BaseRsp updateWarehousing(@RequestBody WarehousingVO warehousingVO) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==warehousingVO.getId()) {
            LOGGER.error("WarehousingController========>updateWarehousing失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",updateWarehousing失败,id为空");
            return baseRsp;
        }

        int re;
        try {
            Warehousing warehousing=new Warehousing();
            BeanUtils.copyProperties(warehousingVO,warehousing);
            warehousing.setId(Long.valueOf(warehousingVO.getId()));
            re = warehousingService.updateByPrimaryKey(warehousing);
            if (re>0){
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",受影响行数"+re);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",修改影响行数"+re);
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
     * @param warehousingVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteWarehousing",method = RequestMethod.POST)
    public BaseRsp deleteWarehousing(@RequestBody WarehousingVO warehousingVO) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==warehousingVO.getId()) {
            LOGGER.error("WarehousingController========>deleteWarehousing失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",deleteWarehousing失败,id为空");
            return baseRsp;
        }

        int re;
        int ree=0;
        try {
            re=warehousingService.deleteByPrimaryKey(Long.valueOf(warehousingVO.getId()));
            if (re>0){
                //删除中间表的全部相关信息
                Stylewar stylewar =new Stylewar();
                stylewar.setWarehousingid(Long.valueOf(warehousingVO.getId()));
                 ree= stylewarService.deleteBy(stylewar);
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+re+","+ree);
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
