import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Logger;

public class Request {
    //Initialise the logger
    static Logger myLogger = Logger.getLogger("ServerLogs");
    private BufferedReader br;
    private String path;
    private String method;
    private String fullUrl;
    private Map<String, String> headers = new HashMap<String, String>();
    private Map<String, String> queryParameters = new HashMap<String, String>();
    private Reader in;
    public Request(BufferedReader br) {
        this.br = br;
    }
    public String getMethod()  {
        return method;
    }

    public String getPath()  {
        return path;
    }

    public String getFullUrl()  {
        return fullUrl;
    }

    public String getHeader(String headerName)  {
        return headers.get(headerName);
    }

    public String getParameter(String paramName)  {
        return queryParameters.get(paramName);
    }

    public boolean parse() throws IOException {
        myLogger = ServerLogger.getMyLogger();
        String initialLine = br.readLine();
        myLogger.info(initialLine);
        StringTokenizer tok = new StringTokenizer(initialLine);
        String[] components = new String[3];
        for (int i = 0; i < components.length; i++) {
            if (tok.hasMoreTokens())  {
                components[i] = tok.nextToken();
            } else  {
                return false;
            }
        }

        method = components[0];
        fullUrl = components[1];

        // Consume headers
        while (true)  {
            String headerLine = br.readLine();
            myLogger.info(headerLine);
            if (headerLine.length() == 0) {
                break;
            }

            int separator = headerLine.indexOf(":");
            if (separator == -1)  {
                return false;
            }
            headers.put(headerLine.substring(0, separator),
                    headerLine.substring(separator + 1));
        }

        if (components[1].indexOf("?") == -1) {
            path = components[1];
        } else  {
            path = components[1].substring(0, components[1].indexOf("?"));
            parseQueryParameters(components[1].substring(
                    components[1].indexOf("?") + 1));
        }

        return true;
    }
    private void parseQueryParameters(String queryString) {
        for (String parameter : queryString.split("&")) {
            int separator = parameter.indexOf('=');
            if (separator > -1) {
                queryParameters.put(parameter.substring(0, separator),
                        parameter.substring(separator + 1));
            } else  {
                queryParameters.put(parameter, null);
            }
        }
    }
    public InputStream getBody() throws IOException {
        return new HttpInputStream(in, headers);
    }
}
