package com.jhr.controller;

import com.jhr.controller.vo.RefundsVO;
import com.jhr.entity.*;
import com.jhr.service.*;
import com.jhr.utils.Sequence;
import com.jhr.utils.base.BaseRsp;
import com.jhr.utils.base.BaseRspConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
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
 * 标题： 退货表
 * 描述：
 *
 * @author jhr
 * @create 2019/03/10 21:24
 */
@Controller
@RequestMapping("/lecture")
public class RefundsController extends BaseController {
    public static final Logger LOGGER = LoggerFactory.getLogger(RefundsController.class);

    @Autowired
    private StyleService styleService;
    @Autowired
    private RefundsService refundsService;
    @Autowired
    private StylefunService stylefunService;
    @Autowired
    private StockService stockService;
    @Autowired
    private SaleService saleService;

    /**
     * 插入 退货条目
     * @param refundsVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertRefunds",method = RequestMethod.POST)
    public BaseRsp insertRefunds(@RequestBody RefundsVO refundsVO,HttpSession session){
        BaseRsp baseRsp=new BaseRsp();
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
        try {
            List<Style> styles=new ArrayList<Style>();
            Stock stock=new Stock();//修改库存
            List<Stock> stocks =new ArrayList<Stock>();
            //
            String ss = refundsVO.getNumstr();
            if (ss == null) {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",自定义编号不能为空");
                return baseRsp;
            }
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
                stock.setNumstr(refundsVO.getNumstr());
                stocks = stockService.selectStockListBy(stock);
                if (stocks.size()<=0) {
                    baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                    baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",库存表中没有这个款式");
                    return baseRsp;
                }
            }
            //退货数不为空
            if(null==refundsVO.getRetnum()){
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",退货数量为空");
                return baseRsp;
            }
            //退货数<=出售数
            Sale sale=new Sale();
            sale.setNumstr(refundsVO.getNumstr());
            sale.setFlag(1);
            List<Sale> sales = saleService.selectSaleListBy(sale);
            if(sales.size()==0){
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",出售表中未有该款信息");
                return baseRsp;
            }
            int num=0;
            for (int i = 0; i <sales.size() ; i++) {
                    num+=sales.get(i).getSalenum();
            }
            if(refundsVO.getRetnum()>num){
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR + ",退货数大于售出总数");
                return baseRsp;
            }

            // 返厂表插入
            Refunds refunds=new Refunds();
            BeanUtils.copyProperties(refundsVO,refunds);
            refunds.setId(Sequence.getInstance().nextId());
            refunds.setFlag(1);//1 有效
            refunds.setCreatetime(new Date());
            refunds.setOperator(super.isLogin(session).getUsername());
            int i = refundsService.insertRefunds(refunds);
            int ii=0;
            if (i>0){

                //中间表插入
               Stylefun stylefun=new Stylefun();
                stylefun.setId(Sequence.getInstance().nextId());
                stylefun.setRefundsid(refunds.getId());
                //有这款式
                if(styles.size()>0){
                    stylefun.setStyleid(styles.get(0).getId());
                }
                ii= stylefunService.insert(stylefun);

                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+i+","+ii);
                //
                //自动修改 现库存 数量 +
                Stock stock1 = stocks.get(0);
                stock1.setNownumber(stock1.getNownumber()+refunds.getRetnum());
                stockService.updateByPrimaryKey(stock1);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+i+","+ii);
            }


        }catch (Exception e){
            LOGGER.error("RefundsController========>insertRefunds失败", e);
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
    @RequestMapping(value = "/selectRefundsListByFlag",method = RequestMethod.GET)
    public BaseRsp<List<RefundsVO>> selectRefundsListByFlag(){
        BaseRsp<List<RefundsVO>> baseRsp=new BaseRsp<List<RefundsVO>>();
        Refunds refunds=new Refunds();
        List<Refunds> list =null;
        List<RefundsVO> listVO=new ArrayList<>();
        try {
            refunds.setFlag(1);
            list =refundsService.selectRefundsListBy(refunds);
            if(list.size()>0){
                for (Refunds refunds1 : list) {
                    RefundsVO vo=new RefundsVO();
                    BeanUtils.copyProperties(refunds1,vo);
                    vo.setId(String.valueOf(refunds1.getId()));
                    listVO.add(vo);
                }
            }
            baseRsp.setData(listVO);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        }catch (Exception e){
            LOGGER.error("RefundsController========>selectRefundsListByFlag失败", e);
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
    @RequestMapping(value = "/selectRefundsListByNumStr",method = RequestMethod.POST)
    public BaseRsp<List<RefundsVO>> selectRefundsListByNumStr(@RequestBody RefundsVO refundsVO){
        BaseRsp<List<RefundsVO>> baseRsp=new BaseRsp<List<RefundsVO>>();
        List<Refunds> list =null;
        List<RefundsVO> listVO=new ArrayList<>();
        if (null==refundsVO.getNumstr()) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",Numstr 为空");
            return baseRsp;
        }

        try {
            Refunds refunds=new Refunds();
            BeanUtils.copyProperties(refundsVO,refunds);
            refunds.setFlag(1);
            list =refundsService.selectRefundsListBy(refunds);
            if(list.size()>0){
                for (Refunds refunds1 : list) {
                    RefundsVO vo=new RefundsVO();
                    BeanUtils.copyProperties(refunds1,vo);
                    vo.setId(String.valueOf(refunds1.getId()));
                    listVO.add(vo);
                }
            }
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(listVO);
        }catch (Exception e){
            LOGGER.error("RefundsController========>selectRefundsListByNumStr失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     *  通过id查询
     * @param refundsVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectRefunds",method = RequestMethod.POST)
    public BaseRsp<RefundsVO> selectRefunds(@RequestBody RefundsVO refundsVO){
        BaseRsp<RefundsVO> baseRsp =new BaseRsp<RefundsVO>();

        if(null==refundsVO.getId()){
            LOGGER.error("RefundsController========>selectRefunds失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",selectRefunds失败,id为空");
            return baseRsp;
        }
        try {
            Refunds refunds=new Refunds();
            BeanUtils.copyProperties(refundsVO,refunds);
            refunds.setId(Long.valueOf(refundsVO.getId()));
            List<Refunds> refunds1 = refundsService.selectRefundsListBy(refunds);
            RefundsVO rsp=new RefundsVO();
            if (refunds1.size()>0){
                BeanUtils.copyProperties(refunds1.get(0),rsp);
                rsp.setId(String.valueOf(refunds1.get(0).getId()));
            }
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(rsp);
        }catch (Exception e){
            LOGGER.error("RefundsController========>selectRefunds失败", e);
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
    @RequestMapping(value = "/updateRefunds",method = RequestMethod.POST)
    public BaseRsp updateRefunds(@RequestBody RefundsVO refundsVO,HttpSession session) {
        BaseRsp baseRsp=new BaseRsp();
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
        if (null==refundsVO.getId()) {
            LOGGER.error("RefundsController========>updateRefunds失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",updateRefunds失败,id为空");
            return baseRsp;
        }

        int re;
        try {
            Refunds refunds=new Refunds();
            BeanUtils.copyProperties(refundsVO,refunds);
            refunds.setId(Long.valueOf(refundsVO.getId()));
            re = refundsService.updateByPrimaryKey(refunds);
            if (re>0){
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",受影响行数"+re);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",修改影响行数"+re);
            }
        } catch (Exception e) {
            LOGGER.error("RefundsController========>updateRefunds失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 物理删除
     * @param refundsVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteRefunds",method = RequestMethod.POST)
    public BaseRsp deleteRefunds(@RequestBody RefundsVO refundsVO,HttpSession session) {
        BaseRsp baseRsp=new BaseRsp();
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
        if (null==refundsVO.getId()) {
            LOGGER.error("RefundsController========>deleteRefunds失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",deleteRefunds失败,id为空");
            return baseRsp;
        }

        int re;
        int ree=0;
        try {
            re=refundsService.deleteByPrimaryKey(Long.valueOf(refundsVO.getId()));
            if (re>0){
                //删除中间表的全部相关信息
               Stylefun stylefun=new Stylefun();
                stylefun.setRefundsid(Long.valueOf(refundsVO.getId()));
                ree= stylefunService.deleteBy(stylefun);
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS+",影响行数"+re+","+ree);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+re+","+ree);
            }


        } catch (Exception e) {
            LOGGER.error("RefundsController========>deleteRefunds失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }


}
