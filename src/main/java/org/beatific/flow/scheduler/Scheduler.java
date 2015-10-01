package org.beatific.flow.scheduler;

import org.beatific.flow.flow.Flow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

	@Autowired
	private Flow flow;
	
	@Scheduled(cron = "${cronExpression}")
	public void execute() {
		
		flow.flow();
	}
}
