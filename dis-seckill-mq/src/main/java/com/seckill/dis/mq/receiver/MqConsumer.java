package com.seckill.dis.mq.receiver;

import java.io.IOException;

import com.rabbitmq.client.Channel;
import com.seckill.dis.common.api.cache.RedisServiceApi;
import com.seckill.dis.common.api.cache.vo.GoodsKeyPrefix; 
import com.seckill.dis.common.api.goods.GoodsServiceApi;
import com.seckill.dis.common.api.goods.vo.GoodsVo;
import com.seckill.dis.common.api.mq.vo.SkMessage;
import com.seckill.dis.common.api.seckill.SeckillServiceApi;
import com.seckill.dis.common.api.user.vo.UserVo;

import com.seckill.dis.mq.config.MQConfig;
import org.apache.dubbo.config.annotation.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * MQ消息接收者, 消费者
 * 消费者绑定在队列监听，既可以接收到队列中的消息
 *
 * @author noodle
 */
@Service
public class MqConsumer {

    private static Logger logger = LoggerFactory.getLogger(MqConsumer.class);

    @Reference(interfaceClass = GoodsServiceApi.class)
    GoodsServiceApi goodsService;

    @Reference(interfaceClass = SeckillServiceApi.class)
    SeckillServiceApi seckillService;

    @Reference(interfaceClass = RedisServiceApi.class)
    RedisServiceApi redisService;

    /**
     * 处理收到的秒杀成功信息（核心业务实现）
     *
     * @param message
     */
    @RabbitListener(queues = MQConfig.SECKILL_QUEUE)
    @RabbitHandler
    public void receiveSkInfo(SkMessage message, Channel channel, Message mes) throws IOException {
        logger.info("MQ receive a message: " + message);
         // 1.减库存 2.写入订单 3.写入秒杀订单
        try {
            // channel.basicAck(mes.getMessageProperties().getDeliveryTag(), false);
          // 获取秒杀用户信息与商品id
            UserVo user = message.getUser();
            long goodsId = message.getGoodsId();

            // 获取商品的库存
            GoodsVo goods = redisService.get(GoodsKeyPrefix.seckillGoodsInf, ""+goodsId ,GoodsVo.class);
        
            seckillService.seckill(user, goods);
            channel.basicAck(mes.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            if (mes.getMessageProperties().getRedelivered()) {
                
                logger.debug("消息已重复处理,拒绝再次接收...");
                
                channel.basicReject(mes.getMessageProperties().getDeliveryTag(), false); // 拒绝消息
            } else {
                
                logger.error("消息即将再次返回队列处理...");
                
                channel.basicNack(mes.getMessageProperties().getDeliveryTag(), false, true); 
            }
        }

    }
}

