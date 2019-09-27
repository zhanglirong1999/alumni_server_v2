package cn.edu.seu.alumni_server;

import java.util.concurrent.CountDownLatch;

class Demo {
    private int n;

    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(0);

    public Demo(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            latch2.await();
            printFoo.run();
            this.latch2 = new CountDownLatch(1);

            latch1.countDown();

        }
    }

    public void bar(Runnable printBar) throws InterruptedException {

        for (int i = 0; i < n; i++) {
            latch1.await();
            printBar.run();
            this.latch1 = new CountDownLatch(1);

            latch2.countDown();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        FooBar foobar = new FooBar(10);

        new Thread(() -> {
            try {
                foobar.foo();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                foobar.bar();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }
}

class FooBar {

    int n;
    CountDownLatch latch1 = new CountDownLatch(1);
    CountDownLatch latch2 = new CountDownLatch(0);

    public FooBar(int n) {
        this.n = n;
    }

    public void foo() throws InterruptedException {
        for (int i = 0; i < n; i++) {
            latch2.await();
            System.out.print("foo");
            this.latch2 = new CountDownLatch(1);

            latch1.countDown();
        }
    }

    public void bar() throws InterruptedException {
        for (int i = 0; i < n; i++) {
            latch1.await();
            System.out.print("bar ");
            this.latch1 = new CountDownLatch(1);

            latch2.countDown();
        }
    }

}