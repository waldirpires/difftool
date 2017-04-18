package com.webapp.demo.model;

import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.webapp.demo.service.DiffDocumentException;

public class DiffDocumentTest {

	private DiffDocument doc;
	
	@Before
	public void setup()
	{
		doc = new DiffDocument();
	}

	@After
	public void tearDown()
	{
		doc = null;
	}
	
	@Test
	public void testSetLeft()
	{
		String s = "blah";
		doc.setLeft(s);
		Assert.assertEquals(s.length(), doc.getLengthLeft().intValue());
		
		doc.setLeft("");
		Assert.assertEquals(0, doc.getLengthLeft().intValue());

		doc.setLeft(null);
		Assert.assertEquals(0, doc.getLengthLeft().intValue());
	}

	@Test
	public void testSetLeftNull()
	{
		doc.setLeft(null);
		Assert.assertEquals(0, doc.getLengthLeft().intValue());
	}

	@Test
	public void testSetRight()
	{
		String s = "blah";
		doc.setRight(s);
		Assert.assertEquals(s.length(), doc.getLengthRight().intValue());
		
		doc.setRight("");
		Assert.assertEquals(0, doc.getLengthRight().intValue());

		doc.setRight(null);
		Assert.assertEquals(0, doc.getLengthRight().intValue());
	}

	@Test
	public void testSetRightNull()
	{
		doc.setRight(null);
		Assert.assertEquals(0, doc.getLengthRight().intValue());
	}
	
	@Test
	public void testUpdateSide()
	{
		String value = "test";
		doc.updateSide(DiffDocument.DIFF_SIDE_LEFT, value);
		Assert.assertEquals(value, doc.getLeft());
		Assert.assertEquals(value.length(), doc.getLengthLeft().intValue());
		
		value = "test2";
		doc.updateSide(DiffDocument.DIFF_SIDE_RIGHT, value);
		Assert.assertEquals(value, doc.getRight());
		Assert.assertEquals(value.length(), doc.getLengthRight().intValue());
	}

	@Test
	public void testUpdateSideInvalid()
	{
		String value = null;
		doc.updateSide(DiffDocument.DIFF_SIDE_LEFT, value);
		Assert.assertEquals(value, doc.getLeft());
		Assert.assertEquals(0, doc.getLengthLeft().intValue());
		
		doc.updateSide(DiffDocument.DIFF_SIDE_RIGHT, value);
		Assert.assertEquals(value, doc.getRight());
		Assert.assertEquals(0, doc.getLengthRight().intValue());
		
		doc.updateSide("blah", value);
		Assert.assertEquals(value, doc.getRight());
		Assert.assertEquals(0, doc.getLengthRight().intValue());
	}

	@Test
	public void testUpdateSideInvalid2()
	{
		String value = null;
		doc.updateSide(null, value);
		Assert.assertThat(doc.getLeft(), CoreMatchers.nullValue());
		Assert.assertThat(doc.getRight(), CoreMatchers.nullValue());
	}

	@Test
	public void testIsEqual_LeftNull_shouldReturnFalse()
	{
		doc.setLeft(null);
		doc.setRight("");
		Assert.assertThat(doc.isEqual(), CoreMatchers.equalTo(false));
	}

	@Test
	public void testIsEqual_RightNull_shouldReturnFalse()
	{
		doc.setLeft("");
		doc.setRight(null);
		Assert.assertThat(doc.isEqual(), CoreMatchers.equalTo(false));
	}

	@Test
	public void testIsEqual_LeftAndRightNull_shouldReturnFalse()
	{
		doc.setLeft(null);
		doc.setRight(null);
		Assert.assertThat(doc.isEqual(), CoreMatchers.equalTo(false));
	}

	@Test
	public void testIsEqual_LeftAndRightEqual_shouldReturnTrue()
	{
		doc.setLeft("book");
		doc.setRight("book");
		Assert.assertThat(doc.isEqual(), CoreMatchers.equalTo(true));
	}
	
	@Test
	public void testIsEqual_LeftAndRightDifferent_shouldReturnFalse()
	{
		doc.setLeft("book");
		doc.setRight("table");
		Assert.assertThat(doc.isEqual(), CoreMatchers.equalTo(false));
	}
	
	@Test
	public void testIsSameSize()
	{
		doc.setLeft("book");
		doc.setRight("tabl");
		Assert.assertThat(doc.isSameSize(), CoreMatchers.equalTo(true));
		
		doc.setLeft("book");
		doc.setRight("table");
		Assert.assertThat(doc.isSameSize(), CoreMatchers.equalTo(false));
		
		doc.setLeft(null);
		doc.setRight("table");
		Assert.assertThat(doc.isSameSize(), CoreMatchers.equalTo(false));

		doc.setLeft("book");
		doc.setRight(null);
		Assert.assertThat(doc.isSameSize(), CoreMatchers.equalTo(false));

		doc.setLeft(null);
		doc.setRight(null);
		Assert.assertThat(doc.isSameSize(), CoreMatchers.equalTo(false));
		
	}
	
	@Test
	public void test_isComplete()
	{
		doc.setLeft("book");
		doc.setRight("tabl");
		Assert.assertThat(doc.isComplete(), CoreMatchers.equalTo(true));
		
		doc.setLeft(null);
		doc.setRight("table");
		Assert.assertThat(doc.isComplete(), CoreMatchers.equalTo(false));

		doc.setLeft("book");
		doc.setRight(null);
		Assert.assertThat(doc.isComplete(), CoreMatchers.equalTo(false));

		doc.setLeft(null);
		doc.setRight(null);
		Assert.assertThat(doc.isComplete(), CoreMatchers.equalTo(false));
		
	}
}
