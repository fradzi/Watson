'''
Revanth Reddy
Filip Radzikowski
Watson Jr.
'''
import nltk
import sqlite3


def main():
	inputs = parseLines()
	tagNameEntity(inputs)


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





if __name__ == "__main__":
    main()