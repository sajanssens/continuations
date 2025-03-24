package com.balarawool.continuations.virtualthread;

import jdk.internal.vm.Continuation;

import java.util.Timer;
import java.util.TimerTask;

import static com.balarawool.continuations.virtualthread.VirtualThreadDemo.SCHEDULER;
import static com.balarawool.continuations.virtualthread.VirtualThread.SCOPE;
import static com.balarawool.continuations.virtualthread.VirtualThreadScheduler.CURRENT_VIRTUAL_THREAD;

public class WaitingOperation {

    public static void perform(String name, int duration) {
        System.out.println("Waiting for " + name + " for " + duration + " seconds");

        asyncCall(duration);       // 👈🏼 RESUME in ... seconds
        Continuation.yield(SCOPE); // 👈🏼 PAUSE now
    }

    private static void asyncCall(int duration) {
        var virtualThread = CURRENT_VIRTUAL_THREAD.get();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SCHEDULER.schedule(virtualThread); // 👈🏼 reschedule in ... seconds
                cancel();
            }
        }, duration * 1_000L);
    }
}
