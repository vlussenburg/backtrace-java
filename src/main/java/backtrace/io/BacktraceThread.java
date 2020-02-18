package backtrace.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class BacktraceThread extends Thread {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(BacktraceThread.class);
    private final static String THREAD_NAME = "backtrace-daemon";
    private final Backtrace backtrace;
    private static BacktraceThread BACKTRACE_DAEMON;

    /**
     * Creates new thread for handling and sending error reports passed to queue
     *
     * @param config library configuration
     * @param queue  queue containing error reports that should be sent to the Backtrace console
     */
    private BacktraceThread(BacktraceConfig config, BlockingQueue<BacktraceMessage> queue) {
        super();
        this.backtrace = new Backtrace(config, queue);
    }

    /**
     * Creates, configures and start BacktraceThread which will handle and send error reports passed to queue
     *
     * @param config library configuration
     * @param queue  queue containing error reports that should be sent to the Backtrace console
     */
    static void init(BacktraceConfig config, BlockingQueue<BacktraceMessage> queue) {
        LOGGER.info("Initialize BacktraceThread");
        BACKTRACE_DAEMON = new BacktraceThread(config, queue);
        BACKTRACE_DAEMON.setDaemon(true);
        BACKTRACE_DAEMON.setName(THREAD_NAME);
        BACKTRACE_DAEMON.start();

        Runtime.getRuntime().addShutdownHook(new Thread(){
            @Override
            public void run() {
                try {
                    BACKTRACE_DAEMON.join();
                } catch (InterruptedException e) {
                    LOGGER.error("Interrupted waiting for the daemon thread to finish.", e);
                }
            }
        });
    }

    @Override
    public void run() {
        backtrace.handleBacktraceMessages();
    }
}
