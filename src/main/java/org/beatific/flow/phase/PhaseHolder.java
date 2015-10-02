package org.beatific.flow.phase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.beatific.flow.annotation.AnnotationMap;
import org.beatific.flow.flow.ClassNotMatchException;
import org.beatific.flow.properties.PropertiesConverter;
import org.beatific.flow.scheduler.TimeTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PhaseHolder implements Iterable<PhaseExecutor>{

	@Autowired
	private AnnotationMap map;
	
	@Autowired
	private TimeTable timetable;
	
	@Autowired
	PropertiesConverter converter;

	private List<PhaseExecutor> phases = null;

	public void initailize(boolean auto) {

		if (phases == null) {

			phases = Arrays.asList(map.get(Phase.class).toArray(new PhaseExecutor[0]));

			if (phases == null)
				phases = new ArrayList<PhaseExecutor>();

			Collections.sort(phases, new PhaseComparator());

			renderContext(phases, "auto", auto);

			PhaseExecutor first = (PhaseExecutor) phases.get(0);
			first.put("first", true);

			PhaseExecutor last = (PhaseExecutor) phases.get(phases.size() - 1);
			last.put("last", true);
		}

	}

	private void renderContext(List<PhaseExecutor> phases, String key, Object value) {

		PrevInterval prev = new PrevInterval();

		for (PhaseExecutor phase : phases) {

			((PhaseExecutor) phase).put(key, value);

			prev = setInterval(phase, prev);
		}

	}
	
	private PrevInterval setInterval(Object phase, PrevInterval prev) {
		Class<?> clazz = phase.getClass();
	    Phase phaseAnnotation = clazz.getAnnotation(Phase.class);
	    
	    String cron = converter.convert(phaseAnnotation.cron());
	    String fixedDelayString = converter.convert(phaseAnnotation.fixedDelayString());
	    Integer fixedDelay = phaseAnnotation.fixedDelay();
	    
	    if("".equals(cron)) {
	    	if("".equals(fixedDelayString)) {
                if(fixedDelay == 0) {
                	Object validValue = prev.validValue(phaseAnnotation.id());
                	if(validValue != null)timetable.setFireTime(phase, validValue);
                	return prev;
	    		} else if(fixedDelay < 0) {
	    			return prev;
	    		}
                
	    	} else {
	    		fixedDelay = Integer.parseInt(fixedDelayString);
	    		
	    	}
	    	prev.setFixedDelay(fixedDelay);
	    	timetable.setFireTime(phase, fixedDelay);
	    } else {
	    	prev.setCron(cron);
	    	timetable.setFireTime(phase, cron);
	    }
	    
	    prev.setId(phaseAnnotation.id());
	    return prev;
	    
		
	}

	@Override
	public Iterator<PhaseExecutor> iterator() {
		return phases.iterator();
	}

	public boolean isSatisfiedBy(Object object) {
		
		return timetable.isSatisfiedBy(object);
		
	}
	
	private class PhaseComparator implements Comparator<Object> {

		@Override
		public int compare(Object o1, Object o2) {

			if (o1 instanceof PhaseExecutor && o2 instanceof PhaseExecutor) {
				Class<?> clazz = o1.getClass();
				Phase p1 = clazz.getAnnotation(Phase.class);
				int prev = p1.order();
				String id1 = p1.id();

				Class<?> klass = o2.getClass();
				Phase p2 = klass.getAnnotation(Phase.class);
				int next = p2.order();
				String id2 = p2.id();

				if (id1.equals(id2)) {
					return prev - next;
				} else {
					return id1.compareTo(id2);
				}

			} else {

				throw new ClassNotMatchException(
						"Class is not instance of PhaseExecutor");
			}
		}

	}

	private class PrevInterval {

		public Object validValue(String id) {
			if (id.equals(this.id))
				return cron == null ? fixedDelay : cron;
			else
				return null;
		}

		public void setId(String id) {
			this.id = id;
		}

		private String id;
		private String cron;
		private int fixedDelay;

		public void setCron(String cron) {
			this.cron = cron;
		}

		public void setFixedDelay(int fixedDelay) {
			this.fixedDelay = fixedDelay;
		}
	}
	
}
