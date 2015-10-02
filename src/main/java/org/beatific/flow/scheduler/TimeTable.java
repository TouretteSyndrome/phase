package org.beatific.flow.scheduler;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.quartz.CronExpression;
import org.springframework.stereotype.Component;

@Component
public class TimeTable {

	private Map<Object, CronExpression> times = new HashMap<Object, CronExpression>();
	
	
	private String repeteString(int number) {
		StringBuffer sb = new StringBuffer();
		return number > 0 ? sb.append("*/").append(String.valueOf(number)).append(" ").toString() : "* ";
	}
	
	public void setFireTime(Object object, Object time) {
		
		if(time instanceof Integer) {
			
			TimeDivider divider = new TimeDivider((Integer)time);
			
			StringBuffer timeString = new StringBuffer();
			timeString.append(repeteString(divider.seconds))   
			          .append(repeteString(divider.minutes))
			          .append(repeteString(divider.hours))
			          .append(repeteString(divider.days))
			          .append(repeteString(divider.months))
			          .append("? ")
			          .append(repeteString(divider.years));
			time = timeString.toString();
		}
		
		try {
			times.put(object, new CronExpression((String)time));
		} catch (ParseException e) {
			throw new CronParseException(e);
		}
		
	}
	
	public boolean isSatisfiedBy(Object object) {
		
		CronExpression expression = times.get(object);
		if(expression == null) return false;
		
	    return expression.isSatisfiedBy(new Date());
	}
	
	private class TimeDivider {

		int years = 0;
		int months = 0;
		int days = 0;
		int hours = 0;
		int minutes = 0;
		int seconds = 0;
		
		private TimeDivider(int sec) {
			
			if(sec > 3600 * 24) throw new FixedDelayMaxExceedException("Given seconds greater than 24 hours: " + sec);
			hours = sec / 3600;
			minutes = sec % 3600 / 60;
			seconds = sec % 3600 % 60;
		}
		
	}
}
