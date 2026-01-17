package monitor;

public class SynchronizedExpt {

    int i = 0;
    int j = 0;

    synchronized void inc() throws InterruptedException {
        i++;
        Thread.sleep(1000);
        System.out.println("Thread#" + Thread.currentThread().getId() + " inc i=" + i);
        decj("inc");
    }

    private void decj(String method) {
        j--;
        System.out.println("Thread#" + Thread.currentThread().getId() + " From " + method + " j=" + j);
    }

    synchronized void dec() throws InterruptedException {
        i--;
        Thread.sleep(50);
        System.out.println("Thread#" + Thread.currentThread().getId() + " dec i=" + i);
        decj("dec");
    }

}
