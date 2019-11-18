/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package backtrace.io;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;

public class BacktraceReportTest {
    private static final String message = "test";

    @Test public void initReportUsingException() {
        // GIVEN
        Exception exception = new Exception(message);
        // WHEN
        BacktraceReport report = new BacktraceReport(exception);
        // THEN
        Assert.assertNull(report.getMessage());
        Assert.assertTrue(report.getExceptionTypeReport());
        Assert.assertEquals(exception, report.getException());
    }

    @Test public void initReportUsingMessage() {
        // WHEN
        BacktraceReport report = new BacktraceReport(message);
        // THEN
        Assert.assertNull(report.getException());
        Assert.assertEquals(message, report.getMessage());
        Assert.assertFalse(report.getExceptionTypeReport());
    }
}