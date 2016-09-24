import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Download extends Thread{

    private String url;
    private String file;

    public Download(String url, String file) {
        this.url = url;
        this.file = file;
    }

    @Override
    public void run() {

        try {

            URLConnection connection = new URL(this.url).openConnection();
            connection.addRequestProperty("User-Agent", "Mozilla/5.0");
            ReadableByteChannel rbc = Channels.newChannel(connection.getInputStream());
            FileOutputStream fos = new FileOutputStream(new File(file));
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            fos.close();
            rbc.close();

        } catch (IOException e) {
            System.out.println(getName());
            e.printStackTrace();
        }

    }

}
