import java.util.Properties;
import java.util.Scanner;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class MainDriver {
    private static Sentence currentQuery;
    private static String sqlQuery = null;
    private static StanfordCoreNLP pipeline;
  
    // Initialize the Stanford NLP pipeline
    private static void initPipeline() {
      Properties props = new Properties();
      props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse");
      pipeline = new StanfordCoreNLP(props);
    }
    
    public static void main(String[] args) 
    {
        //Initialize Stanford NLP tools
        initPipeline();
        
        // Greeting"
        System.out.println("Hello! My name is Watson Jr.");
        
        // Capture input and process query
        String input;
        Scanner scanner = new Scanner(System.in);
        do{
            System.out.println("Ask a question or type 'q' to quit.");
            input = scanner.nextLine().trim();
            
            if(!input.equalsIgnoreCase("q")){
                currentQuery = new Sentence(pipeline, input);
                System.out.println("<QUERY>\n" + currentQuery.getRawSentence());
                
                //TODO perform any query processing
                printSQL(); //TODO implement method below
                printAnswer(); //TODO implement method below
                System.out.println();
            }
        }while(!input.equalsIgnoreCase("q"));
        
        scanner.close();
        System.out.println("Goodbye.");
    }
    
    public static void printSQL(){
        //TODO update this to get and print appropriate SQL
        sqlQuery = ""; // map currentQuery to sqlQuery
        System.out.println("<SQL>\n" + sqlQuery);
    }
    public static void printAnswer(){
        String answer = ""; // execute sqlQuery in order to generate appropriate natural language response 
        System.out.println("<ANSWER>\n" + answer);
    }
}
