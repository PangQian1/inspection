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
	private static String matchUserInfoPath = "/home/pq/inspect/resData/dividedByCardId/matchUserInfo";
	
	public static void matchUserInfo(String userInfoPath,String originPath, String matchUserInfoPath) {
		
		File file = new File(originPath);
		List<String> list = Arrays.asList(file.list());

		File userInfoFile = new File(userInfoPath);
		
		Map<String, ArrayList<String>> userInfoMap = new HashMap<>();
		Map<String, ArrayList<String>> provinceMap = new HashMap<>();
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(userInfoFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				
				String cardId = data[0];// 用户卡编号
				String exId = data[1];// 注册车牌
				String exType = data[2];// 注册车型
				
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
		

		for (int i = 0; i < list.size(); i++) {
			// 依次处理每一个省份
			String pathIn = originPath + "/" + list.get(i);
			String outPath = matchUserInfoPath + "/" + list.get(i);
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(outPath), "utf-8");
				BufferedWriter writer = new BufferedWriter(writerStream);
				
				String line = "";
				String[] data;
				
				while ((line = reader.readLine()) != null) {
					//针对每个id进行判断
					data = line.split(",", 10);
					String cardId = data[1];// 用户卡编号
				
					if(userInfoMap.containsKey(cardId)){
						ArrayList<String> userList = userInfoMap.get(cardId);
						line += userList.get(0) + "," + userList.get(1);
						writer.write(line + "\n");
					}
				}
			
				reader.close();
				writer.flush();
				writer.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			System.out.println(pathIn + "read finish!");
		}

	}
	
	public static void generateSummaryTab(String mulDataPath, String userInfoPath, String outPath){
		
		File mulDataFile = new File(mulDataPath);
		File userInfoFile = new File(userInfoPath);
		
		Map<String, ArrayList<String>> userInfoMap = new HashMap<>();
		Map<String, String> summaryTalMap = new HashMap<>();
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(userInfoFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				
				String cardId = data[0];// 用户卡编号
				String exId = data[1];// 注册车牌
				String exType = data[2];// 注册车型
				
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
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(mulDataFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			while ((line = reader.readLine()) != null) {
				//针对每个id进行判断
				data = line.split("\\|");
				
				int consistCount = 0;
				int notConsistCount = 0;
				String str = "";
				
				for(int j=0; j<data.length; j++){
					String[] trace = data[j].split(",", 10);

					String cardId = trace[1];// 用户卡编号
					String exVehType = trace[8];// 出口收费车型	
					
					if(userInfoMap.containsKey(cardId)){

						if (!exVehType.equals("null")) {
							
							ArrayList<String> list = userInfoMap.get(cardId);
							String vehId = list.get(0);//注册车牌
							int r_type = Integer.parseInt(list.get(1));//注册车型
							int a_type = Integer.parseInt(exVehType);//实际车型
							
							if(a_type == r_type){//注册车型和实际车型一致的情况
								consistCount++;
							}else{
								notConsistCount++;
							}

							String proNum = cardId.substring(0, 2);
							String proName = DividedByCardId.getCardIdRealeaser(proNum);
							
							if((j == data.length-1)){
								str = cardId + "," + vehId + "," + proName + "," + r_type + "," + consistCount + "," + notConsistCount;
								summaryTalMap.put(cardId, str);
							}			
						}
					}else{
						break;
					}
	
					
				}
			}
			
			reader.close();
			
			InspectByType.writeData(outPath, summaryTalMap);
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void main(String[] args) {
		
	}

}
