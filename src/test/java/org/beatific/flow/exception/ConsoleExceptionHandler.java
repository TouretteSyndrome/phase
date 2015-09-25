package org.beatific.flow.exception;

import org.springframework.stereotype.Component;

@Component
public class ConsoleExceptionHandler extends ExceptionHandler {

	@Override
	public void handle(Exception ex) {
		ex.printStackTrace();
	}

}
