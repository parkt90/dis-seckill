package com.seckill.dis.gateway.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.BlockExceptionHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.seckill.dis.common.api.user.vo.UserVo;
import com.seckill.dis.common.result.CodeMsg;
import com.seckill.dis.common.result.Result;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MyBlockHandler  {
    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    // @Override
    // public void handle(HttpServletRequest request, HttpServletResponse response, BlockException ex) throws Exception {
    //     logger.info("出现异常2");
    //     // String msg = null;
    //     // if (ex instanceof FlowException) {
    //     //     msg = "限流了";
    //     // } else if (ex instanceof DegradeException) {
    //     //     msg = "降级了";
    //     // } 
    //     // // else if (ex instanceof ParamFlowException) {
    //     // //     msg = "热点参数限流";
    //     // // } 
    //     // else if (ex instanceof SystemBlockException) {
    //     //     msg = "系统规则（负载/...不满足要求）";
    //     // } else if (ex instanceof AuthorityException) {
    //     //     msg = "授权规则不通过";
    //     // }
    //     // http状态码
    //     response.setStatus(500);
    //     response.setCharacterEncoding("utf-8");
    //     response.setHeader("Content-Type", "application/json;charset=utf-8");
    //     response.setContentType("application/json;charset=utf-8");
    //     // spring mvc自带的json操作工具，叫jackson
    //     new ObjectMapper()
    //         .writeValue(
    //             response.getWriter(),
    //             Result.error(CodeMsg.SERVER_BUSY)
    //         );
    public static  Result<Integer> blockHandlerFordoSeckill ( UserVo user,
                                                            @RequestParam("goodsId") long goodsId,
                                                            BlockException ex) {
                                                
        logger.info("秒杀接口限流");
        return Result.error(CodeMsg.SERVER_BUSY);
    }
    public static  Result<String>  blockHandlerForVerifyCodel ( HttpServletResponse response,UserVo user,
                                                                @RequestParam("goodsId") long goodsId,
                                                                BlockException ex) {
        
        logger.info("验证码接口限流");
        return Result.error(CodeMsg.SERVER_BUSY);
    }


    }
    
    

