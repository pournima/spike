package com.checkinlibrary.org;

import java.util.concurrent.BlockingQueue;

public class SupportQueueAdder implements Runnable {
    private BlockingQueue<SupportParams> queue;
    SupportParams params;

    public SupportQueueAdder(BlockingQueue<SupportParams> queue, SupportParams params) {
        this.queue = queue;
        this.params = params;
    }

    public void run() {
        try {
            queue.put(params);
        } catch(InterruptedException e) {
        }
    }                
}
