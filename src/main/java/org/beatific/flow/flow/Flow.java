package org.beatific.flow.flow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.beatific.flow.annotation.AnnotationMap;
import org.beatific.flow.exception.ExceptionHandler;
import org.beatific.flow.phase.Phase;
import org.beatific.flow.phase.PhaseExecutor;
import org.beatific.flow.repository.RepositoryStore;
import org.beatific.flow.scheduler.TimeTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * flow system is consisted by phase
 * 
 * In the order by phase
 * process is executed.
 *  
 * @author beatific J
 */
@Component
public class Flow {

	@Autowired
	private AnnotationMap map;
	
	@Autowired
	private RepositoryStore store;
	
	@Autowired
	private ExceptionHandler[] handlers; /* When exception is occured, all spring beans that implements ExceptionHandler is executed*/
	
	private List<Object> phases = null;
	
	private boolean auto = true;
	private boolean isLoad = false;
	
	@Autowired
	private TimeTable timetable;
	
	public void setAuto(boolean auto) {
		this.auto = auto;
	}
	
	public void flow() {
		
		if(!isLoad)load();
		if(!process(phases)) return;
	}
	
	public void load() {
		
		isLoad = true;
        store.loadStore();
		loadPhase();
	}
	
	private void loadPhase() {
		
		if(phases == null) {
			
			phases = map.get(Phase.class);
			
			if(phases == null) phases = new ArrayList<Object>();
			
			Collections.sort(phases, new PhaseComparator());
			
			renderContext(phases, "auto", auto);
			
			PhaseExecutor first = (PhaseExecutor)phases.get(0);
			first.put("first", true);
			
			PhaseExecutor last = (PhaseExecutor)phases.get(phases.size()-1);
			last.put("last", true);
		}
	}
	
	private PrevInterval setInterval(Object phase, PrevInterval prev) {
		Class<?> clazz = phase.getClass();
	    Phase phaseAnnotation = clazz.getAnnotation(Phase.class);
	    
	    String cron = phaseAnnotation.cron();
	    String fixedDelayString = null;
	    Integer fixedDelay = 0;
	    
	    if("".equals(cron)) {
	    	fixedDelayString = phaseAnnotation.fixedDelayString();
	    	if("".equals(fixedDelayString)) {
	    		fixedDelay = phaseAnnotation.fixedDelay();
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
	
	private void renderContext(List<Object> phases, String key, Object value) {
		
		PrevInterval prev = new PrevInterval();
		
		for(Object phase : phases) {
			
			if(phase instanceof PhaseExecutor) {
			    ((PhaseExecutor)phase).put(key, value);
			    
			    prev = setInterval(phase, prev);
			}
		}
			
	}
	
	private boolean process(List<Object> objects) {
		
		if(objects == null) return false;
		try {
			
			for(Object object : objects) {
				
				if(timetable.touch(object) && object instanceof PhaseExecutor) {
					
					PhaseExecutor phase = (PhaseExecutor)object;
					phase.execute();
				}
			}
			
			return true;
			
		} catch (Exception ex) {
			
			for(ExceptionHandler handler : handlers) {
				handler.handle(ex);
			}
		}
		
		return false;
	}
	
	private class PhaseComparator implements Comparator<Object> {

		@Override
		public int compare(Object o1, Object o2) {
			
			if(o1 instanceof PhaseExecutor && o2 instanceof PhaseExecutor) {
				Class<?> clazz = o1.getClass();
				Phase p1 = clazz.getAnnotation(Phase.class);
				int prev = p1.order();
				String id1 = p1.id();
				
				Class<?> klass = o2.getClass();
				Phase p2 = klass.getAnnotation(Phase.class);
				int next = p2.order();
				String id2 = p2.id();
				
				if(id1.equals(id2)) {
					return prev - next;
				} else  {
					return id1.compareTo(id2);
				}
				
			} else {
				
				throw new ClassNotMatchException("Class is not instance of PhaseExecutor");
			}
		}
		
	}
	
	private class PrevInterval {
		
		public Object validValue(String id) {
			if(this.id.equals(id)) return cron==null ? fixedDelay:cron;
			else return null;
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
