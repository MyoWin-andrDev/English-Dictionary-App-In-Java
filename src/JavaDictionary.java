import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JsonArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
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
            JSONObject jsonObj = (JSONObject) jsonArray.get(0);
            String word = (String) jsonObj.get("word");//word
            System.out.println(word);
            JSONArray phonetics = (JSONArray) jsonObj.get("phonetics");
            JSONObject phoneticObj = (JSONObject) phonetics.get(0);
            System.out.println(phoneticObj);
            String text = (String) phoneticObj.get("text");//phonetic Text in UK
            System.out.println(text);


        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    public static void runAudio(File audio){
        try {
            AudioInputStream audiStream = AudioSystem.getAudioInputStream(audio);
            Clip clip = AudioSystem.getClip();
            clip.open(audiStream);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

    }
}



