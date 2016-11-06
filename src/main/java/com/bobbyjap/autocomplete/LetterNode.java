package com.bobbyjap.autocomplete;

import java.util.ArrayList;
import java.util.List;

class LetterNode
{
    // PROPERTIES ----------------------------------------------------------------------------------------------------------
    
    char value;
    List<LetterNode> children;
    List<Suggestion> suggestions;
    
    // CONSTRUCTORS --------------------------------------------------------------------------------------------------------
    
    LetterNode(char value)
    {
        this.value = value;
        this.children = new ArrayList<LetterNode>();
        this.suggestions = new ArrayList<Suggestion>();
    }
}
