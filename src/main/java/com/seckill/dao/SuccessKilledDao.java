package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;
/*把业务想清楚之后就会知道为什么这样设计这些借口了*/

public interface SuccessKilledDao {

    /**
     * 插入购买明细，可过滤重复，也就是对应sql第二个表设计时设定两个主键
     * @param seckillId
     * @param userPhone
     * @return 插入的行数，返回0则表示插入失败
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据id查询SuccessKilled并携带秒杀产品对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}





