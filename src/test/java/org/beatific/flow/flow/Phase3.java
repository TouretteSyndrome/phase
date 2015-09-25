package org.beatific.flow.flow;

import org.beatific.flow.phase.Phase;
import org.beatific.flow.phase.PhaseExecutor;

@Phase(id="test2", order=3)
public class Phase3 extends PhaseExecutor {

	@Override
	public void execute() throws Exception {
		store.save(this);
	}

}
