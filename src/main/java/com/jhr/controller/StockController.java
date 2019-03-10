package com.jhr.controller;

import com.jhr.entity.Stock;
import com.jhr.entity.Style;
import com.jhr.entity.Stylesto;
import com.jhr.service.StockService;
import com.jhr.service.StyleService;
import com.jhr.service.StylestoService;
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
 * 标题： 库存
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 12:22
 */
@Controller
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
     * @param stock
     * @return
     */
    @RequestMapping("/insertStock")
    public String insertStock(Stock stock){
//        BaseRsp baseRsp=new BaseRsp();
        try {
            String ss = stock.getNumstr();
            Style style=new Style();
            style.setNumstr(ss);
            List<Style> styles = styleService.selectStyleBy(style);
            if (styles == null) { //没有这个款式
                return "error";
            }
            // 库存表插入
            stock.setId(Sequence.getInstance().nextId());
            stock.setFlag(1);//1 有效
            stock.setCreatetime(new Date());
            int i = stockService.insertStock(stock);

            //中间表插入
            Stylesto stylesto=new Stylesto();
            stylesto.setId(Sequence.getInstance().nextId());
            stylesto.setStockid(stock.getId());
            stylesto.setStyleid( styles.get(0).getId());
            int ii=stylestoService.insert(stylesto);
        }catch (Exception e){
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }
        //跳转tableKC页面
        return "tableKC";
    }


    /**
     * 查询全部条目(首页显示有效状态的)
     * 有效状态的
     * @return
     */
    @ResponseBody
    @RequestMapping("/selectStockListByFlag")
    public List<Stock> selectStockListByFlag(){
//        BaseRsp baseRsp=new BaseRsp();
        Stock stock=new Stock();
        List<Stock> list =null;
        try {
            stock.setFlag(1);
            list =stockService.selectStockListBy(stock);
        }catch (Exception e){
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
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
    @RequestMapping("/selectStockListByNumStr")
    public List<Stock> selectStockListByNumStr(String numstr){
//        BaseRsp baseRsp=new BaseRsp();
        List<Stock> list =null;
        Stock stock=new Stock();
        try {
            stock.setFlag(1);
            stock.setNumstr(numstr);
            list =stockService.selectStockListBy(stock);
        }catch (Exception e){
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return list;
        }

        return list;
    }

    @RequestMapping("/selectStock")
    public Stock selectStock(String id){
        Stock stock=null;

        try {
            Stock req=new Stock();
            if (null!=id){
                req.setId(Long.valueOf(id));
            }
            List<Stock> stocks = stockService.selectStockListBy(req);
            if (null !=stocks && stocks.size()>0) {
                stock=stocks.get(0);
            }
        }catch (Exception e){
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return stock;
        }
        return stock;
    }

    /**
     * 通过主键修改
     *
     * @return
     */

    @RequestMapping("/updateStock")
    public String updateStock(Stock stock) {
//        BaseRsp baseRsp=new BaseRsp();
        int re;
        try {
            re = stockService.updateByPrimaryKey(stock);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        //跳转tableYS页面
        return "tableKC";
    }

    /**
     * 物理删除
     * @param key
     * @return
     */
    @RequestMapping("/deleteStock")
    public String deleteStock(Long key) {
//        BaseRsp baseRsp=new BaseRsp();
        int re;
        try {
            re=stockService.deleteByPrimaryKey(key);
            //删除中间表的全部相关信息
            Stylesto stylesto=new Stylesto();
            stylesto.setStockid(key);
           int re2= stylestoService.deleteBy(stylesto);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        //跳转tableYS页面
        return "tableKC";
    }

}
