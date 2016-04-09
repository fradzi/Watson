'''
Revanth Reddy
Filip Radzikowski
Watson Jr.
'''
import sys
import sqlite3
import nltk.tokenize
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


def main(argv):

    # For debugging in SublimeText
    if not argv:
        argv.append('input.txt')

    # Argv error checking for single argument
    if len(argv) != 1:
        print 'error: missing input filename argument'
        print 'usage: python main.py input.txt'
        sys.exit(2)

    # Get sentences from input file
    rawSentences = parseLines(argv[0])

    # Pass the sentences through a POS tagger
    taggedSentences = getPOSTags(rawSentences)

    # Determine catagories of each sentence
    categories = getCategories(taggedSentences)

    # Print output to the console
    printOutput(rawSentences, categories)
    

# Open file and parse each line 
# Accepts a string with the filename
# Returns a list of strings with the parsed sentences
def parseLines(inputFileName):
    try:
        with open(inputFileName, 'r') as f:
            data = [line.strip() for line in f]
    except IOError as e:
        print "I/O error({0}): {1}".format(e.errno, e.strerror)
        sys.exit(2)

    return data


# Scans each sentence and assigns a Part of Speech (POS) tag to each word
# Accepts a list of strings containing the raw untagged sentences
# Returns a list of list of tuples [[(word, POS), (word, POS), ...], [(), (), ...], ...]
def getPOSTags(sentences):
    taggedSentences = []
    for s in sentences:
        taggedSentences.append(nltk.pos_tag(nltk.word_tokenize(s)))
    return taggedSentences


# Displays a formatted output to the console
# Accepts a list of raw sentence strings and categories for each sentence
def printOutput(sentences, categories):
    for s,c in izip(sentences, categories):
        print '<QUESTION> ' + s
        print '<CATEGORY> ' + c
        print '<PARSETREE> '
        printTree(s)
        print '\n=====================================================\n'


# Uses the Stanford parser to create a parse tree and output to the console
# Accepts a string with a raw sentence
def printTree(sentence):
    # Extract the stanford-parser-3.3.0-models.jar file in order to set the model_path
    parser = stanford.StanfordParser(model_path= USERSTANFORDDIR + "/stanford-parser/stanford-parser-3.3.0-models/edu/stanford/nlp/models/lexparser/englishPCFG.ser.gz")

    # Parse single sentence to produce a tree object
    tree = parser.raw_parse(sentence)

    # Parse multiple sentences at same time
    # tree = parser.raw_parse_sents(("Hello, My name is Melroy.", "What is your name?"))

    # Draw tree graphic
    for branch in tree: 
        for node in branch:
            print str(node)    # show nested tree in console
            # node.draw()      # show tree graphic image in new window


if __name__ == "__main__":
    main(sys.argv[1:])