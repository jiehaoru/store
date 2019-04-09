package com.jhr.controller;

import com.jhr.controller.vo.SaleVO;
import com.jhr.entity.*;
import com.jhr.service.SaleService;
import com.jhr.service.StockService;
import com.jhr.service.StyleService;
import com.jhr.service.StylesalService;
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

import javax.servlet.http.HttpSession;
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
    @Autowired
    private StockService stockService;


    /**
     * 插入 出售条目
     *
     * @param saleVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertSale", method = RequestMethod.POST)
    public BaseRsp insertSale(@RequestBody SaleVO saleVO,HttpSession session) {
        BaseRsp baseRsp = new BaseRsp();
        if (super.isLogin(session) == null) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",用户未登录");
            return baseRsp;
        }
        if (super.isLogin(session).getAuthorityid()>0){
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",用户权限不足");
            return baseRsp;
        }
        Stock stock=new Stock();//修改库存
        try {
            List<Style> styles = new ArrayList<Style>();
            List<Stock> stocks =new ArrayList<Stock>();
            // 编号非空
            String ss = saleVO.getNumstr();
            if (null != ss) {
                Style style = new Style();
                style.setNumstr(ss);
                style.setFlag(1);//有效的
                styles = styleService.selectStyleBy(style);
                if (styles.size() == 0) { //没有这个款式
                    baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                    baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",没有这个款式");
                    return baseRsp;
                }
                // 库存表查询
                stock.setNumstr(saleVO.getNumstr());
                stocks = stockService.selectStockListBy(stock);
                if (stocks.size()<=0) {
                    baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                    baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",库存表中没有这个款式");
                    return baseRsp;
                }
            }

            //出售数不为空
            if(null==saleVO.getSalenum()){
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",出售数量为空");
                return baseRsp;
            }

            //
            //出售数＜=库存数
            Stock stock1 = stocks.get(0);
            if(saleVO.getSalenum()>stock1.getNownumber()){
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",出售数量>库存数量");
                return baseRsp;
            }

            // 入库表插入
            Sale sale=new Sale();
            BeanUtils.copyProperties(saleVO,sale);
            sale.setId(Sequence.getInstance().nextId());
            sale.setFlag(1);//1 有效
            sale.setCreatetime(new Date());
            sale.setOperator(super.isLogin(session).getUsername());
            int i = saleService.insertSale(sale);
            int ii = 0;
            if (i > 0) {

                //中间表插入
                Stylesal stylesal = new Stylesal();
                stylesal.setId(Sequence.getInstance().nextId());
                stylesal.setSaleid(sale.getId());
                //有这款式
                if (styles.size() > 0) {
                    stylesal.setStyleid(styles.get(0).getId());
                }

                ii = stylesalService.insert(stylesal);

                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS + ",影响行数" + i + "," + ii);

                //
                //自动修改 现库存 数量
                stock1.setNownumber(stock1.getNownumber()-sale.getSalenum());
                stockService.updateByPrimaryKey(stock1);
            } else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",影响行数" + i + "," + ii);
            }


        } catch (Exception e) {
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
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectSaleListByFlag", method = RequestMethod.GET)
    public BaseRsp<List<SaleVO>> selectSaleListByFlag() {
        BaseRsp<List<SaleVO>> baseRsp = new BaseRsp<List<SaleVO>>();
        Sale sale = new Sale();
        List<Sale> list = null;
        List<SaleVO> listVO=new ArrayList<>();
        try {
            sale.setFlag(1);
            list = saleService.selectSaleListBy(sale);
            if (list.size()>0){
                for (Sale sale1 : list) {
                    SaleVO vo=new SaleVO();
                    BeanUtils.copyProperties(sale1,vo);
                    vo.setId(String.valueOf(sale1.getId()));
                    listVO.add(vo);
                }
            }
            baseRsp.setData(listVO);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("SaleController========>selectSaleListByFlag失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }


    /**
     * 根据+自定义编号查询有效的
     * (搜索框用)
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectSaleListByNumStr", method = RequestMethod.POST)
    public BaseRsp<List<SaleVO>> selectSaleListByNumStr(@RequestBody SaleVO saleVO) {
        BaseRsp<List<SaleVO>> baseRsp = new BaseRsp<List<SaleVO>>();
        List<Sale> list = null;
        List<SaleVO> listVO=new ArrayList<>();
        if (null == saleVO.getNumstr()) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",Numstr 为空");
            return baseRsp;
        }

        try {
            Sale sale=new Sale();
            BeanUtils.copyProperties(saleVO,sale);
            sale.setFlag(1);
            list = saleService.selectSaleListBy(sale);
            if (list.size()>0){
                for (Sale sale1 : list) {
                    SaleVO vo=new SaleVO();
                    BeanUtils.copyProperties(sale1,vo);
                    vo.setId(String.valueOf(sale1.getId()));
                    listVO.add(vo);
                }
            }
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(listVO);
        } catch (Exception e) {
            LOGGER.error("SaleController========>selectSaleListByNumStr失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 通过id查询
     *
     * @param saleVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectSale", method = RequestMethod.POST)
    public BaseRsp<SaleVO> selectSale(@RequestBody SaleVO saleVO) {
        BaseRsp<SaleVO> baseRsp = new BaseRsp<SaleVO>();

        if (null == saleVO.getId()) {
            LOGGER.error("SaleController========>selectSale失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",selectSale失败,id为空");
            return baseRsp;
        }
        try {
            SaleVO rsp = new SaleVO();
            Sale req=new Sale();
            BeanUtils.copyProperties(saleVO,req);
            req.setId(Long.valueOf(saleVO.getId()));
            List<Sale> sales = saleService.selectSaleListBy(req);
            if (sales.size() > 0) {
               Sale sale=sales.get(0);
               BeanUtils.copyProperties(sale,rsp);
               rsp.setId(String.valueOf(sale.getId()));

            }
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(rsp);
        } catch (Exception e) {
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
    @RequestMapping(value = "/updateSale", method = RequestMethod.POST)
    public BaseRsp updateSale(@RequestBody SaleVO saleVO,HttpSession session) {
        BaseRsp baseRsp = new BaseRsp();
        if (super.isLogin(session) == null) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",用户未登录");
            return baseRsp;
        }
        if (super.isLogin(session).getAuthorityid()>0){
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",用户权限不足");
            return baseRsp;
        }
        if (null == saleVO.getId()) {
            LOGGER.error("SaleController========>updateSale失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",updateSale失败,id为空");
            return baseRsp;
        }

        int re;
        try {
            Sale sale=new Sale();
            BeanUtils.copyProperties(saleVO,sale);
            sale.setId(Long.valueOf(saleVO.getId()));
            re = saleService.updateByPrimaryKey(sale);
            if (re > 0) {
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS + ",受影响行数" + re);
            } else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",修改影响行数" + re);
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
     *
     * @param saleVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteSale", method = RequestMethod.POST)
    public BaseRsp deleteSale(@RequestBody SaleVO saleVO,HttpSession session) {
        BaseRsp baseRsp = new BaseRsp();
        if (super.isLogin(session) == null) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",用户未登录");
            return baseRsp;
        }
        if (super.isLogin(session).getAuthorityid()>0){
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",用户权限不足");
            return baseRsp;
        }
        if (null == saleVO.getId()) {
            LOGGER.error("SaleController========>deleteSale失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",deleteSale失败,id为空");
            return baseRsp;
        }

        int re;
        int ree = 0;
        try {
            re = saleService.deleteByPrimaryKey(Long.valueOf(saleVO.getId()));
            if (re > 0) {
                //删除中间表的全部相关信息
                Stylesal stylesal = new Stylesal();
                stylesal.setSaleid(Long.valueOf(saleVO.getId()));
                ree = stylesalService.deleteBy(stylesal);
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS + ",影响行数" + re + "," + ree);
            } else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",影响行数" + re + "," + ree);
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
