package com.jhr.controller;

import com.jhr.entity.*;
import com.jhr.service.SaleService;
import com.jhr.service.StyleService;
import com.jhr.service.StylesalService;
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
 * 标题：出售表
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 20:43
 */
@Controller
@RequestMapping("/lecture")
public class SaleController extends BaseController {
    public static final Logger LOGGER = LoggerFactory.getLogger(SaleController.class);

    @Autowired
    private StyleService styleService;
    @Autowired
    private SaleService saleService;
    @Autowired
    private StylesalService stylesalService;



    /**
     * 插入 出售条目
     * @param sale
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertSale",method = RequestMethod.POST)
    public BaseRsp insertSale(@RequestBody Sale sale){
        BaseRsp baseRsp=new BaseRsp();
        try {
            List<Style> styles=new ArrayList<Style>();
            //
            String ss = sale.getNumstr();
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
            sale.setId(Sequence.getInstance().nextId());
            sale.setFlag(1);//1 有效
            sale.setCreatetime(new Date());
            int i = saleService.insertSale(sale);
            int ii=0;
            if (i>0){

                //中间表插入
                Stylesal stylesal=new Stylesal();
                stylesal.setId(Sequence.getInstance().nextId());
                stylesal.setSaleid(sale.getId());
                //有这款式
                if(styles.size()>0){
                    stylesal.setStyleid(styles.get(0).getId());
                }
                ii= stylesalService.insert(stylesal);

                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+i+","+ii);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+i+","+ii);
            }


        }catch (Exception e){
            LOGGER.error("SaleController========>insertSale失败", e);
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
    @RequestMapping(value = "/selectSaleListByFlag",method = RequestMethod.GET)
    public BaseRsp<List<Sale>> selectSaleListByFlag(){
        BaseRsp<List<Sale>> baseRsp=new BaseRsp<List<Sale>>();
        Sale sale=new Sale();
        List<Sale> list =null;
        try {
            sale.setFlag(1);
            list =saleService.selectSaleListBy(sale);
            baseRsp.setData(list);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        }catch (Exception e){
            LOGGER.error("SaleController========>selectSaleListByFlag失败", e);
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
    @RequestMapping(value = "/selectSaleListByNumStr",method = RequestMethod.POST)
    public BaseRsp<List<Sale>> selectSaleListByNumStr(@RequestBody Sale sale){
        BaseRsp<List<Sale>> baseRsp=new BaseRsp<List<Sale>>();
        List<Sale> list =null;
        if (null==sale.getNumstr()) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",Numstr 为空");
            return baseRsp;
        }

        try {
            sale.setFlag(1);
            list =saleService.selectSaleListBy(sale);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(list);
        }catch (Exception e){
            LOGGER.error("SaleController========>selectSaleListByNumStr失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     *  通过id查询
     * @param sale
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectSale",method = RequestMethod.POST)
    public BaseRsp<Sale> selectSale(@RequestBody Sale sale){
        BaseRsp<Sale> baseRsp =new BaseRsp<Sale>();

        if(null==sale.getId()){
            LOGGER.error("SaleController========>selectSale失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",selectSale失败,id为空");
            return baseRsp;
        }
        try {
            Sale rsp=new Sale();
            List<Sale> sales = saleService.selectSaleListBy(sale);
            if (sales.size()>0){
                rsp=sales.get(0);
            }
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(rsp);
        }catch (Exception e){
            LOGGER.error("SaleController========>selectSale失败", e);
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
    @RequestMapping(value = "/updateSale",method = RequestMethod.POST)
    public BaseRsp updateSale(@RequestBody Sale sale) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==sale.getId()) {
            LOGGER.error("SaleController========>updateSale失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",updateSale失败,id为空");
            return baseRsp;
        }

        int re;
        try {
            re = saleService.updateByPrimaryKey(sale);
            if (re>0){
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",受影响行数"+re);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",修改影响行数"+re);
            }
        } catch (Exception e) {
            LOGGER.error("SaleController========>updateSale失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 物理删除
     * @param sale
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteSale",method = RequestMethod.POST)
    public BaseRsp deleteSale(@RequestBody Sale sale) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==sale.getId()) {
            LOGGER.error("SaleController========>deleteSale失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",deleteSale失败,id为空");
            return baseRsp;
        }

        int re;
        int ree=0;
        try {
            re=saleService.deleteByPrimaryKey(sale.getId());
            if (re>0){
                //删除中间表的全部相关信息
                Stylesal stylesal=new Stylesal();
                stylesal.setSaleid(sale.getId());
                ree= stylesalService.deleteBy(stylesal);
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespCode(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+re+","+ree);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+re+","+ree);
            }


        } catch (Exception e) {
            LOGGER.error("SaleController========>deleteSale失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }
}
