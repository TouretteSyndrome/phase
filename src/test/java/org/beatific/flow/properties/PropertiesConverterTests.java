package org.beatific.flow.properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"classpath:applicationContext*.xml"})
public class PropertiesConverterTests {

	@Value("${basePackage}")
	private String basePackage;
	
	@Autowired
	public PropertiesConverter converter;
	
	@Test
	public void testConvert() {
		
		assertThat(converter.convert("${basePackage}"), is(basePackage));
		
	}
}
