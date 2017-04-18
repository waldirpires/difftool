package com.webapp.demo.model;

import java.util.List;

/**
 * Stores a Diff process report
 *
 */
public class DiffProcessorReport {

	private Long id;
	private boolean isEqual;
	private List<DiffOccurrence> occurrences;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public boolean isEqual() {
		return isEqual;
	}
	public void setEqual(boolean isEqual) {
		this.isEqual = isEqual;
	}

	public void setOccurrences(List<DiffOccurrence> occurrences) {
		this.occurrences = occurrences;
	}
	
	public List<DiffOccurrence> getOccurrences() {
		return occurrences;
	}
}
