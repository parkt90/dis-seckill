package com.seckill.dis.gateway.index;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 根路径，默认页面
 *
 * @author mata
 */
@Controller
public class Index {
      /**
     * 首页
     *
     * @return
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "login";// login页面
        
    }
    
}
