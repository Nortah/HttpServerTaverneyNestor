import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Response {
    private OutputStream os = null;
    private int statusCode;
    private String statusMessage;
    private Map<String, String> headers = new HashMap<String, String>();
    private String body;
    public Response(OutputStream os)  {
        this.os = os;
    }
    public void setResponseCode(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public void addHeader(String headerName, String headerValue)  {
        this.headers.put(headerName, headerValue);
    }

    public void addBody(String body)  {
        headers.put("Content-Length", Integer.toString(body.length()));
        this.body = body;
    }
    //send a correct request
    public void send() throws IOException {
        headers.put("Connection", "Close");
        os.write(("HTTP/1.1 " + statusCode + " " + statusMessage + "\r\n").getBytes());
        for (String headerName : headers.keySet())  {
            os.write((headerName + ": " + headers.get(headerName) + "\r\n").getBytes());
        }
        os.write("\r\n".getBytes());
        if (body != null) {
            os.write(body.getBytes());
        }
    }
}
