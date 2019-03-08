package dealByProDemand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.sql.SQLNonTransientConnectionException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CombineProInfoGD {
	
	private static String GDUserInfoPath = "I:/稽查/地方用户数据/异常收费数据核查结果-广东.csv";
	private static String GDDataPath = "I:/稽查/resData/问题车辆数据表/广东省.csv";
	private static String GDCombineProInfoDataPath = "I:/稽查/resData/问题车辆数据表/广东省(结合地方信息).csv";
	
	public static void main(String[] args) {
		combineProInfoGD(GDUserInfoPath, GDDataPath, GDCombineProInfoDataPath);
	}
	
	public static Map<String, ArrayList<String>> gdUserInfoMap(String GDUserInfoPath) {
		File userInfoFile = new File(GDUserInfoPath);
		
		Map<String, ArrayList<String>> userInfoMap = new HashMap<>();
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(userInfoFile), "GBK");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				
				if(data.length < 4) {
					continue;
				}
				
				String exId = data[0];// 注册车牌
				String cardId = data[1];// ETC ID
				String exType = data[2];// 注册车型

				ArrayList<String> userList = new ArrayList<>();
				userList.add(exId);
				userList.add(exType);
				userInfoMap.put(cardId, userList);
				
			}
			reader.close();
			
			System.out.println(GDUserInfoPath + " read finish!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userInfoMap;
	}
	
	public static void combineProInfoGD(String GDUserInfoPath, String GDDataPath, String GDCombineProInfoDataPath) {
		
		Map<String, ArrayList<String>> userInfoMap = gdUserInfoMap(GDUserInfoPath);
		Map<String, String> gdMap = new HashMap<>();
		
		File gdDataFile = new File(GDDataPath);
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(gdDataFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				
				String cardId = data[1].substring(4);// ETC ID

				if(userInfoMap.containsKey(cardId)) {
					ArrayList<String> gdData = userInfoMap.get(cardId);
					String gdInfo = "'" + cardId + "," + gdData.get(0) + "," + gdData.get(1) + ",," + data[2] + "," + data[3] + "," + data[4] + "," + data[5] + "," + data[6] + "," + data[7] + "," + data[8] + "," + data[9]; 
					gdMap.put(cardId, gdInfo);
				}
				
			}
			reader.close();
			
			System.out.println(GDUserInfoPath + " read finish!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		writeData(gdMap, GDCombineProInfoDataPath);
		
	}
	
	public static void writeData(Map<String, String> map, String outPath) {

		try {
			
			OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(outPath), "utf-8");
			BufferedWriter writer = new BufferedWriter(writerStream);
			
			writer.write("序号,ETC ID,车牌(省中心),发行车型(省中心),OBU ID(省中心),车牌(国家中心信息),发行车型(国家中心信息),OBU ID(国家中心信息),1型出现次数,2型出现次数,3型出现次数,4型出现次数,其它次数\n");
			int serial_num = 0;
			for(String cardId: map.keySet()) {
				serial_num++;
				writer.write(serial_num + "," + map.get(cardId) + "\n");
			}
				
			writer.flush();
			writer.close();
			System.out.println(outPath + " write finish!");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	


}
