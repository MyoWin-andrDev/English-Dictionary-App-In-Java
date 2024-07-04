import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.Scanner;

public class JavaDictionary {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word ;
        do{
            System.out.println("**___Welcome___To___Java___Dictionary___***");
            System.out.print("Enter a word : ");
            word = scanner.nextLine();
            getData(word);
            if(word.equalsIgnoreCase("No")) break;



        }while (!word.equalsIgnoreCase("No"));
    }

    public static void getData(String word){
            //API URL
            String urlString = "https://api.dictionaryapi.dev/api/v2/entries/en/"+word;

            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //Requesting to Get data
                conn.setRequestMethod("GET");
                int respondCode = conn.getResponseCode();
                if(respondCode == 200){
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while((inputLine = in.readLine()) != null){
                        content.append(inputLine);
                    }
                    in.close();
                    conn.disconnect();
                    System.out.println("Successful");

                }else{
                    System.out.println("Word Not Found! Try Another.");
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }
}

