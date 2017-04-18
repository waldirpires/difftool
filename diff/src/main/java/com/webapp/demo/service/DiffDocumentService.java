package com.webapp.demo.service;

import com.webapp.demo.model.DiffDocument;
import com.webapp.demo.model.DiffProcessorReport;

public interface DiffDocumentService {

	DiffDocument getDocument(Long id);

	DiffProcessorReport doDiff(Long id);

	DiffDocument saveDocument(Long id, String left, String right);

	boolean delete(Long id);

}
