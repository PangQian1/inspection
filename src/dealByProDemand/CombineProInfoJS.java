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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CombineProInfoJS {

	private static String jsUserInfoPath = "I:/����/�ط��û�����/�쳣�շ����ݺ˲���Ϣ��-����3.csv";
	private static String jsDataPath = "I:/����/resData/���⳵�����ݱ�/����ʡ.csv";
	private static String jsCombineProInfoDataPath = "I:/����/resData/���⳵�����ݱ�/����ʡ(��ϵط���Ϣ).csv";
	
	public static void main(String[] args) {
		combineProInfoJS(jsUserInfoPath, jsDataPath, jsCombineProInfoDataPath);
	}
	
	public static String getPlateColorCode(String color) {
		String code = "9";
		switch(color) {
			case "��":
				return "0";
			case "��":
				return "1";
			case "��":
				return "2";
			case "��":
				return "3";
		}
		
		return code;
	}
	
	public static Map<String, ArrayList<String>> jsUserInfoMap(String jsUserInfoPath) {
		File userInfoFile = new File(jsUserInfoPath);
		
		Map<String, ArrayList<String>> userInfoMap = new HashMap<>();
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(userInfoFile), "GBK");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				data = line.split(",");

				String exId = data[0] + "_" + getPlateColorCode(data[1]);// ע�ᳵ��
				String cardId = data[2];// ETC ID
				String OBUId = data[3];
				String exType = data[5];// ע�ᳵ��

				ArrayList<String> userList = new ArrayList<>();
				userList.add(exId);
				userList.add(exType);
				userList.add(OBUId);
				userInfoMap.put(cardId, userList);
				
			}
			reader.close();
			
			System.out.println(jsUserInfoPath + " read finish!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userInfoMap;
	}
	
	public static void combineProInfoJS(String jsUserInfoPath, String jsDataPath, String jsCombineProInfoDataPath) {
		
		Map<String, ArrayList<String>> userInfoMap = jsUserInfoMap(jsUserInfoPath);
		Map<String, String> jsMap = new HashMap<>();
		
		File jsDataFile = new File(jsDataPath);
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(jsDataFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				
				String cardId = data[1].substring(4);// ETC ID

				if(userInfoMap.containsKey(cardId)) {
					ArrayList<String> jsData = userInfoMap.get(cardId);
					String jsInfo = "'" + cardId + "," + jsData.get(0) + "," + jsData.get(1) + ",'" + jsData.get(2) + "," + data[2] + "," + data[3] + "," + data[4] + "," + data[5] + "," + data[6] + "," + data[7] + "," + data[8] + "," + data[9]; 
					jsMap.put(cardId, jsInfo);
				}
				
			}
			reader.close();
			
			System.out.println(jsUserInfoPath + " read finish!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		CombineProInfoGD.writeData(jsMap, jsCombineProInfoDataPath);
		
	}
	

}
