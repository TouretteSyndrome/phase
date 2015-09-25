package org.beatific.flow.util;

public abstract class Holder<T> {

	private T previousValue;
	private T holdedValue;

	protected abstract T defaultValue();

	protected abstract T initailValue();
	
	synchronized void hold() {
		
		if(holdedValue == null) previousValue = initailValue();
		else previousValue = holdedValue;
		
		holdedValue = defaultValue();
	}

	synchronized T holdedValue() {

		return holdedValue;
	}

	synchronized T previousValue() {

		return previousValue;
	}

}
