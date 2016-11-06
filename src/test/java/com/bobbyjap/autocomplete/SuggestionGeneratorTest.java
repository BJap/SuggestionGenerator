package com.bobbyjap.autocomplete;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(JUnit4.class)
public class SuggestionGeneratorTest
{ 
    // TEST CONSTANTS ------------------------------------------------------------------------------------------------------

    private static final int MAX_WORD_SUGGESTION_COUNT = 5;
    private static final String INPUT_FOLDER = "src/test/resources/input/";
    private static final String PATH_WORD_FREQUENCY_LIST = INPUT_FOLDER + "word-frequencies.txt";
    private static final String PATH_TEST_INPUT = INPUT_FOLDER + "sample-input.txt";
    private static final String PATH_TEST_OUTPUT = INPUT_FOLDER + "sample-output.txt";
    
    // TEST PROPERTIES -----------------------------------------------------------------------------------------------------
    
    private static final Logger LOGGER = LoggerFactory.getLogger(SuggestionGeneratorTest.class);

    // TESTS ---------------------------------------------------------------------------------------------------------------
    
    @Test
    public void testGetWordSuggestions()
    {
        LOGGER.info("Get test data from input files");
        
        List<String> frequencyList = getTestInput(PATH_WORD_FREQUENCY_LIST);
        List<String> inputs = getTestInput(PATH_TEST_INPUT);
        List<List<Suggestion>> outputs = parseExpected(getTestInput(PATH_TEST_OUTPUT));
        
        LOGGER.info("Create a suggestion generator");
        
        SuggestionGenerator generator = new SuggestionGenerator(frequencyList);

        for (int i = 0; i < inputs.size(); i++)
        {
            String input = inputs.get(i);
            
            LOGGER.info("Generate autocompletion suggestions for " + input);
            
            List<Suggestion> expectedSuggestions = outputs.get(i);
            List<Suggestion> actualSuggestions = generator.generateSuggestions(input, MAX_WORD_SUGGESTION_COUNT);

            if (expectedSuggestions.size() > actualSuggestions.size())
            {
                fail("Fewer suggestions were returned than should have been");
            }
            
            LOGGER.info("Verify autocompletion suggestions for: " + input);

            for (int j = 0; j < expectedSuggestions.size(); j++)
            {
                Suggestion expected = expectedSuggestions.get(j);
                Suggestion actual = actualSuggestions.get(j);
                
                LOGGER.info("Expected suggestion " + (j + 1) + " is: " + expected);
                LOGGER.info("Actual suggestion " + (j + 1) + " is: " + actual);

                assertEquals("Suggested word was not correct", expected.toString(), actual.toString());
                
                LOGGER.info("The suggestion was correct");
            }
        }
        
        LOGGER.info("Test passed");
    }

    // TEST DATA COLLECTION ------------------------------------------------------------------------------------------------

    private static List<String> getTestInput(String path)
    {
        File file = new File(path);
        List<String> input = new ArrayList<String>();

        try
        {
            Scanner in = new Scanner(file);

            while (in.hasNextLine())
            {
                String s = in.nextLine();
                
                input.add(s);
            }

            in.close();
        } 
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        return input;
    }
    
    private static List<List<Suggestion>> parseExpected(List<String> input)
    {
        List<List<Suggestion>> expected = new ArrayList<List<Suggestion>>();
        List<Suggestion> section = new ArrayList<Suggestion>();
        
        for (int i = 0; i < input.size(); i++)
        {
            String s = input.get(i);

            if (s.equals(""))
            {
                expected.add(section);

                section = new ArrayList<Suggestion>();
            }
            else if (s.contains(":")) continue;
            else
            {
                String[] line = s.split("\\s+");
                String word = line[0];
                int wordRank = Integer.parseInt(line[1].substring(1, line[1].length() - 1));
                Suggestion suggestion = new Suggestion(word, wordRank);
                        
                section.add(suggestion);
            }
        }
        
        return expected;
    }
}
