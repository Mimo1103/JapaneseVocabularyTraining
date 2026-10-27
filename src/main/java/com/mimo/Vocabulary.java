package com.mimo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;

public class Vocabulary {
    public String JAPANESE_WORD;
    public String JAPANESE_PRONUNCIATION = "";
    public List<String> ENGLISH_MEANINGS;
    public List<String> TAGS;
    public static int ID = 0;

    public Vocabulary(String japaneseWord, String englishMeanings, String tags) {
        this.JAPANESE_WORD = japaneseWord;
        this.ENGLISH_MEANINGS = Arrays.asList(englishMeanings.split(", "));
        this.TAGS = Arrays.asList(tags.split(", "));
        ID++;

        WriteJSON.writeVocabulary(this);
    }

    public Vocabulary(String japaneseWord, String japanesePronunciation, String englishMeanings, String tags) {
        this.JAPANESE_WORD = japaneseWord;
        this.JAPANESE_PRONUNCIATION = japanesePronunciation;
        this.ENGLISH_MEANINGS = Arrays.asList(englishMeanings.split(", "));
        this.TAGS = Arrays.asList(tags.split(", "));
        ID++;

        WriteJSON.writeVocabulary(this);
    }

    //Empty constructor for Jackson
    @JsonIgnoreProperties(ignoreUnknown = true)
    public Vocabulary() {
    }


    
    public void setEnglishMeanings(List<String> englishMeanings) {
        this.ENGLISH_MEANINGS = englishMeanings;
    }

    @JsonIgnore
    public List<String> getEnglishMeanings() {
        return this.ENGLISH_MEANINGS;
    }

    public void setJapaneseWord(String japaneseWord) {
        this.JAPANESE_WORD = japaneseWord;
    }

    @JsonIgnore
    public String getJapaneseWord() {
        return this.JAPANESE_WORD;
    }

    public void setTags(List<String> tags) {
        this.TAGS = tags;
    }

    @JsonIgnore
    public List<String> getTags() {
        return this.TAGS;
    }

    @JsonIgnore
    public int getID() {
        return ID;
    }

    public void setPronounciation(String pronounciation) {
        this.JAPANESE_PRONUNCIATION = pronounciation;
    }

    @JsonIgnore
    public String getPronounciation() {
        return this.JAPANESE_PRONUNCIATION;
    }

    
}
