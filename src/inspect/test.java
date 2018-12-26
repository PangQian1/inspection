package inspect;

import java.io.BufferedWriter;
import java.sql.DatabaseMetaData;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.management.timer.TimerMBean;
import javax.swing.text.TabableView;

import dao.io;

public class test {

	public static boolean isLetterDigitOrChinese(String str) {
		  String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";//其他需要，直接修改正则表达式就好
		  return str.matches(regex);
		 }
	
	public static void main(String[] args) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		String time = "2018-01-01T11:03:56";
		System.out.println(time.length());
		String statusChangeTime = time.substring(0, 10) + " " + time.substring(11);
		System.out.println(statusChangeTime);
	
		String t1 = "2018-01-01 11:03:56";
		try {
			Date t2 = sdf.parse(statusChangeTime);
			Date t3 = sdf.parse(t1);
			System.out.println(t2.before(t3));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

}

