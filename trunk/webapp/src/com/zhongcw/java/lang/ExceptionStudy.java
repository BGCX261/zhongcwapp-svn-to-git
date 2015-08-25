package com.zhongcw.java.lang;

/**
 * 
 *Java异常处理
 */
public class ExceptionStudy {

	/**
	 * 产生异常方法
	 * 
	 * @return
	 * @throws Exception
	 */
	private String getName() {
		String str = null; // 产生异常操作
		// String str = "abc"; // 正常操作
		str.substring(1);
		return str;

	}

	// private String getName() {
	// try {
	// String str = null; // 产生异常操作
	// str.substring(1);
	// } catch (Exception e) {
	// System.out.println("catch Exception in method");
	// }
	// return "";
	//
	// }

	public static void main(String[] args) {
		ExceptionStudy es = new ExceptionStudy();
		try {
			String str = es.getName();
			System.out.println("no Exception");
		} catch (Exception e) {
			System.out.println("catch Exception");
			// e.printStackTrace();
		}
		System.out.println("outof try-catch");
	}

}
