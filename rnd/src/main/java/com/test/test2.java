package com.test;

import java.io.File;
import java.io.FilenameFilter;

public class test2 {

	public static void main(String[] args) {
		StringBuffer sb = new StringBuffer();
		sb.append("abc");
		System.out.println(sb.toString());
		foo(sb);
		System.out.println(sb.toString());

	}

	private static void foo(StringBuffer sb) {
		sb.append("xyz");
	}

}
