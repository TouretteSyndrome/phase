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
	
	public void setFireTime(Object object, Object time) {
		
		if(time instanceof Integer) {
			StringBuffer timeString = new StringBuffer();
			timeString.append("*/").append(time).append(" * * * * ?");
			time = timeString.toString();
		}
		
		try {
			times.put(object, new CronExpression((String)time));
		} catch (ParseException e) {
			throw new CronParseException(e);
		}
		
	}
	
	public boolean touch(Object object) {
		
		CronExpression expression = times.get(object);
		if(expression == null) return false;
		
	    return expression.isSatisfiedBy(new Date());
	}
}
