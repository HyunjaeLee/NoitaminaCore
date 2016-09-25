import java.io.File;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String args[]) {

        bigMap().forEach((k, v) -> {
            SmallMap sm = new SmallMap(v, args[0]+ File.separator + k);
            sm.setName(k);
            sm.start();
        });

    }

    public static Map<String, String> bigMap() {

        String html = Util.getHtml("http://ani.today");

        Map<String, String> bigMap = new HashMap<>();

        Deque<String> url = new ArrayDeque<>();
        Deque<String> title = new ArrayDeque<>();
        Util.parse(html, "((https?://ani.today/list/)+\\d{2,}+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)" , url );
        Util.parse(html , "<a href=\"https?://ani.today/list/+\\d{2,}+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*\">(.*?)</a>" , title);

        while(!url.isEmpty() && !title.isEmpty()) {
            bigMap.put(title.poll(), url.poll());
        }

        return bigMap;

    }

}
