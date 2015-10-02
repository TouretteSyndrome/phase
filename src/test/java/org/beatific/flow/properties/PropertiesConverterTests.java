package org.beatific.flow.properties;

import org.beatific.flow.flow.Phase1;
import org.beatific.flow.phase.Phase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"classpath:applicationContext*.xml"})
public class PropertiesConverterTests {

	@Autowired
	public PropertiesConverter converter;
	
	@Autowired
	public Phase1 p1;
	
	@Test
	public void testConvert() {
		converter.init();
		Phase phase = p1.getClass().getAnnotation(Phase.class);
		System.out.println(phase.cron());
		
	}
}
