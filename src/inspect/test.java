package inspect;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import dao.io;

public class test {

	public static boolean isLetterDigitOrChinese(String str) {
		  String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";//其他需要，直接修改正则表达式就好
		  return str.matches(regex);
		 }
	
	public static void main(String[] args) {
		String a = "1";
		String b = "粤D96932_u";
		
		System.out.println(isLetterDigitOrChinese(b.substring(0, 8)));
	}

}

