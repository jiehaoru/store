package com.jhr.controller;

import com.jhr.entity.Style;
import com.jhr.service.StyleService;
import com.jhr.utils.Sequence;
import com.jhr.utils.base.BaseRsp;
import com.jhr.utils.base.BaseRspConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;
import java.util.UUID;

/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/07 15:04
 */

@Controller
@RequestMapping("/style")
public class StyleController {

    public static final Logger LOGGER=LoggerFactory.getLogger(StyleController.class);

    @Autowired
    private StyleService styleService;

    @RequestMapping("/insertStyle")
    public String insertStyle(Style style){
//        BaseRsp baseRsp=new BaseRsp();
        try {
            style.setId(Sequence.getInstance().nextId());
            style.setCreatetime(new Date());
            int i = styleService.insertStyle(style);

        }catch (Exception e){
            LOGGER.error("StyleServiceImpl========>insertStyle失败", e);
//            baseRsp.setRespCode(BaseRspConstants.RSP_CODE_FAILUR);
//            baseRsp.setRespDesc(BaseRspConstants.RSP_DESC_FAILUR);
            return "error";
        }

        return "index";
    }
}
