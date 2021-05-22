
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;
import java.nio.file.Files;
import java.util.*;
import java.util.logging.Logger;

public class HttpServerConnection {
    static int portNumber;
    //Initialise the logger
    static Logger myLogger = Logger.getLogger("ServerLogs");
    //handler
    private Map<String, Map<String, Handler>> handlers = new HashMap<String, Map<String, Handler>>();

    public HttpServerConnection(int portNumber)  {
        this.portNumber = portNumber;
    }
    public void start() throws IOException  {
        myLogger = ServerLogger.getMyLogger();
        //create the socket
        ServerSocket socket = new ServerSocket(portNumber);
        myLogger.info("Listening on port " + portNumber);
        Socket client;
        while ((client = socket.accept()) != null)  {
            myLogger.info("Received connection from " + client.getRemoteSocketAddress().toString());
            SocketHandler handler = new SocketHandler(client, handlers);
            Thread t = new Thread(handler);
            t.start();
        }
    }
    //method to get a handler
    public void addHandler(String method, String path, Handler handler) {
        Map<String, Handler> methodHandlers = handlers.get(method);
        if (methodHandlers == null) {
            methodHandlers = new HashMap<String, Handler>();
            handlers.put(method, methodHandlers);
        }
        methodHandlers.put(path, handler);
    }
    //Server actions
    public static void main(String[] args) throws IOException  {
        HttpServerConnection server = new HttpServerConnection(8081);
        server.addHandler("GET", "/hello", new Handler()  {
            public void handle(Request request, Response response) throws IOException  {
                String html = "Hello " + request.getParameter("name") + "";
                response.setResponseCode(200, "OK");
                response.addHeader("Content-Type", "text/html");
                response.addBody(html);
            }
        });
        server.addHandler("GET", "/", new Handler()  {
            public void handle(Request request, Response response) throws IOException  {
                String html = "Hello " + request.getParameter("name") + "";
                response.setResponseCode(200, "OK");
                response.addHeader("Content-Type", "text/html");
                response.addBody(html);
            }
        });
        server.addHandler("GET", "/*", new FileHandler());  // Default handler
        server.start();

    }


}
