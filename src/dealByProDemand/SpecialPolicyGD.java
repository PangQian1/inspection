package dealByProDemand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SpecialPolicyGD {

	private static String GDCombineProInfoDataPath = "I:/����/resData/���⳵�����ݱ�/�㶫ʡ(��ϵط���Ϣ).csv";
	private static String GDEscapeCarPath = "I:/����/resData/ʡ������/�㶫ʡ���⳵�����ݱ�.csv";
	
	public static void main(String[] args) {
		
		getEscapeCarList(GDCombineProInfoDataPath, GDEscapeCarPath);
	}

	public static void getEscapeCarList(String GDCombineProInfoDataPath, String GDEscapeCarPath) {
		
		File userInfoFile = new File(GDCombineProInfoDataPath);
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(userInfoFile), "utf-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(GDEscapeCarPath), "utf-8");
			BufferedWriter writer = new BufferedWriter(writerStream);
			
			String line = "";
			String[] data;
			int serial_num = 0;
			
			String tabHead = reader.readLine();
			writer.write(tabHead + "\n");
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				String exType = data[3].trim();// ע�ᳵ��
				
				if(exType.length() != 4 || exType.substring(2).equals("����")) {
					continue;
				}
				
				int carType = Integer.parseInt(exType.substring(0, 1));
				int type_1_num = Integer.parseInt(data[8].trim());
				int type_2_num = Integer.parseInt(data[9].trim());
				int type_3_num = Integer.parseInt(data[10].trim());
				int type_4_num = Integer.parseInt(data[11].trim());
				int type_other_num = Integer.parseInt(data[12].trim());
				
				if(carType == 1) {
					int sum = type_2_num + type_3_num + type_4_num + type_other_num;
					if(sum > 3) 
						writer.write(++serial_num + "," + line.substring((line.indexOf(",")+1)) + "\n");
				}else if(carType == 2) {
					int sum = type_1_num + type_3_num + type_4_num + type_other_num;
					if(sum > 3) 
						writer.write(++serial_num + "," + line.substring((line.indexOf(",")+1)) + "\n");
				}else if(carType == 3) {
					int sum = type_2_num + type_1_num + type_4_num + type_other_num;
					if(sum > 3) 
						writer.write(++serial_num + "," + line.substring((line.indexOf(",")+1)) + "\n");
				}else {//4�ͽ�3���շ�������Ҫ����
					int sum = type_2_num + type_1_num + type_other_num;
					if(sum > 3) 
						writer.write(++serial_num + "," + line.substring((line.indexOf(",")+1)) + "\n");
				}
			}
			
			reader.close();
			writer.flush();
			writer.close();
			System.out.println(GDCombineProInfoDataPath + " read finish!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
