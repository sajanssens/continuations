package com.balarawool.continuations;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

public class ContinuationsDemo {
    public static void main(String[] args) {
        var cont = getContinuation();
        cont.run();
        System.out.println("Do something");
        // cont.run(); // 👈🏼 RESUMES execution 1
        // System.out.println("Do something else");
        // cont.run(); // 👈🏼 RESUMES execution 2
        // cont.run(); // 👈🏼 RESUMES execution ?
    }

    private static Continuation getContinuation() {
        var scope = new ContinuationScope("Demo"); // like a namespace for a cont.
        var cont = new Continuation(scope, () -> { // requires scope and runnable
            System.out.println("A");
            // Continuation.yield(scope); // 👈🏼 PAUSES execution 1
            System.out.println("B");
            // Continuation.yield(scope); // 👈🏼 PAUSES execution 2
            System.out.println("C");
        });
        return cont;
    }

}
