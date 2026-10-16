import com.mimo.HandlingTags;
import com.mimo.VocabList;
import com.mimo.WriteJSON;
import java.util.Scanner;

public static void main(String[] args) {


    VocabList.initializingAllVocab();
    HashMap<String, List<String>> trainingVocab = startWalkthrough();
    beginTraining(trainingVocab);




}

public static void printLinearLine() {
    System.out.println("------------------------------");
}

public static void beginTraining(HashMap<String, List<String>> vocabularyList) {
    if(vocabularyList == null) {throw new IllegalArgumentException("vocabularyList cannot be null");}
    int size = vocabularyList.size();
    System.out.println("Size:" + size);
    ArrayList<String> lastUsedVocabs = new ArrayList<>();
    String startLanguage = WriteJSON.getSettings().get("startLanguage");

    Random rand = new Random();
    rand.setSeed(System.currentTimeMillis());
    int wordCounter = 0;
    while (true) {
        int randomVocabIndex = rand.nextInt(size);
        String japaneseMeaning = vocabularyList.keySet().toArray()[randomVocabIndex].toString();
        List<String> englishMeanings = vocabularyList.get(japaneseMeaning);
        if(!lastUsedVocabs.contains(englishMeanings.getFirst())) {
            lastUsedVocabs.add(englishMeanings.getFirst());

            String showingVocab = (startLanguage == "japanese") ? englishMeanings.toString() : japaneseMeaning;
            String guessMeaningVocab = (startLanguage == "japanese") ? japaneseMeaning : englishMeanings.toString();

            System.out.println(showingVocab);

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(cancelLoop(input)) {
                break;
            } else {
                System.out.println(guessMeaningVocab);
                printLinearLine();
            }

            if(lastUsedVocabs.size() > 5) {
                lastUsedVocabs.remove(wordCounter);
                wordCounter++;
                if(wordCounter > 5) {
                    wordCounter = 0;
                }
            }
        }
    }
}

public static HashMap<String, List<String>> startWalkthrough() {
    printLinearLine();
    System.out.println("Would you like to start training or change the settings?");
    System.out.println("1. Train Vocabulary");
    System.out.println("2. Change Settings");
    Scanner scanner = new Scanner(System.in);
    String userInput = scanner.nextLine();

    printLinearLine();

    switch (userInput.trim()) {
        case "1":
            String lastUsedTags = WriteJSON.getSettings().get("lastUsedTags");
            if(WriteJSON.getSettings().get("askForNewTags").equals("true")) {
                WriteJSON.setLastUsedTags(handlingUserTagsInput());
                lastUsedTags = WriteJSON.getSettings().get("lastUsedTags");
            }

            System.out.println("Selected tags:" + lastUsedTags);

            printLinearLine();

            return HandlingTags.createAllowedVocabList(HandlingTags.detectingTags(lastUsedTags));

        case "2":
            System.out.println("How would you like to change the settings?");
            System.out.println("1. Change start language");
            System.out.println("2. Change tags to search / disregard for");
            System.out.println("3. Change if the program should always ask for the tags at the start of training");
            System.out.println("4. Show current settings");
            String settingsUserInput = scanner.nextLine();

            printLinearLine();

            String configUserInput;
            switch (settingsUserInput) {
                case "1":
                    System.out.println("Current start language: " + WriteJSON.getSettings().get("startLanguage"));
                    System.out.print("New start language: ");
                    configUserInput = scanner.nextLine();
                    WriteJSON.setStartLanguage(configUserInput);
                    break;

                case "2":
                    System.out.println("Current tags to search / disregard for: " + WriteJSON.getSettings().get("lastUsedTags"));
                    System.out.println("Existing tags:     hiragana, katakana, kanji, verb, music, color, language, country, family,\n" +
                            "bodypart, food, vegetable, fruit, drink, animal, month, cloth, sport, adjective, na_adjective, i_adjective, time, \n" +
                            "temperature, weather, profession, hobby, furniture, kanjireplace");
                    System.out.print("New tags to search / disregard for: ");
                    configUserInput = scanner.nextLine();
                    WriteJSON.setLastUsedTags(configUserInput);
                    break;

                case "3":
                    System.out.println("Ask for new tags at start of training: " + WriteJSON.getSettings().get("askForNewTags"));
                    System.out.println("Should it ask for new tags at start of training? (Y/N)");
                    configUserInput = scanner.nextLine();
                    WriteJSON.setAskForNewTags(configUserInput);
                    break;

                case "4":
                    System.out.println("Current start language: " + WriteJSON.getSettings().get("startLanguage"));
                    System.out.println("Current tags to search / disregard for: " + WriteJSON.getSettings().get("lastUsedTags"));
                    System.out.println("Ask for new tags at start of training: " + WriteJSON.getSettings().get("askForNewTags"));
                    break;

                default:
                    System.out.println("Invalid input. Select a whole number from 1 to 4.");
                    startWalkthrough();
                    break;
            }
            break;
        default:
            if(cancelLoop(userInput)) {
                throw  new RuntimeException("Ended program");
            }
            System.out.println("Invalid input. Select '1' or '2'.");
            startWalkthrough();
            break;
    }
    startWalkthrough();
    return null;
}

public static boolean cancelLoop(String userInput) {
    String[] cancelWords = {"quit", "q", "leave", "stop", "cancel", "n", "no", "break", "halt"};
    return (Arrays.stream(cancelWords).toList().contains(userInput.toLowerCase()));
}

public static String handlingUserTagsInput() {
    Scanner userInput = new Scanner(System.in);
    System.out.print("Enter the tags to search / disregard for: ");
    String input = userInput.nextLine();
    if(!input.isBlank()) {
        return input;
    } else {
        throw(new IllegalArgumentException("Invalid input"));
    }

}