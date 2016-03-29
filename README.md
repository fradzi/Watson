# WatsonJr
---
####Description:

Build scaled down version of such a question/answering (Q/A) system in the form of baby Watson Jr learning to interpret inputs using Natural Language Processing (NLP). Essentially Q/A systems retrieve information from a number of repositories of information, including databases. Watson Jr will gain knowledge of three categories: world geography, music, and movies. Three toy databases are provided for these three domains. Parse the query, and translate it into SQL. Infer the domain of the query from the query itself, as big daddy Watson would do. At the end of the project, we should be able to retrieve answers to questions such as *When did Streep win an oscar?* and *Who directed Silence of the Lamb?*

---
####Part 1:

- Choose one of the NLP parsers 2. Parse all the sentences listed. 
- Develop a module that infers the category (world geography, music, movies) of the question.

---
####Part 2:

- Derive a semantic representation from the syntactic trees returned by the parser. 

- Use SQLite and translate the language query into an SQL query, then compute the answer from those toy databases. 

To give you a sense of the semantics we’re looking for, here’s an example SQL query in a different domain, winter olympics: 

Input:
	
	Who arrived first in ski jumping?

Query:    SELECT results.winner    FROM results, competitions    WHERE results.comp_id = competitions.comp_id      AND medal = ’gold’      AND name = ’skijumping’;
      
---
####Part 3:

Write a brief report summarizing the components and implementation of the project. Include results of various inputs processed by the program.

---
####Constraints:

- Work in pairs
- Should be developed in Java or Python
- Use one of the following parsers: Stanford (Java), OpenNLP (Java), or NLTK (Python)