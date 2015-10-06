package org.beatific.flow.properties;

import java.util.Properties;

import org.beatific.flow.util.Parser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class PropertiesConverter {

	@Autowired
	private Environment[] envs;
	
	@Value("${basePackage}")
	private String basePackage;
	
	@Autowired
    private Properties[] props;
	
	public String convert(String str) {
		
		String key = key(str);
		if(key == null) return str;
		
		String value = null;
		for(Properties prop : props) {
			value = prop.getProperty(key);
			if(value != null)break;
		}
		
		for(Environment env : envs) {
			value = env.getProperty(key);
			if(value != null)break;
		}
		
		return value;
	}
	
	private String key(String str) {
		Parser parser = new Parser("\\$\\{(\\w*)\\}");
		return parser.parseFirstOne(str);
	}
}
