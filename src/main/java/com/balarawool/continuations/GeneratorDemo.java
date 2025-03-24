package com.balarawool.continuations;

import jdk.internal.vm.Continuation;
import jdk.internal.vm.ContinuationScope;

import java.awt.*;
import java.util.function.Consumer;

public class GeneratorDemo {
    public static void main(String[] args) {
        // Generator has a generator function as lambda
        var generator = new Generator<String>(source -> {
            source.yield("A"); // 👈🏼 should generate the value and pause execution
            source.yield("B"); // 👈🏼 should generate the value and pause execution
            source.yield("C"); // 👈🏼 should generate the value and pause execution
        });

        while (generator.hasNext()) {
            System.out.println(generator.next()); // 👈🏼 should return the current value and resume execution
            System.out.println("Do something");
        }

        // var fibonacci = new Generator<Integer>(source -> {
        //     var terms = new Point(0, 1);
        //
        //     while (true) { // infinite stream of Integers
        //         source.yield(terms.x);
        //         terms = new Point(terms.y, terms.x + terms.y);
        //     }
        // });
        //
        // for (int i = 0; i < 10; i++) {
        //     System.out.println(fibonacci.next());
        // }
    }

    public static class Generator<T> {
        private ContinuationScope scope;
        private Continuation cont;
        private Source source;

        public boolean hasNext() {
            return !cont.isDone();
        }

        public T next() {
            var t = source.getValue();
            cont.run(); // 👈🏼 RESUMES execution
            return t;
        }

        // Helper that contains the current value and the yield() method
        public class Source {
            private T value;

            public void yield(T t) {
                value = t;                 // remember the value
                Continuation.yield(scope); // 👈🏼 PAUSES execution
            }

            private T getValue() {         // get the current value
                return value;
            }
        }

        public Generator(Consumer<Source> consumer) {
            scope = new ContinuationScope("Generator");
            source = new Source();
            cont = new Continuation(scope, () -> consumer.accept(source));
            cont.run();
        }
    }
}
