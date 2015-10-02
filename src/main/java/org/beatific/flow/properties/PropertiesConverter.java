package org.beatific.flow.properties;

import org.beatific.flow.util.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertiesConverter {

	@Autowired
	private Environment props;
	
	public String convert(String str) {
		
		String key = key(str);
		if(key == null) return str;
		return (String)props.getProperty(key);
	}
	
	private String key(String str) {
		Parser parser = new Parser("\\$\\{(\\w*)\\}");
		return parser.parseFirstOne(str);
	}
}
