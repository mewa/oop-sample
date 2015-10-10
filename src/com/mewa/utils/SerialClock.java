package com.mewa.utils;


import com.mewa.utils.i.IClock;

public class SerialClock implements IClock {
	long serial = -1;
	
	@Override
	public long time() {
		return ++serial;
	}

}
