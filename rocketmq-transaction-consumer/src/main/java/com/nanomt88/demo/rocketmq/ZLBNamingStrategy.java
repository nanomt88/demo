package com.nanomt88.demo.rocketmq;

import org.hibernate.cfg.DefaultNamingStrategy;

public class ZLBNamingStrategy extends DefaultNamingStrategy {

	private static final long serialVersionUID = 1L;

	private String splitAndDown(String original) {
		if (original == null) {
			return null;
		}
		String[] words = original.split("(?=[A-Z])");
		StringBuffer sb = new StringBuffer();
		for (String word : words) {
			if (word.length() > 0)
				sb.append(word.toLowerCase() + "_");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	@Override
	public String classToTableName(String className) {
		return "t_" + splitAndDown(className);
	}

	@Override
	public String propertyToColumnName(String propertyName) {
		return splitAndDown(propertyName);
	}
}
