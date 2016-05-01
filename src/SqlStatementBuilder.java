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
    
    public String createSqlStatement() {
        Tree t = null;
        // Get tree at Root
        try {
            t = currentQuery.getParsedSentence().get(0);
        } catch (Exception e) {
            System.out.println("Error1: Check your query format. Might be missing words that help me understand your question.");
            return ";";
        }
        // For Yes/No question, add Select statement to grab count
        if (getQuestionType(t).equals("closed")) {
            SELECT.add("SELECT COUNT(*) ");
        } else { // set bool to false and do not add Select statement yet
            currentQuery.setClosedQuestion(false);
        }
        
        String keyPattern = getGrammaticalPattern(t); 
//        System.out.println("keyPattern  :  " +  keyPattern);
        
        if (keyPattern.isEmpty()) {
            return ";";
        }
        
        compareKeyPattern(keyPattern);
        
        // Combine Select, From, Where fragments into single statement
        StringBuilder finalQuery = new StringBuilder();
        SELECT.forEach(x -> finalQuery.append(x));
        FROM.forEach(x -> finalQuery.append(x));
        WHERE.forEach(x -> finalQuery.append(x));
        finalQuery.append(";");
        
        // clear for next query
        SELECT.clear();
        FROM.clear();
        WHERE.clear();
            
        return finalQuery.toString();
    } // end createStatement()
    
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
        StringBuilder s = new StringBuilder();

        // Peek at firstChild of Root and grab the POS tags for child nodes
        String v = t.getChildrenAsList().get(0).value();
        
        try {
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
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error2: Check your query format. Might be missing words that help me understand your question.");
            return "";
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
    
    public static String splitMovieName (String movie) {
        StringBuilder str = new StringBuilder();
        
        char c;
        str.append(movie.charAt(0)); // get first letter
        for (int i = 1; i < movie.length(); i++ ) { // remaining letters
            c = movie.charAt(i);
            if (Character.isUpperCase(c) || Character.isDigit(c)) {
                str.append(" ");
                str.append(c);
            }
            else {
                str.append(c);
            }
        }
        return str.toString();
    }
    
    private void compareKeyPattern(String keyPattern) {
        // Get POS tagged and tokenized sentence
        List<String> taggedSentence = currentQuery.getTaggedSentence();
        List<String> tokenizedSentence = currentQuery.getTokenizedSentence();
        
        try {
            if ( keyPattern.equals("VBZNPNP.") ){ // Is Kubrick a director? True
                String nnpName = tokenizedSentence.get(taggedSentence.indexOf("NNP"));
                String nnName = tokenizedSentence.get(taggedSentence.indexOf("NN"));
                
                FROM.add("FROM Person as P ");
                if( nnName.equals("director")){
                    FROM.add("INNER JOIN Director D ON P.id = D.director_id ");
                } else{
                    FROM.add("INNER JOIN Actor A ON P.id = A.actor_id ");
                }
                WHERE.add("WHERE P.name LIKE \"%" + nnpName + "%\"");
            } 
            else if (keyPattern.equals("VBZNPADVPNP.")){ // Is MightyAphrodite by Allen? True
                int i = taggedSentence.indexOf("NN");
                if (i < 0)
                    i = taggedSentence.indexOf("NNP");
                String nnpMovieName = tokenizedSentence.get(i);
                taggedSentence =  taggedSentence.subList(i+1, taggedSentence.size());
                tokenizedSentence = tokenizedSentence.subList(i+1, tokenizedSentence.size());
                String nnpDirectorName = tokenizedSentence.get(taggedSentence.indexOf("NNP"));
                
                FROM.add("FROM Person as P ");
                FROM.add("INNER JOIN Director D on D.director_id = P.id ");
                FROM.add("INNER JOIN Movie M on D.movie_id = M.id ");
                WHERE.add("WHERE P.name LIKE \"%" + nnpDirectorName + "%\"");
                WHERE.add("and M.name LIKE \"%" + splitMovieName(nnpMovieName) + "%\"");
            } 
            else if (keyPattern.equals("VBDNPVP.VBNPP")){ //Was Loren born in Italy? True
                int i = taggedSentence.indexOf("NNP");
                String nnpName = tokenizedSentence.get(i);
                taggedSentence =  taggedSentence.subList(i+1, taggedSentence.size());
                tokenizedSentence = tokenizedSentence.subList(i+1, tokenizedSentence.size());
                String nnpLocation = tokenizedSentence.get(taggedSentence.indexOf("NNP"));
                
                FROM.add("FROM Person as P ");
                WHERE.add("WHERE P.name LIKE \"%" + nnpName + "%\"");
                WHERE.add("and P.pob LIKE \"%" + nnpLocation + "%\"");
            }
            else if (keyPattern.equals("VBDNPPP.") || keyPattern.equals("VBDNPSBAR.")) { // Was Birdman the best movie in 2015? True
                String nnpName = tokenizedSentence.get(taggedSentence.indexOf("NNP"));
                String cd = tokenizedSentence.get(taggedSentence.indexOf("CD"));
                String bestCatagory = tokenizedSentence.get(tokenizedSentence.indexOf("best")+1);
                
                WHERE.add("WHERE name LIKE \"%" + nnpName + "%\"");
                WHERE.add("AND O.year LIKE \"%" + cd + "%\"") ;
                
                if ( bestCatagory.equals("actor") || bestCatagory.equalsIgnoreCase("actress") 
                                                  || bestCatagory.equalsIgnoreCase("supporting")){
                    
                    if (bestCatagory.equalsIgnoreCase("supporting")) {
                        bestCatagory = bestCatagory.concat("-");
                        bestCatagory = bestCatagory.concat(tokenizedSentence.get(tokenizedSentence.indexOf("supporting")+1));
                    }
                    
                    System.out.println(bestCatagory);
                    FROM.add("FROM Person as P ");
                    FROM.add("INNER JOIN Actor A on P.id = A.actor_id ");
                    FROM.add("INNER JOIN Oscar O on P.id = O.person_id ");
                    WHERE.add("AND O.type LIKE \"%best-" + bestCatagory + "%\"") ;
                }
                else if ( bestCatagory.equals("director")){
                    FROM.add("FROM Person as P ");
                    FROM.add("INNER JOIN Director D on P.id = D.director_id ");
                    FROM.add("INNER JOIN Oscar O on P.id = O.person_id ");
                    WHERE.add("AND O.type LIKE \"%best-" + bestCatagory + "%\"") ;
                }
                else { // movie, film, or picture
                    FROM.add("FROM Movie as M ");
                    FROM.add("INNER JOIN Oscar O on M.id = O.movie_id ");
                    WHERE.add("AND O.type LIKE \"%best-picture%\"") ;
                }
            }
            else if (keyPattern.equals("VBDNPVP.VBPP")){ // Did Neeson star in Schindler’s List? True
                int i = taggedSentence.indexOf("NNP");
                String nnpName = tokenizedSentence.get(i);
                taggedSentence =  taggedSentence.subList(i+1, taggedSentence.size());
                tokenizedSentence = tokenizedSentence.subList(i+1, tokenizedSentence.size());
                String nnpMovieName = tokenizedSentence.get(taggedSentence.indexOf("NNP"));
                
                FROM.add("FROM Person as P ");
                FROM.add("INNER JOIN Actor A on P.id = A.actor_id ");
                FROM.add("INNER JOIN Movie M on A.movie_id = M.id ");
                WHERE.add("WHERE P.name LIKE \"%" + nnpName + "%\"");
                WHERE.add("and M.name LIKE \"%" + splitMovieName(nnpMovieName) + "%\"") ;
            }
            else if (keyPattern.equals("VBDNPVP.VBNP")){
                int i = taggedSentence.indexOf("NNP");
    
                if (i < 0){   // Did a French actor win the oscar in 2012? True
                   String cd = tokenizedSentence.get(taggedSentence.indexOf("CD"));
                   String jj = tokenizedSentence.get(taggedSentence.indexOf("JJ"));
                   
                   FROM.add("FROM Person as P ");
                   FROM.add("INNER JOIN Oscar O on P.id = O.person_id ");
                   WHERE.add("WHERE P.pob LIKE \"%" + jj.substring(0, 2) + "%\"");
                   WHERE.add("AND O.year LIKE \"%" + cd + "%\"");
                }
                else{        // Did Swank win the oscar in 2000? True
                   String nnpName = tokenizedSentence.get(i);
                   String cd = tokenizedSentence.get(taggedSentence.indexOf("CD"));
                   
                   FROM.add("FROM Person as P ");
                   FROM.add("INNER JOIN Oscar O on P.id = O.person_id ");
                   WHERE.add("WHERE name LIKE \"%" + nnpName + "%\"");
                   WHERE.add("AND O.year LIKE \"%" + cd + "%\"");
                }
            }
            else if (keyPattern.equals("SVP.")){ // Did a movie with Neeson win the oscar for best film? False
                String nnpName = tokenizedSentence.get(taggedSentence.indexOf("NNP"));
                String bestCatagory = tokenizedSentence.get(tokenizedSentence.indexOf("best")+1);
                
                if( bestCatagory.equals("movie")|| bestCatagory.equals("film")){
                    bestCatagory =  "picture";
                }
                
                FROM.add("FROM Person as P ");
                FROM.add("INNER JOIN Oscar O on P.id = O.person_id ");
                WHERE.add("WHERE P.name LIKE \"%" + nnpName + "%\"");
                WHERE.add("AND O.type LIKE \"%best-" + bestCatagory + "%\" ");
            }
            else if (keyPattern.equals("VPVBDNP")){
                int i = taggedSentence.indexOf("NNP");
                
                if ( i < 0){ // if no NNP exists
                    if ( tokenizedSentence.indexOf("best") <  0){ // Who directed Milk? Gus Van Sant
                        String nnpMovieName = tokenizedSentence.get(taggedSentence.indexOf("NN"));
                        
                        SELECT.add("SELECT P.name ");
                        FROM.add("FROM Person as P ");
                        FROM.add("INNER JOIN Director D on P.id = D.director_id ");
                        FROM.add("INNER JOIN Movie M on D.movie_id = M.id ");
                        WHERE.add("WHERE M.name LIKE \"%" + splitMovieName(nnpMovieName) + "%\"");
                    }
                    else {  // Who directed the best movie in 2014?  Steve McQueen
                        String cd = tokenizedSentence.get(taggedSentence.indexOf("CD"));
                        String bestCatagory = tokenizedSentence.get(tokenizedSentence.indexOf("best")+1);
                        
                        if( bestCatagory.equals("movie")|| bestCatagory.equals("film")){
                            bestCatagory =  "picture";
                        }
                        
                        SELECT.add("SELECT P.name ");
                        FROM.add("FROM Person as P ");
                        FROM.add("INNER JOIN Director D on P.id = D.director_id ");
                        FROM.add("INNER JOIN Oscar O on D.movie_id = O.movie_id ");
                        FROM.add("INNER JOIN Actor A on A.movie_id = O.movie_id ");
                        WHERE.add("WHERE O.year LIKE \"%" + cd + "%\"") ;
                        WHERE.add("AND O.type LIKE \"%best-" + bestCatagory + "%\"");
                    }
                } else { // NNP exists;  Who directed Schindler’s List? Steven Spielberg
                    String nnpMovieName = tokenizedSentence.get(i);
                    System.out.println(nnpMovieName);
                    SELECT.add("SELECT P.name ");
                    FROM.add("FROM Person as P ");
                    FROM.add("INNER JOIN Director D on P.id = D.director_id ");
                    FROM.add("INNER JOIN Movie M on D.movie_id = M.id ");
                    WHERE.add("WHERE M.name LIKE \"%" + splitMovieName(nnpMovieName) + "%\"");
                }
            }
            else if (keyPattern.equals("VPVBDNPPPPP")){ // Who won the oscar for best actor in 2005? Jamie Foxx
                String cd = tokenizedSentence.get(taggedSentence.indexOf("CD"));
                String bestCatagory = tokenizedSentence.get(tokenizedSentence.indexOf("best")+1);
                
                // TODO: error handling
                if( bestCatagory.equals("movie")|| bestCatagory.equals("film") || bestCatagory.equals("picture") ){
                    return;
                }
                
                SELECT.add("SELECT P.name FROM Person as P " );
                WHERE.add("WHERE O.year LIKE \"%" + cd + "%\" ") ;  
                FROM.add("INNER JOIN Oscar O on P.id = O.person_id ");
                WHERE.add("AND O.type LIKE \"%best-" + bestCatagory + "%\" ") ;
            }
            else if (keyPattern.equals("VPVBDNPPP")){
                String cd = tokenizedSentence.get(taggedSentence.indexOf("CD"));
                String bestCatagory = tokenizedSentence.get(tokenizedSentence.indexOf("Which")+1);
                
                WHERE.add("WHERE O.year LIKE \"%" + cd + "%\"") ;
                // person;  Which actress won the oscar in 2012? Meryl Streep
                if ( bestCatagory.equals("actor") || bestCatagory.equalsIgnoreCase("actress") 
                     || bestCatagory.equalsIgnoreCase("supporting") || bestCatagory.equals("director") ){
                    
                    if (bestCatagory.equalsIgnoreCase("supporting")) {
                        bestCatagory = bestCatagory.concat("-");
                        bestCatagory = bestCatagory.concat(tokenizedSentence.get(tokenizedSentence.indexOf("supporting")+1));
                    }
                    
                    SELECT.add("SELECT P.name ");
                    FROM.add("FROM Person as P ");
                    FROM.add("INNER JOIN Oscar O on P.id = O.person_id ");
                    WHERE.add("AND O.type LIKE \"%best-" + bestCatagory + "%\"") ;
                }
                else { // movie;  Which movie won the oscar in 2000? American Beauty
                    SELECT.add("SELECT M.name " );
                    FROM.add("FROM Movie as M ");
                    FROM.add("INNER JOIN Oscar O on M.id = O.movie_id ");
                }
            }
            else if (keyPattern.equals("VBDNPVPVBNP")){ // When did Blanchett win an oscar for best actress? 2014
                String nnpName = tokenizedSentence.get(taggedSentence.indexOf("NNP"));
                String bestCatagory = tokenizedSentence.get(tokenizedSentence.indexOf("best")+1);
                
                if( bestCatagory.equals("movie")|| bestCatagory.equals("film")){
                    bestCatagory =  "picture";
                }
                
                SELECT.add("SELECT O.year ");
                FROM.add("FROM Person as P " );
                FROM.add("INNER JOIN Oscar O on P.id = O.person_id ");
                WHERE.add("WHERE name LIKE \"%"  + nnpName + "%\" ");
                WHERE.add("AND O.type LIKE \"%best-" + bestCatagory + "%\" ") ;
            }
            else {
                System.out.println("Error3: Something went wrong. Check your query format and try again.");
            }
        }
        catch (Exception e) {
            System.out.println("Error4: Check your query format. Might be missing words that help me understand your question.");
            SELECT.clear(); WHERE.clear(); FROM.clear();
        }
    } // end compareKeyPattern()
}
