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

import com.upb.factchecker.model.Fact;

public class IOOp {
	
	//Read all the facts from the file and add it as a list of facts
	public static List<Fact> readFactsFromCSV() {

		String file = "./src/main/resources/SNLP2019_test.tsv";
		List<Fact> allFacts = new ArrayList<Fact>();
		BufferedReader reader = null;
		String line = "";
		String splitBy = "\t";

		try {
			reader = new BufferedReader(
				    new InputStreamReader(new FileInputStream(file),"UTF-8"));
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
	
	public static void prepareResult(List<Fact> allFacts) {

		String factURI = "http://swc2017.aksw.org/task2/dataset/";//the Fact URI
		String propURI = "http://swc2017.aksw.org/hasTruthValue";//the property URI
		Model model = ModelFactory.createDefaultModel();
		FileOutputStream resFile = null;
		
		try {
			resFile = new FileOutputStream("./src/main/resources/output.ttl", false);//Prepare the outputStream
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};

		for (Fact fact : allFacts) {

			Resource node = model.createResource(factURI + fact.getFactID());
			Property prop = model.createProperty(propURI);
			Literal value = model.createTypedLiteral(new Double(fact.getTruthValue()));

			model.add(node, prop, value);//Add the triples to a model
		}
		model.write(resFile, "TURTLE");//write the model into a turtle file 
	}


}
