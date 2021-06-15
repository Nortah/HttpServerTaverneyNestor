import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.logging.Logger;

public class SocketHandler implements Runnable {
    private Socket socket;
    private Handler defaultHandler;
    private Map<String, Map<String, Handler>> handlers;

    //Initialise the logger
    static Logger myLogger = Logger.getLogger("ServerLogs");

    public SocketHandler(Socket socket, Map<String, Map<String, Handler>> handlers) {
        this.socket = socket;
        this.handlers = handlers;
    }

    private void respond(int statusCode, String msg, OutputStream out) throws IOException  {
        String responseLine = "HTTP/1.1 " + statusCode + " " + msg + "\r\n\r\n";
        myLogger.warning(responseLine);
        out.write(responseLine.getBytes());
    }
    public void run() {
        myLogger = ServerLogger.getMyLogger();
        BufferedReader br = null;
        OutputStream os = null;

        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = socket.getOutputStream();
            // Request not parsed correctly
            Request request = new Request(br);
            if (!request.parse()) {
                respond(500, "Unable to parse request", os);
                return;
            }

            boolean foundHandler = false;
            Response response = new Response(os);
            Map<String, Handler> methodHandlers = handlers.get(request.getMethod());
            if (methodHandlers == null) {
                respond(405, "Method not supported", os);
                return;
            }

            for (String handlerPath : methodHandlers.keySet()) {
                if (handlerPath.equals(request.getPath())) {
                    methodHandlers.get(request.getPath()).handle(request, response);
                    response.send();
                    foundHandler = true;
                    break;
                }
            }
            myLogger.info("Handler is found: " + foundHandler);
            if (!foundHandler)  {
                if (methodHandlers.get("/*") != null) {
                    methodHandlers.get("/*").handle(request, response);
                    response.send();
                } else  {
                    respond(404, "Not Found", os);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
