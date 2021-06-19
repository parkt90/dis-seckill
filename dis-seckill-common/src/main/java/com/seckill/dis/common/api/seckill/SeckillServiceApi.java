package com.seckill.dis.common.api.seckill;

import com.seckill.dis.common.api.goods.vo.GoodsVo;
import com.seckill.dis.common.api.user.vo.UserVo;
import com.seckill.dis.common.domain.OrderInfo;

/**
 * 秒杀服务接口
 *
 * @author noodle
 */
public interface SeckillServiceApi {
    /**
     * 创建验证码
     *
     * @param user
     * @param goodsId
     * @return
     */
    String createVerifyCode(UserVo user, long goodsId);

    /**
     * 执行秒杀操作，包含以下两步：
     * 1. 从goods表中减库存
     * 2. 将生成的订单写入miaosha_order表中
     *
     * @param userID  秒杀商品的用户
     * @param goodsID 所秒杀的商品ID
     * @return 生成的订单信息
     */
    OrderInfo seckill(long userID, long goodsId);

    /**
     * 获取秒杀结果
     *
     * @param userId
     * @param goodsId
     * @return
     */
    long getSeckillResult(Long userId, long goodsId);
}
