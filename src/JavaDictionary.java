import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class JavaDictionary {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String word;
        do {
            System.out.println("***___Welcome___To___Java___Dictionary___***");
            System.out.println("(Enter 'Q' to exit)");
            System.out.print("Enter a word : ");
            word = scanner.nextLine();
            if (word.equalsIgnoreCase("q")) break;
            readData(word);

        } while (!word.equalsIgnoreCase("q"));
    }

    public static void readData(String word) {
        //API URL
        String urlString = "https://api.dictionaryapi.dev/api/v2/entries/en/" + word;

        try {
            //Fetch API
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //Requesting to Get data
            conn.setRequestMethod("GET");
            //Finished fetching

            //Start Reading API
            int respondCode = conn.getResponseCode();
            if (respondCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder content = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();
                conn.disconnect();
                getData(content);
            } else {
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
            JSONObject jsonObj = (JSONObject) jsonArray.getFirst();//Word
            String word = (String) jsonObj.get("word");
            System.out.println("Word : "+word);

            JSONArray phonetics = (JSONArray) jsonObj.get("phonetics");
            JSONObject phoneticObj = (JSONObject) phonetics.getFirst();
            String text = (String) phoneticObj.get("text");//phonetic Text
            System.out.println("Phonetic : "+text);
            getAudio(jsonObj);
            getDefinition(content);

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }


    //Get Audio
    public static void getAudio(JSONObject jsonObj) {

        JSONArray phonetics = (JSONArray) jsonObj.get("phonetics");
        for (int i = 0; i < phonetics.size(); i++) {
            JSONObject phoneticObj = (JSONObject) phonetics.get(i);
            String audio = null;
            if (phoneticObj.containsKey("audio")) {
                audio = (String) phoneticObj.get("audio");
            }
            if (audio != null && !audio.isEmpty()) {
                runAudio(audio);
            } else {
                System.out.println("Audio not found for this word");
            }
        }
    }

    public static void runAudio(String audio) {
        if(audio != null && !audio.isEmpty()) {
            try {
                URL audioURL = new URL(audio);
                InputStream inputStream = audioURL.openStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                Player player = new Player(bufferedInputStream);
                player.play();
            } catch (IOException | JavaLayerException e) {
                throw new RuntimeException(e);
            }
        }else{
            System.out.println("Audio URL is null or empty, unable to play audio.");
        }

    }

    //Get Definitions
    public static void getDefinition(StringBuilder content) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray jsonArray = (JSONArray) parser.parse(content.toString());
            JSONObject jsonObj = (JSONObject) jsonArray.getFirst();
            JSONArray meanings = (JSONArray) jsonObj.get("meanings");
            for (int i = 0; i < meanings.size(); i++) {
                JSONObject index = (JSONObject) meanings.get(i);
                String partOfSpeech = (String) index.get("partOfSpeech");
                System.out.println("Part Of Speech : " + partOfSpeech);

                System.out.print("\n");//Empty Space
                System.out.println("***___Definitions___***");
                JSONArray defArray = (JSONArray) index.get("definitions");
                for (int j = 0; j < defArray.size(); j++) {
                    JSONObject defObj = (JSONObject) defArray.get(j);
                    String definitions = (String) defObj.get("definition");
                    System.out.println("Definition " + (j + 1) + " : " + definitions);
                }
                System.out.print("\n");//Empty Space
                System.out.println("***___Synonyms___***");
                JSONArray synArray = (JSONArray) index.get("synonyms");
                for(int k = 0; k < synArray.size() ; k++){
                    String synonyms = (String) synArray.get(k);
                    System.out.println("Synonyms " + (k + 1) + " : " + synonyms);
                }
                System.out.print("\n");//Empty Space
                System.out.println("***___Antonyms___***");
                JSONArray antArray = (JSONArray) index.get("antonyms");
                for(int k = 0; k < antArray.size() ; k++){
                    String antonyms = (String) antArray.get(k);
                    System.out.println("Antonyms " + (k + 1) + " : " + antonyms);
                }
            }
            System.out.print("\n\n");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}



