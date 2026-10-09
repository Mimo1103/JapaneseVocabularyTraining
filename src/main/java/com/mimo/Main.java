import com.mimo.HandlingTags;
import com.mimo.Vocabulary;
import com.mimo.WriteJSON;
import java.util.Scanner;

public static void main(String[] args) {


    initializingAllVocab();
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
            if(input.equals("stop") || input.equals("quit") || input.equals("leave")) {
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
            System.out.println("Invalid input. Select '1' or '2'.");
            startWalkthrough();
            break;
    }
    startWalkthrough();
    return null;
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

public static void initializingAllVocab() {
    //ABBREVIATIONS
    //sth. = something
    //swh. = somewhere
    //sb. = somebody
    //sb.'s = somebody's

    //AVAILABLE TAGS
    //hiragana
    //katakana
    //kanji
    //verb
    //music
    //color
    //language
    //country
    //family
    //bodypart
    //food
    //vegetable
    //fruit
    //drink
    //animal
    //month
    //time

    //cloth
    //sport
    //adjective
    //na_adjective
    //i_adjective
    //temperature
    //weather
    //profession
    //hobby
    //furniture
    //kanjireplace

    System.out.println("Writing all JSON files: 0%");

    Vocabulary CEO = new Vocabulary("CEO", "CEO", "romaji, job");
    Vocabulary JPOP = new Vocabulary("Jポップ", "J-pop", "romaji, katakana, music");
    Vocabulary KPOP = new Vocabulary("Kポップ", "K-pop", "romaji, katakana, music");
    Vocabulary MSIZE = new Vocabulary("Mサイズ", "M size", "romaji, katakana");
    Vocabulary SSIZE = new Vocabulary("Sサイズ", "size S", "romaji, katakana");
    Vocabulary TSHIRT = new Vocabulary("Tシャツ", "T-shirt", "romaji, katakana");
    Vocabulary WIFI = new Vocabulary("WiFi", "wifi", "romaji");
    Vocabulary O = new Vocabulary("0", "0", "romaji");
    Vocabulary BLUE = new Vocabulary("あおい", "blue", "hiragana, color");
    Vocabulary RED = new Vocabulary("あかい", "red", "hiragana, color");
    Vocabulary BABY = new Vocabulary("あかちゃん", "baby", "hiragana");
    Vocabulary BRIGHT = new Vocabulary("あかるい", "bright, cheerful, well-lit", "hiragana");
    Vocabulary AUTUMN = new Vocabulary("あき", "autumn", "hiragana, time");
    Vocabulary AKIHABARA = new Vocabulary("あきはばら", "Akihabara", "hiragana");
    Vocabulary OPEN = new Vocabulary("あきます", "(to) open sth.", "hiragana, verb");
    Vocabulary ASAKUSA = new Vocabulary("あさくさ", "Asakusa", "hiragana");
    Vocabulary BREAKFAST = new Vocabulary("あさごはん", "breakfast", "hiragana, food");
    Vocabulary LEG = new Vocabulary("あし", "leg", "hiragana, bodypart");
    Vocabulary TOMORROW = new Vocabulary("あした", "tomorrow", "hiragana, kanjireplace, time");
    Vocabulary OVERTHERE = new Vocabulary("あそこ", "(over) there", "hiragana");
    Vocabulary HANGOUT = new Vocabulary("あそびます", "(to) hang out, (to) play with sth.", "hiragana, verb");
    Vocabulary WARM = new Vocabulary("あたたかい", "warm", "hiragana");
    Vocabulary HOT = new Vocabulary("あつい", "hot, thick", "hiragana");
    Vocabulary AFTER = new Vocabulary("あと", "after", "hiragana, time");
    Vocabulary OLDERBROTHER_MY = new Vocabulary("あに", "my older brother", "hiragana, family");
    Vocabulary OLDERSISTER = new Vocabulary("あね", "older sister", "hiragana, family");
    Vocabulary THOSE = new Vocabulary("あの", "those, that (over there), ah", "hiragana");
    Vocabulary DANGEROUS = new Vocabulary("あぶない", "dangerous", "hiragana");
    Vocabulary OILY = new Vocabulary("あぶらっこい", "oily", "hiragana");
    Vocabulary SWEET = new Vocabulary("あまい", "sweet", "hiragana");
    Vocabulary NOTALOTOF = new Vocabulary("あまり", "(not) a lot of, (not) so, (not) too", "hiragana");
    Vocabulary CANDY = new Vocabulary("あめ", "candies, cany, sweets", "hiragana, food");
    Vocabulary THANKYOU = new Vocabulary("ありがとうございます", "thank you", "hiragana");
    Vocabulary IS_ARI = new Vocabulary("あります", "(to) be, (to) have, there is", "hiragana, verb");
    Vocabulary THATONE = new Vocabulary("あれ", "that (one)(over there), hey, what", "hiragana");
    Vocabulary SAFE = new Vocabulary("あんぜん", "safe", "hiragana");
    Vocabulary WELL = new Vocabulary("いい", "like, well, do you want", "hiragana");
    Vocabulary NO = new Vocabulary("いいえ", "no", "hiragana");
    Vocabulary THATSGOOD = new Vocabulary("いいですね", "that's good", "hiragana");
    Vocabulary HOUSE_HIR = new Vocabulary("いえ", "house", "hiragana, kanjireplace");
    Vocabulary HOWMANY = new Vocabulary("いくつ", "how many", "hiragana");
    Vocabulary HOWMUCH = new Vocabulary("いくら", "how much", "hiragana");
    Vocabulary DOCTOR = new Vocabulary("いしゃ", "doctor", "hiragana");
    Vocabulary CHAIR = new Vocabulary("いす", "chair", "hiragana");
    Vocabulary BUSY = new Vocabulary("いそがしい", "busy, occupied", "hiragana");
    Vocabulary ONE = new Vocabulary("いち", "one", "hiragana, kanjireplace");
    Vocabulary STRAWBERRY = new Vocabulary("いちご", "strawberry", "hiragana, food, fruit");
    Vocabulary TOGETHER = new Vocabulary("いっしょに", "together", "hiragana");
    Vocabulary WHEN = new Vocabulary("いつ", "when", "hiragana, time");
    Vocabulary ALWAYS = new Vocabulary("いつも", "always", "hiragana, time");
    Vocabulary COUSIN = new Vocabulary("いとこ", "cousin", "hiragana, family");
    Vocabulary DOG = new Vocabulary("いぬ", "dog", "hiragana, animal");
    Vocabulary NOW_HIR = new Vocabulary("いま", "now", "hiragana, kanjireplace, time");
    Vocabulary FROMNOWON_NOW = new Vocabulary("いまから", "from now on, in the future", "hiragana, time");
    Vocabulary IS_I = new Vocabulary("います", "(to) be, (to) have, there is", "hiragana, verb");
    Vocabulary LAST = new Vocabulary("いまの", "last, current", "hiragana, time");
    Vocabulary YOUNGERSISTER = new Vocabulary("いもうと", "younger sister", "hiragana, family");
    Vocabulary WELCOME = new Vocabulary("いらっしゃい", "welcome", "hiragana");
    Vocabulary ENTRANCE_HIR = new Vocabulary("いりぐち", "entrance", "hiragana");
    Vocabulary UENOPARK = new Vocabulary("うえのこうえん", "Ueno Park", "hiragana");
    Vocabulary RECEPTION = new Vocabulary("うけつけ", "reception", "hiragana");
    Vocabulary RABBIT = new Vocabulary("うさぎ", "rabbit", "hiragana, animal");
    Vocabulary BEHIND = new Vocabulary("うしろ", "behind", "hiragana");
    Vocabulary SING = new Vocabulary("うたいます", "(to) sing", "hiragana, verb");
    Vocabulary WRISTWATCH = new Vocabulary("うでどけい", "wristwatch", "hiragana");
    Vocabulary UDON = new Vocabulary("うどん", "udon", "hiragana, food");
    Vocabulary BEACH = new Vocabulary("うみ", "beach, sea, ocean", "hiragana, kanjireplace");
    Vocabulary NOISY = new Vocabulary("うるさい", "noisy, loud, annoying", "hiragana");
    Vocabulary EXERCISE = new Vocabulary("うんどうします", "(to) exercise", "hiragana, verb");
    Vocabulary PICTURE = new Vocabulary("え", "picture, painting", "hiragana");
    Vocabulary MOVIE = new Vocabulary("えいが", "movie", "hiragana");
    Vocabulary MOVIETHEATER = new Vocabulary("えいがかん", "movie theater", "hiragana");
    Vocabulary ENGLISH = new Vocabulary("えいご", "English", "hiragana, language");
    Vocabulary UM = new Vocabulary("ええと", "um", "hiragana");
    Vocabulary STATION = new Vocabulary("えき", "station, train station", "hiragana, kanjireplace");
    Vocabulary YEN = new Vocabulary("えん", "yen", "hiragana");
    Vocabulary DELICIOUS = new Vocabulary("おいしい", "delicious, tasty, good", "hiragana");
    Vocabulary BIG = new Vocabulary("おおきい", "big", "hiragana");
    Vocabulary OSAKA = new Vocabulary("おおさか", "Osaka", "hiragana");
    Vocabulary NEWYEARSEVE = new Vocabulary("おおみそか", "New Year's Eve", "hiragana, time");
    Vocabulary SNACK = new Vocabulary("おかし", "snack", "hiragana, food");
    Vocabulary WAKEUP = new Vocabulary("おきます", "(to) wake up, (to) get up", "hiragana, verb");
    Vocabulary BACK = new Vocabulary("おく", "back", "hiragana");
    Vocabulary BELATE = new Vocabulary("おくれます", "(to) be late", "hiragana, verb");
    Vocabulary CHILD_YOU = new Vocabulary("おこさん", "(your) child", "hiragana, family");
    Vocabulary GETANGRY = new Vocabulary("おこります", "(to) get angry", "hiragana, verb");
    Vocabulary ALCOHOL = new Vocabulary("おさけ", "alcohol", "hiragana, drink");
    Vocabulary PLATE = new Vocabulary("おさら", "plate", "hiragana");
    Vocabulary STYLISH = new Vocabulary("おしゃれ", "stylish", "hiragana");
    Vocabulary LATE = new Vocabulary("おそい", "slow, late", "hiragana, time");
    Vocabulary UNTILLATE = new Vocabulary("おそくまで", "until late", "hiragana, time");
    Vocabulary GREENTEA = new Vocabulary("おちゃ", "green tea", "hiragana, drink");
    Vocabulary HUSBAND = new Vocabulary("おっと", "husband", "hiragana, family");
    Vocabulary RESTROOM = new Vocabulary("おてあらい", "restroom", "hiragana");
    Vocabulary TEMPLE = new Vocabulary("おてら", "temple", "hiragana");
    Vocabulary YOUNGERBROTHER = new Vocabulary("おとうと", "younger brother", "hiragana, family");
    Vocabulary DANCE_VER = new Vocabulary("おどります", "(to) dance", "hiragana, verb");
    Vocabulary HUNGRY = new Vocabulary("おなかがすきました", "(to) be hungry", "hiragana, verb");
    Vocabulary SAME = new Vocabulary("おなじ", "same", "hiragana");
    Vocabulary WHATSYOURNAME = new Vocabulary("おなまえは", "What's your name?", "hiragana");
    Vocabulary OLDERBROTHER = new Vocabulary("おにいさん", "(your) older brother", "hiragana, family");
    Vocabulary RICEBALL = new Vocabulary("おにぎり", "rice ball", "hiragana, food");
    Vocabulary OLDERSISTER_YOU = new Vocabulary("おねえさん", "(your) older sister", "hiragana, family");
    Vocabulary NEED = new Vocabulary("おねがいします", "(to) need sth.", "hiragana, verb");
    Vocabulary BATH = new Vocabulary("おふろ", "bath", "hiragana");
    Vocabulary FESTIVAL = new Vocabulary("おまつり", "festival", "hiragana");
    Vocabulary SOUVENIR = new Vocabulary("おみやげ", "souvenir", "hiragana");
    Vocabulary HEAVY = new Vocabulary("おもい", "heavy", "hiragana");
    Vocabulary INTERESTING = new Vocabulary("おもしろい", "interesting, fun, funny", "hiragana");
    Vocabulary TOY = new Vocabulary("おもちゃ", "toy", "hiragana");
    Vocabulary SWIM = new Vocabulary("およぎます", "(to) swim", "hiragana, verb");
    Vocabulary END = new Vocabulary("おわります", "(to) end sth.", "hiragana, verb");
    Vocabulary MUSIC = new Vocabulary("おんがく", "music", "hiragana, music");
    Vocabulary HOTSPRING = new Vocabulary("おんせん", "hot spring", "hiragana");
    Vocabulary MOTHER = new Vocabulary("お母さん", "(your) mom, mother, mom", "hiragana,kanji, family");
    Vocabulary DAD = new Vocabulary("お父さん", "(your) dad, (your) father, dad", "hiragana,kanji, family");
    Vocabulary ISIT = new Vocabulary("か", "or, is it?", "particle");
    Vocabulary BUY_HIR = new Vocabulary("かい", "(to) buy sth.", "hiragana, verb");
    Vocabulary TICKETGATE = new Vocabulary("かいさつ", "ticket gate", "hiragana");
    Vocabulary COMPANY = new Vocabulary("かいしゃ", "company, work, office", "hiragana");
    Vocabulary STAIRS = new Vocabulary("かいだん", "stairs", "hiragana");
    Vocabulary RETURN = new Vocabulary("かえります", "(to) return home/swh.", "hiragana, verb");
    Vocabulary TAKE = new Vocabulary("かかります", "(to) take sth.", "hiragana, verb");
    Vocabulary WRITE = new Vocabulary("かきます", "write, draw", "hiragana");
    Vocabulary KEY = new Vocabulary("かぎ", "key", "hiragana");
    Vocabulary BASKET = new Vocabulary("かご", "basket", "hiragana");
    Vocabulary UMBRELLA = new Vocabulary("かさ", "umbrella", "hiragana");
    Vocabulary FAMILY = new Vocabulary("かぞく", "family", "hiragana, family");
    Vocabulary COOL = new Vocabulary("かっこいい", "cool", "hiragana");
    Vocabulary GIRLFRIEND = new Vocabulary("かのじょ", "girlfriend", "hiragana");
    Vocabulary BAG = new Vocabulary("かばん", "bag", "hiragana");
    Vocabulary KABUKI = new Vocabulary("かぶき", "kabuki", "hiragana");
    Vocabulary BECAUSE = new Vocabulary("から", "because, starting, through", "hiragana");
    Vocabulary KARATE = new Vocabulary("からて", "karate", "hiragana");
    Vocabulary LIGHT = new Vocabulary("かるい", "light, lightweight", "hiragana");
    Vocabulary BOYFRIEND = new Vocabulary("かれし", "(my) boyfriend", "hiragana");
    Vocabulary CUTE = new Vocabulary("かわいい", "cute, pretty", "hiragana");
    Vocabulary SIGHTSEEING = new Vocabulary("かんこうをします", "(to) go sightseeing, (to) tour", "hiragana, verb");
    Vocabulary KOREA = new Vocabulary("かんこく", "Korea", "hiragana, country");
    Vocabulary KOREAN = new Vocabulary("かんこくご", "Korean", "hiragana, language");
    Vocabulary DIRECTOR = new Vocabulary("かんとく", "director", "hiragana");
    Vocabulary BUT = new Vocabulary("が", "but", "hiragana");
    Vocabulary STUDENT = new Vocabulary("がくせい", "student", "hiragana");
    Vocabulary LOOKINGFORWARDTO = new Vocabulary("たのしみです", "(to) look forward to sth.", "hiragana, verb");
    Vocabulary CANPLAY = new Vocabulary("できます", "know, can play", "hiragana");
    Vocabulary DOBEST = new Vocabulary("がんばります", "(to) try my/our/your best, (to) do my/our/your best", "hiragana, verb");
    Vocabulary LISTEN = new Vocabulary("かきます", "(to) listen, (to) ask", "hiragana, verb");
    Vocabulary ARTICLE = new Vocabulary("かじ", "article", "hiragana");
    Vocabulary DIRTY = new Vocabulary("きたない", "dirty, foul", "hiragana");
    Vocabulary TICKET = new Vocabulary("きっぷ", "ticket", "hiragana");
    Vocabulary YESTERDAY = new Vocabulary("きのう", "yesterday", "hiragana, time");
    Vocabulary WEAR = new Vocabulary("きます", "(to) wear sth.", "hiragana, verb");
    Vocabulary AMBULANCE = new Vocabulary("きゅきゅしゃ", "ambulance", "hiragana");
    Vocabulary TODAY = new Vocabulary("きょう", "today", "hiragana, time, kanjireplace");
    Vocabulary CLASSROOM = new Vocabulary("きょうしつ", "classroom", "hiragana");
    Vocabulary SIBLING = new Vocabulary("きょうだい", "sibling", "hiragana");
    Vocabulary LASTYEAR = new Vocabulary("きょねん", "last year", "hiragana, time, kanjireplace");
    Vocabulary CUT = new Vocabulary("きります", "(to) cut sth.", "hiragana, verb");
    Vocabulary GIRAFFE = new Vocabulary("きりん", "giraffe", "hiragana, animal");
    Vocabulary PRETTY = new Vocabulary("きれい", "beautiful, pretty, clean", "hiragana");
    Vocabulary GYUDON = new Vocabulary("ぎゅうどん", "gyudon", "hiragana, food");
    Vocabulary MILK = new Vocabulary("ぎゅうにゅう", "milk", "hiragana, drink");
    Vocabulary BEEF = new Vocabulary("ぎゅう肉", "beef", "hiragana, food");
    Vocabulary GINZA = new Vocabulary("ぎんざ", "Ginza", "hiragana");
    Vocabulary AIRPORT = new Vocabulary("くうこう", "airport", "hiragana");
    Vocabulary TAKE_MED = new Vocabulary("くすりを飲みます", "(to) take (medicine)", "hiragana, verb");
    Vocabulary PLEASE = new Vocabulary("ください", "please", "hiragana");
    Vocabulary FRUIT = new Vocabulary("くだもの", "fruit", "hiragana, fruit");
    Vocabulary SHOE = new Vocabulary("くつ", "shoe", "hiragana");
    Vocabulary SOCK = new Vocabulary("くつした", "sock", "hiragana");
    Vocabulary DARK = new Vocabulary("くらい", "dark, around, about", "hiragana");
    Vocabulary BLACK = new Vocabulary("くろい", "black", "hiragana, color");
    Vocabulary BLACKPEPPER = new Vocabulary("くろこしょう", "black pepper", "hiragana");
    Vocabulary UNWELL = new Vocabulary("ぐあいがわるい", "(to) feel unwell", "hiragana, verb");
    Vocabulary APPROXIMATELY = new Vocabulary("ぐらい", "approximately", "hiragana");
    Vocabulary POLICE = new Vocabulary("けいさつ", "police", "hiragana");
    Vocabulary KEN = new Vocabulary("けん", "Ken", "hiragana");
    Vocabulary HEALTHY = new Vocabulary("けんこうてき", "healthy", "hiragana");
    Vocabulary LIVELY = new Vocabulary("げんき", "lively", "hiragana");
    Vocabulary LANGUAGE = new Vocabulary("げんご", "language", "hiragana");
    Vocabulary PARK = new Vocabulary("こうえん", "park", "hiragana");
    Vocabulary BLACKTEA = new Vocabulary("こうちゃ", "black tea", "hiragana, drink");
    Vocabulary HERE = new Vocabulary("ここ", "here, this (place), over here", "hiragana");
    Vocabulary THIS = new Vocabulary("こちら", "this, these, (over) here", "hiragana");
    Vocabulary CHILD = new Vocabulary("こども", "child", "hiragana, family");
    Vocabulary THESE = new Vocabulary("この", "this, these, that's", "hiragana");
    Vocabulary THISAREA = new Vocabulary("このあたり", "this area", "hiragana");
    Vocabulary AREA = new Vocabulary("あたり", "area", "hiragana");
    Vocabulary AFTERTHIS = new Vocabulary("このあと", "after this", "hiragana");
    Vocabulary FLOUR = new Vocabulary("こむぎこ", "flour", "hiragana");
    Vocabulary THIS_ = new Vocabulary("これ", "this, these, this (one)", "hiragana");
    Vocabulary FROMNOWON = new Vocabulary("これから", "from now on, starting right now", "hiragana");
    Vocabulary BREAK = new Vocabulary("こわれます", "(to) break sth.", "hiragana, verb");
    Vocabulary THISWEEK = new Vocabulary("こんしゅう", "this week", "hiragana, kanjireplace, time");
    Vocabulary THISWEEKEND = new Vocabulary("こんしゅうまつ", "this weekend", "hiragana, time, kanjireplace");
    Vocabulary NEXTTIME = new Vocabulary("こんど", "next time", "hiragana, time");
    Vocabulary GOODAFTERNOON = new Vocabulary("こんにちは", "hi, hello, good afternoon", "hiragana");
    Vocabulary THISEVENING = new Vocabulary("こんばん", "this evening", "hiragana, time");
    Vocabulary GOODEVENING = new Vocabulary("こんばんは", "good evening", "hiragana");
    Vocabulary TONIGHT_HIR = new Vocabulary("こんや", "tonight", "hiragana, time, kanjireplace");
    Vocabulary FAMILY_POL = new Vocabulary("ごかぞく", "(your) family", "hiragana, family");
    Vocabulary AFTERNOON = new Vocabulary("ごご", "afternoon, p.m.", "hiragana, time");
    Vocabulary MORNING = new Vocabulary("ごぜん", "morning, a.m.", "hiragana, time");
    Vocabulary RICE = new Vocabulary("ごはん", "rice", "hiragana, food");
    Vocabulary YOURPARENTS = new Vocabulary("ごりょうしん", "(your) parents", "hiragana, family");
    Vocabulary AROUND = new Vocabulary("ごろ", "around", "hiragana");
    Vocabulary YEARSOLD = new Vocabulary("さい", "years old", "hiragana");
    Vocabulary LAST_ = new Vocabulary("さいご", "last", "hiragana");
    Vocabulary WALLET = new Vocabulary("さいふ", "wallet", "hiragana");
    Vocabulary LOOKFOR = new Vocabulary("さがします", "(to) look for sth./sb.", "hiragana, verb");
    Vocabulary BLOOM = new Vocabulary("さきます", "(to) bloom", "hiragana, verb");
    Vocabulary CHERRYBLOSSOM = new Vocabulary("さくら", "cherry blossom", "hiragana");
    Vocabulary SWEETPOTATO = new Vocabulary("さつまいも", "sweet potato", "hiragana, food, vegetable");
    Vocabulary SUGAR = new Vocabulary("さとう", "sugar", "hiragana");
    Vocabulary COLD = new Vocabulary("さむい", "cold", "hiragana");
    Vocabulary MONKEY = new Vocabulary("さる", "monkey", "hiragana");
    Vocabulary MISTER = new Vocabulary("さん", "Mr., Miss, Mrs.", "hiragana");
    Vocabulary MR = new Vocabulary("さんは", "Mr./Ms.", "hiragana");
    Vocabulary MAGAZINE = new Vocabulary("ざっし", "magazine", "hiragana");
    Vocabulary MATCH = new Vocabulary("しあい", "match", "hiragana");
    Vocabulary SALT = new Vocabulary("しお", "salt", "hiragana, food");
    Vocabulary JOB = new Vocabulary("しごと", "job, work", "hiragana");
    Vocabulary WORK = new Vocabulary("しごとします", "(to) work", "hiragana, verb");
    Vocabulary QUIET = new Vocabulary("しずか", "quiet", "hiragana");
    Vocabulary KNOW = new Vocabulary("しっています", "(to) know (a fact about) sth./sb.", "hiragana, verb");
    Vocabulary QUESTION = new Vocabulary("しつもん", "question", "hiragana");
    Vocabulary SHIBUYA = new Vocabulary("しぶや", "Shibuya", "hiragana");
    Vocabulary DO = new Vocabulary("します", "(to) do sth., (to) make sth.", "hiragana, verb");
    Vocabulary DEADLINE = new Vocabulary("しめきり", "deadline", "hiragana");
    Vocabulary PHOTO = new Vocabulary("しゃしん", "photo", "hiragana");
    Vocabulary LASTSTOP = new Vocabulary("しゅうてん", "last stop", "hiragana");
    Vocabulary WEEKEND = new Vocabulary("しゅうまつ", "weekend", "hiragana, time, kanjireplace");
    Vocabulary HOMEWORK = new Vocabulary("しゅうくだい", "homework", "hiragana");
    Vocabulary FROM = new Vocabulary("しゅっしん", "from swh., origin, hometown", "hiragana");
    Vocabulary INTRODUCE = new Vocabulary("しょうかいします", "(to) introduce sb to sb", "hiragana, verb");
    Vocabulary SOYSAUCE = new Vocabulary("しょうゆ", "soy sauce", "hiragana, drink, food");
    Vocabulary CAFETERIA = new Vocabulary("しょくどう", "cafeteria", "hiragana");
    Vocabulary SALTY = new Vocabulary("しょっぱい", "salty", "hiragana");
    Vocabulary CASTLE = new Vocabulary("しろ", "castle", "hiragana");
    Vocabulary WHITE = new Vocabulary("しろい", "white", "hiragana, color");
    Vocabulary SHINKANSEN = new Vocabulary("しんかんせん", "Shinkansen", "hiragana");
    Vocabulary EXAMINATIONROOM = new Vocabulary("しんさつしつ", "examination room", "hiragana");
    Vocabulary SHINJUKU = new Vocabulary("しんじゅく", "Shinjuku", "hiragana");
    Vocabulary WELLHEARTED = new Vocabulary("しんせつ", "kind, well-hearted", "hiragana");
    Vocabulary OCLOCK_HIR = new Vocabulary("じ", "o'clock", "hiragana, time");
    Vocabulary VENDINGMACHINE = new Vocabulary("じはんき", "vending machine", "hiragana");
    Vocabulary WELLTHEN = new Vocabulary("じゃあ", "well then, well, see you", "hiragana");
    Vocabulary POTATO = new Vocabulary("じゃがいも", "potato", "hiragana, food, vegetable");
    Vocabulary DONOT = new Vocabulary("じゃない", "do not, am not, I am not", "hiragana");
    Vocabulary JUDO = new Vocabulary("じゅうどう", "judo", "hiragana");
    Vocabulary CLASS = new Vocabulary("ぐぎょう", "class", "hiragana");
    Vocabulary SHRINE = new Vocabulary("じんじゃ", "shrine", "hiragana");
    Vocabulary SWIM_NOU = new Vocabulary("すいえい", "swim", "hiragana");
    Vocabulary WATERMELON = new Vocabulary("すいか", "watermelon", "hiragana, food, fruit");
    Vocabulary AQUARIUM = new Vocabulary("すいぞくかん", "aquarium", "hiragana");
    Vocabulary MATH = new Vocabulary("すうがく", "math", "hiragana");
    Vocabulary LIKE_HIR = new Vocabulary("すき", "(to) like sth.", "hiragana, verb");
    Vocabulary FAVORITE = new Vocabulary("すきな", "favorite", "hiragana");
    Vocabulary SOON = new Vocabulary("すぐ", "soon, then, in a minute", "hiragana, time");
    Vocabulary ABIT = new Vocabulary("すこし", "a bit, few, some", "hiragana");
    Vocabulary SUSHI = new Vocabulary("すし", "sushi", "hiragana, food");
    Vocabulary SUSHIRESTAURANT = new Vocabulary("すしや", "sushi restaurant", "hiragana");
    Vocabulary LOVELY = new Vocabulary("すてき", "lovely", "hiragana");
    Vocabulary CORNER = new Vocabulary("すみ", "corner", "hiragana");
    Vocabulary SORRY = new Vocabulary("すみません", "sorry, pardon me, excuse me", "hiragana");
    Vocabulary SIT = new Vocabulary("すわります", "(to) sit", "hiragana, verb");
    Vocabulary SEAT = new Vocabulary("せき", "seat", "hiragana");
    Vocabulary CRAMPED = new Vocabulary("せまい", "narrow, cramped, small", "hiragana");
    Vocabulary MAJOR = new Vocabulary("せんこう", "major", "hiragana");
    Vocabulary TEACHER = new Vocabulary("せんせい", "teacher", "hiragana");
    Vocabulary WASHINGMACHINE = new Vocabulary("せんたくき", "washing machine", "hiragana");
    Vocabulary INTOTAL = new Vocabulary("ぜんぶで", "in total", "hiragana");
    Vocabulary VACUUMCLEANER = new Vocabulary("そうじき", "vacuum cleaner", "hiragana");
    Vocabulary THERE = new Vocabulary("そこ", "there", "hiragana");
    Vocabulary THAT = new Vocabulary("その", "the, that, those", "hiragana");
    Vocabulary SOBA = new Vocabulary("そば", "soba", "hiragana, food");
    Vocabulary IT = new Vocabulary("それ", "that, that (one), it", "hiragana");
    Vocabulary TAI = new Vocabulary("たい", "want to", "hiragana");
    Vocabulary TAIKO = new Vocabulary("たいこ", "taiko", "hiragana");
    Vocabulary HARD = new Vocabulary("たいへん", "hard", "hiragana");
    Vocabulary TAIWAN = new Vocabulary("たいわん", "Taiwan", "hiragana, country");
    Vocabulary EXPENSIVE_HIR = new Vocabulary("たかい", "expensive", "hiragana");
    Vocabulary MANY = new Vocabulary("たくさん", "many", "hiragana");
    Vocabulary ANDTHEOTHERS = new Vocabulary("たち", "and the others", "hiragana");
    Vocabulary BULDING = new Vocabulary("たてもの", "building", "hiragana");
    Vocabulary SHELF = new Vocabulary("たな", "shelf", "hiragana");
    Vocabulary TANAKA_HIR = new Vocabulary("たなか", "Tanaka", "hiragana");
    Vocabulary FUN = new Vocabulary("たのしい", "fun", "hiragana");
    Vocabulary SMOKE = new Vocabulary("たばこをすいます", "(to) smoke", "hiragana, verb");
    Vocabulary PROBABLY = new Vocabulary("たぶん", "probably", "hiragana");
    Vocabulary EAT_HIR = new Vocabulary("たべます", "(to) eat", "hiragana, verb");
    Vocabulary ONION = new Vocabulary("たまねぎ", "onion", "hiragana, food, vegetable");
    Vocabulary BIRTHDAY = new Vocabulary("たんじょうび", "birthday", "hiragana, time");
    Vocabulary UNIVERSITY = new Vocabulary("だいがく", "university", "hiragana");
    Vocabulary OK = new Vocabulary("だいじょうぶ", "alright, ok, OK", "hiragana");
    Vocabulary LOVE = new Vocabulary("たいすき", "(to) love sth.", "hiragana, verb");
    Vocabulary WHO = new Vocabulary("だれ", "who", "hiragana");
    Vocabulary HEATER = new Vocabulary("だんぼう", "heater", "hiragana");
    Vocabulary SMALL_HIR = new Vocabulary("ちいさい", "small", "hiragana");
    Vocabulary UNDERGROUND = new Vocabulary("ちか", "underground", "hiragana");
    Vocabulary CLOSE = new Vocabulary("ちかい", "near, close", "hiragana");
    Vocabulary NEAR = new Vocabulary("ちかく", "near", "hiragana");
    Vocabulary NEARBY = new Vocabulary("ちかくの", "nearby, sth. is near", "hiragana");
    Vocabulary SUBWAY = new Vocabulary("ちかてつ", "subway", "hiragana");
    Vocabulary MAP = new Vocabulary("ちず", "map", "hiragana");
    Vocabulary CHIBA = new Vocabulary("ちば", "Chiba", "hiragana");
    Vocabulary BROWN = new Vocabulary("ちゃいろ", "brown", "hiragana, color");
    Vocabulary TEABOWL = new Vocabulary("ちゃわん", "tea bowl", "hiragana");
    Vocabulary PROPERLY = new Vocabulary("ちゃんと", "properly", "hiragana");
    Vocabulary CHINA = new Vocabulary("ちゅうごく", "China", "hiragana, country");
    Vocabulary CHINESE = new Vocabulary("ちゅうごくご", "Chinese", "hiragana, language");
    Vocabulary PARKINGLOT = new Vocabulary("ちゅうしゃじょう", "parking lot", "hiragana");
    Vocabulary POORCONDITION = new Vocabulary("ちょうしがわるい", "(to) be in poor condition, (to) not feel well", "hiragana, verb");
    Vocabulary USE = new Vocabulary("つかいます", "(to) use sth.", "hiragana, verb");
    Vocabulary TIRED = new Vocabulary("つかれました", "(to) be tired", "hiragana, verb");
    Vocabulary TIRING = new Vocabulary("つかれます", "is tiring", "hiragana");
    Vocabulary NEXT = new Vocabulary("つぎ", "next", "hiragana, time");
    Vocabulary DESK = new Vocabulary("つくえ", "desk", "hiragana");
    Vocabulary MAKE = new Vocabulary("つかります", "(to) make sth.", "hiragana, verb");
    Vocabulary TURNON = new Vocabulary("つけます", "(to) turn sth. on, (to) put sth. on", "hiragana, verb");
    Vocabulary WIFE = new Vocabulary("つま", "wife", "hiragana, family");
    Vocabulary MEALSET = new Vocabulary("ていしょく", "meal set", "hiragana, food");
    Vocabulary GLOVE = new Vocabulary("でぶくろ", "glove, mitten", "hiragana");
    Vocabulary WASHHANDS = new Vocabulary("てをあらいます", "(to) wash hands", "hiragana, verb");
    Vocabulary CLERK = new Vocabulary("てんいん", "clerk", "hiragana");
    Vocabulary WEATHER = new Vocabulary("てんき", "weather", "hiragana");
    Vocabulary TEMPURA = new Vocabulary("てんぷら", "tempura", "hiragana");
    Vocabulary ASA = new Vocabulary("で", "as a, under, through", "hiragana");
    Vocabulary GOOUT = new Vocabulary("でかけます", "(to) go out", "hiragana, verb");
    Vocabulary EXIT_HIR = new Vocabulary("でぐち", "exit", "hiragana, kanjireplace");
    Vocabulary IS = new Vocabulary("です", "is, it's, you're", "hiragana");
    Vocabulary BUT_GRAM = new Vocabulary("でも", "but", "hiragana");
    Vocabulary ELECTRONICSSTORE = new Vocabulary("でんきや", "electronics store", "hiragana");
    Vocabulary TRAIN = new Vocabulary("でんしゃ", "train", "hiragana, kanjireplace");
    Vocabulary MICROWAVEOVEN = new Vocabulary("でんしレンジ", "microwave oven", "hiragana, katakana");
    Vocabulary BATTERY = new Vocabulary("でんち", "battery", "hiragana");
    Vocabulary TELEPHONE = new Vocabulary("でんわ", "telephone", "hiragana");
    Vocabulary PHONENUMBER = new Vocabulary("でんわばんごう", "phone number", "hiragana");
    Vocabulary AND = new Vocabulary("と", "and", "hiragana");
    Vocabulary SOYMILK = new Vocabulary("とうにゅ", "soy milk", "hiragana, drink");
    Vocabulary TOFU = new Vocabulary("とうふ", "tofu", "hiragana, food");
    Vocabulary FAR = new Vocabulary("とおい", "far", "hiragana");
    Vocabulary OCCASIONALLY = new Vocabulary("ときどき", "occasionally, sometimes", "hiragana, time");
    Vocabulary PLACE = new Vocabulary("ところ", "place, somewhere, about to", "hiragana");
    Vocabulary CITY = new Vocabulary("とし", "city", "hiragana");
    Vocabulary LIBRARY = new Vocabulary("としょかん", "library", "hiragana");
    Vocabulary ONTHEWAY = new Vocabulary("とちゅうで", "on the way", "hiragana");
    Vocabulary VERY = new Vocabulary("とても", "very", "hiragana");
    Vocabulary NEXTDOOR = new Vocabulary("となり", "next door, next to sth.", "hiragana");
    Vocabulary NEIGHBOR = new Vocabulary("となりの人", "neighbor", "hiragana, kanji");
    Vocabulary FRIEND = new Vocabulary("ともだち", "friend", "hiragana");
    Vocabulary BIRD = new Vocabulary("とり", "bird", "hiragana, animal");
    Vocabulary GET = new Vocabulary("とります", "(to) get sth., (to) take sth.", "hiragana, verb");
    Vocabulary PORKCUTLETS = new Vocabulary("とんかつ", "pork cutlets", "hiragana, food");
    Vocabulary ISCALLED = new Vocabulary("と言います", "(to) be called sth.", "hiragana, kanji, verb");
    Vocabulary HOW = new Vocabulary("どう", "how", "hiragana");
    Vocabulary VIDEO = new Vocabulary("どうが", "video", "hiragana");
    Vocabulary HOWABOUT = new Vocabulary("どうですか", "How about sth./sb.", "hiragana");
    Vocabulary ANIMAL = new Vocabulary("どうぶつ", "animal", "hiragana, animal");
    Vocabulary ZOO = new Vocabulary("どうぶつえん", "zoo", "hiragana");
    Vocabulary HOW_QUE = new Vocabulary("どうやって", "how", "hiragana");
    Vocabulary COWORKER = new Vocabulary("どうりょう", "coworker", "hiragana");
    Vocabulary WHERE = new Vocabulary("どこ", "where", "hiragana");
    Vocabulary ANY = new Vocabulary("どの", "any, each, which", "hiragana");
    Vocabulary HOWLONG = new Vocabulary("どのぐらい", "how long", "hiragana, time");
    Vocabulary WHATKINDOF = new Vocabulary("どんな", "what kind of", "hiragana");
    Vocabulary DONT = new Vocabulary("な", "do not, don't, (adjective ending)", "hiragana");
    Vocabulary NAOMI = new Vocabulary("なおみ", "Naomi", "hiragana");
    Vocabulary NAKAYAMA = new Vocabulary("なかやま", "Nakayama", "hiragana");
    Vocabulary LONG = new Vocabulary("ながい", "long", "hiragana, time");
    Vocabulary NAGOYA = new Vocabulary("なごや", "Nagoya", "hiragana");
    Vocabulary SUMMER = new Vocabulary("なつ", "summer", "hiragana");
    Vocabulary PET = new Vocabulary("なでます", "(to) pet sth., (to) stroke sth.", "hiragana, verb");
    Vocabulary HOTPOT = new Vocabulary("なべ", "hot pot, pot", "hiragana, food");
    Vocabulary RAW_HIR = new Vocabulary("なま", "raw", "hiragana");
    Vocabulary WHATTIME_HIR = new Vocabulary("なんじ", "what time", "hiragana, time");
    Vocabulary THROUGH = new Vocabulary("に", "through, made, with", "hiragana");
    Vocabulary HEAR = new Vocabulary("にききます", "(to) listen, (to) hear, (to) ask", "hiragana, verb");
    Vocabulary LIVELY_PLA = new Vocabulary("にぎやか", "lively", "hiragana");
    Vocabulary MEAT_HIR = new Vocabulary("にく", "meat", "hiragana, food");
    Vocabulary WESTEXIT = new Vocabulary("にしぐち", "west exit", "hiragana");
    Vocabulary WILLHAVE = new Vocabulary("にします", "(to) have, (to) make", "hiragana, verb");
    Vocabulary LIVEIN = new Vocabulary("にすんでいます", "(to) live in sth.", "hiragana, verb, kanjireplace");
    Vocabulary ARRIVE = new Vocabulary("につきます", "(to) arrive swh.", "hiragana, verb");
    Vocabulary APPEAR = new Vocabulary("にでます", "(to) appear swh., (to) attend sth., (to) go out", "hiragana, verb");
    Vocabulary CALL = new Vocabulary("にでんわします", "(to) call, (to) telephone, (to) phone", "hiragana, verb");
    Vocabulary STAY = new Vocabulary("にとまります", "(to) stay swh., (to) stop over, (to) lodge", "hiragana, verb");
    Vocabulary LINEUP = new Vocabulary("にならびます", "(to) line up, (to) stand in line, (to) queue", "hiragana, verb");
    Vocabulary GETON_HIR = new Vocabulary("にのります", "(to) get on sth./swh., (to) take sth., (to) ride sth.", "hiragana, verb");
    Vocabulary ENROLL = new Vocabulary("にはいります", "(to) take sth., (to) enroll swh.", "hiragana, verb");
    Vocabulary JAPAN_HIR = new Vocabulary("にほん", "Japan", "hiragana");
    Vocabulary JAPANESEPERSON = new Vocabulary("にほんじん", "Japanese Person", "hiragana");
    Vocabulary GOBACK = new Vocabulary("にもどります", "(to) return swh., (to) go back swh./to sth., (to) come back swh.", "hiragana, verb");
    Vocabulary RESERVE = new Vocabulary("によやくをいれます", "(to) reverse sth., (to) book sth., (to) make a reservation", "hiragana, verb");
    Vocabulary POPULAR = new Vocabulary("にんき", "popular", "hiragana");
    Vocabulary CARROT = new Vocabulary("にんじん", "carrot", "hiragana, food, vegetable");
    Vocabulary LIVEIN_KAN = new Vocabulary("に住んでいます", "(to) live in sth., (to) reside in sth., (to) dwell in sth.", "hiragana, kanji, verb");
    Vocabulary STUFFEDTOY = new Vocabulary("ぬいぐるみ", "stuffed toy", "hiragana");
    Vocabulary ISITNOT = new Vocabulary("ね", "is it not, wouldn't you agree", "hiragana");
    Vocabulary CAT = new Vocabulary("ねこ", "cat", "hiragana, animal");
    Vocabulary SLEEP = new Vocabulary("ねます", "(to) sleep", "hiragana, verb");
    Vocabulary FOR = new Vocabulary("の", "for, 's, one", "hiragana");
    Vocabulary CLOSETO = new Vocabulary("のちかく", "close to sth., near sth.", "hiragana");
    Vocabulary WHICHPART = new Vocabulary("のどこ", "which part, where", "hiragana");
    Vocabulary DRINK_HIR = new Vocabulary("のみます", "(to) drink sth.", "hiragana, verb");
    Vocabulary TRANSFER = new Vocabulary("のりかえ", "transfer", "hiragana");
    Vocabulary REGARDING = new Vocabulary("は", "is, with, regarding", "hiragana");
    Vocabulary YES = new Vocabulary("はい", "yes", "hiragana");
    Vocabulary ACTOR = new Vocabulary("はいゆう", "actor", "hiragana");
    Vocabulary CHOPSTICKS = new Vocabulary("はし", "chopsticks, bridge", "hiragana, S3U12");
    Vocabulary RUN = new Vocabulary("はしります", "(to) run", "hiragana, verb");
    Vocabulary STARTING = new Vocabulary("はじまります", "(to) start sth., (to) begin sth., is starting", "hiragana, verb");
    Vocabulary FIRSTTIME = new Vocabulary("はじめて", "first time", "hiragana, time");
    Vocabulary NICETOMEETYOU = new Vocabulary("はじめまして", "Nice to meet you", "hiragana");
    Vocabulary KINDOF = new Vocabulary("はちょっと", "I don't really like, doesn't work very well, kind of", "hiragana");
    Vocabulary FLOWER = new Vocabulary("はな", "flower", "hiragana");
    Vocabulary FIREWORK = new Vocabulary("はなび", "firework", "hiragana");
    Vocabulary FIREWORKSFESTIVAL = new Vocabulary("はなびたいかい", "fireworks festival", "hiragana");
    Vocabulary QUICK = new Vocabulary("はやい", "fast, early, quick", "hiragana");
    Vocabulary QUICKLY = new Vocabulary("はやく", "fast, early, quickly", "hiragana");
    Vocabulary HARAJUKU = new Vocabulary("はらじゅく", "Harajuku", "hiragana");
    Vocabulary SPRING = new Vocabulary("はる", "spring", "hiragana");
    Vocabulary CLEAR = new Vocabulary("はれ", "clear", "hiragana");
    Vocabulary THIRTY_HIR = new Vocabulary("はん", "thirty", "hiragana");
    Vocabulary NUMBER = new Vocabulary("ばんごう", "number", "hiragana");
    Vocabulary DINNER = new Vocabulary("ばんごはん", "dinner", "hiragana, food");
    Vocabulary LIGHTUP = new Vocabulary("ひかります", "(to) light sth. up, sth. lights up", "hiragana, verb");
    Vocabulary DRAWER = new Vocabulary("ひきだし", "drawer", "hiragana");
    Vocabulary AIRPLANE = new Vocabulary("ひこうき", "airplane", "hiragana");
    Vocabulary MOVE = new Vocabulary("ひっこします", "(to) move to swh.", "hiragana, verb");
    Vocabulary PERSON_HIR = new Vocabulary("ひと", "person", "hiragana, kanjireplace");
    Vocabulary ALONE = new Vocabulary("ひとりで", "alone", "hiragana");
    Vocabulary FREE = new Vocabulary("ひま", "free (on time), available", "hiragana");
    Vocabulary LUNCH_HIR = new Vocabulary("ひるごはん", "lunch", "hiragana, food");
    Vocabulary LUNCHBREAK = new Vocabulary("ひる休み", "lunch break", "hiragana, kanji, time");
    Vocabulary WIDE = new Vocabulary("ひろい", "wide, spacious, big", "hiragana");
    Vocabulary PLAZA = new Vocabulary("ひろば", "plaza", "hiragana");
    Vocabulary ARTMUSEUM = new Vocabulary("びじゅつかん", "art museum", "hiragana");
    Vocabulary HOSPITAL = new Vocabulary("びょういん", "hospital", "hiragana");
    Vocabulary CLOTHES = new Vocabulary("ふく", "clothes", "hiragana");
    Vocabulary MTFUJI = new Vocabulary("ふじさん", "Mt. Fuji", "hiragana");
    Vocabulary FUTON = new Vocabulary("ふとん", "futon", "hiragana");
    Vocabulary WINTER = new Vocabulary("ふゆ", "winter", "hiragana, time");
    Vocabulary FLUFFY = new Vocabulary("ふわふわ", "fluffy", "hiragana");
    Vocabulary PORK_HIR = new Vocabulary("ぶたにく", "pork", "hiragana, food");
    Vocabulary CULTURE = new Vocabulary("ぶんか", "culture", "hiragana");
    Vocabulary ROOM = new Vocabulary("へや", "room", "hiragana");
    Vocabulary DIFFERENT = new Vocabulary("べつの", "different", "hiragana");
    Vocabulary LAWYER = new Vocabulary("べんごし", "lawyer", "hiragana");
    Vocabulary BENTO = new Vocabulary("べんとう", "bento", "hiragana, food");
    Vocabulary CONVENIENT = new Vocabulary("べんり", "convenient", "hiragana");
    Vocabulary WANT = new Vocabulary("ほしい", "(to) want sth.", "hiragana, verb");
    Vocabulary HOKKAIDO = new Vocabulary("ほっかいどう", "Hokkaido", "hiragana");
    Vocabulary BOOKSHELF = new Vocabulary("ほんだな", "bookshelf", "hiragana");
    Vocabulary REALLY = new Vocabulary("ほんとうに", "really", "hiragana");
    Vocabulary HAT = new Vocabulary("ぼうし", "hat", "hiragana");
    Vocabulary EVERYMORNING_HIR = new Vocabulary("まいあさ", "every morning", "hiragana, time");
    Vocabulary EVERYWEEK_HIR = new Vocabulary("まいしゅう", "every week", "hiragana, time");
    Vocabulary EVERYDAY_HIR = new Vocabulary("まいばん", "every day", "hiragana, time");
    Vocabulary FRONT = new Vocabulary("まえ", "front", "hiragana, kanjireplace");
    Vocabulary SERIOUS = new Vocabulary("まじめ", "serious", "hiragana");
    Vocabulary FIRST = new Vocabulary("まず", "first", "hiragana, time");
    Vocabulary TASTEBAD = new Vocabulary("まずい", "sth. tastes bad, bad", "hiragana");
    Vocabulary AGAIN = new Vocabulary("また", "again", "hiragana, time");
    Vocabulary SEEYOUTOMORROW = new Vocabulary("またあした", "see you tomorrow", "hiragana");
    Vocabulary TOWN = new Vocabulary("まち", "town", "hiragana");
    Vocabulary WAIT = new Vocabulary("まちます", "(to) wait", "hiragana, verb");
    Vocabulary MATCHA = new Vocabulary("まっちゃ", "matcha", "hiragana, drink");
    Vocabulary BEFORE = new Vocabulary("まで", "through, even, before", "hiragana");
    Vocabulary WALKUPTO = new Vocabulary("まであるきます", "(to) walk up to sth., (to) walk as far as, (to) walk until", "hiragana, verb");
    Vocabulary WINDOW = new Vocabulary("まど", "window", "hiragana");
    Vocabulary MANGA = new Vocabulary("まんが", "manga", "hiragana");
    Vocabulary MIDDLE = new Vocabulary("まんなか", "middle", "hiragana");
    Vocabulary MANDARIN = new Vocabulary("みかん", "mandarin", "hiragana, food, fruit");
    Vocabulary WATER_HIR = new Vocabulary("みず", "water", "hiragana, drink");
    Vocabulary STORE = new Vocabulary("みせ", "store", "hiragana");
    Vocabulary MISOSOUP = new Vocabulary("みそしる", "miso soup", "hiragana");
    Vocabulary STREET = new Vocabulary("みち", "street", "hiragana");
    Vocabulary GREEN = new Vocabulary("みどり", "green", "hiragana, color");
    Vocabulary EAR = new Vocabulary("みみ", "ear", "hiragana, bodypart");
    Vocabulary EVERYONE = new Vocabulary("みんな", "everyone", "hiragana");
    Vocabulary SON = new Vocabulary("むすこ", "son", "hiragana, family");
    Vocabulary DAUGHTER = new Vocabulary("むすめ", "daughter", "hiragana, family");
    Vocabulary DIFFICULT = new Vocabulary("むずかしい", "difficult, tough", "hiragana");
    Vocabulary PURPLE = new Vocabulary("むらさき", "purple", "hiragana, color");
    Vocabulary FREE_MON = new Vocabulary("みりょう", "free (money)", "hiragana");
    Vocabulary EYE = new Vocabulary("め", "eye", "hiragana, bodypart");
    Vocabulary EVEN = new Vocabulary("も", "even, any, also", "hiragana");
    Vocabulary LETS = new Vocabulary("もう", "yet, now, let's", "hiragana");
    Vocabulary VERYSOON = new Vocabulary("もうすぐ", "very soon", "hiragana, time");
    Vocabulary ALITTLEMORE = new Vocabulary("もうすこし", "a little more", "hiragana");
    Vocabulary MORE = new Vocabulary("もっと", "more", "hiragana");
    Vocabulary BETTER = new Vocabulary("もといい", "better", "hiragana");
    Vocabulary SOMETHING = new Vocabulary("もの", "something, thing", "hiragana");
    Vocabulary BASEBALL = new Vocabulary("やきゅう", "baseball", "hiragana");
    Vocabulary GOAT = new Vocabulary("やぎ", "goat", "hiragana");
    Vocabulary VEGETABLE = new Vocabulary("やさい", "vegetable", "hiragana, food, vegetable");
    Vocabulary KIND = new Vocabulary("やさしい", "kind, easy, nice", "hiragana");
    Vocabulary CHEAP_HIR = new Vocabulary("やすい", "cheap, inexpensive", "hiragana");
    Vocabulary RENT = new Vocabulary("やちん", "rent", "hiragana");
    Vocabulary YAMAGUCHI = new Vocabulary("やまぐち", "Yamaguchi", "hiragana");
    Vocabulary QUIT = new Vocabulary("やめます", "(to) quit sth., (to) leave sth., (to) stop sth.", "hiragana, verb");
    Vocabulary FAMOUS = new Vocabulary("ゆうめい", "famous", "hiragana");
    Vocabulary SNOW = new Vocabulary("ゆき", "snow", "hiragana");
    Vocabulary RELAX = new Vocabulary("ゆっくりします", "(to) relax", "hiragana, verb");
    Vocabulary OFTEN = new Vocabulary("よく", "often, frequently, well", "hiragana, time");
    Vocabulary NOTGOOD = new Vocabulary("よくない", "not good, do not ... good", "hiragana");
    Vocabulary YOKOHAMA = new Vocabulary("よこはま", "Yokohama", "hiragana");
    Vocabulary PLAN = new Vocabulary("よてい", "plan", "hiragana");
    Vocabulary RESERVATION = new Vocabulary("よやく", "reservation", "hiragana");
    Vocabulary TORESERVE = new Vocabulary("よやくします", "(to) reserve sth., (to) book sth.", "hiragana, verb");
    Vocabulary GLADTOWORKWITHYOU = new Vocabulary("よろしくあねがいします", "nice to meet you, glad to work with you", "hiragana");
    Vocabulary NEXTWEEK = new Vocabulary("らいしゅう", "next week", "hiragana, time");
    Vocabulary PORTION = new Vocabulary("りょう", "portion, fee", "hiragana");
    Vocabulary PARENT = new Vocabulary("りょうしん", "parent", "hiragana, family");
    Vocabulary TRIP = new Vocabulary("りょこう", "trip", "hiragana");
    Vocabulary TRAVEL = new Vocabulary("りょこうします", "(to) travel swh.", "hiragana, verb");
    Vocabulary APPLE = new Vocabulary("りんご", "apple", "hiragana");
    Vocabulary REFRIGERATOR = new Vocabulary("れいぞうこ", "refrigerator", "hiragana");
    Vocabulary ROMANCE = new Vocabulary("れんあい", "romance", "hiragana");
    Vocabulary COTTONCANDY = new Vocabulary("わたああ", "cotton candy", "hiragana, food");
    Vocabulary CROSS = new Vocabulary("わたります", "(to) cross sth.", "hiragana, verb");
    Vocabulary THROUGH_WO = new Vocabulary("を", "through, with, for", "hiragana");
    Vocabulary LEAVE = new Vocabulary("をでます", "(to) leave, (to) exit, (to) go out", "hiragana, verb, kanjireplace");

    Vocabulary iceCream = new Vocabulary("アイスクリーム", "ice cream", "katakana, food");
    Vocabulary icedCoffee = new Vocabulary("アイスコーヒー", "iced coffee", "katakana, food");
    Vocabulary action = new Vocabulary("アクション", "action", "katakana");
    Vocabulary accessory = new Vocabulary("アクセソリ", "accessory", "katakana");
    Vocabulary animeSong = new Vocabulary("アニソン", "anime song", "katakana");
    Vocabulary anime = new Vocabulary("アニメ", "anime", "katakana");
    Vocabulary apartment = new Vocabulary("アパート", "apartment", "katakana");
    Vocabulary America = new Vocabulary("アメリカ", "America, the USA, the US", "katakana, country");
    Vocabulary American = new Vocabulary("アメリカの", "American", "katakana, hiragana");
    Vocabulary American_PER = new Vocabulary("アメリカじん", "American", "katakana");
    Vocabulary Alex = new Vocabulary("アレックス", "Alex", "katakana");
    Vocabulary album = new Vocabulary("アルバム", "album", "katakana");
    Vocabulary Andrew = new Vocabulary("アンドリュー", "Andrew", "katakana");
    Vocabulary allergy = new Vocabulary("アレルギー", "allergy", "katakana");
    Vocabulary artist = new Vocabulary("アーティスト", "artist", "katakana");
    Vocabulary Britain = new Vocabulary("イギリス", "Britain, the United Kingdom, British", "katakana, country");
    Vocabulary event = new Vocabulary("イベント", "event", "katakana");
    Vocabulary illumination = new Vocabulary("イルミネーション", "illumination", "katakana");
    Vocabulary instant = new Vocabulary("インスタント", "instant", "katakana");
    Vocabulary inch = new Vocabulary("インチ", "inch", "katakana");
    Vocabulary Indonesia = new Vocabulary("インドネシア", "Indonesia", "katakana, country");
    Vocabulary airConditioner = new Vocabulary("エアコン", "air conditioner", "katakana");
    Vocabulary Erica = new Vocabulary("エリカ", "Erica, Erika", "katakana");
    Vocabulary elevator = new Vocabulary("エレベーター", "elevator", "katakana");
    Vocabulary engineer = new Vocabulary("エンジニア", "engineer", "katakana");
    Vocabulary office = new Vocabulary("オフィス", "office", "katakana");
    Vocabulary orientation = new Vocabulary("オリエンテーション", "orientation", "katakana");
    Vocabulary orange = new Vocabulary("オレンジ", "orange", "katakana");
    Vocabulary Australia = new Vocabulary("オーストラリア", "Australia, Australian, australia's", "katakana, country");
    Vocabulary countdown = new Vocabulary("カウントダウン", "countdown", "katakana");
    Vocabulary cup = new Vocabulary("カップ", "cup", "katakana");
    Vocabulary cupcake = new Vocabulary("カップケーキ", "cupcake", "katakana, food");
    Vocabulary Canada = new Vocabulary("カナダ", "Canada", "katakana, country");
    Vocabulary Canadian = new Vocabulary("カナダじん", "Canadian", "katakana, hiragana");
    Vocabulary cafe = new Vocabulary("カフェ", "cafe", "katakana");
    Vocabulary cappuccino = new Vocabulary("カプチーノ", "cappuccino", "katakana, drink");
    Vocabulary chameleon = new Vocabulary("カメレオン", "chameleon", "katakana, animal");
    Vocabulary karaoke = new Vocabulary("カラオケ", "karaoke", "katakana");
    Vocabulary colorful = new Vocabulary("カラフルな", "colorful", "katakana, hiragana");
    Vocabulary curry = new Vocabulary("カレー", "curry", "katakana, food");
    Vocabulary cardGame = new Vocabulary("カードゲーム", "card game", "katakana");
    Vocabulary kitchen = new Vocabulary("キッチン", "kitchen", "katakana");
    Vocabulary campus = new Vocabulary("キャンパス", "campus", "katakana");
    Vocabulary sparkling = new Vocabulary("キラキラ", "sparkling", "katakana");
    Vocabulary keychain = new Vocabulary("キーホルダー", "keychain", "katakana");
    Vocabulary keyboard = new Vocabulary("キーボード", "keyboard", "katakana");
    Vocabulary giftShop = new Vocabulary("キフトショップ", "gift shop", "katakana");
    Vocabulary cookie = new Vocabulary("クッキー", "cookie", "katakana, food");
    Vocabulary client = new Vocabulary("クライアント", "client", "katakana");
    Vocabulary Christmas = new Vocabulary("クリスマス", "Christmas", "katakana");
    Vocabulary creditCard = new Vocabulary("クレジットカード", "credit card", "katakana");
    Vocabulary closet = new Vocabulary("クローゼット", "closet", "katakana");
    Vocabulary merchandise = new Vocabulary("グッズ", "merchandise", "katakana");
    Vocabulary gram = new Vocabulary("グラム", "gram", "katakana");
    Vocabulary gray = new Vocabulary("グレー", "gray", "katakana, color");
    Vocabulary cake = new Vocabulary("ケーキ", "cake", "katakana, food");
    Vocabulary cakeShop = new Vocabulary("ケーキや", "cake shop", "katakana, hiragana");
    Vocabulary cable = new Vocabulary("ケーブル", "cable", "katakana");
    Vocabulary gaming = new Vocabulary("ゲーミング", "gaming", "katakana");
    Vocabulary game = new Vocabulary("ゲーム", "game", "katakana");
    Vocabulary coinLocker = new Vocabulary("コインロッカー", "coin locker", "katakana");
    Vocabulary hotChocolate = new Vocabulary("ココア", "hot chocolate", "katakana, drink");
    Vocabulary communityCenter = new Vocabulary("コミュニティーセンター", "community center", "katakana");
    Vocabulary comedy = new Vocabulary("コメティ", "comedy", "katakana");
    Vocabulary concert = new Vocabulary("コンサート", "concert", "katakana");
    Vocabulary outlet = new Vocabulary("コンセント", "outlet", "katakana");
    Vocabulary contest = new Vocabulary("コンテスト", "contest", "katakana");
    Vocabulary convenienceStore = new Vocabulary("コンビニ", "convenience store", "katakana");
    Vocabulary coach = new Vocabulary("コーチ", "coach", "katakana");
    Vocabulary coat = new Vocabulary("コート", "coat, court", "katakana");
    Vocabulary section = new Vocabulary("コーナー", "section", "katakana");
    Vocabulary coffee = new Vocabulary("コーヒー", "coffee", "katakana, drink");
    Vocabulary coffeeMaker = new Vocabulary("コーヒーメーカー", "coffee maker", "katakana");
    Vocabulary cola = new Vocabulary("コーラ", "cola", "katakana, drink");
    Vocabulary trashCan = new Vocabulary("ゴミばこ", "trash can", "katakana, hiragana");
    Vocabulary autograph = new Vocabulary("サイン", "autograph", "katakana");
    Vocabulary soccer = new Vocabulary("サッカー", "soccer", "katakana");
    Vocabulary Sam = new Vocabulary("サム", "Sam", "katakana");
    Vocabulary salad = new Vocabulary("サラダ", "salad", "katakana");
    Vocabulary sandwich = new Vocabulary("サンドイッチ", "sandwich", "katakana, food");
    Vocabulary club = new Vocabulary("サールク", "club", "katakana");
    Vocabulary shower = new Vocabulary("シャフーをあびます", "(to) shower, (to) take a shower", "katakana, hiragana, verb");
    Vocabulary shoppingCenter = new Vocabulary("ショッピングゼンター", "shopping center", "katakana");
    Vocabulary gym = new Vocabulary("ジム", "gym", "katakana");
    Vocabulary jacket = new Vocabulary("ジャケット", "jacket", "katakana");
    Vocabulary jazz = new Vocabulary("ジャズ", "jazz", "katakana");
    Vocabulary juice = new Vocabulary("ジュース", "juice", "katakana, drink");
    Vocabulary jeans = new Vocabulary("ジーンス", "jeans", "katakana");
    Vocabulary skirt = new Vocabulary("スカート", "skirt", "katakana");
    Vocabulary skill = new Vocabulary("スキル", "skill", "katakana");
    Vocabulary ski = new Vocabulary("スキー", "ski", "katakana");
    Vocabulary skating = new Vocabulary("スケート", "skating", "katakana");
    Vocabulary steak = new Vocabulary("ステーキ", "steak", "katakana, food");
    Vocabulary stage = new Vocabulary("ステージ", "stage", "katakana");
    Vocabulary snowboard = new Vocabulary("スノボ", "snowboard", "katakana");
    Vocabulary spa = new Vocabulary("スパ", "spa", "katakana");
    Vocabulary speaker = new Vocabulary("スピーカー", "speaker", "katakana");
    Vocabulary spoon = new Vocabulary("スプーン", "spoon", "katakana");
    Vocabulary Spanish = new Vocabulary("スペインご", "Spanish", "katakana, hiragana, language");
    Vocabulary sport = new Vocabulary("スポーツ", "sport", "katakana");
    Vocabulary smartphone = new Vocabulary("スマホ", "smartphone", "katakana");
    Vocabulary Smith = new Vocabulary("スミス", "Smith", "katakana");
    Vocabulary smoothie = new Vocabulary("スムージー", "smoothie", "katakana, drink");
    Vocabulary suit = new Vocabulary("スーツ", "suit", "katakana");
    Vocabulary suitcase = new Vocabulary("スーツケース", "suitcase", "katakana");
    Vocabulary supermarket = new Vocabulary("スーパー", "supermarket", "katakana");
    Vocabulary soup = new Vocabulary("スープ", "soup", "katakana, food");
    Vocabulary sweater = new Vocabulary("セーター", "sweater", "katakana");
    Vocabulary sale = new Vocabulary("セール", "sale", "katakana");
    Vocabulary Seoul = new Vocabulary("ソウル", "Seoul", "katakana");
    Vocabulary sauce = new Vocabulary("ソース", "sauce", "katakana, food, drink");
    Vocabulary towel = new Vocabulary("タオル", "towel", "katakana");
    Vocabulary Tagalog = new Vocabulary("タガログご", "Tagalog", "katakana, hiragana, language");
    Vocabulary taxi = new Vocabulary("タクシー", "taxi", "katakana");
    Vocabulary tablet = new Vocabulary("タブレット", "tablet", "katakana");
    Vocabulary terminal = new Vocabulary("ターミナル", "terminal", "katakana");
    Vocabulary Daniel = new Vocabulary("ダニエル", "Daniel", "katakana");
    Vocabulary dance = new Vocabulary("ダンス", "dance", "katakana");
    Vocabulary chess = new Vocabulary("チッス", "chess", "katakana");
    Vocabulary checkout = new Vocabulary("チェックアウトします", "(to) checkout of sth./swh., checkout, check out", "katakana, hiragana, verb");
    Vocabulary checkin = new Vocabulary("チェックイン", "check-in", "katakana");
    Vocabulary ticket = new Vocabulary("チケット", "ticket", "katakana");
    Vocabulary friedRice = new Vocabulary("チャーハン", "fried rice", "katakana, food");
    Vocabulary chocoBanana = new Vocabulary("チョコバナナ", "choco banana", "katakana, food");
    Vocabulary chocolate = new Vocabulary("チョコレート", "chocolate", "katakana, food");
    Vocabulary cheesecake = new Vocabulary("チーズケーキ", "cheesecake", "katakana, food");
    Vocabulary teammate = new Vocabulary("チームメト", "teammate", "katakana");
    Vocabulary test = new Vocabulary("テスト", "test", "katakana");
    Vocabulary tennis = new Vocabulary("テニス", "tennis", "katakana");
    Vocabulary TV = new Vocabulary("テレビ", "TV", "katakana");
    Vocabulary table = new Vocabulary("テーブル", "table", "katakana");
    Vocabulary dessert = new Vocabulary("デザート", "dessert", "katakana, food");
    Vocabulary departmentStore = new Vocabulary("デパート", "department store", "katakana");
    Vocabulary date = new Vocabulary("デート", "date", "katakana");
    Vocabulary tomato = new Vocabulary("トマト", "tomato", "katakana, food, vegetable, fruit");
    Vocabulary Toronto = new Vocabulary("トロント", "Toronto", "katakana");
    Vocabulary toast = new Vocabulary("トースト", "toast", "katakana, food");
    Vocabulary door = new Vocabulary("ドア", "door", "katakana");
    Vocabulary Germany = new Vocabulary("ドイツ", "Germany, German", "katakana, country");
    Vocabulary German = new Vocabulary("ドイツご", "German", "katakana, hiragana, language");
    Vocabulary documentary = new Vocabulary("ドキュメンタリー", "documentary", "katakana");
    Vocabulary drama = new Vocabulary("ドラマ", "drama", "katakana");
    Vocabulary dress = new Vocabulary("ドレス", "dress", "katakana");
    Vocabulary donut = new Vocabulary("ドーナツ", "donut", "katakana, food");
    Vocabulary knife = new Vocabulary("ナイフ", "knife", "katakana");
    Vocabulary news = new Vocabulary("ニュース", "news", "katakana");
    Vocabulary NewYork = new Vocabulary("ニューヨーク", "New York", "katakana");
    Vocabulary tie = new Vocabulary("ネクタイ", "tie", "katakana");
    Vocabulary necklace = new Vocabulary("ネックレス", "necklace", "katakana");
    Vocabulary online = new Vocabulary("ネットで", "online", "katakana");
    Vocabulary nonfiction = new Vocabulary("ノンフィクション", "nonfiction", "katakana");
    Vocabulary hamster = new Vocabulary("ハムスター", "hamster", "katakana, animal");
    Vocabulary Halloween = new Vocabulary("ハロウィン", "Halloween", "katakana");
    Vocabulary handSanitizer = new Vocabulary("ハンドジェル", "hand sanitizer", "katakana");
    Vocabulary bus = new Vocabulary("バス", "bus", "katakana");
    Vocabulary busStop = new Vocabulary("バスてい", "bus stop", "katakana");
    Vocabulary basketball = new Vocabulary("バスケットボール", "basketball", "katakana");
    Vocabulary butter = new Vocabulary("バター", "butter", "katakana, food");
    Vocabulary badminton = new Vocabulary("バドミントン", "badminton", "katakana");
    Vocabulary ballet = new Vocabulary("バレエ", "ballet", "katakana");
    Vocabulary volleyball = new Vocabulary("バレーボール", "volleyball", "katakana");
    Vocabulary band = new Vocabulary("バンド", "band", "katakana");
    Vocabulary bar = new Vocabulary("バー", "bar", "katakana");
    Vocabulary pie = new Vocabulary("パイ", "pie", "katakana, food");
    Vocabulary pasta = new Vocabulary("パスタ", "pasta", "katakana, food");
    Vocabulary passport = new Vocabulary("パスポート", "passport", "katakana");
    Vocabulary password = new Vocabulary("パスワード", "password", "katakana");
    Vocabulary puzzle = new Vocabulary("パズル", "puzzle", "katakana");
    Vocabulary computer = new Vocabulary("パソコン", "computer", "katakana");
    Vocabulary performance = new Vocabulary("パフォーマンス", "performance", "katakana");
    Vocabulary parade = new Vocabulary("パレード", "parade", "katakana");
    Vocabulary bread = new Vocabulary("パン", "bread", "katakana, food");
    Vocabulary bakery = new Vocabulary("パンや", "bakery", "katakana");
    Vocabulary pancake = new Vocabulary("パンケーキ", "pancake", "katakana, food");
    Vocabulary panda = new Vocabulary("パンダ", "panda", "katakana");
    Vocabulary hoodie = new Vocabulary("パーカー", "hoodie", "katakana");
    Vocabulary party = new Vocabulary("パーティー", "party", "katakana");
    Vocabulary business = new Vocabulary("ビジネス", "business", "katakana");
    Vocabulary beefStew = new Vocabulary("ビーフシチュー", "beef stew", "katakana, food");
    Vocabulary beer = new Vocabulary("ビール", "beer", "katakana,d rink");
    Vocabulary pizza = new Vocabulary("ピザ", "pizza", "katakana, food");
    Vocabulary pizzeria = new Vocabulary("ピザや", "pizzeria", "katakana");
    Vocabulary pink = new Vocabulary("ピンク", "pink", "katakana, color");
    Vocabulary fantasy = new Vocabulary("ファンタジー", "fantasy", "katakana");
    Vocabulary fastFood = new Vocabulary("ファーストフード", "fast food", "katakana, food");
    Vocabulary fiction = new Vocabulary("フィクション", "fiction", "katakana");
    Vocabulary Philippines = new Vocabulary("フィリピン", "the Philippines", "katakana, country");
    Vocabulary fence = new Vocabulary("フェンス", "fence", "katakana");
    Vocabulary fork = new Vocabulary("フォーク", "fork", "katakana");
    Vocabulary friedChicken = new Vocabulary("フライドチキン", "fried chicken", "katakana, food");
    Vocabulary FrenchFries = new Vocabulary("フライドポテト", "French fries", "katakana, food");
    Vocabulary French = new Vocabulary("フランス", "French, France", "katakana, country");
    Vocabulary FrenchCuisine = new Vocabulary("フランスりょうり", "French cuisine", "katakana, hiragana, food, drink");
    Vocabulary blouse = new Vocabulary("ブラウス", "blouse", "katakana");
    Vocabulary Brazil = new Vocabulary("ブラジル", "Brazil", "katakana, country");
    Vocabulary Brazilian = new Vocabulary("ブラジルじん", "Brazilian", "katakana");
    Vocabulary blog = new Vocabulary("ブログ", "blog", "katakana");
    Vocabulary broccoli = new Vocabulary("ブロッコリー", "broccoli", "katakana, food, vegetable");
    Vocabulary printer = new Vocabulary("プリンター", "printer", "katakana");
    Vocabulary presentation = new Vocabulary("プレゼン", "presentation", "katakana");
    Vocabulary gift = new Vocabulary("プレゼント", "gift", "katakana");
    Vocabulary project = new Vocabulary("プロジェクト", "project", "katakana");
    Vocabulary proteinBar = new Vocabulary("プロテインバー", "protein bar", "katakana, food");
    Vocabulary pool = new Vocabulary("プール", "pool", "katakana");
    Vocabulary headphone = new Vocabulary("ヘッドホン", "headphone", "katakana");
    Vocabulary bed = new Vocabulary("ベッド", "bed", "katakana");
    Vocabulary Vietnam = new Vocabulary("ベトナム", "Vietnam", "katakana, country");
    Vocabulary Vietnamese = new Vocabulary("ベトナムじん", "Vietnamese", "katakana, hiragana");
    Vocabulary bench = new Vocabulary("ベンチ", "bench", "katakana");
    Vocabulary markerPen = new Vocabulary("ペン", "marker pen", "katakana");
    Vocabulary hotDog = new Vocabulary("ホットドッグ", "hot dog", "katakana, food");
    Vocabulary hotel = new Vocabulary("ホテル", "hotel", "katakana");
    Vocabulary horror = new Vocabulary("ホラー", "horror", "katakana");
    Vocabulary platform = new Vocabulary("ホーム", "platform", "katakana");
    Vocabulary volunteer = new Vocabulary("ボランティア", "volunteer", "katakana");
    Vocabulary tattered = new Vocabulary("ボロボロ", "tattered", "katakana");
    Vocabulary boardGame = new Vocabulary("ボードゲーム", "board game", "katakana");
    Vocabulary pointCard = new Vocabulary("ポイントカード", "point card", "katakana");
    Vocabulary poster = new Vocabulary("ポスター", "poster", "katakana");
    Vocabulary postcard = new Vocabulary("ポストカード", "postcard", "katakana");
    Vocabulary popcorn = new Vocabulary("ポップコーン", "popcorn", "katakana, food");
    Vocabulary popMusic = new Vocabulary("ポップス", "pop music", "katakana, music");
    Vocabulary potatoChip = new Vocabulary("ポテトチップス", "potato chip", "katakana, food");
    Vocabulary computerMouse = new Vocabulary("マウス", "(computer) mouse", "katakana");
    Vocabulary magnet = new Vocabulary("マグネット", "magnet", "katakana");
    Vocabulary scarf = new Vocabulary("マフラー", "scarf", "katakana");
    Vocabulary runMarathon = new Vocabulary("マラソンをはしります", "(to) run a marathon, (to) run the marathon", "katakana, verb");
    Vocabulary mystery = new Vocabulary("ミステリー", "mystery", "katakana");
    Vocabulary meeting = new Vocabulary("ミーティング", "meeting", "katakana");
    Vocabulary sendMessage = new Vocabulary("メッセージをおくります", "(to) send a message", "katakana, hiragana, verb");
    Vocabulary mens = new Vocabulary("メンズ", "men’s", "katakana");
    Vocabulary checkEmail = new Vocabulary("メールを読みます", "(to) check emails", "katakana, kanji, hiragana, verb");
    Vocabulary monitor = new Vocabulary("モニター", "monitor", "katakana");
    Vocabulary yoga = new Vocabulary("ヨガ", "yoga", "katakana");
    Vocabulary yogurt = new Vocabulary("ヨーグルト", "yogurt", "katakana, food");
    Vocabulary lion = new Vocabulary("ライオン", "lion", "katakana, animal");
    Vocabulary radio = new Vocabulary("ラジオ", "radio", "katakana");
    Vocabulary ramen = new Vocabulary("ラーメン", "ramen", "katakana, food");
    Vocabulary ramenShop = new Vocabulary("ラーメンや", "ramen shop", "katakana");
    Vocabulary livingRoom = new Vocabulary("リビング", "living room", "katakana");
    Vocabulary roommate = new Vocabulary("ルームメイト", "roommate", "katakana");
    Vocabulary recipe = new Vocabulary("レシピ", "recipe", "katakana");
    Vocabulary receipt = new Vocabulary("レシート", "receipt", "katakana");
    Vocabulary checkOut = new Vocabulary("レジ", "checkout", "katakana");
    Vocabulary plasticBag = new Vocabulary("レジぶくろ", "plastic bag", "katakana, hiragana");
    Vocabulary restaurant = new Vocabulary("レストラン", "restaurant", "katakana");
    Vocabulary lesson = new Vocabulary("レッスン", "lesson", "katakana");
    Vocabulary womens = new Vocabulary("レディース", "women's", "katakana");
    Vocabulary rock = new Vocabulary("ロック", "rock", "katakana, music");
    Vocabulary lobby = new Vocabulary("ロビー", "lobby", "katakana");
    Vocabulary wireless = new Vocabulary("ワイヤレス", "wireless", "katakana");
    Vocabulary wine = new Vocabulary("ワイン", "wine", "katakana");

    Vocabulary ONE_KAN = new Vocabulary("一", "one", "kanji");
    Vocabulary one_cou = new Vocabulary("一つ", "one thing", "kanji, hiragana");
    Vocabulary thenThousand = new Vocabulary("一万", "ten thousand", "kanji");
    Vocabulary onePerson = new Vocabulary("一人", "one person", "kanji");
    Vocabulary oneMinute = new Vocabulary("一分", "one minute", "kanji");
    Vocabulary one_ani = new Vocabulary("一匹", "one medium/small sized animal", "kanji");
    Vocabulary firstYearStudent = new Vocabulary("一年生", "first-year student", "kanji");
    Vocabulary day = new Vocabulary("一日", "day", "kanji, time");
    Vocabulary January = new Vocabulary("一月", "January", "kanji, month, time");
    Vocabulary THEMOST = new Vocabulary("一番", "the most, the best, number one", "kanji");
    Vocabulary FIRSTFLOOR = new Vocabulary("一階", "first floor", "kanji");
    Vocabulary seven_cou = new Vocabulary("七つ", "seven things", "kanji, hiragana");
    Vocabulary sevenOClock = new Vocabulary("七時", "seven o'clock", "kanji, time");
    Vocabulary July = new Vocabulary("七月", "July", "kanji, month, time");
    Vocabulary tenThousand = new Vocabulary("万", "ten thousand", "kanji");
    Vocabulary three = new Vocabulary("三", "three", "kanji");
    Vocabulary three_cou = new Vocabulary("三つ", "three things", "kanji, hiragana");
    Vocabulary threeMinutes = new Vocabulary("三分", "three minutes", "kanji, time");
    Vocabulary three_ani = new Vocabulary("三匹", "three medium/small sized animals", "kanji");
    Vocabulary thirtyFirst = new Vocabulary("三十一日", "thirty-first", "kanji");
    Vocabulary threeThousand = new Vocabulary("三千", "three thousand", "kanji");
    Vocabulary thirdYearStudent = new Vocabulary("三年生", "third-year student", "kanji");
    Vocabulary THIRD = new Vocabulary("三日", "third, three days", "kanji, time");
    Vocabulary March = new Vocabulary("三月", "March", "kanji, month, time");
    Vocabulary thirdFloor = new Vocabulary("三階", "third floor", "kanji");
    Vocabulary ONTOP = new Vocabulary("上", "on top of sth., atop of sth., up", "kanji");
    Vocabulary GOUP = new Vocabulary("上がります", "(to) go up", "kanji, hiragana, verb");
    Vocabulary under = new Vocabulary("下", "under sth.", "kanji");
    Vocabulary during = new Vocabulary("中", "during", "kanji");
    Vocabulary Nakayama = new Vocabulary("中山", "Nakayama", "kanji");
    Vocabulary RIDE = new Vocabulary("乗ります", "(to) ride sth., (to) take sth.", "kanji, hiragana, verb");
    Vocabulary nine = new Vocabulary("九", "nine", "kanji");
    Vocabulary nine_cou = new Vocabulary("九つ", "nine things", "kanji, hiragana");
    Vocabulary nineOClock = new Vocabulary("九時", "nine o'clock", "kanji, time");
    Vocabulary nineHours = new Vocabulary("九時間", "nine hours", "kanji, time");
    Vocabulary September = new Vocabulary("九月", "September", "kanji, month, time");
    Vocabulary two = new Vocabulary("二", "two", "kanji");
    Vocabulary two_cou = new Vocabulary("二つ", "two things", "kanji, hiragana");
    Vocabulary two_peo = new Vocabulary("二人", "two people", "kanji");
    Vocabulary two_ani = new Vocabulary("二匹", "two medium/small sized animals", "kanji");
    Vocabulary secondYearStudent = new Vocabulary("二年生", "second-year student", "kanji");
    Vocabulary secondDay = new Vocabulary("二日", "second day", "kanji, time");
    Vocabulary February = new Vocabulary("二月", "February", "kanji, month, time");
    Vocabulary secondFloor = new Vocabulary("二階", "second floor", "kanji");
    Vocabulary five = new Vocabulary("五", "five", "kanji");
    Vocabulary five_cou = new Vocabulary("五つ", "five things", "kanji, hiragana");
    Vocabulary fifth = new Vocabulary("五日", "fifth", "kanji");
    Vocabulary Kyoto = new Vocabulary("京都", "Kyoto", "kanji");
    Vocabulary person = new Vocabulary("人", "person", "kanji");
    Vocabulary tonight = new Vocabulary("今夜", "tonight", "kanji, time");
    Vocabulary today = new Vocabulary("今日", "today", "kanji, time");
    Vocabulary BREAK_KAN = new Vocabulary("休み", "break", "kanji, hiragana");
    Vocabulary REST = new Vocabulary("休みます", "(to) rest", "kanji, hiragana, verb");
    Vocabulary MEETUP = new Vocabulary("会います", "(to) meetup swh.", "kanji, hiragana, verb");
    Vocabulary live = new Vocabulary("住んでいます", "(to) live swh.", "kanji, hiragana, verb");
    Vocabulary what = new Vocabulary("何", "what", "kanji");
    Vocabulary howOld = new Vocabulary("何さい", "how old", "kanji, hiragana");
    Vocabulary WHATISIT = new Vocabulary("何ですか", "What is it?", "kanji, hiragana");
    Vocabulary howMany = new Vocabulary("何匹", "how many", "kanji");
    Vocabulary whatSchoolYear = new Vocabulary("何年生", "what school year", "kanji");
    Vocabulary whatTime = new Vocabulary("何時", "what time, when", "kanji, time");
    Vocabulary howManyHours = new Vocabulary("何時間", "how many hours", "kanji, time");
    Vocabulary dayOfTheWeek = new Vocabulary("何曜日", "day of the week", "kanji, time");
    Vocabulary whichFloor = new Vocabulary("何階", "which floor", "kanji");
    Vocabulary make = new Vocabulary("作ります", "(to) make sth.", "kanji, hiragana, verb");
    Vocabulary lastMonth = new Vocabulary("先月", "last month", "kanji, time");
    Vocabulary lastWeek = new Vocabulary("先週", "last week", "kanji, time");
    Vocabulary eight = new Vocabulary("八", "eight, eights", "kanji");
    Vocabulary eight_cou = new Vocabulary("八つ", "eight things", "kanji, hiragana");
    Vocabulary eightThousand = new Vocabulary("八千", "eight thousand", "kanji");
    Vocabulary August = new Vocabulary("八月", "August", "kanji, month, time");
    Vocabulary six = new Vocabulary("六", "six", "kanji");
    Vocabulary six_cou = new Vocabulary("六つ", "six things", "kanji, hiragana");
    Vocabulary sixMinutes = new Vocabulary("六分", "six minutes", "kanji, time");
    Vocabulary sixHundred = new Vocabulary("六百", "six hundred", "kanji");
    Vocabulary yen = new Vocabulary("円", "yen", "kanji");
    Vocabulary exit = new Vocabulary("出口", "exit", "kanji");
    Vocabulary minute = new Vocabulary("分", "minute", "kanji, time");
    Vocabulary ago = new Vocabulary("前", "ago, before, front", "kanji");
    Vocabulary north = new Vocabulary("北", "north", "kanji");
    Vocabulary ten = new Vocabulary("十", "ten, tenth", "kanji");
    Vocabulary eleven = new Vocabulary("十一", "eleven", "kanji");
    Vocabulary twelve = new Vocabulary("十二", "twelve", "kanji");
    Vocabulary December = new Vocabulary("十二月", "December", "kanji, month, time");
    Vocabulary sufficient = new Vocabulary("十分", "sufficient, enough, adequate", "kanji");
    Vocabulary October = new Vocabulary("十月", "October", "kanji, month, time");
    Vocabulary tenthFloor = new Vocabulary("十階", "tenth floor", "kanji");
    Vocabulary thousand = new Vocabulary("千", "thousand", "kanji");
    Vocabulary half = new Vocabulary("半", "half, thirty, and a half", "kanji");
    Vocabulary egg = new Vocabulary("卵", "egg", "kanji");
    Vocabulary mouth = new Vocabulary("口", "mouth", "kanji");
    Vocabulary old = new Vocabulary("古い", "old", "kanji, hiragana");
    Vocabulary right = new Vocabulary("右", "right", "kanji");
    Vocabulary four = new Vocabulary("四", "four", "kanji");
    Vocabulary four_cou = new Vocabulary("四つ", "four things", "kanji, hiragana");
    Vocabulary four_ani = new Vocabulary("四匹", "four small/medium sized animals", "kanji");
    Vocabulary fourthYearStudent = new Vocabulary("四年生", "fourth-year student", "kanji");
    Vocabulary fourOClock = new Vocabulary("四時", "four o'clock", "kanji, time");
    Vocabulary fourHours = new Vocabulary("四時間", "four hours", "kanji, time");
    Vocabulary April = new Vocabulary("四月", "April", "kanji, month, time");
    Vocabulary fourthFloor = new Vocabulary("四階", "fourth floor", "kanji");
    Vocabulary Saturday = new Vocabulary("土曜日", "Saturday", "kanji, time");
    Vocabulary night = new Vocabulary("夜", "night", "kanji, time");
    Vocabulary large = new Vocabulary("大きい", "large, great, considerable", "kanji, hiragana");
    Vocabulary tournament = new Vocabulary("大会", "tournament", "kanji");
    Vocabulary landlord = new Vocabulary("大家", "landlord", "kanji");
    Vocabulary school = new Vocabulary("学校", "school", "kanji");
    Vocabulary cheap = new Vocabulary("安い", "cheap, inexpensive", "kanji, hiragana");
    Vocabulary home = new Vocabulary("家", "home", "kanji");
    Vocabulary small = new Vocabulary("小さい", "small, little", "kanji, hiragana");
    Vocabulary slightly = new Vocabulary("少し", "a bit, slightly, a slight", "kanji, hiragana");
    Vocabulary mountain = new Vocabulary("山", "mountain", "kanji");
    Vocabulary Yamaguchi = new Vocabulary("山口", "Yamaguchi", "kanji");
    Vocabulary Yamada = new Vocabulary("山田", "Yamada", "kanji");
    Vocabulary left = new Vocabulary("左", "left", "kanji");
    Vocabulary NEW = new Vocabulary("新しい", "new", "kanji, hiragana");
    Vocabulary day_kan = new Vocabulary("日", "day", "kanji, time");
    Vocabulary Sunday = new Vocabulary("日曜日", "Sunday", "kanji, time");
    Vocabulary Japan = new Vocabulary("日本", "Japan", "kanji, country");
    Vocabulary Japanese_LIN = new Vocabulary("日本ご", "Japanese", "kanji, hiragana, language");
    Vocabulary Japanese = new Vocabulary("日本の", "Japanese", "kanji, hiragana");
    Vocabulary JapaneseCuisine = new Vocabulary("日本りょうり", "Japanese cuisine", "kanji, food, drink, hiragana");
    Vocabulary lunch = new Vocabulary("昼ご飯", "lunch", "kanji, hiragana, food, time");
    Vocabulary clock = new Vocabulary("時", "clock, time, while", "kanji, time");
    Vocabulary time = new Vocabulary("時間", "time, hour", "kanji, time");
    Vocabulary Thursday = new Vocabulary("木曜日", "Thursday", "kanji, time");
    Vocabulary book = new Vocabulary("本", "book", "kanji");
    Vocabulary bookstore = new Vocabulary("本や", "bookstore", "kanji, hiragana");
    Vocabulary come = new Vocabulary("来ます", "(to) come to swh., (to) will be here, (to) be coming", "kanji, hiragana, verb");
    Vocabulary nextMonth = new Vocabulary("来月", "next month", "kanji, time");
    Vocabulary Tokyo = new Vocabulary("東京", "Tokyo", "kanji");
    Vocabulary mother = new Vocabulary("母", "mother", "kanji, family");
    Vocabulary everyDay = new Vocabulary("毎日", "every day", "kanji, time");
    Vocabulary water = new Vocabulary("水", "water", "kanji, drink");
    Vocabulary Wednesday = new Vocabulary("水曜日", "Wednesday", "kanji, time");
    Vocabulary Tuesday = new Vocabulary("火曜日", "Tuesday", "kanji, time");
    Vocabulary father = new Vocabulary("父", "father", "kanji, family");
    Vocabulary Tanaka = new Vocabulary("田中", "Tanaka", "kanji");
    Vocabulary HUNDRED = new Vocabulary("百", "hundred, a hundred, 100", "kanji");
    Vocabulary HUNDREDYENSHOP = new Vocabulary("百円ショップ", "100-yen shop", "kanji, katakana");
    Vocabulary me = new Vocabulary("私", "I, me", "kanji");
    Vocabulary we = new Vocabulary("私たち", "we", "kanji, hiragana");
    Vocabulary meat = new Vocabulary("肉", "meat", "kanji");
    Vocabulary go = new Vocabulary("行きます", "(to) go swh.", "kanji, hiragana, verb");
    Vocabulary find = new Vocabulary("見つけます", "(to) find sth./sb.", "kanji, hiragana, verb");
    Vocabulary watch = new Vocabulary("見ます", "(to) watch", "kanji, hiragana, verb");
    Vocabulary story = new Vocabulary("話", "story", "kanji");
    Vocabulary SPEAK = new Vocabulary("話します", "(to) speak, (to) tell", "kanji, hiragana, verb");
    Vocabulary READ = new Vocabulary("読みます", "(to) read sth.", "kanji, hiragana, verb");
    Vocabulary BUY = new Vocabulary("買います", "(to) buy sth.", "kanji, hiragana, verb");
    Vocabulary SHOPPING = new Vocabulary("買い物をします", "(to) shop, (to) do shopping, (to) buy things", "kanji, hiragana, verb");
    Vocabulary weekend = new Vocabulary("週末", "weekend", "kanji, time");
    Vocabulary Friday = new Vocabulary("金曜日", "Friday", "kanji, time");
    Vocabulary eat = new Vocabulary("食べます", "(to) eat sth.", "kanji, hiragana, verb");
    Vocabulary food = new Vocabulary("食べ物", "food", "kanji, hiragana, food");
    Vocabulary drink = new Vocabulary("飲みます", "(to) drink sth.", "kanji, hiragana, verb");
    Vocabulary station = new Vocabulary("駅", "station", "kanji");
    Vocabulary expensive = new Vocabulary("高い", "higher, expensive, pricey", "kanji, hiragana");
    Vocabulary fish = new Vocabulary("魚", "fish", "kanji, food, animal");


    Vocabulary SLEEPY = new Vocabulary("ねむい", "sleepy", "hiragana, S3U11");
    Vocabulary alarm = new Vocabulary("アラーム", "alarm", "katakana, S3U11");
    Vocabulary YET = new Vocabulary("まだ", "yet, too, hasn't", "hiragana, S3U11");
    Vocabulary now = new Vocabulary("今", "now", "kanji, S3U11");
    Vocabulary remoteControl = new Vocabulary("リモコン", "remote control", "katakana, S3U11");
    Vocabulary blanket = new Vocabulary("ブランケット", "blanket", "katakana, S3U11");
    Vocabulary OUTSIDE = new Vocabulary("外", "out, outside", "kanji, S3U11");
    Vocabulary LEAVE_KAN = new Vocabulary("から出ます", "(to) leave, (to) exit, (to) go out of swh.", "kanji, hiragana, verb");
    Vocabulary RICEPORRIDGE = new Vocabulary("おかゆ", "rice porridge", "hiragana, food, S3U11");
    Vocabulary MEDICINE = new Vocabulary("くすり", "medicine", "hiragana, S3U11");
    Vocabulary banana = new Vocabulary("バナナ", "banana", "katakana, food, fruit, S3U11");
    Vocabulary HOME = new Vocabulary("うち", "home", "hiragana, S3U11");
    Vocabulary OVERSLEPT = new Vocabulary("ねぼうします", "(to) oversleep", "hiragana, S3U11");
    Vocabulary cancel = new Vocabulary("キャンセルします", "(to) cancel sth.", "katakana, hiragana, verb, S3U11");
    Vocabulary CHORE = new Vocabulary("かじ", "household chore, chore", "hiragana, S3U11");
    Vocabulary CLEAN = new Vocabulary("そうじ", "clean", "hiragana, S3U11");
    Vocabulary tomorrow = new Vocabulary("明日", "tomorrow", "kanji, S3U11");

    new Vocabulary("すずしい", "chill, cool, pleasantly cool", "S3U12, hiragana, adjective");
    new Vocabulary("さくらもち", "cherry blossom mochi", "S3U12, hiragana, food");
    new Vocabulary("今週", "this week", "S3U12, kanji, time");
    new Vocabulary("もち", "mochi", "S3U12, hiragana, food");
    new Vocabulary("花見をします", "cherry blossom viewing", "S3U12, kanji, hiragana, verb");
    new Vocabulary("電車", "train", "S3U12, kanji");
    new Vocabulary("モノレール", "monorail", "S3U12, katakana");
    new Vocabulary("八分", "eight minutes", "S3U12, kanji, time");
    new Vocabulary("すわれます", "(to) forget sth.", "S3U12, hiragana, verb");
    new Vocabulary("アナウンス", "(public) announcement, notice", "S3U12, katakana");
    new Vocabulary("じこ", "accident", "S3U12, hiragana");
    new Vocabulary("えきいん", "station attendant, station employee, station staff", "S3U12, hiragana, profession");
    new Vocabulary("どうします", "What should I do?", "S3U12, hiragana");
    new Vocabulary("やきそば", "stir-fried noodles, yakisoba", "S3U12, hiragana, food");
    new Vocabulary("いけ", "pond", "S3U12, hiragana");
    new Vocabulary("もうーつ", "one more, another one", "S3U12, katakana");
    new Vocabulary("やたい", "food stall", "S3U12, hiragana");
    new Vocabulary("カモ", "duck", "S3U12, katakana, animal");
    new Vocabulary("そろそろ", "in a bit, soon", "S3U12, hiragana, time");
    new Vocabulary("来年", "next year", "S3U12, kanji, time");

    new Vocabulary("海", "sea", "S3U13");
    new Vocabulary("サーフィン", "surf", "S3U13");
    new Vocabulary("イルカ", "dolphin", "S3U13, animal");
    new Vocabulary("五月", "May", "S3U13, month");
    new Vocabulary("メール", "email", "S3U13");
    new Vocabulary("まどがわのせき", "window seat", "S3U13");
    new Vocabulary("しらべます", "(to) check sth.", "S3U13, verb");
    new Vocabulary("ねだん", "price, cost", "S3U13");
new Vocabulary("まあまあ", "So so, not bad", "S3U13");
new Vocabulary("げんきん", "cash", "S3U13");
new Vocabulary("はらいます", "(to) pay sth.", "S3U13, verb");
new Vocabulary("本", "tree", "S3U13");
new Vocabulary("うみがめ", "sea turtle", "S3U13, animal");
new Vocabulary("車", "car", "S3U13");
new Vocabulary("けしき", "scenery", "S3U13");
new Vocabulary("XX", "XX", "S3U13");


    System.out.println("Writing all JSON files: 100%");
}