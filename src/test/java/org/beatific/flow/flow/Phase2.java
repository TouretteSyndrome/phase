package org.beatific.flow.flow;

import org.beatific.flow.phase.Phase;
import org.beatific.flow.phase.PhaseExecutor;
import org.beatific.flow.repository.RepositoryStore;
import org.springframework.beans.factory.annotation.Autowired;

@Phase(id="test", order=2)
public class Phase2 extends PhaseExecutor {

	@Autowired
	private RepositoryStore store;
	
	@Autowired
	private Shared shared;
	
	@Override
	public void execute() throws Exception {
		store.save(this);
		switch(shared.getValue()) {
		case 1 : shared.setValue(2); break;
		default : break;
		}
	}

}
