package backtrace.io;

import backtrace.io.events.RequestHandler;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class BacktraceClientTest {
    private final String message = "message";
    private BacktraceConfig config;
    private BacktraceClient backtraceClient;
    private BacktraceReport report;

    @Test(expected = NullPointerException.class)
    public void initBacktraceClientWithNullConfig() {
        // WHEN
        new BacktraceClient(null);
    }

    @Before
    public void init(){
        config = new BacktraceConfig("url", "token");
        this.backtraceClient = new BacktraceClient(config);
        this.report = new BacktraceReport(message);
    }



}