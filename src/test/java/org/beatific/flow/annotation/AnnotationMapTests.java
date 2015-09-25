package org.beatific.flow.annotation;

import java.util.ArrayList;
import java.util.List;

import org.beatific.flow.flow.Phase1;
import org.beatific.flow.flow.Phase2;
import org.beatific.flow.flow.Phase3;
import org.beatific.flow.phase.Phase;
import org.beatific.flow.phase.PhaseExecutor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"classpath:applicationContext*.xml"})
public class AnnotationMapTests {

	@Autowired
	private AnnotationMap map;
	
	@Autowired
	private Phase1 phase1;
	
	@Autowired
	private Phase2 phase2;
	
	@Autowired
	private Phase3 phase3;
	
	private List<PhaseExecutor> phases;
	
	@Before
	public void setup() {
		phases = new ArrayList<PhaseExecutor>();
		phases.add(phase1);
		phases.add(phase2);
		phases.add(phase3);
	}
	
	@Test
	public void testGet() {
		
		List<Object> objects = map.get(Phase.class);
		
			Assert.assertArrayEquals(phases.toArray(new PhaseExecutor[0]), objects.toArray(new PhaseExecutor[0]));
		
	}
	
}
