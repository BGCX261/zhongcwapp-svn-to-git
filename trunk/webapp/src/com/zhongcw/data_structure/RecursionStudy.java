package com.zhongcw.data_structure;

public class RecursionStudy {
	
	static int multiply(int n){
		if(n == 0 || n ==1){
			return n;
		}else{
			return n*multiply(n-1);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(multiply(5));
	}

}
