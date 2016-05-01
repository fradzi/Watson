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
    
    private String getQuestionType(Tree t){
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
    
    public static String getGrammaticalPattern(Tree t){
//        System.out.println("When exists  :  " +  currentQuery.getTokenizedSentence().contains("When"));
        
        StringBuilder s = new StringBuilder();
        
        // Peek at firstChild of Root and grab the POS tags for child nodes
        String v = t.getChildrenAsList().get(0).value();
        if ( v.equals("S"))
        {
            t = t.getChild(0); // Root 
        }
        else if (v.equals("SQ")) {
            t = t.getChild(0);  // get tree from SQ
        }
        else if (currentQuery.getTokenizedSentence().contains("When")) {
            t = t.getChild(0);  // get tree from SQ
            t = t.getChild(1);  // Go to SQ
            t.getChildrenAsList().forEach(x -> s.append(x.value()));
            t = t.getChild(2);  // Go to VP
        }
        else // if (!v.equals("SQ"))  WH- type
        {
            t = t.getChild(0);  // Go to SBARQ   
            t = t.getChild(1);  // Go to SQ
            t.getChildrenAsList().forEach(x -> s.append(x.value()));
            t = t.getChild(0);  // Go to VP
        }
        t.getChildrenAsList().forEach(x -> s.append(x.value()));
        
        // Save string representing the first set of children for S or SQ parents
        String initPattern = s.toString();
        
        // For cases where more than one query has same initial key pattern, go deeper into tree
        if (initPattern.equals("VBDNPVP.")){
            t.getChild(2).getChildrenAsList().forEach(x -> s.append(x.value()));
        }
        else if (initPattern.equals("VP.")){
            t.getChild(0).getChildrenAsList().forEach(x -> s.append(x.value()));
            if( s.toString().equals("VP.VBDNP")) {
                t.getChild(1).getChildrenAsList().forEach(x -> s.append(x.value())); // append NN
            }
        }
        
        return s.toString();
    } // end getGrammaticalPattern()
    
    public String createSqlStatement() {
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
        
        String keyPattern = getGrammaticalPattern(t); 
        System.out.println("keyPattern  :  " +  keyPattern);
        
        return "";
    } // end createStatement()
}
