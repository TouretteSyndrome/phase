package org.beatific.flow.common;

import org.beatific.flow.util.ReflectionUtils;

public abstract class AutoDataResolver {

	public synchronized Object get(String key) {

		return ReflectionUtils.get(this, key);
	}

	public synchronized void put(String key, Object value) {

		ReflectionUtils.set(this, key, value);
	}

}
