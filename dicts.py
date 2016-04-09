# All the nouns and verb POS tags found in the Penn Treebank
dictNVs =       {
                    'NN':True, 
                    'NNP':True, 
                    'NNPS':True, 
                    'NNS':True, 
                    'VB':True,
                    'VBD':True,
                    'VBG':True,
                    'VBN':True,
                    'VBP':True,
                    'VBZ':True,
                }


# Contains keywords with very high association to specific categories
dictCategories = {
                    # Music keywords
                    'sing': 'music', 
                    'sings': 'music',
                    'singer': 'music', 
                    'album': 'music', 
                    'albums': 'music',
                    'rock': 'music',
                    'pop': 'music',
                    'dance': 'music',
                    'music': 'music',

                    # Movie keywords
                    'actor': 'movies', 
                    'actress': 'movies',
                    'directs': 'movies', 
                    'director': 'movies', 
                    'directors': 'movies', 
                    'directed': 'movies', 
                    'star': 'movies', 
                    'stars': 'movies',
                    'movie': 'movies', 
                    'movies': 'movies',
                    'oscar': 'movies',
                    'oscars': 'movies',

                    # Geography keywords
                    'where': 'geography', 
                    'capital': 'geography', 
                    'country': 'geography', 
                    'countries': 'geography', 
                    'continent': 'geography',
                    'continents': 'geography', 
                    'capital': 'geography', 
                    'capitals': 'geography',
                    'state': 'geography',
                    'border': 'geography', 
                    'borders': 'geography',
                    'river': 'geography', 
                    'lake': 'geography',
                    'sea': 'geography',
                    'ocean': 'geography',
                    'oceans': 'geography',
                    'mountain': 'geography',
                    'mountains': 'geography',
                }