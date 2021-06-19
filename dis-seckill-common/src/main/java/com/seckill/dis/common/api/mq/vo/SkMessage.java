package com.seckill.dis.common.api.mq.vo;


import java.io.Serializable;

/**
 * 在MQ中传递的秒杀信息
 * 包含参与秒杀的用户和商品的id
 *
 * @author noodle
 */
public class SkMessage implements Serializable{

    private long userID;
    private long goodsId;

    public long getUserID() {
        return userID;
    }

    public void setUserID(long userID) {
        this.userID = userID;
    }

    public long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(long goodsId) {
        this.goodsId = goodsId;
    }
    
    public SkMessage(long userID, long goodsId) {
        this.userID = userID;
        this.goodsId = goodsId;
    }

    @Override
    public String toString() {
        return "SkMessage [goodsId=" + goodsId + ", userID=" + userID + "]";
    }

  
}
