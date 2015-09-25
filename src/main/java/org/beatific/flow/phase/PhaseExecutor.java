package org.beatific.flow.phase;

import org.beatific.flow.common.AutoGetter;
import org.beatific.flow.repository.RepositoryStore;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PhaseExecutor extends AutoGetter {

	@Autowired
	protected RepositoryStore store;
	
	public abstract void execute() throws Exception;
}
