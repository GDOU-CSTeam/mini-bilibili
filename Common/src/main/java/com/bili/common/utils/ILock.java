package com.bili.common.utils;

public interface ILock {
    /**
     * 尝试获取锁
     * @param timeoutSec 锁持有的时间，过期后自动释放
     * @return true表示成功，false表示失败
     */
    boolean tryLock(Long timeoutSec);

    /**
     * 释放锁
     */
    void unlock();
}
