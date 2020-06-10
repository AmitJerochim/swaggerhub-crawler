import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 *      The BrowserManager is a singleton class which opens opens a browser and is
 *      responsible for everything that happens between the opening and closing of the browser.
 */
public class BrowserManager {
    private static WebDriver DRIVER;
    private BrowserManager(){}

    /**
     *      The crawling process is performed in sequential order of five steps using one browser browser instance.
     *
     *      1. the BrowserManager opens a Browser and configures it
     *      2. it iterates through the first searching pages and save the HTML files
     *      3. it filters links and save them to a links.txt
     *      4. it calls every link and downloads the OAS File as json
     *      5. it closes the browser and exits
     *
     */
    public static void start() throws Exception {
        configureBrowser();
        collectSearchingPages(1);
        Robot robot = new Robot();
        robot.mouseMove(550,500);
        Thread.sleep(30000);
        filterLinks();
        collectOASFiles();
        closeBrowser();
    }

    /**
     * returns a singleton instance of the browser.
     * creates instance if not already exists
     * @return
     */
    public static WebDriver getDriver(){
        if(DRIVER==null){
            System.setProperty("webdriver.gecko.driver","C:\\Users\\aje\\Downloads\\webdriver\\geckodriver.exe");
            DRIVER = new FirefoxDriver();
        }
        return DRIVER;
    }

    /**
     * This is the first process the crawler has to perform. It maximizes the browser and makes the browser full screen
     * @throws Exception
     */
    public static void configureBrowser() throws Exception{
        getDriver().manage().window().maximize();
        Robot robot = new Robot();
        robot.keyPress(KeyEvent.VK_F11);
        Thread.sleep(200);
        robot.keyRelease(KeyEvent.VK_F11);
    }

    /**
     *  This method starts the collecting process of the HtmlCollector class.
     *  This class is responsible to save the HTML-representation of each searching page.
     *
     * @param pages the number of searching pages that has to be collected.
     * @throws Exception
     */
    public static void collectSearchingPages(int pages) throws Exception{
        HtmlCollector collector = new HtmlCollector();
        collector.collect(getDriver(), pages);
    }

    /**
     * This method calls the startFilter method of the UrlFilter class.
     * The UrlFilter class go through all HTML-Files and copies the URLs that matches to certain criteria
     * to a file called links.txt
     * @throws Exception
     */
    public static void filterLinks() throws Exception{
        UrlFilter.startFilter();
    }

    /**
     * This method call the collect files method in OASFileCollector class.
     * collectFiles iterates through all links in links.txt and downloads for each the OAS File from swaggerhub
     * @throws Exception
     */
    public static void collectOASFiles() throws Exception{
        OASFileCollector.collectFiles();
    }

    /**
     * This method closes the browser window and ends the webdriver session.
     */
    public static void closeBrowser(){
        getDriver().quit();
    }

}
