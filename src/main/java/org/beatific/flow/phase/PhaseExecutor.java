package org.beatific.flow.phase;

import java.util.Date;
import java.util.Map;

import org.beatific.flow.common.AutoDataResolver;
import org.beatific.flow.repository.RepositoryStore;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class PhaseExecutor extends AutoDataResolver {

	@Autowired
	protected RepositoryStore store;

	@SuppressWarnings("rawtypes")
	protected Map dataMap;

	private boolean first = false;
	private boolean last = false;
	private boolean auto = true;
	private Date lastDate = null;
	
	public boolean isFirst() {
		return first;
	}

	public boolean isLast() {
		return last;
	}

	public boolean isAuto() {
		return auto;
	}
	
	public Date getLastDate() {
		return lastDate;
	}

	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

	public void execute() throws Exception {

		if(isAuto()) autoExecute();
		else innerExecute();
		
	}

	@SuppressWarnings("rawtypes")
	private void autoExecute() throws Exception {
		
		if (!isFirst()) dataMap = (Map) store.load(this);

		innerExecute();

		if (isLast()) store.remove(this);
		else store.save(this);
	}

	protected abstract void innerExecute() throws Exception;
}
