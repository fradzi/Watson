'''
Revanth Reddy
Filip Radzikowski
Watson Jr.
'''
import nltk
import sqlite3
from itertools import izip
from categories import *

import os
from nltk.parse import stanford
from nltk.tag import StanfordPOSTagger


# For user to point to location to their stanford parser and tagger folders
# USERSTANFORDDIR = '/Users/fr/Downloads'  # for Filip
USERSTANFORDDIR = '/Users/revanthreddy/Downloads'  # for Rev
# USERSTANFORDDIR = '/home/mehrdad/Downloads/Stanford/tools/'  # for TA/Linux


# Getting and setting class paths
STANFORDTOOLSDIR = USERSTANFORDDIR + '/stanford-parser'
os.environ['STANFORD_PARSER'] = STANFORDTOOLSDIR+'/stanford-parser.jar'
os.environ['STANFORD_MODELS'] = STANFORDTOOLSDIR+'/stanford-parser-3.3.0-models.jar'
os.environ['CLASSPATH'] = STANFORDTOOLSDIR+'/stanford-parser.jar:' + STANFORDTOOLSDIR + '/stanford-parser-3.3.0-models.jar'


# Stanford tagger (not used, but saving these commands for later if needed)
# tagger = StanfordPOSTagger(USERSTANFORDDIR + '/stanford-postagger/models/english-bidirectional-distsim.tagger', USERSTANFORDDIR + '/stanford-postagger/stanford-postagger.jar')
# tagger = StanfordPOSTagger(USERSTANFORDDIR + '/stanford-postagger/models/english-left3words-distsim.tagger', USERSTANFORDDIR + '/stanford-postagger/stanford-postagger.jar')
# print tagger.tag('Did a movie by Spielberg with Neeson win the oscar for best film?'.split())


def main():
	# Extract the stanford-parser-3.3.0-models.jar file in order to set the model_path
	# parser = stanford.StanfordParser(model_path= USERSTANFORDDIR + "/stanford-parser/stanford-parser-3.3.0-models/edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz")
	# Parse multiple sentences at same time
	# sentences = parser.raw_parse_sents(("Hello, My name is Melroy.", "What is your name?"))

	# # Draw tree graphic
	# for line in sentences: 
	#     for sentence in line:
	# 		print str(sentence)  # show nested tree in console
	#         # sentence.draw()      # show tree graphic image


	# Get sentences from input file
	inputs = parseLines()

	# # Pass the sentences through a POS tagger
	taggedSentences = getPOSTags(inputs)

	# # Extract NE from sentences and contain into list of tuples
	# entities = getNameEntities(taggedSentences)

	# # Determine catagories of each sentence
	# # getSentenceCategory(entities);
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


if __name__ == "__main__":
    main()