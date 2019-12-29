package com.upb.factchecker.model;

//Model for the triples 
public class Triples {
	
	private String subject;
	
	private String predicate;
	
	private String object;

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getPredicate() {
		return predicate;
	}

	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	@Override
	public String toString() {
		return "Sentence [subject=" + subject + ", predicate=" + predicate + ", object=" + object + "]";
	}
	
	

}
