package backtrace.io;

import com.google.gson.annotations.SerializedName;


/**
 * Send method result
 */
public class BacktraceResult {

    /**
     * Object identifier
     */
    @SerializedName("_rxid")
    @SuppressWarnings({"UnusedDeclaration"})
    public String rxId;


    /**
     * Message
     */
    @SerializedName("response")
    private String message;


    /**
     * Result status eg. server error, ok
     */
    private BacktraceResultStatus status;

    /**
     * Current report
     */
    private BacktraceReport backtraceReport;

    /**
     * Create new instance of BacktraceResult
     *
     * @param report  executed report
     * @param message message
     * @param status  result status eg. ok, server error
     */
    private BacktraceResult(BacktraceReport report, String message, BacktraceResultStatus status) {
        setBacktraceReport(report);
        setStatus(status);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    BacktraceResultStatus getStatus() {
        return status;
    }

    void setBacktraceReport(BacktraceReport backtraceReport) {
        this.backtraceReport = backtraceReport;
    }

    void setStatus(BacktraceResultStatus status) {
        this.status = status;
    }

    @SuppressWarnings({"UnusedDeclaration"})
    public BacktraceReport getBacktraceReport() {
        return backtraceReport;
    }

    /**
     * Set result when error occurs while sending data to API
     *
     * @param report    executed report
     * @param exception current exception
     * @return BacktraceResult with exception information
     */
    static BacktraceResult OnError(BacktraceReport report, Exception exception) {
        return new BacktraceResult(
                report, exception.getMessage(),
                BacktraceResultStatus.ServerError);
    }

    static BacktraceResult OnSuccess(BacktraceReport report, String message){
        return new BacktraceResult(report, message, BacktraceResultStatus.Ok);
    }
}