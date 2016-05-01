import java.util.ArrayList;
import java.util.List;

import edu.stanford.nlp.trees.Tree;

public class SqlStatementBuilder {
    private static Sentence currentQuery;
    private static ArrayList<String> SELECT;
    private static ArrayList<String> FROM;
    private static ArrayList<String> WHERE;
    
    // Constructor
    public SqlStatementBuilder (Sentence currentQuery) {
        this.currentQuery = currentQuery;
        this.SELECT = new ArrayList<String>();
        this.FROM = new ArrayList<String>();
        this.WHERE = new ArrayList<String>();
    }
    
    private static String getQuestionType(Tree t){
        t = t.getChild(0); // Tree at SQ
        
        do {
            t = t.firstChild();
        } while(t.depth() > 1 );
        
        switch (t.value()) {
        case "VBZ":
        case "VBD":
            return "closed"; // Yes/No Question
        default:
            return "open";   // Wh Question
        }
    } // end getQuestionType()
    
    public static String createStatement() {
        // Get tree at Root
        Tree t = currentQuery.getParsedSentence().get(0); 
        
        // Get POS tagged and tokenized sentence
        List<String> taggedSentence = currentQuery.getTaggedSentence();
        List<String> tokenizedSentence = currentQuery.getTokenizedSentence();
        
        // For Yes/No question, add Select statement to grab count
        if (getQuestionType(t).equals("closed")) {
            SELECT.add("SELECT COUNT(*) ");
        } else { // set bool to false and do not add Select statement yet
            currentQuery.setClosedQuestion(false);
        }
        
        return null;
    } // end createStatement()
}
