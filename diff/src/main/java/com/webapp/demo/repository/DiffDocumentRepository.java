package com.webapp.demo.repository;

import org.springframework.data.repository.CrudRepository;

import com.webapp.demo.model.DiffDocument;

/**
 * Spring Data Repository for CRUD operations in Diff Document
 */
public interface DiffDocumentRepository extends CrudRepository<DiffDocument, Long> {

}
