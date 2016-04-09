import nltk.chunk
from dicts import dictNVs, dictCategories

# Determines category corresponding to each sentence based on keywords and name entities
# Accepts list of tagged sentences
# Returns list of strings 
def getCategories(taggedSentences):
    categoriesForAllSents = []             # Stores the determined categories for each sentence 

    # Process each tagged sentence on at a time to determine category (movies, music, geography)
    for sentence in taggedSentences:
        # sentence = [(tpl), (tpl), ...]
        # tpl = ('word', 'POS tag')
        # For each word in current sentence, save only nouns and verbs
        NounsAndVerbs = [tpl for tpl in sentence if dictNVs.get(tpl[1])]    # [(tpl), (tpl), ...]

        # Use nouns/verbs to see if they are category keywords
        categoriesPerSentence = []          # Stores all categories for a single sentence
        for tpl in NounsAndVerbs:           # For each noun/verb in a single sentence
            c = dictCategories.get(tpl[0])  # Find word in dictCategories and get value, c = None if not found
            if c != None:                   # If noun/verb is in dictCategories, then...
                categoriesPerSentence.append(c)  # Save the category to the list

        # For ambiguous sentence that does not contain any category keywords,
        # process the sentence for name entities to determine category
        if len(categoriesPerSentence) == 0:
            categoriesForAllSents.append(getCategoryByNameEntity(sentence))
        
        # For sentences that contains at least one keyword found,
        # save the category for later
        else:
            categoriesForAllSents.append(categoriesPerSentence[0])	#save only first category found
            # categoriesForAllSents.append(', '.join(categoriesPerSentence)) #save all categories found

    return categoriesForAllSents 


# Determines the category of a sentence based on name entity recognition
# Accepts a list of tuples representing a tagged sentenced [('word', 'POS tag'), (), ...]
# Returns a string representing the category of the sentence
def getCategoryByNameEntity(sentence):
    # Get name entities in sentence, ignore remaining words
    entities = getNameEntities(sentence)

    # If sentence did not have any NEs, then sentence is still ambiguous
    if len(entities) == 0:
        return 'ambiguous'

    orgsProof = []
    sentenceWordsOnly = []
    for word in entities:
        if word[0] == 'GPE':                    # If NE of word is geo-political entity
            return 'geographic'                 # then sentence is related to geography
        elif word[0] == 'PERSON':               # If NE of word is person
            return 'music, movies'              # then sentence is related to either music or movies
        elif word[0] == 'ORGANIZATION':         # If NE of word is organization
            if len(sentenceWordsOnly) == 0:     # If first time in this branch and list is still empty
                sentenceWordsOnly = [x[0] for x in sentence] # then initialize by getting words in sentence
            index = sentenceWordsOnly.index(word[1])   # Get the position of the current word in the sentence
            if sentence[index-1][1] == 'DT':           # If preceding word before current word is determiner
                orgsProof.append(True)                 # Save as true
            else:                                      # If preceding word before current word is not determiner
                orgsProof.append(False)                # Save as false

    # When all entities in sentence are marked ORGANIZATION and preceded by a DT, 
    # then they are related to geophraphic (e.g. 'the Atlantic')
    if sum(orgsProof) == len(orgsProof):            # [True, True] == 2
        return 'geographic'
    # When at least one entity does not follow the above rule, they are related to music or movies
    # Assumption is that titles of songs/movies or people are not preceded by a determiner
    # (e.g. we typically say just 'Beyonce' instead of 'the Beyonce')
    else:                                           # [False, True] != 2
        return 'music, movies'


# Finds nouns that are classified as name entity (organization, person, geo-political entity)
# Accepts a list of tuples representing a tagged sentenced [('word', 'POS tag'), (), ...]
# Returns a list of tuples containing only the name entities found in the sentence
def getNameEntities(sentence):
    entities = ()
    # Get name enties from each sentence and save the name entities
    for word in nltk.ne_chunk(sentence):
        #  If word is a name entity (Name entities have type nltk.tree.Tree, other words are tuples)
        if type(word) is nltk.tree.Tree:
            newline =  str(word)[1:-1].split()          # Convert (GPE France/NNP) to ['GPE', 'France/NNP']
            newline2 = newline[1].split('/')            # Convert 'France/NNP' to ['France', 'NNP']
            finalline = [newline[0], newline2[0], newline2[1]]	# Combine to produce ['GPE', 'France', 'NNP']
            entities = entities + (finalline,)          # Store the name entity to be returned later
        # Else go to next word
    return entities