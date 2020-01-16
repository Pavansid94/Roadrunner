package com.upb.factchecker.io;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.slf4j.LoggerFactory;

import com.upb.factchecker.FactCheckerApplication;
import com.upb.factchecker.model.Fact;

public class IOOp {
	
	private static final String FACT_URI = "http://swc2017.aksw.org/task2/dataset/";
	private static final String PROP_URI =	"http://swc2017.aksw.org/hasTruthValue";
	
	private static final String TRAIN_OP = "./src/main/resources/output_train.ttl";
	private static final String TEST_OP =	"./src/main/resources/output_test.ttl";
	
	private static final String UTF ="UTF-8";
	private static final String TRAIN =	"train";
	private static final String TURTLE ="turtle";
	
	private static org.slf4j.Logger log = LoggerFactory.getLogger(IOOp.class);
	
	//Read all the facts from the file and add it as a list of facts
	public static List<Fact> readFactsFromCSV(String fileName) {
		
		log.debug("Reading Facts from the File...");
		
		String file = fileName;
		List<Fact> allFacts = new ArrayList<Fact>();
		BufferedReader reader = null;
		String line = "";
		String splitBy = "\t";

		try {
			reader = new BufferedReader(
				    new InputStreamReader(new FileInputStream(file),UTF));
			reader.readLine();// skip the header
			while ((line = reader.readLine()) != null) {
				String[] data = line.split(splitBy);
				Fact fact = null;

				if (data != null) {
					fact = new Fact();
					fact.setFactID(Integer.parseInt(data[0]));
					fact.setStatement(data[1]);
					//fact.setTruthValue(Double.parseDouble(data[2]));
				}
				allFacts.add(fact);//add the facts into a list
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return allFacts;
	}
	
	public static void prepareResult(List<Fact> allFacts,String fileName) {
		
		log.debug("Preparing the output ttl file...Please wait...");

		Model model = ModelFactory.createDefaultModel();
		FileOutputStream resFile = null;
		
		try {
			if(fileName.contains(TRAIN))
				resFile = new FileOutputStream(TRAIN_OP, false);//Prepare the outputStream
			else
				resFile = new FileOutputStream(TEST_OP, false);//Prepare the outputStream
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};

		for (Fact fact : allFacts) {

			Resource node = model.createResource(FACT_URI + fact.getFactID());
			Property prop = model.createProperty(PROP_URI);
			Literal value = model.createTypedLiteral(new Double(fact.getTruthValue()));

			model.add(node, prop, value);//Add the triples to a model
		}
		model.write(resFile, TURTLE);//write the model into a turtle file 
		
		log.debug("turtle output file generated...");
	}


}
