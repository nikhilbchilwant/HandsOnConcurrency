package spin;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.atomic.AtomicInteger;

import static junit.framework.Assert.assertEquals;

public class TryThreadLocalTest extends TestCase {

    private final static int TIMEOUT = 200;
    private final static int THREADS = 8;
    private final static int PER_THREAD = 2;
    private final static int COUNT = THREADS * PER_THREAD;
    private static final Logger logger = LogManager.getLogger(TryThreadLocalTest.class);
    Thread[] thread = new Thread[THREADS];
    int counter = 0;
    AtomicInteger failed = new AtomicInteger(0);
    TryThreadLocal instance = new TryThreadLocal();


    public TryThreadLocalTest(String testName) {
        super(testName);
    }

    public static Test suite() {
        TestSuite suite = new TestSuite(TryThreadLocalTest.class);

        return suite;
    }

    public void testParallel() throws Exception {
        logger.info("locking");
        ThreadID.reset();
        for (int i = 0; i < THREADS; i++) {
            thread[i] = new MyThread();
        }
        for (int i = 0; i < THREADS; i++) {
            thread[i].start();
        }
        for (int i = 0; i < THREADS; i++) {
            thread[i].join();
        }

//        assertEquals(counter, COUNT);
    }

//    public void testTimeout() throws Exception {
//        logger.info("Testing timeout");
//        ThreadID.reset();
//        for (int i = 0; i < THREADS; i++) {
//            thread[i] = new TryThreadLocalTest.TOThread();
//        }
//        for (int i = 0; i < THREADS; i++) {
//            thread[i].start();
//        }
//        for (int i = 0; i < THREADS; i++) {
//            thread[i].join();
//        }
//
//        assertEquals(counter, COUNT - failed.get());
//        logger.info("timeouts: {}", failed.get());
//    }

    class MyThread extends Thread {
        public void run() {
            for (int i = 0; i < PER_THREAD; i++) {
                instance.execute();
            }
        }
    }

//    class TOThread extends Thread {
//        Random random = new Random(System.nanoTime());
//
//        public void run() {
//            for (int i = 0; i < PER_THREAD; i++) {
//                boolean ok = false;
//                try {
//                    ok = instance.tryLock(50, TimeUnit.MILLISECONDS);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
//                if (ok) {
//                    try {
//                        logger.info("Incrementing the counter");
//                        counter = counter + 1;
//                        // force others to time out
//                        synchronized (this) {
//                            try {
//                                int timeoutMillis = random.nextInt(50, TIMEOUT);
//                                logger.info("TimeoutMillis={}", timeoutMillis);
//                                wait(timeoutMillis);
//                            } catch (InterruptedException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    } finally {
//                        instance.unlock();
//                    }
//                } else {
//                    failed.getAndIncrement();
//                }
//            }
//        }
//    }
}
