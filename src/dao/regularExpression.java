package dao;

public class regularExpression {
	
	
	public static boolean isLetterDigitOrChinese(String str) {
		  String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";//������Ҫ��ֱ���޸�������ʽ�ͺ�
		  return str.matches(regex);
		 }
	
	public static void main(String[] args) {

	}

}
