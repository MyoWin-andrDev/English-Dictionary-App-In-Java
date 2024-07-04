import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class JavaDictionary {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word ;
        do{
            System.out.println("**___Welcome___To___Java___Dictionary___***");
            System.out.print("Enter a word : ");
            word = scanner.nextLine();
            if(word.equalsIgnoreCase("No")) break;
            //Get Data
        }while (!word.equalsIgnoreCase("No"));
    }
}
class GetData{
    public static void main(String[] args) {
        String word = "";
        String urlString = "https://api.dictionaryapi.dev/api/v2/entries/en/"+word;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

