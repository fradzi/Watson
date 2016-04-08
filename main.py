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
from nltk.tag import StanfordPOSTagger

from itertools import izip

# For user to change
# USERSTANFORDDIR = '/Users/fr/Downloads'  # for Filip
USERSTANFORDDIR = '/Users/revanthreddy/Downloads'  # for Rev

# Getting and setting class paths
STANFORDTOOLSDIR = USERSTANFORDDIR + '/stanford-parser'
os.environ['STANFORD_PARSER'] = STANFORDTOOLSDIR+'/stanford-parser.jar'
os.environ['STANFORD_MODELS'] = STANFORDTOOLSDIR+'/stanford-parser-3.3.0-models.jar'
os.environ['CLASSPATH'] = STANFORDTOOLSDIR+'/stanford-parser.jar:' + STANFORDTOOLSDIR + '/stanford-parser-3.3.0-models.jar'

# Stanford tagger
# tagger = StanfordPOSTagger(USERSTANFORDDIR + '/stanford-postagger/models/english-bidirectional-distsim.tagger', USERSTANFORDDIR + '/stanford-postagger/stanford-postagger.jar')
# tagger = StanfordPOSTagger(USERSTANFORDDIR + '/stanford-postagger/models/english-left3words-distsim.tagger', USERSTANFORDDIR + '/stanford-postagger/stanford-postagger.jar')
# print tagger.tag('Did a movie by Spielberg with Neeson win the oscar for best film?'.split())


def main():
	# # Extract the stanford-parser-3.3.0-models.jar file in order to set the model_path
    # parser = stanford.StanfordParser(model_path= USERSTANFORDDIR + "/stanford-parser/stanford-parser-3.3.0-models/edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz")
    # sentences = parser.raw_parse_sents(("Hello, My name is Melroy.", "What is your name?"))

    # # Draw tree graphic
    # for line in sentences: 
    #     for sentence in line:
            # sentence.draw()      # show tree graphic image
    		# print str(sentence)  # show nested tree in console


	# Get sentences from input file
    inputs = parseLines()
    
    # Pass the sentences through a POS tagger
    taggedSentences = getPOSTags(inputs)

    # Extract NE from sentences and contain into list of tuples
    # entities = getNameEntities(taggedSentences)

    # Determine what catagory each sentence is in
    # getSentenceCategory(entities);
    cats = getCategories(taggedSentences)

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


def getCategories(tagged):
	categoriesForAllSents = []

	for taggedSentence in tagged:
		# print sentence
		# For each word in sentence, save only nouns and verbs in new list
		NounsAndVerbs = [tpl for tpl in taggedSentence if NVs.get(tpl[1])]

		categoriesPerSentence = [] #meow
		for tpl in NounsAndVerbs:
			c = dictCategories.get(tpl[0]) #Checks dictCategories if word exists
			if c != None:				   #If it exists
				categoriesPerSentence.append(c)			   #Save one of categories for this sentences

		# For ambiguous imputs
		if len(categoriesPerSentence) == 0:
			categoriesForAllSents.append('**unknown**')
		
		# Save categories
		else:
			categoriesForAllSents.append(', '.join(categoriesPerSentence))

	return categoriesForAllSents

			
def getNameEntity(entities):
	count=0
	for tpl in entities:
		count+=1
		print '**********Sentence %s' % count
		print tpl
		if len(tpl) == 0:
			print '*empty*'
			continue
		for word in tpl:
			if word[0] == 'GPE':
				print 'Geographic'
			elif word[0] == 'ORGANIZATION':
				print 'Organization'
			elif word[0] == 'PERSON':
				print 'Person'


if __name__ == "__main__":
    main()