package org.beatific.flow.flow;

import org.beatific.flow.phase.Phase;
import org.beatific.flow.phase.PhaseExecutor;
import org.beatific.flow.repository.RepositoryStore;
import org.springframework.beans.factory.annotation.Autowired;

@Phase(id="test2", order=3)
public class Phase3 extends PhaseExecutor {

	@Autowired
	private RepositoryStore store;
	
	@Override
	public void execute() throws Exception {
		store.save(this);
	}

}
