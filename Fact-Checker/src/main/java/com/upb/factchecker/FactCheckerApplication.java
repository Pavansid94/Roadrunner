package com.upb.factchecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.LoggerFactory;

import com.upb.factchecker.io.IOOp;
import com.upb.factchecker.model.Fact;
import com.upb.factchecker.model.Triples;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class FactCheckerApplication {

	public static String trainFile = "./src/main/resources/SNLP2019_training.tsv";
	public static String testFile = "./src/main/resources/SNLP2019_test.tsv";

	private static org.slf4j.Logger log = LoggerFactory.getLogger(FactCheckerApplication.class);

	public static void main(String[] args) {

		execute(trainFile);
		execute(testFile);
		
		System.out.println("Execution Completed...");

	}

	private static void execute(String fileName) {
		
		log.debug("Starting Execution...");
		// Read all the facts from the file
		List<Fact> allFacts = IOOp.readFactsFromCSV(fileName);

		// set the annotators for the NLP pipeline
		Properties properties = new Properties();
		properties.setProperty("annotators", "tokenize,ssplit,pos,lemma,depparse,natlog,openie");
		StanfordCoreNLP pipeline = new StanfordCoreNLP(properties);

		List<Triples> allTriplets = new ArrayList<Triples>();

		// Form the facts into triples i.e, subject,predicate and object
		for (Fact fact : allFacts) {
			Annotation document = new Annotation(fact.getStatement());
			pipeline.annotate(document);
			allTriplets.add(FactChecker.formTriples(document));
		}

		// Perform Fact Checking
		FactChecker.checkFacts(allFacts, allTriplets);

		// Generate the response ttl file
		IOOp.prepareResult(allFacts,fileName);
		// log.debug("Execution Completed...");
	}

}
