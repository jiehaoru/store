package com.jhr.controller;

import com.jhr.controller.vo.StyleVO;
import com.jhr.entity.Style;
import com.jhr.service.StyleService;
import com.jhr.utils.Sequence;
import com.jhr.utils.base.BaseRsp;
import com.jhr.utils.base.BaseRspConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * <br>
 * 标题： 款式
 * 描述：
 *
 * @author jhr
 * @create 2019/03/07 15:04
 */

@Controller
@RequestMapping("/lecture")
public class StyleController extends BaseController {

    public static final Logger LOGGER = LoggerFactory.getLogger(StyleController.class);

    @Autowired
    private StyleService styleService;

    /**
     * 插入 款式
     *
     * @param styleVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/insertStyle", method = RequestMethod.POST)
    public BaseRsp insertStyle(@RequestBody StyleVO styleVO) {

        BaseRsp baseRsp=new BaseRsp();
        if(null==styleVO.getStyleid()){
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+"，入库信息为空");
            return baseRsp;
        }
        try {
            Style style=new Style();
            BeanUtils.copyProperties(styleVO,style);
            style.setId(Sequence.getInstance().nextId());
            style.setFlag(1);//1 有效
            String numstr=style.getStyleid()+style.getStandard()+style.getColour();
            style.setNumstr(numstr);
            style.setCreatetime(new Date());
            int i = styleService.insertStyle(style);
            if(i>0){
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            }else{
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            }


        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }
        return baseRsp;
    }

    /**
     * 查询全部
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectStyleList",method = RequestMethod.GET)
    public BaseRsp<List<StyleVO>> selectStyleList() {
        BaseRsp<List<StyleVO>> baseRsp=new BaseRsp<List<StyleVO>>();
        List<Style> list = null;
        List<StyleVO> listvo=new ArrayList<StyleVO>();
        try {
            list = styleService.selectStyleList();
           if(list.size()>0){
               //Long 转 String
               for (Style style : list) {
                   StyleVO styleVO=new StyleVO();
                   BeanUtils.copyProperties(style,styleVO);
                   styleVO.setId(String.valueOf(style.getId()));
                   listvo.add(styleVO);
               }
           }
            baseRsp.setData(listvo);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>selectStyleList失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 查询全部
     * 有效状态的
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectStyleListByFlag",method = RequestMethod.GET)
    public BaseRsp<List<StyleVO>> selectStyleListByFlag() {
        BaseRsp<List<StyleVO>> baseRsp=new BaseRsp<List<StyleVO>>();
        Style style = new Style();
        List<Style> list = null;
        List<StyleVO> listvo=new ArrayList<StyleVO>();
        try {
            style.setFlag(1);
            list = styleService.selectStyleBy(style);
            if(list.size()>0){
                //Long 转 String
                for (Style style1 : list) {
                    StyleVO styleVO=new StyleVO();
                    BeanUtils.copyProperties(style1,styleVO);
                    styleVO.setId(String.valueOf(style1.getId()));
                    listvo.add(styleVO);
                }
            }
            baseRsp.setData(listvo);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>selectStyleListByFlag失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 通过主键查询
     * @param styleVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectStyleByPrimaryKey",method = RequestMethod.POST)
    public BaseRsp<StyleVO> selectStyleByPrimaryKey(@RequestBody StyleVO styleVO) {
        BaseRsp<StyleVO> baseRsp=new BaseRsp<StyleVO>();
        Style styleRsp = null;
        StyleVO vo=new StyleVO();
        if(null==styleVO.getId()){
            LOGGER.error("StyleServiceImpl========>selectStyleByPrimaryKey失败,id为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",id为空");
            return baseRsp;
        }

        try {
            Long id=Long.valueOf(styleVO.getId());
            styleRsp = styleService.selectStyleByPrimaryKey(id);
            //Long 转 String
            BeanUtils.copyProperties(styleRsp,vo);
            vo.setId(String.valueOf(styleRsp.getId()));
            baseRsp.setData(vo);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>selectStyleByPrimaryKey失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }


    /**
     * 模糊 查询 款名
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectStyleByName",method = RequestMethod.POST)
    public BaseRsp<List<StyleVO>> selectStyleByName(@RequestBody StyleVO styleVO) {
        BaseRsp<List<StyleVO>> baseRsp=new BaseRsp<List<StyleVO>>();
        List<Style> list = null;
        List<StyleVO> listvo=new ArrayList<StyleVO>();
        if (null ==styleVO.getStylename()) {
            LOGGER.error("StyleServiceImpl========>selectStyleByName,款名为空");
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",款名为空");
            return baseRsp;
        }
        try {
            Style style=new Style();
            BeanUtils.copyProperties(styleVO,style);
            style.setFlag(1);//有效
            list = styleService.selectStyleBy(style);
            if(list.size()>0){
                //Long 转 String
                for (Style style1 : list) {
                    StyleVO vo=new StyleVO();
                    BeanUtils.copyProperties(style1,vo);
                    vo.setId(String.valueOf(style1.getId()));
                    listvo.add(vo);
                }
            }
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
            baseRsp.setData(listvo);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>selectStyleByName失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }


    /**
     * 通过 自定义编号 动态条件  查询
     * 提供给其他表调用
     * 有效无效都 返回
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/selectStyleBy",method = RequestMethod.POST)
    public BaseRsp<StyleVO> selectStyleBy(@RequestBody StyleVO styleVO) {
        BaseRsp<StyleVO> baseRsp=new BaseRsp<StyleVO>();
        Style rspstyle = null;
        Style req=new Style();
       StyleVO vo=new StyleVO();
        try {
            BeanUtils.copyProperties(styleVO,req);
            if(null!=styleVO.getId()){
                req.setId(Long.valueOf(styleVO.getId()));
            }

            List<Style> styles = styleService.selectStyleBy(req);//返回单个
            if(styles.size()>0){
                rspstyle=styles.get(0);
                BeanUtils.copyProperties(rspstyle,vo);
                vo.setId(String.valueOf(rspstyle.getId()));
            }
            baseRsp.setData(vo);
            baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>selectStyleBynumstr失败", e);
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
    @RequestMapping(value = "/updateStyle",method = RequestMethod.POST)
    public BaseRsp updateStyle(@RequestBody StyleVO styleVO) {
       BaseRsp baseRsp=new BaseRsp();

       if(null==styleVO.getId()){
           baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
           baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",主键id为空");
           return baseRsp;
       }

        int re;
        try {
            Style style=new Style();
            BeanUtils.copyProperties(styleVO,style);
            style.setId(Long.valueOf(styleVO.getId()));
            re = styleService.updateByPrimaryKey(style);
            if (re>0){
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);
                baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
            }else {
                baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",修改影响行数"+re);
            }
        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }

        return baseRsp;
    }

    /**
     * 物理删除
     * @param styleVO
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteByPrimaryKey",method = RequestMethod.GET)
    public BaseRsp deleteByPrimaryKey(@RequestBody StyleVO styleVO) {
        BaseRsp baseRsp=new BaseRsp();
        if (null==styleVO.getId()) {
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",id值为空");
            return baseRsp;
        }

        int re;
        try {
            re = styleService.deleteByPrimaryKey(Long.valueOf(styleVO.getId()));
            if (re>0){

                    baseRsp.setRespCode(BaseRspConstants.CODE_SUCCESS);
                    baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_SUCCESS);

                }else {

                    baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
                    baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR+",影响行数"+re);
            }

        } catch (Exception e) {
            LOGGER.error("StyleServiceImpl========>deleteByPrimaryKey失败", e);
            baseRsp.setRespCode(BaseRspConstants.CODE_FAILUR);
            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_ERROR);
            return baseRsp;
        }
        return baseRsp;
    }

    /**
     * 文件上传
     * @param file
     * @param request
     * @return
     * @throws IOException
     */
    @RequestMapping("/fileupload.do")
    public @ResponseBody  String upload(MultipartFile file, HttpServletRequest request) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String res = sdf.format(new Date());

        // uploads文件夹位置
        String rootPath = request.getSession().getServletContext().getRealPath("resource/uploads/");
        // 原始名称
        String originalFileName = file.getOriginalFilename();
        // 新文件名
        String newFileName = "sliver" + res + originalFileName.substring(originalFileName.lastIndexOf("."));
        // 创建年月文件夹
        Calendar date = Calendar.getInstance();
        File dateDirs = new File(date.get(Calendar.YEAR) + File.separator + (date.get(Calendar.MONTH)+1));

        // 新文件
        File newFile = new File(rootPath + File.separator + dateDirs + File.separator + newFileName);
        // 判断目标文件所在目录是否存在
        if( !newFile.getParentFile().exists()) {
            // 如果目标文件所在的目录不存在，则创建父目录
            newFile.getParentFile().mkdirs();
        }
        System.out.println(newFile);
        // 将内存中的数据写入磁盘
        file.transferTo(newFile);
        // 完整的url
        String fileUrl = date.get(Calendar.YEAR) + "\\" + (date.get(Calendar.MONTH)+1) + "\\" + newFileName;
        System.out.println("D:\\IdeaProjects\\store\\target\\store\\resource\\uploads\\"+fileUrl);
        return  "resource\\uploads\\"+fileUrl;
    }


}