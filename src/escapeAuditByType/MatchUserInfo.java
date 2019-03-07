package escapeAuditByType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import divideByReleaser.DividedByCardId;
import inspect.InspectByType;

public class MatchUserInfo {
	
	private static String userInfoPath = "/home/pq/inspect/registrationInfo/userInfo.csv";
	private static String originPath = "/home/pq/inspect/resData/dividedByCardId/origin";
	private static String sumDataPath = "/home/pq/inspect/intermediateData/sumDataBySever";
	
	public static Map<String, ArrayList<String>> matchUserInfo(String userInfoPath) {

		File userInfoFile = new File(userInfoPath);
		
		Map<String, ArrayList<String>> userInfoMap = new HashMap<>();
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(userInfoFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				
				String cardId = data[0];// ÓÃ»§¿¨±àºÅ
				String exId = data[1];// ×¢²á³µÅÆ
				String exType = data[2];// ×¢²á³µÐÍ
				
				if(!exType.equals("null")){
					ArrayList<String> userList = new ArrayList<>();
					userList.add(exId);
					userList.add(exType);
					userInfoMap.put(cardId, userList);
				}
			}
			reader.close();
			
			System.out.println(userInfoPath + " read finish!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return userInfoMap;
	}

	public static void main(String[] args) {
		
	}

}
