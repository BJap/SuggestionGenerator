package com.bobbyjap.autocomplete;

public class Suggestion
{
    // PROPERTIES ----------------------------------------------------------------------------------------------------------
    
    String word;
    int wordRank;
    
    // CONSTRUCTORS --------------------------------------------------------------------------------------------------------
    
    public Suggestion(String word, int wordRank)
    {
        this.word = word;
        this.wordRank = wordRank;
    }
    
    // PUBLIC METHODS ------------------------------------------------------------------------------------------------------
    
    public String toString()
    {
        return word + " (" + wordRank + ")";
    }
}
