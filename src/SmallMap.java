import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class SmallMap extends Thread{

    private String url;
    private String args;

    public SmallMap(String url, String args){
        this.url = url;
        this.args = args;
    }

    @Override
    public void run() {

        Map<String, String> smallMap = new HashMap<>();
        StringBuilder htmlBuilder = new StringBuilder();
        String htmlTemp;
        int pageNum = 1;
        while(true) {
            htmlTemp = Util.getHtml(url + "/" + pageNum);
            if (htmlTemp.contains("<div class=\"board-list-item\">")) {
                htmlBuilder.append(htmlTemp);
                pageNum++;
            } else {
                break;
            }
        }

        String html = htmlBuilder.toString();
        Deque<String> href = new ArrayDeque<>();
        Deque<String> title = new ArrayDeque<>();
        Util.parse (html, "<div class=\"board-list-item\"><a href=\"(.*?)\" title=", href);
        Util.parse (html, "<span class=\"text\">(.*?)</span>", title);

        while(!href.isEmpty() && !title.isEmpty()) {
            smallMap.put(title.poll(), href.poll());
        }

        File file = new File(args);
        file.mkdir();

        smallMap.forEach((k,v) -> {
            if(!new File(args + File.separator + k + ".mp4").exists()) {
                Download download = new Download(Util.videoUrl(v), args + File.separator + k + ".mp4");
                download.setName(k);
                download.start();
            }
        });

    }

}
