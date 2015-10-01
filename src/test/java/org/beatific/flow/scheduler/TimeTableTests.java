package org.beatific.flow.scheduler;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.beatific.flow.util.DateUtils;
import org.junit.Test;

public class TimeTableTests {

	@Test
	public void testFixedDelayTouch() {

		TimeTable timetable = new TimeTable();
		timetable.setFireTime(this, 2);
		
		List<Date> times = new ArrayList<Date>();
		
		for ( int i =0; i < 10 ; i++) {
			if(timetable.touch(this))
				times.add(new Date());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		assertResults(times);

	}
	
	@Test
	public void testCronTouch() {

		TimeTable timetable = new TimeTable();
		timetable.setFireTime(this, "*/2 * * * * ?");
		
		List<Date> times = new ArrayList<Date>();
		
		for ( int i =0; i < 10 ; i++) {
			if(timetable.touch(this))
				times.add(new Date());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		assertResults(times);

	}
	
	private void assertResults(List<Date> times) {
		
		long prev = 0;
		long next = 0;
		for(Date time : times) {
			if(prev == 0) {
				prev = DateUtils.noMiliSecondsDate(time).getTime();
				next = DateUtils.noMiliSecondsDate(time).getTime();
			} else {
				next = DateUtils.noMiliSecondsDate(time).getTime();
				assertThat(next - prev, is(2000L));
			}
			
			prev = next;
		}
	}
	
}
