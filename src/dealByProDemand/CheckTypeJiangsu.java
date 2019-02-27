package dealByProDemand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;


/**
 * ���ڿ��ų��ƾ�һ�µ����ݣ����ճ����Ƿ�һ�½������ֿ���
 * @author pangqian
 *
 */

public class CheckTypeJiangsu {	
	
	public static String  getCardType(String str){
		if(str.length() > 2){
			str = str.substring(0, 2);
		}
		
		if(str.length() == 1){
			return str;
		}
		
		String type = str.substring(1);
		if(type.equals("1") || type.equals("һ")){
			return "1";
		}else if(type.equals("2") || type.equals("��")){
			return "2";
		}else if(type.equals("3") || type.equals("��")){
			return "3";
		}else if(type.equals("4") || type.equals("��")){
			return "4";
		}
		
		return "";
	}
	
	public static void main(String[] args) {
		
		int count = 0;
		int count1 =0;
		int count2 = 0;
		String resultPath      = "E:/inspect/�������ݺ˶�/����ļ�/����-����ȫ����ͬ.csv";
		String typeConsistPath      = "E:/inspect/�������ݺ˶�/����ļ�/����-����-����һ��.csv";
		String typeNotConsistPath = "E:/inspect/�������ݺ˶�/����ļ�/����-������ͬ�����Ͳ�һ�£�.csv";
		
		HashMap<String, String> typeConsist = new HashMap<>();
		HashMap<String, String> typeNotConsist = new HashMap<>();
		
		try{
			File userFile = new File(resultPath);
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(userFile), "GBK");
			BufferedReader reader = new BufferedReader(inStream);
			
		    
            OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(typeConsistPath), "utf-8");
			BufferedWriter typeConsistRes = new BufferedWriter(writerStream);
			OutputStreamWriter writerStream1 = new OutputStreamWriter(new FileOutputStream(typeNotConsistPath), "utf-8");
			BufferedWriter typeNotConsistRes = new BufferedWriter(writerStream1);
		
		    String line;
		    line = reader.readLine();
		    while (true) {
		        if ((line = reader.readLine()) == null) {
		            break;
		        }
		        count ++;
		        String[] tmp    = line.split(",");
		        String cardID = tmp[0];
		        String   luwang_type = tmp[3];
		        String   jiangsu_type = getCardType(tmp[10]);

		        if(luwang_type.equals(jiangsu_type)){
		        	typeConsist.put(cardID, line);
		        	typeConsistRes.write(line);
		        	typeConsistRes.write("\n");
		        	count1++;
		        }else{
		        	typeNotConsist.put(cardID, line);
		        	typeNotConsistRes.write(line + "\n");
		        	count2++;
		        }
		    }
		    reader.close();
		    typeConsistRes.close();
		    typeNotConsistRes.close();
		    System.out.println(count + " " + count1 + " " +count2);

			
		}catch (IOException e) {
			e.printStackTrace();
		}
	   
	}

}
