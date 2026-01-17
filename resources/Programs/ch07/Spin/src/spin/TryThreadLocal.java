package spin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class TryThreadLocal {

    private static final Logger log = LogManager.getLogger(TryThreadLocal.class);
    ThreadLocal<QNode> localNode = new ThreadLocal<>() {
        protected TryThreadLocal.QNode initialValue() {
            return new TryThreadLocal.QNode();
        }
    };

    public void execute() {
        log.info(localNode.get());
    }

    static class QNode {
        private int id = 0;
        public QNode(){
            id = new Random().nextInt(Integer.MAX_VALUE);
        }

        public int getId() {
            return id;
        }
    }
}
