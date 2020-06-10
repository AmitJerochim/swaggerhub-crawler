import org.openqa.selenium.WebDriver;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;

/**
 * The searching results in swaggerhub are represented using pagination. Each page shows ten results.
 * The HTMLCollector manages the collection of the HTML representation of each results page.
 */
public class HtmlCollector {
    /**
     * Go through the first x pages of the searching results and save the HTML representation.
     *
     * @param driver the browser that has to be used to perform the collecting
     * @param pages number of pages that have to be collected
     * @throws Exception
     */
    public void collect(WebDriver driver, int pages) throws Exception {
        String baseUrl = null;
        for(int i=1; i<=pages ; i++) {
            baseUrl = "https://app.swaggerhub.com/search?state=PUBLISHED&visibility=PUBLIC&type=API&page=";
            baseUrl = baseUrl + i;
            System.out.println("Visiting : " + baseUrl);
            try {
                Robot robot = new Robot();
                driver.get(baseUrl);
                robot.mouseMove(0,0);
                Thread.sleep(30000);
                robot.mouseMove(100,100);
                if (new File("./data/html").mkdirs()) ;
                String path = "./data/html/html" + i;
                File file = new File(path);
                file.createNewFile();
                FileWriter writer = new FileWriter(file);
                writer.write(driver.getPageSource());
                writer.close();
            }catch (Exception e){
                System.out.println("couldn't request page " + i);
            }
        }
    }
}
