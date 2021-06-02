package com.seckill.dis.goods.service;

import com.seckill.dis.common.api.goods.GoodsServiceApi;
import com.seckill.dis.common.api.goods.vo.GoodsVo;
import com.seckill.dis.common.domain.SeckillGoods;
import com.seckill.dis.goods.persistence.GoodsMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 商品服务接口实现
 *
 * @author noodle
 */
@Service(interfaceClass = GoodsServiceApi.class)
public class GoodsServiceImpl implements GoodsServiceApi {

    //乐观锁冲突最大重试次数
    // private static final int DEFAULT_MAX_RETRIES = 10;

    @Autowired
    GoodsMapper goodsMapper;

    @Override
    public List<GoodsVo> listGoodsVo() {
        return goodsMapper.listGoodsVo();
    }

    @Override
    public GoodsVo getGoodsVoByGoodsId(long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }

    /**
     * 通过商品的id查出商品的所有信息（包含该商品的秒杀信息）
     *
     * @param goodsId
     * @return
     */
    @Override
    public GoodsVo getGoodsVoByGoodsId(Long goodsId) {
        return goodsMapper.getGoodsVoByGoodsId(goodsId);
    }


    /**
     * 减库存
     *
     * @param goods
     * @return
     */
    @Override
    public boolean reduceStock(GoodsVo goods) {
        // int attemptNums = 0;
        // int res = 0;
        SeckillGoods seckillGoods = new SeckillGoods();
        // 秒杀商品的id和商品的id是一样的
        seckillGoods.setGoodsId(goods.getId());
        // seckillGoods.setVersion(goods.getVersion());
        // do {
        //     attemptNums++;
        //     // try {
        //         seckillGoods.setVersion(goodsMapper.getVersionByGoodsId(goods.getId()));
        //         res = goodsMapper.reduceStack(seckillGoods);
        //     // } catch (Exception e) {
        //     //     logger.info(e.toString());
        //     //     e.printStackTrace();
        //     // }
        //     if (res != 0)
        //         break;
        // } while (attemptNums<DEFAULT_MAX_RETRIES);
        int ret = goodsMapper.reduceStack(seckillGoods);
        // logger.info("自选锁次数: "+attemptNums );
        return ret>0;
    }
  

}
