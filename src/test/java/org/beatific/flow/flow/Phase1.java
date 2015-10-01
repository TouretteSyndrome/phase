package org.beatific.flow.flow;

import org.beatific.flow.phase.Phase;
import org.beatific.flow.phase.PhaseExecutor;
import org.springframework.beans.factory.annotation.Autowired;

@Phase(id="test", order=1, fixedDelay=1)
public class Phase1 extends PhaseExecutor {

	@Autowired
	private Shared shared;
	
	@Override
	protected void innerExecute() throws Exception {
		store.save(this);
		switch(shared.getValue()) {
		case 0 : shared.setValue(1); break;
		default : break;
		}
		
	}
}
