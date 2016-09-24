import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {

    public static void main(String args[]) {

        bigMap().forEach((bigTitle, bigUrl) -> {
            smallMap(bigUrl).forEach((smallTitle, smallUrl) -> {
                File dir = new File(new StringBuilder().append(args[0]).append(File.separator).append(bigTitle).toString());
                if(!dir.exists())
                    dir.mkdir();
                String fileString = new StringBuilder().append(args[0]).append(File.separator).append(bigTitle).append(File.separator).append(smallTitle).append(".mp4").toString();
                File file = new File(fileString);
                if(!file.exists())
                    new Download(videoUrl(smallUrl), fileString)
                            .start();
            });
        });

    }

    public static Map<String, String> bigMap() {

        String html = getHtml("http://ani.today");

        Map<String, String> bigMap = new HashMap<>();

        Deque<String> url = new ArrayDeque<>();
        Deque<String> title = new ArrayDeque<>();
        parse(html, "<a href=\"(http://ani.today/list/\\d{2,}[\\w\\d-%]*)\">.*?</a>", url );
        parse(html, "<a href=\"http://ani.today/list/\\d{2,}[\\w\\d-%]*\">(.*?)</a>", title);

        while(!url.isEmpty() && !title.isEmpty()) {
            bigMap.put(title.poll(), url.poll());
        }

        return bigMap;

    }

    public static Map<String, String> smallMap(String url) {

        Map<String, String> smallMap = new HashMap<>();

        StringBuilder htmlBuilder = new StringBuilder();
        String htmlTemp;
        int pageNum = 1;
        while(true){
            htmlTemp = getHtml(url + "/" + pageNum);
            if (htmlTemp.contains("<div class=\"board-list-item\">")){
                htmlBuilder.append(htmlTemp);
                pageNum++;
            } else {
                break;
            }
        }

        String html = htmlBuilder.toString();

        Deque<String> href = new ArrayDeque<>();
        Deque<String> title = new ArrayDeque<>();
        parse (html, "<div class=\"board-list-item\"><a href=\"(.*?)\" title=", href);
        parse (html, "<span class=\"text\">(.*?)</span>", title);

        while(!href.isEmpty() && !title.isEmpty()) {
            smallMap.put(title.removeFirst(), href.removeFirst());
        }

        return smallMap;

    }

    public static String videoUrl(String url){
        return parse(getHtml(url), "<meta itemprop=\"contentURL\" content=\"(.*?)\">");
    }

    public static String getHtml(String url) {

        StringBuilder contents = new StringBuilder();

        try {

            URLConnection uc = new URL(url).openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader input = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));

            String line;
            while ((line=input.readLine())!=null) {
                contents.append(line);
            }

            input.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return contents.toString();

    }

    public static int parse(String text, String regex, Collection<String> collection) {

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            collection.add(matcher.group(1));
        }

        return collection.size();

    }

    public static String parse(String text, String regex) {

        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        if(matcher.find()) {
            return matcher.group(1);
        } else {
            return null;
        }

    }

}
