package com.balarawool.continuations.virtualthread;

public class VirtualThreadDemo {
    public static final VirtualThreadScheduler SCHEDULER = new VirtualThreadScheduler();

    public static void main(String[] args) {

        for (int i = 0; i < 1000; i++) {
            var vt1 = new VirtualThread(() -> { // TODO 1: show VirtualThread
                System.out.println("1.1");
                System.out.println("1.2");              // TODO 2 show:
                WaitingOperation.perform("Network", 2); // 👈🏼 should perform the operation, suspend now, resume in ... seconds
                System.out.println("1.3");
                System.out.println("1.4");
            });
            var vt2 = new VirtualThread(() -> {
                System.out.println("2.1");
                System.out.println("2.2");
                WaitingOperation.perform("DB", 5);     // 👈🏼 should perform the operation, suspend now, resume in ... seconds
                System.out.println("2.3");
                System.out.println("2.4");
            });

            SCHEDULER.schedule(vt1); // 👈🏼 should start the VT
            SCHEDULER.schedule(vt2); // 👈🏼 should start the VT
        }

        // TODO 3: show VirtualThreadScheduler
        VirtualThreadScheduler.start(SCHEDULER);
    }
}
