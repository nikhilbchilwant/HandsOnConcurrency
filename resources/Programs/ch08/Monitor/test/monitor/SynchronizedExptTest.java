package monitor;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class SynchronizedExptTest extends TestCase {

    private final static int THREADS = 8;
    private final static int TEST_SIZE = 64;
    private final static int PER_THREAD = TEST_SIZE / THREADS;
    int index;
    SynchronizedExpt instance;
    boolean[] map = new boolean[TEST_SIZE];
    Thread[] thread = new Thread[THREADS];

    public SynchronizedExptTest(String testName) {
        super(testName);
        instance = new SynchronizedExpt();
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(SynchronizedExptTest.class);
        return suite;
    }

    public void testParallelIncDec()  throws Exception {
        System.out.println("parallel inc dec");
        for (int i = 0; i < THREADS; i++) {
            if(i%2==0) {
                thread[i] = new SynchronizedExptTest.IncThread();
            }
            else {
                thread[i] = new SynchronizedExptTest.DecThread();
            }

        }
        for (int i = 0; i < THREADS; i ++) {
            thread[i].start();
        }
        for (int i = 0; i < THREADS; i ++) {
            thread[i].join();
        }

    }

    class IncThread extends Thread {
        public void run() {
            for (int i = 0; i < PER_THREAD; i++) {
                try {
                    instance.inc();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
    class DecThread extends Thread {
        public void run() {
            for (int i = 0; i < PER_THREAD; i++) {
                int value = -1;
                try {
                   instance.dec();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
