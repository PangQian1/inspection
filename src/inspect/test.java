package inspect;

import java.awt.List;
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
		
		String time = "2018-01-01T11:03:56";
		Map<String,String> a = new HashMap();
		a.put("2018-04", "1");
		a.put("2018-05", "1");
		a.put("2018-06", "1");
		a.put("2018-07", "1");
		a.put("2018-08", "1");
		
		System.out.println(time.substring(0, 7));
	}

}

