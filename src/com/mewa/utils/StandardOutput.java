package com.mewa.utils;


import com.mewa.utils.i.OutputStream;

public class StandardOutput implements OutputStream {
	
	@Override
	public void writeToStream(String data) {
		System.out.println(data);		
	}
	

}
