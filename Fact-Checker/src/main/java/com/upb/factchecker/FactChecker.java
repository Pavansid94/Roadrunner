package com.upb.factchecker;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.upb.factchecker.model.Fact;
import com.upb.factchecker.model.Triples;
import com.upb.factchecker.util.CharsetUtil;

import edu.stanford.nlp.ie.util.RelationTriple;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.naturalli.NaturalLogicAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.util.CoreMap;

public class FactChecker {
	
	private static final String BASE_URL = "http://en.wikipedia.org/wiki/";// baseURL for the wiki
	private static final String DUCK_SRCH_URL =	"https://duckduckgo.com/html?q=";// URL for the duckSearch

	// Fact checking Implementation using Wikipedia Scraping
	public static void checkFacts(List<Fact> allFacts, List<Triples> allTriplets) {

		for (int index = 0; index < allTriplets.size(); index++) {
			Boolean present = false;
			Triples triple = allTriplets.get(index);
			String url = BASE_URL + ((triple.getSubject().length() == 1) ? triple.getSubject()
					: triple.getSubject().replace(" ", "_"));// form the url for the wiki
			org.jsoup.nodes.Document doc = null;
			try {
				doc = Jsoup.connect(url).get();
			} catch (IOException e) {
				e.printStackTrace();
				String gurl = DUCK_SRCH_URL + triple.getSubject().replace(" ", "+") + "+"
						+ triple.getPredicate().replace(" ", "+") + "+" + triple.getObject().replace(" ", "+");
				doc = duckSearch(gurl);
			}
			try {
				if (doc != null && doc.hasText()) {
					Elements paragraphs = doc.select(".mw-body-content p");// get the paragraph content of the body section
					Iterator<Element> itr = paragraphs.iterator();

					while (itr.hasNext()) {
						if (present)
							break;
						Element p = (Element) itr.next();
						String object = triple.getObject();
						List<Node> allNodes = p.childNodes();// get all the child nodes
						for (Node node : allNodes) {
							String pData = node.toString();
							if (pData.contains(object)) {
								present = true;// check if the object is contained
								break;
							}
						}
					}
				}

			} catch (Exception ex) {
				continue;// Ignore the exception and continue the execution
			} finally {
				if (present)
					allFacts.get(index).setTruthValue(1.0);// set truth value to 1 if the object is contained
			}
		}

	}

	public static Triples formTriples(Annotation document) {
		Triples triplets = null;
		String triad = "";
		int triadLength = 0;
		String subject = "";
		String predicate = "";
		String object = "";
		for (CoreMap sentence : document.get(CoreAnnotations.SentencesAnnotation.class)) {
			// Get the OpenIE triples for the sentence
			Collection<RelationTriple> triples = sentence.get(NaturalLogicAnnotations.RelationTriplesAnnotation.class);

			for (RelationTriple triple : triples) {
				triad = triple.toString();
				int length = triad.length();
				if (length > triadLength) {// get the most descripted triple
											// data
					subject = "";
					int subjectlength = triple.canonicalSubject.size();
					if (subjectlength == 1) {// subject is a single word noun
						subject = triple.subject.get(0).originalText().toString();
					} else if (subjectlength < 3) {// subject is a noun of length greater than 1 but less than three
						for (int subjectIndex = subjectlength; subjectIndex > 0; subjectIndex--) {
							subject = subject
									+ triple.subject.get(subjectlength - subjectIndex).originalText().toString() + " ";
						}
					} else if (subjectlength < 6) {// subject is a noun of length greater than 3 but less than six
						if (triad.contains("place")) {
							for (int subjectIndex = subjectlength; subjectIndex > 3; subjectIndex--) {
								subject = subject
										+ triple.subject.get(subjectlength - subjectIndex).originalText().toString()
										+ " ";
							}
						} else {
							for (int subjectIndex = subjectlength; subjectIndex > 2; subjectIndex--) {
								subject = subject
										+ triple.subject.get(subjectlength - subjectIndex).originalText().toString()
										+ " ";
							}
						}

					} else {
						for (int subjectIndex = subjectlength; subjectIndex > 3; subjectIndex--) {
							subject = subject
									+ triple.subject.get(subjectlength - subjectIndex).originalText().toString() + " ";
						}
					}

					subject = subject.trim();
					predicate = triple.relationGloss().toString();
					object = triple.objectGloss().toString();

					// Reverse the subject and object if object contains clause
					// "X's YYY place"
					if ((object.contains("place")) && (object.contains("'"))) {
						String temp = object.split("'")[0];
						object = subject;
						subject = temp;
					}

					// Finally,set the triadlength
					triadLength = length;
				} else
					continue;
			}

			triplets = new Triples();
			triplets.setSubject(CharsetUtil.replaceUniCodeChars(subject));// replace any unicode characters before setting the subject
			triplets.setPredicate(predicate);
			triplets.setObject(CharsetUtil.replaceUniCodeChars(object));// replace any unicode characters before setting the object
		}
		return triplets;
	}

	public static Document duckSearch(String url) {
		String absHref = null;
		Elements doc1 = null;
		Document dx = null;
		try {
			doc1 = Jsoup.connect(url).userAgent("Mozilla").get().select(".result__extras__url a");
		} catch (IOException e) {
			e.printStackTrace();
		}
		int siteCounter = 0;
		for (Element link : doc1) {
			absHref = link.attr("abs:href");
			boolean isFound = absHref.indexOf("en.wikipedia") != -1 ? true : false; // true
			if (isFound) {
				String remainder = absHref.substring(absHref.indexOf("en.wikipedia.org"));
				absHref = "https://" + remainder.replace("%2F", "/");
				try {
					dx = Jsoup.connect(absHref).userAgent("Mozilla").get();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (dx != null && dx.hasText())
				break;
			if (siteCounter == 10)
				return null;
			siteCounter++;
		}
		return dx;

	}

}
