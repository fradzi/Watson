import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;


public class Main {
    private static StanfordCoreNLP pipeline;
    
    // Initialize the Stanford NLP pipeline
    private static void initPipeline() {
      Properties props = new Properties();
      props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
      pipeline = new StanfordCoreNLP(props);
    }

    // Read an input file line by line and save to list
    private static List<String> parseInputs() {
        List<String> lines = new ArrayList<>();
        
        String fileName = "inputPart1.txt";
        String line = null;
        
        try {
            // Create object to open and read file
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            
            // Read file one line at a time and add to list
            while((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            // Close file
            bufferedReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }
        
        return lines;
    }

    // Tokenize a single sentence
    private static List<String> tokenize(String sentence) {
        // create an empty Annotation just with the given text
        Annotation document = new Annotation(sentence);
        
        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreLabel> tokens = document.get(TokensAnnotation.class);
        
        List<String> result = new ArrayList<String>();
        for (CoreLabel token : tokens) {
            // this is the text of the token
            String word = token.get(TextAnnotation.class);
            result.add(word);
        }
        
        return result;
    }
    
    // Helper function to tokenize a list of sentences
    private static List<List<String>> tokenize(List<String> rawSentences) {
        // Create a list of lists of Strings
        List<List<String>> tokenizedSentences = new ArrayList<>();
        
        // Tokenize each sentence and save result to list
        for (String sentence : rawSentences) {
            List<String> tokensForOneSentence = tokenize(sentence);
            tokenizedSentences.add(tokensForOneSentence);
        }
        
        return tokenizedSentences;
    }

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        // Initialize the Stanford NLP tools on program start
        initPipeline();
        
        // Get input questions from file as raw strings
        List<String> rawSentences = parseInputs();
        
        // Tokenize the questions 
        List<List<String>> tokenizedSentences = tokenize(rawSentences);
        
    }
}
