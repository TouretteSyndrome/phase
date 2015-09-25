package org.beatific.flow.flow;

import org.springframework.stereotype.Component;

@Component
public class Shared {

	private int value = 0;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	
	
}
