package com.upb.factchecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import com.upb.factchecker.io.IOOp;
import com.upb.factchecker.model.Fact;
import com.upb.factchecker.model.Triples;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class FactCheckerApplication {
	
	public static void main(String[] args) {

		//Read all the facts from the file
		List<Fact> allFacts = IOOp.readFactsFromCSV();
		
		//set the annotators for the NLP pipeline
		Properties properties = new Properties();
		properties.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);

		List<Triples> allTriplets = new ArrayList<Triples>();
		
		//Form the facts into triples i.e, subject,predicate and object
		for (Fact fact : allFacts) {
			Annotation document = new Annotation(fact.getStatement());
			pipeline.annotate(document);
			allTriplets.add(FactChecker.formTriples(document));
		}
		
		//Perform Fact Checking
		FactChecker.checkFacts(allFacts, allTriplets);
		
		//Generate the response ttl file
		IOOp.prepareResult(allFacts);
		System.out.println("Just Stalling...");

	}

}
