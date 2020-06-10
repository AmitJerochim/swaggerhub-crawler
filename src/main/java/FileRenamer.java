import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * This class is not directly part of the crawler but rename the files in a way we can organize them in a better way.
 */
public class FileRenamer {
    static Integer counter = 0;

    /**
     * main class specifies the parent directory and runs rename files
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String parent="./data/oasFilesJson/";
        renameFiles(parent);
    }

    /**
     *Iterates through all files in the parent directory and adds a number to the beginning of each file name
     * @param parent
     * @throws IOException
     */
    private static void renameFiles(String parent) throws IOException {
        Stream<Path> walk = Files.walk(Paths.get(parent));
        walk
            .filter(path -> !Files.isDirectory(path))
            .map(path -> path.getFileName())
            .forEach(name -> {
                counter++;
                File file = new File(parent+name);
                File renamed = new File(parent + addLeadingZeros(counter) + "_" + name);
                boolean success = file.renameTo(renamed);
                if (!success) {
                    System.out.println("couldn't rename File: " + name);
                }
            });
    }

    /**
     * this class takes an Integer as input and translate it in a four character long String representation of that Integer.
     * For example 12 will be converted to "0012"
     * @param counter
     * @return
     */
    static String addLeadingZeros( Integer counter){
        String asString = counter.toString();
        while(asString.length() < 4){
            asString = "0" + asString;
        }
        return asString;
    }

}
