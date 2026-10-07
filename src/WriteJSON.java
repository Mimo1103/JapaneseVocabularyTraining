import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sun.tools.javac.Main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class WriteJSON {

    public static void writeVocabulary(Vocabulary vocabulary) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String path = "src/vocabularyLists/";
            String fileName = vocabulary.getID() + ".json";
            File file = new File(path + fileName);;


            mapper.writeValue(file, vocabulary);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeSettings() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String path = "src/";
            String fileName = "settings.json";
            File file = new File(path + fileName);

            ObjectNode json = mapper.createObjectNode();
            json.put("startLanguage", "Japanese");
            json.put("lastUsedTags", "#kanji!kanjireplace");
            json.put("askForNewTags", "false");

            mapper.writeValue(file, json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static HashMap<String, String> getSettings() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            String path = "src/";
            String fileName = "settings.json";
            File file = new File(path + fileName);

            return mapper.readValue(file, HashMap.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setStartLanguage(String startLanguage) {
        if(startLanguage.toLowerCase().trim().equals("japanese") || startLanguage.toLowerCase().trim().equals("english")) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                String path = "src/";
                String fileName = "settings.json";
                File file = new File(path + fileName);

                ObjectNode json = mapper.createObjectNode();
                json.put("startLanguage", startLanguage.toLowerCase().trim());
                json.put("lastUsedTags", getSettings().get("lastUsedTags"));
                json.put("askForNewTags", getSettings().get("askForNewTags"));

                mapper.writeValue(new File(path + fileName), json);
                System.out.println("Sucessfully changed the start language.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid input. Select either 'japanese' or 'english' as the start language.");
        }
    }

    public static void setLastUsedTags(String lastUsedTags) {
        if(!HandlingTags.createAllowedVocabList(HandlingTags.detectingTags(lastUsedTags)).isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                String path = "src/";
                String fileName = "settings.json";
                File file = new File(path + fileName);

                ObjectNode json = mapper.createObjectNode();
                json.put("startLanguage", getSettings().get("startLanguage"));
                json.put("lastUsedTags", lastUsedTags.toLowerCase());
                json.put("askForNewTags", getSettings().get("askForNewTags"));

                mapper.writeValue(new File(path + fileName), json);
                System.out.println("Sucessfully changed the tags to search / disregard for.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid input. Select existing tags.");
        }

    }

    public static void setAskForNewTags(String askForNewTags) {
        String[] allowedInputs = {"y", "n", "yes", "no", "ye", "ne", "yeah", "nope", "never", "yup", "y(es)", "n(o)", "nah", "yip"};

        if(Arrays.stream(allowedInputs).toList().contains(askForNewTags.toLowerCase().trim())) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                String path = "src/";
                String fileName = "settings.json";
                File file = new File(path + fileName);

                ObjectNode json = mapper.createObjectNode();
                json.put("startLanguage", getSettings().get("startLanguage"));
                json.put("lastUsedTags", getSettings().get("lastUsedTags"));
                json.put("askForNewTags", "" + askForNewTags.toLowerCase().trim().startsWith("y"));

                mapper.writeValue(new File(path + fileName), json);
                System.out.println("Sucessfully changed if programm always asks tags to search for at start.");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.out.println("Invalid input. Select either 'y' or 'n'.");
        }

    }

}