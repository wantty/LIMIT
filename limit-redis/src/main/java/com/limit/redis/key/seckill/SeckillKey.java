package com.limit.redis.key.seckill;

import com.limit.redis.key.BasePrefix;

public class SeckillKey extends BasePrefix {

    private SeckillKey(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }
    public static SeckillKey isGoodsOver = new SeckillKey(0, "go");
    public static SeckillKey getSeckillPath = new SeckillKey(60, "mp");
    public static SeckillKey getSeckillVerifyCode = new SeckillKey(300, "vc");
}
