package com.webapp.demo.utils;

public class StringUtils {

	public static boolean isEmpty(String s)
	{
		return s == null || s.trim().length() == 0;
	}
}
