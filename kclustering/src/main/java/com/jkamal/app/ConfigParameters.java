package main.java.com.jkamal.app;

import java.util.ArrayList;
import java.util.List;

public class ConfigParameters {
	private List<Integer> dimensions;
	private List<Integer> transactions;
	private List<Integer> clusters;
	private int runs;
	
	public ConfigParameters(){
		dimensions = new ArrayList<Integer>();
		transactions = new ArrayList<Integer>();
		clusters = new ArrayList<Integer>();
		runs = 0;
	}

	public List<Integer> getDimensions() {
		return dimensions;
	}

	public void setDimensions(List<Integer> dimensions) {
		this.dimensions = dimensions;
	}

	public List<Integer> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Integer> transactions) {
		this.transactions = transactions;
	}

	public List<Integer> getClusters() {
		return clusters;
	}

	public void setClusters(List<Integer> clusters) {
		this.clusters = clusters;
	}

	public int getRuns() {
		return runs;
	}

	public void setRuns(int runs) {
		this.runs = runs;
	}
}