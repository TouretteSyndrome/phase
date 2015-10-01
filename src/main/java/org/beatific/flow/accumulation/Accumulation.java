package org.beatific.flow.accumulation;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class Accumulation {

	private Map<Object, Long> targets = new HashMap<Object, Long>();
	private Map<Object, Long> currents = new HashMap<Object, Long>();
		
	public void setTarget(Object object, long target) {
		targets.put(object, target);
	}
	
	public void setTarget(Object object, int target) {
		targets.put(object, new Long(target));
	}
	
	public boolean touch(Object object) {
		
		Long current = currents.get(object);
		current = (current == null) ? 1 : current + 1 ;
		
		Long target = this.targets.get(object);
		
		current = current % target;
		
		currents.put(object, current);
		
		if(current == 0) return true;
		else return false;
	}
	
}
