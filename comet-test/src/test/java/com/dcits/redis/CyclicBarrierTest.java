package com.dcits.redis;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * @ClassName CyclicBarrierTest
 * @Author leijian
 * @Date 2019/3/26 17:50
 * @Description TODO
 * @Version 1.0
 **/
public class CyclicBarrierTest {
    //访问的线程总数
    public static final int THREAD_COUNT = 100;
    //循环的总次数
    public static final int LOOP_COUNT = 10000;

    static ExecutorService pool = Executors.newFixedThreadPool(THREAD_COUNT);
    static CompletionService<Long> completionService = new ExecutorCompletionService<>(pool);

    //static的共享变量
    static final AtomicLong atomicLong = new AtomicLong(0L);
    static final LongAdder longAdder = new LongAdder();

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        long start = System.currentTimeMillis();
        for (int i = 0; i < THREAD_COUNT; i++) {
            completionService.submit(() -> {
                for (int j = 0; j < 100000; j++) {
                    //对比只需要求欢此方法即可
                    atomicLong.incrementAndGet();
                    //longAdder.increment();
                }
                return 1L;
            });
        }
        for (int i = 0; i < THREAD_COUNT; i++) {
            Future<Long> future = completionService.take();
            future.get();
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - start));
        pool.shutdown();
    }
}
