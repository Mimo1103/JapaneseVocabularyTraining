package com.mimo;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.management.RuntimeErrorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HandlingTags {

    public static List<HashMap<String, List<String>>> createAllowedVocabList(ArrayList<String> tagList) {
        String path = "src/vocabularyLists/";

        ArrayList<String> wantList = new ArrayList<>();
        ArrayList<String> disregardList = new ArrayList<>();
        ArrayList<String> searchList = new ArrayList<>();

        //split tagList into wantList and disregardList
        for(String tag : tagList) {
            if(tag.startsWith("#")) {
                wantList.add(tag.toLowerCase().replace("#", ""));
            } else if(tag.startsWith("!")) {
                disregardList.add(tag.toLowerCase().replace("!", ""));
            } else if(tag.startsWith("?")) {
                searchList.add(tag.toLowerCase().replace("?", ""));
            }
        }
        
        if(searchList.size() > 1) {
            try {
                throw new RuntimeException("Can only have 1 active search filter active via '?'");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        List<HashMap<String, List<String>>> allowedVocabList = new ArrayList<>();
        //check if vocab is allowed to be trained on
        if (Vocabulary.ID != 0) {
            ObjectMapper mapper = new ObjectMapper();
            for (int i = 1; i <= Vocabulary.ID; i++) {
                String filepath = path + i + ".json";


                try {
                    Vocabulary vocab = mapper.readValue(new File(filepath), Vocabulary.class);
                    int wantTagsLeft = wantList.size();

                    for(String vocabTag : vocab.getTags()) {
                        if(disregardList.contains(vocabTag)) {
                            wantTagsLeft += 999;
                            break;
                        }
                        if(wantList.contains(vocabTag)) {
                            wantTagsLeft--;
                        }
                    }
                    if(wantTagsLeft == 0 && vocab.matchesString(searchList.get(0))) {
                        HashMap<String, List<String>> vocabularyInfo = new HashMap<>();
                        vocabularyInfo.put("japaneseWord", List.of(vocab.getJapaneseWord()));
                        vocabularyInfo.put("pronunciation", List.of(vocab.getPronounciation()));
                        vocabularyInfo.put("englishMeanings", vocab.getEnglishMeanings());
                        vocabularyInfo.put("tags", vocab.getTags());

                        allowedVocabList.add(vocabularyInfo);

                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        } else { //is emtpy / no vocabulary
            throw new RuntimeException("Vocabulary was not initialized beforehand");
        }
        return allowedVocabList;
    }

    public static ArrayList<String> detectingTags(String input) {
        ArrayList<String> result = new ArrayList<>();
        String tag = "";
        boolean isFirstLetter = true;

        for (char letter : input.toCharArray()) {
            if(letter == '#' || letter == '!' || letter == '?') {
                if(!isFirstLetter) {
                    result.add(tag);
                    tag = "";
                }
            }
            tag += letter;
            isFirstLetter = false;
        }
        result.add(tag);


        return result;
    }

}
