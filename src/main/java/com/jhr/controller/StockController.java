package com.jhr.controller;

import com.jhr.controller.vo.StockVO;
import com.jhr.entity.*;
import com.jhr.service.StockService;
import com.jhr.service.StyleService;
import com.jhr.service.StylestoService;
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
 * 标题： 库存
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 12:22
 */
@Controller
@RequestMapping("/lecture")
public class StockController extends BaseController {

    public static final Logger LOGGER=LoggerFactory.getLogger(StockController.class);
    @Autowired
    private StockService stockService;
    @Autowired
    private StyleService styleService;
    @Autowired
    private StylestoService stylestoService;


    /**
     * 插入 库存条目
     * @param stockVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertStock",method = RequestMethod.POST)
    public BaseRsp insertStock(@RequestBody StockVO stockVO){
        BaseRsp baseRsp=new BaseRsp();

        try {
            List<Style> styles=new ArrayList<Style>();
            // .
            String ss = stockVO.getNumstr();
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
            }
            // 入库表插入
            Stock stock=new Stock();
            BeanUtils.copyProperties(stockVO,stock);
            stock.setId(Sequence.getInstance().nextId());
            stock.setFlag(1);//1 有效
            stock.setCreatetime(new Date());
            int i = stockService.insertStock(stock);
            int ii=0;
            if (i>0){

                //中间表插入
               Stylesto stylesto=new Stylesto();
                stylesto.setId(Sequence.getInstance().nextId());
                stylesto.setStockid(stock.getId());
                //有这款式
                if(styles.size()>0){
                    stylesto.setStyleid(styles.get(0).getId());
                }
                ii= stylestoService.insert(stylesto);

                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+i+","+ii);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+i+","+ii);
            }


        }catch (Exception e){
            LOGGER.error("StockController========>insertStock失败", e);
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
    @RequestMapping(value = "/selectStockListByFlag",method = RequestMethod.GET)
    public BaseRsp< List<StockVO>> selectStockListByFlag(){


        BaseRsp< List<StockVO>> baseRsp=new BaseRsp< List<StockVO>>();
        List<Stock> list =null;
        List<StockVO> listvo=new ArrayList<>();
        Stock stoc=new Stock();
        try {
            stoc.setFlag(1);
            list =stockService.selectStockListBy(stoc);
            if (list.size()>0){
                for (Stock stock : list) {
                    StockVO vo=new StockVO();
                    BeanUtils.copyProperties(stock,vo);
                    vo.setId(String.valueOf(stock.getId()));
                    listvo.add(vo);
                }
            }
            baseRsp.setData(listvo);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        }catch (Exception e){
            LOGGER.error("StockController========>selectStockListByFlag失败", e);
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
    @RequestMapping(value = "/selectStockListByNumStr",method = RequestMethod.POST)
    public BaseRsp<List<StockVO>> selectStockListByNumStr(@RequestBody StockVO stockVO ){
        BaseRsp<List<StockVO>> baseRsp=new BaseRsp<List<StockVO>>();
        List<Stock> list =null;
        if (null==stockVO.getNumstr()) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",Numstr 为空");
            return baseRsp;
        }

        try {
            Stock stock=new Stock();
            BeanUtils.copyProperties(stockVO,stock);
            stock.setFlag(1);
            list =stockService.selectStockListBy(stock);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            List<StockVO> listvo=new ArrayList<>();
            if (list.size()>0){
                for (Stock stock1 : list) {
                    StockVO vo=new StockVO();
                    BeanUtils.copyProperties(stock1,vo);
                    vo.setId(String.valueOf(stock1.getId()));
                    listvo.add(vo);
                }
            }
            baseRsp.setData(listvo);
        }catch (Exception e){
            LOGGER.error("StockController========>selectStockListByNumStr失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 根据id查询
     * @param stockVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectStock",method = RequestMethod.POST)
    public BaseRsp<StockVO> selectStock(@RequestBody StockVO stockVO){
        BaseRsp<StockVO> baseRsp=new BaseRsp<StockVO>();
        if(null==stockVO.getId()){
            LOGGER.error("StockController========>selectStock失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",selectStock失败,id为空");
            return baseRsp;
        }
        try {
            StockVO rspVO=new StockVO();
            Stock req=new Stock();
            req.setId(Long.valueOf(stockVO.getId()));
            List<Stock> stocks = stockService.selectStockListBy(req);
            if (stocks.size()>0) {
                Stock stock = stocks.get(0);
                BeanUtils.copyProperties(stock,rspVO);
                rspVO.setId(String.valueOf(stock.getId()));
            }
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(rspVO);
        }catch (Exception e){
            LOGGER.error("StockController========>selectStock失败", e);
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
    @RequestMapping(value = "/updateStock",method = RequestMethod.POST)
    public BaseRsp updateStock(@RequestBody StockVO stockVO) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==stockVO.getId()) {
            LOGGER.error("StockController========>updateStock失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",updateStock失败,id为空");
            return baseRsp;
        }

        int re;
        try {
            Stock stock=new Stock();
            BeanUtils.copyProperties(stockVO,stock);
            stock.setId(Long.valueOf(stockVO.getId()));
            re = stockService.updateByPrimaryKey(stock);
            if (re>0){
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",受影响行数"+re);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",修改影响行数"+re);
            }
        } catch (Exception e) {
            LOGGER.error("StockController========>updateStock失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 物理删除
     * @param stockVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteStock",method = RequestMethod.POST)
    public BaseRsp deleteStock(@RequestBody StockVO stockVO) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==stockVO.getId()) {
            LOGGER.error("StockController========>deleteWarehousing失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",deleteWarehousing失败,id为空");
            return baseRsp;
        }

        int re;
        int ree=0;
        try {
            re=stockService.deleteByPrimaryKey(Long.valueOf(stockVO.getId()));
            if (re>0){
                //删除中间表的全部相关信息
                Stylesto stylesto=new Stylesto();
                stylesto.setStockid(Long.valueOf(stockVO.getId()));
                ree= stylestoService.deleteBy(stylesto);
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespCode(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+re+","+ree);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+re+","+ree);
            }


        } catch (Exception e) {
            LOGGER.error("StockController========>deleteStock失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

}
