import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JsonArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sound.sampled.*;
import java.io.*;
import java.net.HttpURLConnection;
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
            readData(word);


        }while (!word.equalsIgnoreCase("No"));
    }

    public static void readData(String word){
            //API URL
            String urlString = "https://api.dictionaryapi.dev/api/v2/entries/en/"+word;

            try {
                //Fetch API
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //Requesting to Get data
                conn.setRequestMethod("GET");
                //Finished fetching

                //Start Reading API
                int respondCode = conn.getResponseCode();
                System.out.println(respondCode);
                if(respondCode == 200){
                    BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String inputLine;
                    StringBuilder content = new StringBuilder();
                    while((inputLine = in.readLine()) != null){
                        content.append(inputLine);
                    }
                    in.close();
                    conn.disconnect();
                    getData(content);
                    System.out.println("Successful");
                }else{
                    System.out.println("Word Not Found! Try Another.");
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

    }

    public static void getData(StringBuilder content) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(content.toString());
            //Word
            JSONObject jsonObj = (JSONObject) jsonArray.get(0);
            String word = (String) jsonObj.get("word");
            System.out.println(word);
            //
            //Phonetic Text And Audio
            JSONArray phonetics = (JSONArray) jsonObj.get("phonetics");
            JSONObject phoneticObj = (JSONObject) phonetics.get(0);
            System.out.println(phoneticObj);
            String text = (String) phoneticObj.get("text");//phonetic Text
            System.out.println(text);
            String audio = (String) phoneticObj.get("audio");//audio String
            runAudio(audio);

            //Meanings
            JSONArray meanings = (JSONArray) jsonObj.get("meanings");
            //Index Zero
            JSONObject meaningsIndexZero = (JSONObject) meanings.get(0);
            String partOfSpeech = (String) meaningsIndexZero.get("partOfSpeech");
            System.out.println(partOfSpeech);
            JSONArray definitions = (JSONArray) meaningsIndexZero.get("definitions");
            for(int i = 0 ; i < definitions.size() ; i++){
                JSONObject defObj = (JSONObject) definitions.get(i);
                String defString = (String) defObj.get("definition");
                System.out.println("Definition " + (i+1) + " : " + defString);
            }
            //
            //Index One


        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static void runAudio(String audio){
        try {
            URL audioURL = new URL(audio);
            InputStream inputStream = audioURL.openStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            Player player = new Player(bufferedInputStream);
            player.play();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JavaLayerException e) {
            throw new RuntimeException(e);
        }

    }
}



