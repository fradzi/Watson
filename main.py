'''
Revanth Reddy
Filip Radzikowski
Watson Jr.
'''
import nltk
import sqlite3



def main():
	# Get sentences from input file
    inputs = parseLines()
    
    # Pass the sentences through a POS tagger
    taggedSentences = getPOSTags(inputs)

    # Extract NE from sentences and contain into list of tuples
    entities = extract_entities(taggedSentences)

    # Determine what catagory each sentence is in
    # getSentenceCategory(entities);


def parseLines():
    with open('sample_sentences.txt', 'r') as f:
        data = [line.strip()[5:] for line in f]
    return data


def getPOSTags(sentences):
	taggedSentences = []
	for s in sentences:
		taggedSentences.append(nltk.pos_tag(nltk.word_tokenize(s)))
	return taggedSentences


def extract_entities(sentences):
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


def getSentenceCategory(entities):
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