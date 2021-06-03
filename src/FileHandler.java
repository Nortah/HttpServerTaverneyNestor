import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileHandler implements Handler {
    public void handle(Request request, Response response) throws IOException {
        try {
            String basePath = new File("").getAbsolutePath();
            File myFile = new File (basePath + "\\src\\WebSite\\index.html");
            FileInputStream file = new FileInputStream(myFile);
            response.setResponseCode(200, "OK");
            response.addHeader("Content-Type", "text/html");
            StringBuffer buf = new StringBuffer();
            int c;
            while ((c = file.read()) != -1) {
                buf.append((char) c);
            }
            response.addBody(buf.toString());
        } catch (FileNotFoundException e) {
            response.setResponseCode(404, "Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}