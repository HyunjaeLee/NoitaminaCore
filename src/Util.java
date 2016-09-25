import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static String videoUrl(String url){

        return parse(getHtml(url), "<meta itemprop=\"contentURL\" content=\"(.*?)\">");

    }

    public static String getHtml(String url) {

        StringBuilder contents = new StringBuilder();

        try {

            URLConnection uc = new URL(url).openConnection();

            uc.setConnectTimeout(60000);
            uc.setReadTimeout(60000);

            uc.addRequestProperty("User-Agent", "Mozilla/5.0");

            BufferedReader input = new BufferedReader(new InputStreamReader(uc.getInputStream(), "UTF-8"));

            String line;
            while ((line=input.readLine())!=null) {
                contents.append(line);
            }

            input.close();

        } catch (Exception e) {
            //System.out.println(e.getMessage());
            return getHtml(url);
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
