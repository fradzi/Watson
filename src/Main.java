import java.io.*;
import java.util.List;
import java.util.ArrayList;
import java.util.Properties;

import edu.stanford.nlp.pipeline.*;

public class Main {
	private static StanfordCoreNLP pipeline;
	
	private static void initPipeline() {
        Properties props = new Properties();
        props.put("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        pipeline = new StanfordCoreNLP(props);
	}
	
    private static List<String> parseInputs() {
        List<String> rawSentences = new ArrayList<>();

        String fileName = "inputPart1.txt";
        String line = null;

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));

            while((line = bufferedReader.readLine()) != null) {
                rawSentences.add(line);
            }
            bufferedReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("Unable to open file '" + fileName + "'");
        } catch(IOException ex) {
            System.out.println("Error reading file '" + fileName + "'");
        }

        return rawSentences;
    }
	
    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        initPipeline();
        
        List<String> rawSentences = parseInputs();
    }
}
