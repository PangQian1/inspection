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

import inspect.test;

/**
 * 将广东异常收费数据核查结果车型以及
 * @author pangqian
 *
 */

public class CheckType {	
	
	public static void main(String[] args) {
		
		int count = 0;
		int count1 =0;
		int count2 = 0;
		String resultPath      = "E:/inspect/广东数据核对/结果文件/卡号-车牌全都相同.csv";
		String typeConsistPath      = "E:/inspect/广东数据核对/结果文件/卡号-车牌-车型一致.csv";
		String typeNotConsistPath = "E:/inspect/广东数据核对/结果文件/卡号-车牌相同（车型不一致）.csv";
		
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
		        int a = line.indexOf("型");
		        String   guangdong_type = "";
		        if(a>0){
		        	guangdong_type = line.substring(a-1, a+3);
		        }
		        luwang_type += "型客车";
		        if(luwang_type.equals(guangdong_type)){
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
