package com.systemscanner.den.worker;

import com.sun.jna.Library;

public interface NativeAccessor extends Library {
	String generateReport();
}
