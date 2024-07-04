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
           // String audio = (String) phoneticObj.get("audio");//audio String
            //runAudio(audio);l

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
            JSONObject meaningsIndexOne = (JSONObject) meanings.get(1);
            String partOfSpeech1 = (String) meaningsIndexOne.get("partOfSpeech");
            System.out.println(partOfSpeech1);
            JSONArray definitions1 = (JSONArray) meaningsIndexOne.get("definitions");
            for(int i = 0 ; i < definitions1.size() ; i++){
                JSONObject defObj = (JSONObject) definitions1.get(i);
                String defString = (String) defObj.get("definition");
                System.out.println("Definition " + (i+1) + " : " + defString);
            }


        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    //Get Audio
    public static void getAudio(StringBuilder content){
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = null;
            jsonArray = (JSONArray) parser.parse(content.toString());

            JSONObject jsonObj = (JSONObject) jsonArray.get(0);
            JSONArray phonetics = (JSONArray) jsonObj.get("phonetics");
            for(int i = 0; i < phonetics.size() ; i++){
                JSONObject phoneticObj = (JSONObject) phonetics.get(i);
                String audio = null;
                if(phoneticObj.containsKey("audio")){
                    audio = (String) phoneticObj.get("audio");
                }
                if(audio != null){
                    runAudio(audio);
                }
                else{
                    System.out.println("Audio not found for this word");
                }
            }
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
        } catch (IOException | JavaLayerException e) {
            throw new RuntimeException(e);
        }

    }
}



