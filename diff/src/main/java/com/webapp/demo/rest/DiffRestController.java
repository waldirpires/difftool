package com.webapp.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.webapp.demo.model.DiffDocument;
import com.webapp.demo.model.DiffProcessorReport;
import com.webapp.demo.service.DiffDocumentService;

/**
 * DiffTool REST controller
 */
@RestController
@RequestMapping("/v1/diff")
public class DiffRestController {

    @Autowired
    private DiffDocumentService diffDocumentService;
	
	@RequestMapping(value = "/hello", produces = "application/json")
    String home() {
        return "{\"msg\": \"Hello REST World!\"}";
    }

    @RequestMapping(value = "/{id}/doc", method = RequestMethod.GET)
	DiffDocument getDoc(@PathVariable String id) {
		return this.diffDocumentService.getDocument(Long.parseLong(id));
	}

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
	DiffProcessorReport doDiff(@PathVariable String id) {
		return this.diffDocumentService.doDiff(Long.parseLong(id));
	}
    
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	boolean deleteDoc(@PathVariable String id) {
		return this.diffDocumentService.delete(Long.parseLong(id));
	}
    
	@RequestMapping(value = "/{id}/{side}", method = RequestMethod.POST, consumes = "text/plain", produces = "application/json")
	public ResponseEntity<DiffDocument> acceptDocTextPlain(@PathVariable("id") Long id, @PathVariable("side") String side, @RequestBody String payload)
	{
		String left = null;
		String right = null;
		if ("left".equals(side))
		{
			left = payload;
		} else if ("right".equals(side))
		{
			right = payload;
		}

		DiffDocument diffDoc = diffDocumentService.saveDocument(id, left, right);
		
		return ResponseEntity.ok(diffDoc);
	}
	
}
