package org.beatific.flow.flow;

import org.beatific.flow.exception.ExceptionHandler;
import org.beatific.flow.phase.PhaseExecutor;
import org.beatific.flow.phase.PhaseHolder;
import org.beatific.flow.repository.RepositoryStore;
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
	private RepositoryStore store;

	@Autowired
	private ExceptionHandler[] handlers; /* When exception is occured, all spring beans that implements ExceptionHandler is executed*/
	
	@Autowired
	PhaseHolder phaseHolder;
	
	private boolean auto = true;
	private boolean isLoad = false;
	
	
	public void setAuto(boolean auto) {
		this.auto = auto;
	}
	
	public void flow() {
		
		if(!isLoad)load();
		if(!process()) return;
	}
	
	public void load() {
		
		isLoad = true;
        store.loadStore();
        phaseHolder.initailize(auto);
	}
	
	
	private boolean process() {
		
		try {
			
			for(PhaseExecutor phase : phaseHolder) {
				
				if(phaseHolder.isSatisfiedBy(phase)) {
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
	
	
}
