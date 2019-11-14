package backtrace.io;


import com.google.gson.annotations.SerializedName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.Map;


/**
 * Serializable Backtrace API data object
 */
public class BacktraceData implements Serializable {
    private static final transient Logger LOGGER = LoggerFactory.getLogger(BacktraceData.class);

    /**
     * 16 bytes of randomness in human readable UUID format
     * server will reject request if uuid is already found
     */
    @SerializedName("uuid")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private String uuid;

    /**
     * UTC timestamp in seconds
     */
    @SerializedName("timestamp")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private long timestamp;

    /**
     * Name of programming language/environment this error comes from.
     */
    @SerializedName("lang")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    public final String lang = "java";

    /**
     * Version of programming language/environment this error comes from.
     */
    @SerializedName("langVersion")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private String langVersion;

    /**
     * Name of the client that is sending this error report.
     */
    @SerializedName("agent")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    public final String agent = "backtrace-java";

    /**
     * Version of the android library
     */
    @SerializedName("agentVersion")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private String agentVersion;

    /**
     * Get built-in attributes
     */
    @SerializedName("attributes")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private Map<String, Object> attributes;

    /**
     * Application thread details
     */
    @SerializedName("threads")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private Map<String, ThreadInformation> threadInformationMap;

    /**
     * Get a main thread name
     */
    @SerializedName("mainThread")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private String mainThread;

    /**
     * Get a report classifiers. If user send custom message, then variable should be null
     */
    @SerializedName("classifiers")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private String[] classifiers;

    /**
     * Current host environment variables
     */
    @SerializedName("annotations")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    public Map<String, Object> annotations;


    @SerializedName("sourceCode")
    @SuppressWarnings({"unused", "FieldCanBeLocal"})
    private Map<String, SourceCode> sourceCode;

    /**
     * Current BacktraceReport
     */
    public BacktraceReport report;


    /**
     * Create instance of report data
     *
     * @param report           current report
     * @param clientAttributes attributes which should be added to BacktraceData object
     */
    public BacktraceData(BacktraceReport report, Map<String, Object> clientAttributes) {
        if (report == null) {
            return;
        }
        this.report = report;

        setReportInformation();
        setThreadsInformation();
        setAttributes(clientAttributes);
    }

    /**
     * Get absolute paths to report attachments
     *
     * @return paths to attachments
     */
    // TODO:
//    public List<String> getAttachments() {
//        return FileHelper.filterOutFiles(report.attachmentPaths);
//    }

    /**
     * Set attributes and add complex attributes to annotations
     *
     * @param clientAttributes
     */
    private void setAttributes(Map<String, Object> clientAttributes) {
        LOGGER.debug("Setting attributes");
        Attributes attributes = new Attributes(report, clientAttributes);
        this.attributes = attributes.getAttributes();
        this.annotations = attributes.getAnnotations();
    }


    /**
     * Set report information such as report identifier (UUID), timestamp, classifier
     */
    private void setReportInformation() {
        LOGGER.debug("Setting report information");
        this.uuid = report.getUuid().toString();
        this.timestamp = report.timestamp;
        this.classifiers = report.exceptionTypeReport ? new String[]{report.classifier} : null;
        this.langVersion = System.getProperty("java.version");
        this.agentVersion = "0.1"; // TODO: FIX
    }

    /**
     * Set information about all threads
     */
    private void setThreadsInformation() {
        LOGGER.debug("Setting threads information");
        ThreadData threadData = new ThreadData(report.diagnosticStack);
        this.mainThread = threadData.getMainThread();
        this.threadInformationMap = threadData.threadInformation;
        SourceCodeData sourceCodeData = new SourceCodeData(report.diagnosticStack);
        this.sourceCode = sourceCodeData.data.isEmpty() ? null : sourceCodeData.data;
    }
}