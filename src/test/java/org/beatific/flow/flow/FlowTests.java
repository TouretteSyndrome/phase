package org.beatific.flow.flow;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.beatific.flow.repository.Test2Repository;
import org.beatific.flow.repository.TestRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"classpath:applicationContext*.xml"})
public class FlowTests {

	@Autowired
	private Flow flow;
	
	@Autowired
	private TestRepository repository1;
	
	@Autowired
	private Test2Repository repository2;
	
	@Autowired
	private Phase1 p1;
	
	@Autowired
	private Phase2 p2;
	
	@Autowired
	private Phase3 p3;
	
	@Autowired
	private Shared shared;
	
	@SuppressWarnings("unchecked")
	@Test
	public void testFlow() {
		
		flow.setAuto(false);
		
		flow.flow();
		
		Map<String, Object> m1 = (Map<String, Object>)repository1.load(p1);
		Map<String, Object> m2 = (Map<String, Object>)repository2.load(p3);
		
		String name1 = p1.getClass().getName();
		String name2 = p2.getClass().getName();
		String name3 = p3.getClass().getName();
		Phase1 p1 = (Phase1)m1.get(name1);
		Phase2 p2 = (Phase2)m1.get(name2);
		Phase3 p3 = (Phase3)m1.get(name3);
		
		assertThat(p1, is(this.p1));
		assertThat(p2, is(this.p2));
		assertThat(p3, nullValue());
		
		p1 = (Phase1)m2.get(name1);
		p2 = (Phase2)m2.get(name2);
		p3 = (Phase3)m2.get(name3);
		
		assertThat(p1, nullValue());
		assertThat(p2, nullValue());
		assertThat(p3, is(this.p3));
		
		assertThat(2, is(shared.getValue()));
		
	}
}
