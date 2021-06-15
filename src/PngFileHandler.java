import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class PngFileHandler implements Handler {
    public void handle(Request request, Response response) throws IOException {
        try {
            String basePath = new File("").getAbsolutePath();
            File myFile = new File (basePath + "\\src\\WebSite\\favicon.PNG");
            //get the size of the file
            byte[] fileByteArray = new byte[(int) myFile.length()];
            BufferedInputStream bis = new BufferedInputStream((new FileInputStream(myFile)));
            //convert file into byte array
            bis.read(fileByteArray, 0, fileByteArray.length);
            response.setResponseCode(200, "OK");
            response.addHeader("Content-Type", "image/png");

            response.addByteBody(fileByteArray);
        } catch (FileNotFoundException e) {
            response.setResponseCode(404, "Not Found");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}