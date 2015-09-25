package org.beatific.flow.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

	private Pattern pattern;
	
	public Parser(String pattern) {
		this.pattern = Pattern.compile(pattern);
	}
	
	public List<String[]> parseAll(String str) {
		
		List<String[]> result = new ArrayList<String[]>();
		
		Matcher matcher = pattern.matcher(str);
		while(matcher.find()) {
			List<String> group = new ArrayList<String>();
			for(int i = 1 ; i <= matcher.groupCount(); i++) {
				group.add(matcher.group(i));
			}
			result.add(group.toArray(new String[0]));
		}
		return result;
	}
	
	public String[] parse(String str) {
		Matcher matcher = pattern.matcher(str);
		List<String> group = new ArrayList<String>();
		
		if(matcher.find()) {
			for(int i = 1 ; i <= matcher.groupCount(); i++) {
				group.add(matcher.group(i));
			}
		}
		return group.toArray(new String[0]);
	}
	
	public String[] parseLast(String str) {
		Matcher matcher = pattern.matcher(str);
		List<String> group = new ArrayList<String>();
		
		while(matcher.find()) {
			group.clear();
			for(int i = 1 ; i <= matcher.groupCount(); i++) {
				group.add(matcher.group(i));
			}
		}
		return group.toArray(new String[0]);
	}

	public String parseOne(String str) {
		Matcher matcher = pattern.matcher(str);
		
		if(matcher.find()) 
			return matcher.group(1);
		
		return null;
	}
	
	public String parseFirstOne(String str) {
		Matcher matcher = pattern.matcher(str);
		
		while(matcher.find()) {
			for(int i = 1 ; i <= matcher.groupCount(); i++) {
				String match = matcher.group(i);
			    if("".equals(match))continue;
			    return match;
			}
		}
		
		return null;
	}
	
	public String parseLastOne(String str) {
		Matcher matcher = pattern.matcher(str);
		
		String result = null;
		
		while(matcher.find())
			result = matcher.group(1);
		
		return result;
	}
}
