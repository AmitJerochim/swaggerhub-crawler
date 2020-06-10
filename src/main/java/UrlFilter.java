import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible to reduce all HTML representations of the searching results to a file containing links
 * that has to be called in a later state
 *
 */
public class UrlFilter {
    /**
     * goes through all files and calls the filter method for each
     * @throws Exception
     */
    public static void startFilter () throws Exception{
            int size =new File("./data/html").listFiles().length;
            for (int i=1; i<=size; i++){
                try {
                    UrlFilter.filter("html" + i);
                }catch (IOException e){

                }
            }
    }

    /**
     * Performs the filtering for a certain HTML file
     * @param htmlFile
     * @throws Exception
     */
    public static void filter(String htmlFile) throws Exception {
            Document doc = Jsoup.parse(new File("./data/html/"+htmlFile),"UTF-8","https://app.swaggerhub.com/");
            Elements divs = doc.select("div.api-list-item");
            Thread.sleep(1000);

            List<Element> onlyOAS3Divs = divs.stream()
                .filter(  div -> {return div.toString().split("OAS3").length != 1;}   )
                .collect(Collectors.toList());

            List<String> links = onlyOAS3Divs.stream()
                .map( div -> {return div.select("a.api-link[href]").last();})
                .map( a -> a.attr("href") )
                .collect(Collectors.toList());

            links =links.stream().limit(5).collect(Collectors.toList());
            File file = new File ("./data/links.txt");
            //file.createNewFile();
            FileWriter writer = new FileWriter(file, true);
            for(String link : links){
                System.out.println("Muesste schreiben");
                writer.write(link + "\n");
                System.out.println("hat geschrieben? " + link);
            }
            writer.close();
    }


}
