'''
Revanth Reddy
Filip Radzikowski
Watson Jr.
'''

def main():
	inputs = parseLines()



def parseLines():
	with open('sample_sentences.txt', 'r') as f:
	    data = [line.strip()[5:] for line in f]
	return data


if __name__ == "__main__":
    main()