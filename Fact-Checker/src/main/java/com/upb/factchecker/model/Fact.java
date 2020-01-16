package com.upb.factchecker.model;

//Model for the individual facts 
public class Fact {
	
	private int factID;
	
	private String statement;
	
	private Double truthValue = -1.0;

	public int getFactID() {
		return factID;
	}

	public void setFactID(int factID) {
		this.factID = factID;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public Double getTruthValue() {
		return truthValue;
	}

	public void setTruthValue(Double truthValue) {
		this.truthValue = truthValue;
	}

	@Override
	public String toString() {
		return "Fact [factID=" + factID + ", statement=" + statement + ", truthValue=" + truthValue + "]";
	}
	
	
	

}
