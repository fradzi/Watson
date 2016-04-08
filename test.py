'''
Revanth Reddy
Filip Radzikowski
Watson Jr.
'''
import nltk
import sqlite3
#import ner

# from nltk.tag import StanfordNERTagger
# from nltk.parse import stanford
import os
from nltk.parse import stanford

# For user to change
USERSTANFORDDIR = '/Users/fr/Downloads'

STANFORDTOOLSDIR = USERSTANFORDDIR + '/stanford-parser'
os.environ['STANFORD_PARSER'] = STANFORDTOOLSDIR+'/stanford-parser.jar'
os.environ['STANFORD_MODELS'] = STANFORDTOOLSDIR+'/stanford-parser-3.3.0-models.jar'
os.environ['CLASSPATH'] = STANFORDTOOLSDIR+'/stanford-parser.jar:' + STANFORDTOOLSDIR + '/stanford-parser-3.3.0-models.jar'



def main():
	#inputs = parseLines()
	#tagNameEntity(inputs)


    # Extract the stanford-parser-3.3.0-models.jar file in order to set the model_path
    parser = stanford.StanfordParser(model_path= USERSTANFORDDIR + "/stanford-parser/stanford-parser-3.3.0-models/edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz")
    sentences = parser.raw_parse_sents(("Hello, My name is Melroy.", "What is your name?"))

    for line in sentences: 
        for sentence in line:
            sentence.draw()


    # st = StanfordNERTagger('english.all.3class.distsim.crf.ser.gz') 
    # st.tag('Rami Eid is studying at Stony Brook University in NY'.split())
    
    
    '''
    tagger = ner.HttpNER(host='localhost', port=8080)
    tagger.get_entities("University of California is located in California, United States")
    tagger.json_entities("Alice went to the Museum of Natural History.")
'''
    

'''
def parseLines():
	with open('sample_sentences.txt', 'r') as f:
	    data = [line.strip()[5:] for line in f]
	return data


def tagNameEntity(sentences):
	try:
		for sentence in sentences:
		    tokens = nltk.word_tokenize(sentence)
		    tagged = nltk.pos_tag(tokens)
		    # nltk.ne_chuck() is a classifier that has already 
            # been trained to recognize named entities 
		    entities = nltk.chunk.ne_chunk(tagged)
		    print entities
	except Exception, e:
		print str(e)


def extract_entity_names(t):
    entity_names = []

    if hasattr(t, 'label') and t.label:
        if t.label() == 'NE':
            entity_names.append(' '.join([child[0] for child in t]))
        else:
            for child in t:
                entity_names.extend(extract_entity_names(child))

    return entity_names

'''



if __name__ == "__main__":
    main()