package com.jhr.controller;

import com.jhr.entity.*;
import com.jhr.service.SaleService;
import com.jhr.service.StyleService;
import com.jhr.service.StylesalService;
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
    @RequestMapping("/insertSale")
    public String insertSale(Sale sale){
//        BaseRsp baseRsp=new BaseRsp();
        try {
            String ss = sale.getNumstr();
            Style style=new Style();
            style.setNumstr(ss);
            List<Style> styles = styleService.selectStyleBy(style);
            if (styles == null) { //没有这个款式
                return "error";
            }
            // 出售表插入
            sale.setId(Sequence.getInstance().nextId());
            sale.setFlag(1);//1 有效
            sale.setCreatetime(new Date());
            int i = saleService.insertSale(sale);

            //中间表插入
            Stylesal stylesal=new Stylesal();
            stylesal.setId(Sequence.getInstance().nextId());
            stylesal.setSaleid(sale.getId());
            stylesal.setStyleid(styles.get(0).getId());
            int ii=stylesalService.insert(stylesal);
        }catch (Exception e){
            LOGGER.error("SaleController========>insertSale失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }
        //跳转tableKC页面
        return "tableCS";
    }


    /**
     * 查询全部条目(首页显示有效状态的)
     * 有效状态的
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectSaleListByFlag")
    public List<Sale> selectSaleListByFlag(){
//        BaseRsp baseRsp=new BaseRsp();
        Sale sale=new Sale();
        List<Sale> list =null;
        try {
            sale.setFlag(1);
            list =saleService.selectSaleListBy(sale);
        }catch (Exception e){
            LOGGER.error("SaleController========>selectSaleListByFlag失败", e);
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
    @RequestMapping("/selectSaleListByNumStr")
    public List<Sale> selectSaleListByNumStr(String numstr){
//        BaseRsp baseRsp=new BaseRsp();
        List<Sale> list =null;
        Sale sale=new Sale();
        try {
            sale.setFlag(1);
            sale.setNumstr(numstr);
            list =saleService.selectSaleListBy(sale);
        }catch (Exception e){
            LOGGER.error("SaleController========>selectSaleListByNumStr失败", e);
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
    @RequestMapping("/selectSale")
    public Sale selectSale(String id){
       Sale sale=null;

        try {
            Sale req=new Sale();
            if (null!=id){
                req.setId(Long.valueOf(id));
            }
            List<Sale> sales = saleService.selectSaleListBy(req);
            if (null !=sales && sales.size()>0) {
                sale=sales.get(0);
            }
        }catch (Exception e){
            LOGGER.error("SaleController========>selectSale失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return sale;
        }
        return sale;
    }

    /**
     * 通过主键修改
     *
     * @return
     */

    @RequestMapping("/updateSale")
    public String updateSale(Sale sale) {
//        BaseRsp baseRsp=new BaseRsp();
        int re;
        try {
            re = saleService.updateByPrimaryKey(sale);
        } catch (Exception e) {
            LOGGER.error("SaleController========>updateSale失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        //跳转页面
        return "tableCS";
    }

    /**
     * 物理删除
     * @param key
     * @return
     */
    @RequestMapping("/deleteSale")
    public String deleteSale(Long key) {
//        BaseRsp baseRsp=new BaseRsp();
        int re;
        try {
            re=saleService.deleteByPrimaryKey(key);
            //删除中间表的全部相关信息
            Stylesal stylesal=new Stylesal();
            stylesal.setSaleid(key);
            int i=stylesalService.deleteBy(stylesal);
        } catch (Exception e) {
            LOGGER.error("SaleController========>deleteSale失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        //跳转tableYS页面
        return "tableCS";
    }
}
