package com.webapp.demo;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;

public class StringUtilsDiffTest {

	@Test
	public void testDiff()
	{
		System.out.println(StringUtils.difference("the book is on the table", "The book was on the table"));
	}
	
//	@Test
//	public void testDiff2()
//	{
//		Patch diff = DiffUtils.diff(getListFor("the book is on the table"), getListFor("The book was on the table"));
//		System.out.println(diff.getDeltas());
//	}
	
	private List<String> getListFor(String data)
	{
		List<String> list = new ArrayList<>();
		list.add(data);
		return list;
	}
	
}
