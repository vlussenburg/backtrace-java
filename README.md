# Backtrace Java support

[Backtrace](http://backtrace.io/)'s integration with Java applications which allows customers to capture and report handled and unhandled java exceptions to their Backtrace instance, instantly offering the ability to prioritize and debug software errors.

# Features Summary <a name="features-summary"></a>
* Light-weight Java client library that quickly submits exceptions and crashes to your Backtrace dashboard. Can include callstack, system metadata, custom metadata and file attachments if needed.
* Supports offline database for error report storage and re-submission in case of network outage.
* Fully customizable and extendable event handlers and base classes for custom implementations.

# Installation <a name="installation"></a>
## Download library via Gradle or Maven
* Gradle
```
dependencies {
    implementation ''
}
```

* Maven
```
<dependency>
  <groupId></groupId>
  <artifactId></artifactId>
  <version></version>
  <type>aar</type>
</dependency>
```

# Using Backtrace library  <a name="using-backtrace"></a>
## Initialize a new BacktraceClient <a name="using-backtrace-initialization"></a>

First create a `BacktraceConfig` instance with your `Backtrace endpoint URL` (e.g. https://xxx.sp.backtrace.io:6098) and `submission token`, and supply it as a parameter in the `BacktraceClient` constructor:

Java
```java
BacktraceConfig config = new BacktraceConfig("https://myserver.sp.backtrace.io:6097/", "4dca18e8769d0f5d10db0d1b665e64b3d716f76bf182fbcdad5d1d8070c12db0");
BacktraceClient client = new BacktraceClient(config);
```

## Database <a name=""></a>
TODO:

## Setting application name and version


```java
backtraceClient.setApplicationName("Backtrace Demo");
backtraceClient.setApplicationVersion("1.0.0");
```

## Sending an error report <a name="using-backtrace-sending-report"></a>
Method `BacktraceClient.send` will send an error report to the Backtrace endpoint specified. There `send` method is overloaded, see examples below:

### Using `BacktraceReport`
The `BacktraceReport` class represents a single error report. (Optional) You can also submit custom attributes using the `attributes` parameter. You can also pass list of file paths to files which will be send to API in `attachmentPaths` parameter.

```java
try {
    // throw exception here
} catch (Exception e) {
    BacktraceReport report = new BacktraceReport(e, 
    new HashMap<String, Object>() {{
        put("key", "value");
    }}, new ArrayList<String>() {{
        add("absoulte_file_path_1");
        add("absoulte_file_path_2");
    }});
    backtraceClient.send(report);
}
```

### Asynchronous `send` support

Method `send` behind the mask use dedicated thread which sending report to server. You can specify the method that should be performed after completion.

```java
        client.send(report, new OnServerResponseEvent() {
            @Override
            public void onEvent(BacktraceResult backtraceResult) {
                   // process result here
            }
        });
```

Method `await` of BacktraceReport allows to block current thread until report will be sent, as a parameter you can set set the maximum time you want to wait for an answer.

```java
report.await(5, TimeUnit.SECONDS);
```

### Other `BacktraceReport` overloads

`BacktraceClient` can also automatically create `BacktraceReport` given an exception or a custom message using the following overloads of the `BacktraceClient.send` method:

Java
```java
try {
  // throw exception here
} catch (Exception exception) {

  backtraceClient.send(new BacktraceReport(exception));
  
  // pass exception to send method
  backtraceClient.send(exception);
  
  // pass your custom message to send method
  backtraceClient.send("Message");
}
```


## Attaching custom event handlers <a name="documentation-events"></a>

All events are written in *listener* pattern. `BacktraceClient` allows you to attach your custom event handlers. For example, you can trigger actions before the `send` method:
 
```java
backtraceClient.setBeforeSendEvent(new BeforeSendEvent() {
        @Override
        public BacktraceData onEvent(BacktraceData data) {
            // another code
            return data;
        }
    });
```

`BacktraceClient` currently supports the following events:
- `BeforeSend`
- `RequestHandler`

## Reporting unhandled application exceptions
`BacktraceClient` also supports reporting of unhandled application exceptions not captured by your try-catch blocks. To enable reporting of unhandled exceptions:
```java
backtraceClient.enableUncaughtExceptionsHandler();
```

# Documentation  <a name="documentation"></a>

## BacktraceReport  <a name="documentation-BacktraceReport"></a>
**`BacktraceReport`** is a class that describe a single error report.

## BacktraceClient  <a name="documentation-BacktraceClient"></a>


## BacktraceData  <a name="documentation-BacktraceData"></a>
**`BacktraceData`** is a serializable class that holds the data to create a diagnostic JSON to be sent to the Backtrace Console endpoint . You can add additional pre-processors for `BacktraceData` by attaching an event handler to the `BacktraceClient.setBeforeSendEvent(event)` event. `BacktraceData` require `BacktraceReport` and `BacktraceClient` client attributes.

## BacktraceResult  <a name="documentation-BacktraceResult"></a>
**`BacktraceResult`** is a class that holds response and result from a `send` method call. The class contains a `status` property that indicates whether the call was completed (`OK`), the call returned with an error (`ServerError`). Additionally, the class has a `message` property that contains details about the status.
