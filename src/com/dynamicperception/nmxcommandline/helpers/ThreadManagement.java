package com.dynamicperception.nmxcommandline.helpers;

public class ThreadManagement {
	
	public static void message(String message) {
	    String threadName =
	        Thread.currentThread().getName();
	    System.out.format("%s: %s%n",
	                      threadName,
	                      message);
	}
}
