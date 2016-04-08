'''
Revanth Reddy
Filip Radzikowski
Watson Jr.
'''
import nltk
import sqlite3
from dicts import *

import os
from nltk.parse import stanford

from itertools import izip

# For user to change
# USERSTANFORDDIR = '/Users/fr/Downloads'  # for Filip
USERSTANFORDDIR = '/Users/revanthreddy/Downloads'  # for Rev


STANFORDTOOLSDIR = USERSTANFORDDIR + '/stanford-parser'
os.environ['STANFORD_PARSER'] = STANFORDTOOLSDIR+'/stanford-parser.jar'
os.environ['STANFORD_MODELS'] = STANFORDTOOLSDIR+'/stanford-parser-3.3.0-models.jar'
os.environ['CLASSPATH'] = STANFORDTOOLSDIR+'/stanford-parser.jar:' + STANFORDTOOLSDIR + '/stanford-parser-3.3.0-models.jar'



def main():
	# Extract the stanford-parser-3.3.0-models.jar file in order to set the model_path
    # parser = stanford.StanfordParser(model_path= USERSTANFORDDIR + "/stanford-parser/stanford-parser-3.3.0-models/edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz")
    # sentences = parser.raw_parse_sents(("Hello, My name is Melroy.", "What is your name?"))

    # # Draw tree graphic
    # for line in sentences: 
    #     for sentence in line:
    #         sentence.draw()      # show tree graphic image
    #		  print str(sentence)  # show nested tree in console


	# Get sentences from input file
    inputs = parseLines()
    
    # Pass the sentences through a POS tagger
    taggedSentences = getPOSTags(inputs)

    # Extract NE from sentences and contain into list of tuples
    entities = getNameEntities(taggedSentences)

    # Determine what catagory each sentence is in
    # getSentenceCategory(entities);
    cats = getCategories(taggedSentences, entities)

    for x,y in izip(inputs, cats):
    	print x
    	print y
	

def parseLines():
    with open('sample_sentences.txt', 'r') as f:
        data = [line.strip()[5:] for line in f]
    return data


def getPOSTags(sentences):
	taggedSentences = []
	for s in sentences:
		taggedSentences.append(nltk.pos_tag(nltk.word_tokenize(s)))
	return taggedSentences


def getNameEntities(sentences):
	alltuples = []
	for s in sentences:
		t = ()
		# get name enties from each sentence and remove the for word of each sentence
		for chunk in nltk.ne_chunk(s)[1:]:
			#  generally identify a NE 
			if type(chunk) is nltk.tree.Tree:
				newline =  str(chunk)[1:-1].split()
				newline2 = newline[1].split('/')
				finalline = [newline[0], newline2[0], newline2[1]]
				t = t+ (finalline,)
			# else there are no NE then get NNP and NN
		alltuples.append(t)
	return alltuples


def getCategories(tagged, entities):
	allcats = []
	for sentence in tagged:
		# For each word in sentence, save only nouns and verbs in new list
		NounsAndVerbs = [tpl for tpl in sentence if NVs.get(tpl[1])]

		cats = [] #meow
		for line in NounsAndVerbs:
			c = dictCategories.get(line[0])
			if c != None:
				cats.append(c)
		
		# For ambiguous imputs
		if len(cats) == 0:
			allcats.append('**unknown**')
		
		# Save categories
		else:
			allcats.append(', '.join(cats))

	return allcats

			
		

# def getSentenceCategory(entities):
# 	count=0
# 	for tpl in entities:
# 		count+=1
# 		print '**********Sentence %s' % count
# 		print tpl
# 		if len(tpl) == 0:
# 			print '*empty*'
# 			continue
# 		for word in tpl:
# 			if word[0] == 'GPE':
# 				print 'Geographic'
# 			elif word[0] == 'ORGANIZATION':
# 				print 'Organization'
# 			elif word[0] == 'PERSON':
# 				print 'Person'


if __name__ == "__main__":
    main()