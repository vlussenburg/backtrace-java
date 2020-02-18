package backtrace.io;

import backtrace.io.data.BacktraceData;
import backtrace.io.data.BacktraceReport;
import backtrace.io.events.OnServerResponseEvent;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

class BacktraceQueueHandler {
    private BlockingQueue<BacktraceMessage> queue;

    /**
     * Creates instance of BacktraceQueueHandler
     *
     * @param config Library configuration
     */
    BacktraceQueueHandler(BacktraceConfig config) {
        queue = new LinkedBlockingQueue<>();
        BacktraceThread.init(config, queue);
    }

    /**
     * Creates BacktraceMessage based on report and attributes and adds message to queue
     *
     * @param report     Current report which contains information about error
     * @param attributes Custom user attributes
     * @param callback   Event which will be executed after receiving the response
     */
    void send(BacktraceReport report, Map<String, Object> attributes, OnServerResponseEvent callback) {
        BacktraceData backtraceData = new BacktraceData(report, attributes);
        queue.add(new BacktraceMessage(backtraceData, callback));
    }
}