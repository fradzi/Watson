# WatsonJr
---
####Description:

Build scaled down version of such a question/answering (Q/A) system in the form of baby Watson Jr learning to interpret inputs using Natural Language Processing (NLP). Essentially Q/A systems retrieve information from a number of repositories of information, including databases. Watson Jr will gain knowledge domains. Parse the query, translate it into SQL, and use the database to compute the result. Infer the domain of the query from the query itself, as big daddy Watson would do. At the end of the project, we should be able to retrieve answers to questions such as *When did Streep win an oscar?* and *Who directed Silence of the Lamb?*

---
####Grammar:
Parse only question sentences in the form of yes-no questions, like Is MightyAphrodite by Kubrick?, and wh-questions, like Which actress won the oscar in 2012? No statements or imperative sentences will be analyzed.

**NPs** can be:
1. Proper nouns: we will simplify people names by only using last names when they exist e.g. Swift, Kubrik, or their only name Madonna; for Lady Gaga, let’s use Gaga. One problem: among actors, there are two Hepburns (Audrey and Katherine), and two Kelly (Gene and Grace), and perhaps others. Don’t worry about this for Part 1, just use the last name;2. Titles of movies, albums or tracks: when they are not NPs, for example I Miss You, you can simplify them by creating a new proper noun by concatenating the words IMissYou;3. Common nouns such as continent, capital, border, river, mountain, actor, movie, track, singer, artist, album, rock, pop, dance, etc;4. Note that oscar is not capitalized so it is treated as a common noun, but feel free to experiment with capitalization:5. Nominals such as mountain chain, rock album6. Complex NPs with determiners, adjectives and numerals: the best movie, the highest mountain, thelast track;7. Complex NPs followed by one or two prepositional phrases: movie by Kubrik with Nicholson8. Wh-NPs that include a so-called wh-word, i.e., who, which, when. Note that who is a pronoun, which is used here as a determiner, when an adverb.**VPs** include:1. An NP only, e.g., win the oscar;2. A prepositional phrase only, e.g., in which continent does Canada lie; did Neeson star in Schindler’s List;3. An NP and one or two prepositional phrases, win the oscar in 2012, release a new album in february in Italy.

---
####Part 1:

- Choose one of the NLP parsers 
- Parse all the sentences listed. 
- Develop a module that infers the category (world geography, music, movies) of the question.

---
####Part 2:

- Derive a semantic representation from the syntactic trees returned by the parser. The goal is to use tree traversal, semantic attachment, and lambda reduction to some extent to implement the algorithm.
- Use SQLite and translate the language query into an SQL query, then compute the answer from those toy databases. 

Note: Although, we did categorization for Part 1, we will only focus on the movie database queries for Part 2.
 
Input:
	
	Was Winslet born in England?

Output:	<QUERY>	Was Winslet born in England?	<SQL>	SELECT COUNT(*) FROM Person as P WHERE P.name LIKE "%Winslet%"and P.pob LIKE "%England%";	<ANSWER>	Yes
      
---
####Part 3:

Write a brief report summarizing the components and implementation of the project. Include results of various inputs processed by the program.

---
####Constraints:

- Work in pairs
- Should be developed in Java or Python
- Use one of the following parsers: Stanford (Java), OpenNLP (Java), or NLTK (Python)
