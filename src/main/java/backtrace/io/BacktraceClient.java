/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package backtrace.io;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.concurrent.ConcurrentLinkedQueue;

public class BacktraceClient {
    private Backtrace backtrace;
    private ConcurrentLinkedQueue queue;

    public BacktraceClient(){
        backtrace = new Backtrace(queue);
    }
    public boolean someLibraryMethod() {
        return true;
    }

    public void test(){
        System.out.println("Backtrace.io");
    }

    public void send(String message) {
        throw new NotImplementedException();
    }


    public void send(Throwable throwable) {
        throw new NotImplementedException();
    }

}
