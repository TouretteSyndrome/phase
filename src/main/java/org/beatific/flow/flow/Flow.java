package org.beatific.flow.flow;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.beatific.flow.annotation.AnnotationMap;
import org.beatific.flow.exception.ExceptionHandler;
import org.beatific.flow.phase.Phase;
import org.beatific.flow.phase.PhaseExecutor;
import org.beatific.flow.repository.RepositoryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * flow system is consisted by collector, analyzer, report
 * 
 * In the order by collector, analyzer, report
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
	
	public void flow() {
		
		store.loadStore();
		
		loadPhase();

		if(!process(phases)) return;
	}
	
	public void loadPhase() {
		
		if(phases == null) {
			
			phases = map.get(Phase.class);
			
			Collections.sort(phases, new PhaseComparator());
			
		}
	}
	
	public boolean process(List<Object> objects) {
		
		if(objects == null) return false;
		
		try {
			
			for(Object object : objects) {
				
				if(object instanceof PhaseExecutor) {
					
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
				
				Class<?> klass = o2.getClass();
				Phase p2 = klass.getAnnotation(Phase.class);
				int next = p2.order();
				
				return prev - next;
				
			} else {
				
				throw new RuntimeException("Phase Class not matched");
			}
		}
		
	}
}
