package com.webapp.demo.service;

import com.webapp.demo.model.DiffDocument;
import com.webapp.demo.model.DiffProcessorReport;

public interface DiffProcessorService {

	public DiffProcessorReport executeDiffTool(DiffDocument doc);
}
