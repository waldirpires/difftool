package com.webapp.demo.service;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import com.webapp.demo.model.DiffDocument;
import com.webapp.demo.model.DiffProcessorReport;

@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@RunWith(SpringRunner.class)
@DataJpaTest
public class DiffDocumentServiceTest {

	@Autowired
	private DiffDocumentService diffDocumentService;
	
	private Long id;	

	@Test
	public void test_DoDiff_equalContent_shouldBeEqual()
	{
		String right = "left";
		String left = "left";
		id = new Long(2);
		diffDocumentService.saveDocument(id , left, right);
		
		DiffProcessorReport report = diffDocumentService.doDiff(id);
		Assert.assertThat(report.isEqual(), CoreMatchers.equalTo(true));
	}

	@Test
	public void test_DoDiff_DifferentContentSameSize_shouldNotBeEqual()
	{
		String right = "bGVmdA==";
		String left = "bGVmdA=!";
		id = new Long(2);
		diffDocumentService.saveDocument(id , left, right);
		
		DiffProcessorReport report = diffDocumentService.doDiff(id);
		
		Assert.assertThat(report.isEqual(), CoreMatchers.equalTo(false));
		Assert.assertThat(report.getOccurrences().size(), CoreMatchers.equalTo(1));
	}

	@Test (expected = DiffDocumentException.class)
	public void test_DoDiff_incompleteDocRight_shouldThrowError()
	{
		String right = null;
		String left = "bGVmdA=!";
		id = new Long(2);
		diffDocumentService.saveDocument(id , left, right);
		
		diffDocumentService.doDiff(id);		
	}
	
	@Test (expected = DiffDocumentException.class)
	public void test_DoDiff_incompleteDocLeft_shouldThrowError()
	{
		String left = null;
		String right = "bGVmdA=!";
		id = new Long(2);
		diffDocumentService.saveDocument(id , left, right);
		
		diffDocumentService.doDiff(id);		
	}
	
	@Test (expected = DiffDocumentException.class)
	public void test_DoDiff_incompleteDocRightAndLeft_shouldThrowError()
	{
		String right = null;
		String left = null;
		id = new Long(2);
		diffDocumentService.saveDocument(id , left, right);
		
		diffDocumentService.doDiff(id);		
	}
	
	
	@Test
	public void test_DoDiff_DifferentContentDifferentSizes_shouldNotBeEqual()
	{
		String right = "bGVmdA==";
		String left = "bGVmdA=!xx";
		id = new Long(2);
		diffDocumentService.saveDocument(id , left, right);
		
		DiffProcessorReport report = diffDocumentService.doDiff(id);
		
		Assert.assertThat(report.isEqual(), CoreMatchers.equalTo(false));
		Assert.assertThat(report.getOccurrences(), CoreMatchers.nullValue());
	}

	@Test
	public void test_SaveDocumentWithLeft_shouldReturnSuccess()
	{
		String right = null;
		String left = "left";
		id = new Long(2);
		DiffDocument doc = diffDocumentService.saveDocument(id , left, right);
		Assert.assertThat(doc.getId(), CoreMatchers.equalTo(id));
		Assert.assertThat(doc.getLeft(), CoreMatchers.equalTo(left));
		Assert.assertThat(doc.getRight(), CoreMatchers.nullValue());
	}

	@Test
	public void test_SaveDocument_fromLeftToRight_shouldReturnSuccess()
	{
		String right = null;
		String left = "left";
		id = new Long(2);
		DiffDocument doc = diffDocumentService.saveDocument(id , left, right);
		Assert.assertThat(doc.getId(), CoreMatchers.equalTo(id));
		Assert.assertThat(doc.getLeft(), CoreMatchers.equalTo(left));
		Assert.assertThat(doc.getRight(), CoreMatchers.nullValue());

		right = "right";
		left = null;
		doc = diffDocumentService.saveDocument(id , left, right);
		Assert.assertThat(doc.getId(), CoreMatchers.equalTo(id));
		Assert.assertThat(doc.getLeft(), CoreMatchers.equalTo("left"));
		Assert.assertThat(doc.getRight(), CoreMatchers.equalTo("right"));
	}

	@Test
	public void test_SaveDocument_fromRightToLeft_shouldReturnSuccess()
	{
		String right = "test";
		String left = null;
		id = new Long(2);
		DiffDocument doc = diffDocumentService.saveDocument(id , left, right);
		Assert.assertThat(doc.getId(), CoreMatchers.equalTo(id));
		Assert.assertThat(doc.getRight(), CoreMatchers.equalTo(right));
		Assert.assertThat(doc.getLeft(), CoreMatchers.nullValue());

		right = null;
		left = "test2";
		doc = diffDocumentService.saveDocument(id , left, right);
		Assert.assertThat(doc.getId(), CoreMatchers.equalTo(id));
		Assert.assertThat(doc.getLeft(), CoreMatchers.equalTo("test2"));
		Assert.assertThat(doc.getRight(), CoreMatchers.equalTo("test"));
	}

	@Test
	public void test_SaveDocumentWithRight_shouldReturnSuccess()
	{
		String right = "right";
		String left = null;
		Long id = new Long(2);
		DiffDocument doc = diffDocumentService.saveDocument(id , left, right);
		Assert.assertThat(doc.getId(), CoreMatchers.equalTo(id));
		Assert.assertThat(doc.getLeft(), CoreMatchers.nullValue());
		Assert.assertThat(doc.getRight(), CoreMatchers.equalTo(right));
	}

	@Test
	public void test_SaveDocumentWithLeftAndRight_shouldReturnSuccess()
	{
		String right = "right";
		String left = "left";
		Long id = new Long(2);
		DiffDocument doc = diffDocumentService.saveDocument(id , left, right);
		Assert.assertThat(doc.getId(), CoreMatchers.equalTo(id));
		Assert.assertThat(doc.getLeft(), CoreMatchers.equalTo(left));
		Assert.assertThat(doc.getRight(), CoreMatchers.equalTo(right));
	}

	@Test (expected = DiffDocumentException.class)
	public void test_SaveDocumentEmptySides_shouldReturnException()
	{
		String right = null;
		String left = null;
		id = new Long(2);
		diffDocumentService.saveDocument(id , left, right);
	}

	@Test (expected = DiffDocumentException.class)
	public void testDelete()
	{
		String right = "right";
		String left = "left";
		Long id = new Long(2);
		diffDocumentService.saveDocument(id , left, right);

		diffDocumentService.delete(id);
		diffDocumentService.getDocument(id);
	}
	
}
