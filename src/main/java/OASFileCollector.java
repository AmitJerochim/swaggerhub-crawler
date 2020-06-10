import org.openqa.selenium.By;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import java.awt.*;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.FileReader;

public class OASFileCollector {
    /**
     * iterates through link.txt and calls collectFile for each link
     * @throws Exception
     */
    public static void collectFiles() throws Exception{
        WebDriver driver = BrowserManager.getDriver();
        dry_run();
        FileReader fileReader = new FileReader("./data/links.txt");
        BufferedReader br = new BufferedReader(fileReader);
        String line = br.readLine();
        int lineNumber=0;
        while( line != null ) {
            lineNumber++;
            String url = "https://app.swaggerhub.com" + line;
            System.out.println("visiting: " + url);
            try {
                collectFile(driver,url);
            }catch(org.openqa.selenium.NoSuchElementException e){
                System.out.println("couldn't read API " + url + " in line " + lineNumber);
            }catch(org.openqa.selenium.WebDriverException e){
                System.out.println("couldn't read API " + url + " in line " + lineNumber);
            }
            line = br.readLine();
        }
        br.close();
    }

    /**
     * if creating a new instance of the Firefox WebDriver browser configuration is set to default.
     * If trying to download the first json file of this instance the WebDriver asks how to handle the file.
     * dry_run calls handleNewFiletypeDownloadPolicy which tells the browser allways to download files of type json.
     * @throws Exception
     */
    public static void dry_run() throws Exception{
        String url="https://app.swaggerhub.com/apis/amit88/application/1.0.0";
        WebDriver driver= BrowserManager.getDriver();
        driver.get(url);
        Robot robot = new Robot();
        robot.mouseMove(100,100);
        Thread.sleep(6000);
        WebElement export_btn = driver.findElement(By.id("export-dropdown-button"));
        simulateWebElementClick(robot, export_btn);
        WebElement download_dropdown = driver.findElement(By.id("export-dropdown-download"));
        simulateWebElementClick(robot, download_dropdown);
        WebElement download_json_unresolved = driver.findElement(By.id("export-dropdown-download-json-unresolved"));
        simulateWebElementClick(robot, download_json_unresolved);
        Thread.sleep(1000);
        handleNewFiletypeDownloadPolicy(robot);

    }

    /**
     *
     * simulates the procedure of retrieving a link and downloading a json file
     * @param driver
     * @param url
     * @throws Exception
     */
    private static void collectFile(WebDriver driver, String url) throws Exception{
        driver.get(url);
        Robot robot = new Robot();
        robot.mouseMove(100,100);
        Thread.sleep(6000);
        if(verifyCorrectness(driver)) {
            WebElement export_btn = driver.findElement(By.id("export-dropdown-button"));
            simulateWebElementClick(robot, export_btn);
            WebElement download_dropdown = driver.findElement(By.id("export-dropdown-download"));
            simulateWebElementClick(robot, download_dropdown);
            WebElement download_json_unresolved = driver.findElement(By.id("export-dropdown-download-json-unresolved"));
            simulateWebElementClick(robot, download_json_unresolved);
            Thread.sleep(1000);
//            handleNewFiletypeDownloadPolicy(robot);
        }else{
            System.out.println("couldn't download File");
        }
    }

    /**
     * this function make use of AWT Robot to response to a popup window
     * which occurs on the first download of a json file
     * @param robot
     */
    private static void handleNewFiletypeDownloadPolicy(Robot robot){
        Point coordinateSave = new Point(780, 575);
        simulateClick(robot, coordinateSave);
        Point coordinatesAllways = new Point(780, 605);
        simulateClick(robot, coordinatesAllways);
        Point coordinatesOK = new Point(1030, 700);
        simulateClick(robot, coordinatesOK);
    }

    /**
     * get coordinates as parameter and simulates a mouse click
     * @param robot
     * @param coordinates
     */
    private static void simulateClick(Robot robot, Point coordinates){
        try {
            robot.mouseMove(coordinates.getX(), coordinates.getY() );
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(200);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(200);
        } catch (InterruptedException e){}
    }

    /**
     * gets a Web element as parameter and make use of a robot to klick on this web Element.
     * Webelement should be larger then 10*10 pixel.
     * @param robot
     * @param button
     */
    private static void simulateWebElementClick( Robot robot, WebElement button ){
        try {
            Point coordinates = button.getLocation();
            robot.mouseMove(coordinates.getX() + 10, coordinates.getY() + 10);
            robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(200);
            robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
            Thread.sleep(200);
        } catch (InterruptedException e){}
    }

    /**
     * This method checks if Swaggerhub validator
     * @param driver
     * @return
     */
    private static boolean verifyCorrectness(WebDriver driver) {
        WebElement el = null;
        String class_attr = null;
        boolean invalid = false;
        try {
            invalid = driver
                    .findElement(By.className("sh-alert-title"))
                    .getText()
                    .toLowerCase()
                    .contains("error");
        }catch (org.openqa.selenium.NoSuchElementException e) {}
        try {
            if (invalid) {
                throw new OASDefintionInvalidException("Swaggerhub validator found issues");
            }
        }catch (OASDefintionInvalidException e) {
            return false;
        }

        try {
            el = driver.findElement(By.className("api-validator-badge-label"));
            String color = el.getCssValue("Color");
            if (el.getText().equals("VALID") && color.equals("rgb(0, 160, 180)")) {
                return true;
            }
        }catch(org.openqa.selenium.NoSuchElementException e){
            return false;
        }
        return false;
    }
}
