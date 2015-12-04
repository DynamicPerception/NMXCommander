package com.dynamicperception.nmxcommandline.helpers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console {

	public static void p(Object output){
		System.out.print(output);
	}
	
	public static void pln(Object output){
		System.out.println(output);
	}	
	
	public static String getString(){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));        
        try {
			String s = br.readLine();
			return s;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "FAIL";
		}
	}
	
	public static int getInteger(){
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));		        
        try{
            int i = Integer.parseInt(br.readLine());
            return i;
        }catch(NumberFormatException | IOException nfe){
            System.err.println("Invalid Format!");
            return Consts.ERROR;
        }        
	}
}
