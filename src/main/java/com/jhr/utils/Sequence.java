package com.jhr.utils;


/**
 * <br>
 * 标题：
 * 描述：
 *
 * @author jhr
 * @create 2019/03/07 16:24
 */
public class Sequence {
    private static int MAX_ID_NUM = 4096;
    private static SnowFlake snowFlake;
    private static Sequence ourInstance = new Sequence();

    public static Sequence getInstance() {
        return ourInstance;
    }

    private Sequence() {
        snowFlake = new SnowFlake(RandomUtil.randomCenterId(), RandomUtil.randomMachineID());
    }

    public long nextId() {
        return snowFlake.nextId();
    }

    public long[] nextIds(int idNum) {
        if (idNum > MAX_ID_NUM) {
            throw new IllegalArgumentException("The number of Id can't be greater than " + MAX_ID_NUM);
        } else {
            long[] ids = new long[idNum];

            for(int i = 0; i < idNum; ++i) {
                ids[i] = snowFlake.nextId();
            }

            return ids;
        }
    }


}
