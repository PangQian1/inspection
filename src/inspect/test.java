package inspect;

import java.awt.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
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
		  String regex = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";//������Ҫ��ֱ���޸�������ʽ�ͺ�
		  return str.matches(regex);
		 }
	
	public static void main(String[] args) {
		
/*		String trace = "";
						
		String[] data = trace.split("\\|");
		for(int i=0; i<data.length; i++){
			System.out.println(data[i]);
		}
		
		for(int i=0; i<data.length; i++){
			System.out.println(data[i].substring(21, 35));
		}*/

/*		String userPath = "E:/inspect/���Ͳ�һ�»�����Ϣ/typeNotConsistSummaryAll.csv";
		
		File userFile = new File(userPath);

		Map<String, String> matchResMap = new HashMap<>();

		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(userFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				
				String cardId = data[0];
				String type = data[3];
				int t = Integer.parseInt(type);
				if(t<5){
					matchResMap.put(cardId, line);
				}
			}
			reader.close();
			
			System.out.println(userPath + " read finish!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		InspectByType.writeData("E:/inspect/���Ͳ�һ�»�����Ϣ/typeNotConsist.csv", matchResMap);*/
		
		String aString = "'32011650220000063051,��C32088_1,����ʡ,4,309,1,'3201031701067457,301,'0,1,����,'3201031701067457";
		String bString = "'32011802220000042426,��BM7013_0,����ʡ,1,0,35,'3E268712,35,,,,0,�Ͷ�,'3201621802310165";
	
		String b[] = bString.split(",");
		System.out.println(b[8]);
		
		System.out.println(aString.split(",").length);
		System.out.println(bString.split(",").length);

	}

}

