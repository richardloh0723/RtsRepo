package com.rts.lecture.sample;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class PeriodTest {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
        AtomicLong al = new AtomicLong(0);
        ScheduledFuture<?> scheduleFuture = ses.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println("running");
                al.incrementAndGet();
            }
        }, 2, 1, TimeUnit.SECONDS);
        System.out.println("task scheduled");
        Thread.sleep(scheduleFuture.getDelay(TimeUnit.MILLISECONDS));
        while (true) {
            //System.out.println(scheduleFuture.isDone()); will always print false
            Thread.sleep(800);
            long l = al.get();
            System.out.println(l);
            if (l >= 5) {
                System.out.println("cancelling");
                scheduleFuture.cancel(true);
                ses.shutdown();
                break;
            }
        }
    }
}