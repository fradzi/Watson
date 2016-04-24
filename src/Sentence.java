import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations.TreeAnnotation;
import edu.stanford.nlp.util.CoreMap;

public class Sentence{
    private String rawSentence;
    private List<String> tokenizedSentence;
    private List<String> taggedSentence;
    private List<String> nerSentence;
    private List<Tree> parsedSentenced;
    private StanfordCoreNLP pipeline;
    private List<CoreLabel> tokensAnnotation;
    private List<CoreMap> sentencesAnnotation;
    
    public Sentence (StanfordCoreNLP pipeline, String sentence) {
        // Set pipeline
        this.pipeline = pipeline;
        this.rawSentence = sentence;
        
        // Get annotations
        Annotation document = getAnnotation(sentence);
        this.tokensAnnotation = document.get(TokensAnnotation.class);
        this.sentencesAnnotation = document.get(SentencesAnnotation.class);
        
        // Get 
        this.tokenizedSentence = tokenize();
        this.taggedSentence = posTagging();
        this.nerSentence = ner();
        this.parsedSentenced = parseTree();
    }
    
    private Annotation getAnnotation (String sentence) {
        // create an empty Annotation just with the given sentence
        Annotation document = new Annotation(sentence);
        
        // run all Annotators on this sentence
        pipeline.annotate(document);
        
        return document;
    }

    private List<String> tokenize () {
        List<String> result = new ArrayList<String>();
        for (CoreLabel token : tokensAnnotation) {
          String pos = token.get(TextAnnotation.class);
          result.add(pos);
        }
        return result;
    }
    
    private List<String> posTagging () {
        List<String> result = new ArrayList<String>();
        for (CoreLabel token : tokensAnnotation) {
          String pos = token.get(PartOfSpeechAnnotation.class);
          result.add(pos);
        }
        return result;
    }
    
    private List<String> ner () {
        List<String> result = new ArrayList<String>();
        for (CoreLabel token : tokensAnnotation) {
            String pos = token.get(NamedEntityTagAnnotation.class);
            result.add(pos);
        }
        return result;
    }
    
    private List<Tree> parseTree () {
        List<Tree> result = new ArrayList<Tree> ();
        for (CoreMap sentence : sentencesAnnotation) {
          Tree tree = sentence.get(TreeAnnotation.class);
          result.add(tree);
        }
        return result;
    }

    public String getRawSentence() {
        return rawSentence;
    }
}
