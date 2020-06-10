import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Main {
    /**
     calls the BrowserManager.start method
     The BrowserManager class is responsible for everything that happens between
     the opening and closing of the browser
     */
    public static void main(String[] args) throws Exception{
        BrowserManager.start();
    }
}
