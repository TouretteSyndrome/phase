package org.beatific.flow.common;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class AutoDataResolverTests {

	private GetterClass getter;
	
	@Before
	public void setUp() {
		getter = new GetterClass();
	}
	
	@Test
	public void testGet() {
		getter.setAge(10);
		getter.setName("beatificJ");
		
		assertThat(getter.getAge(), is(getter.get("age")));
		assertThat(getter.getName(), is(getter.get("name")));
	}
}
