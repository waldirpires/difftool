package com.webapp.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.webapp.demo.model.DiffDocument;
import com.webapp.demo.model.DiffOccurrence;
import com.webapp.demo.model.DiffProcessorReport;

/**
 * DiffTool processor service class
 */
@Service
public class DiffProcessorServiceImpl implements DiffProcessorService{

	// executes the diff operation over a document
	@Override
	public DiffProcessorReport executeDiffTool(DiffDocument doc) {

		DiffProcessorReport report = new DiffProcessorReport();
		report.setId(doc.getId());
		// case 1: if equal, return that both are equal
		// case 2: if not equal and different sizes, return that they are different
		report.setEqual(doc.isEqual());
		// if not equal in size, return differentes (position and size only)
		if (!doc.isEqual() && doc.isSameSize())
		{
		// if same size, provide info about the diffs
			List<DiffOccurrence> diffIndices = findDiffIndexes(doc);
			report.setOccurrences(diffIndices);
		}
		return report;
	}

	/**
	 * finds the Diff indices (position and size)
	 * @param diffDoc
	 * @return list of Diff occurrences with position and sizes of each difference
	 */
	private List<DiffOccurrence> findDiffIndexes(DiffDocument diffDoc) {
		String s1 = diffDoc.getLeft();
		String s2 = diffDoc.getRight();
	    List<DiffOccurrence> indexes = new ArrayList<>();
	    DiffOccurrence diffOccurrence = null;
	    
	    // O(n), where n = size of string
	    for( int i = 0; i < s1.length() && i < s2.length(); i++ ) {
	    	// if a difference is found between string values and if there are no diff occurence
	        if( s1.charAt(i) != s2.charAt(i) && diffOccurrence == null) {
	        	// create a new one
	        	diffOccurrence = new DiffOccurrence(i);
	        	indexes.add(diffOccurrence);
	        	diffOccurrence.updateSize();
	        } // if they are different and if it is an existing occurrence, add the size
	        else if( s1.charAt(i) != s2.charAt(i) && diffOccurrence != null) {
	        	diffOccurrence.updateSize();
	        } 
	        // if a similarity has ben found and there was a previous diff occurence
	        else if( s1.charAt(i) == s2.charAt(i) && diffOccurrence != null)
	        {
	        	// clear the diff occurrece in case it finds a new one
	        	diffOccurrence = null;
	        }
	    }
	    return indexes;
	}	
}
