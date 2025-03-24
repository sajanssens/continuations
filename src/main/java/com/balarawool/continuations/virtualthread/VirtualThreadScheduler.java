package com.balarawool.continuations.virtualthread;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VirtualThreadScheduler {

    // Binding of VT's and PT's
    private Queue<VirtualThread> queue = new ConcurrentLinkedQueue<>();
    private ExecutorService executor = Executors.newFixedThreadPool(3);

    // Just like Thread.currentThread()
    public static ScopedValue<VirtualThread> CURRENT_VIRTUAL_THREAD = ScopedValue.newInstance();

    // Start the scheduler
    public void run() {
        while (true) {
            if (!queue.isEmpty()) {
                var vt = queue.remove();
                executor.submit(() -> ScopedValue.where(CURRENT_VIRTUAL_THREAD, vt)
                        .run(vt::run)); // 👈🏼 runs the VT
            }
        }
    }

    public void schedule(VirtualThread virtualThread) {
        queue.add(virtualThread);
    }

    public static void start(VirtualThreadScheduler scheduler) {
        // starts the scheduler on a new background thread
        new Thread(scheduler::run).start();
    }
}
