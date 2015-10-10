package com.mewa.utils;


import com.mewa.utils.i.IClock;

public class NanoClock implements IClock {

	@Override
	public long time() {
		return System.nanoTime();
	}

}
