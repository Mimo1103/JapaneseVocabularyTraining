package com.mimo;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HandlingTags {

    public static HashMap<String, List<String>> createAllowedVocabList(ArrayList<String> tagList) {
        String path = "src/vocabularyLists/";
        HashMap<String, List<String>> allowedVocabList = new HashMap<>();
        ArrayList<String> wantList = new ArrayList<>();
        ArrayList<String> disregardList = new ArrayList<>();

        //split tagList into wantList and disregardList
        for(String tag : tagList) {
            if(tag.startsWith("#")) {
                wantList.add(tag.toLowerCase().replace("#", ""));
            } else {
                disregardList.add(tag.toLowerCase().replace("!", ""));
            }
        }

        //check if vocab is allowed to be trained on
        if (Vocabulary.ID != 0) {
            ObjectMapper mapper = new ObjectMapper();
            for (int i = 1; i < Vocabulary.ID; i++) {
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
                    if(wantTagsLeft == 0) {allowedVocabList.put(vocab.getJapaneseWord(), vocab.getEnglishMeanings());}
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
            if(letter == '#' || letter == '!') {
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
