package com.bobbyjap.autocomplete;

import java.util.ArrayList;
import java.util.List;

public class SuggestionGenerator
{
    // PROPERTIES ----------------------------------------------------------------------------------------------------------

    private LetterNode lookup;
    
    // CONSTRUCTORS --------------------------------------------------------------------------------------------------------
    
    /**
     * A class to offer auto-completion capabilities 
     * @param words the frequency-sorted list of words from which to pull suggestions
     */
    public SuggestionGenerator(List<String> words)
    {
        buildLookupTrie(words);
    }
    
    // PUBLIC METHODS ------------------------------------------------------------------------------------------------------

    /**
     * Collect a list of suggestions
     * @param input the substring that the suggestions must contain
     * @param maxCount the maximum number of suggestions to return
     * @return a list containing {@link Suggestion} objects
     */
    public List<Suggestion> generateSuggestions(String input, int maxCount)
    {
        List<Suggestion> suggestions = getSuggestions(lookup, input);
        
        if (maxCount > suggestions.size()) maxCount = suggestions.size();
        
        return suggestions.subList(0, maxCount);
    }
    
    // PRIVATE METHODS -----------------------------------------------------------------------------------------------------
    
    private void buildLookupTrie(List<String> words)
    {
        lookup = new LetterNode('!');
        
        for (int i = 0; i < words.size(); i++)
        {
            String word = words.get(i);
            Suggestion suggestion = new Suggestion(word, i + 1);
            
            insertSuggestion(lookup, suggestion);
        }
    }
    
    private static void insertSuggestion(LetterNode letter, Suggestion suggestion)
    {
        LetterNode current = letter;
        String word = suggestion.word;
        
        for (int i = 0; i < word.length(); i++)
        {
            List<LetterNode> children = current.children;
            LetterNode child = null;
            char c = word.charAt(i);

            for (int j = 0; j < children.size(); j++)
            {
                child = children.get(j);

                if (child.value == c) break;
                else child = null;
            }

            if (child == null)
            {
                child = new LetterNode(c);
                children.add(child);
            }
            
            current.suggestions.add(suggestion);
            current = child;
        }
        
        current.suggestions.add(suggestion);
    }
    
    private static List<Suggestion> getSuggestions(LetterNode letter, String input)
    {
        List<Suggestion> suggestions = new ArrayList<Suggestion>();
        LetterNode current = letter;
        String search = input;
        int i;
        
        for (i = 0; i < search.length() && current != null; i++)
        {
            List<LetterNode> children = current.children;
            char c = search.charAt(i);
            
            for (LetterNode child : children)
            {
                if (child.value == c)
                {
                    current = child;
                    
                    break;
                }
            }
        }
        
        if (i == search.length()) suggestions = current.suggestions;
        
        return suggestions;
    }
}
