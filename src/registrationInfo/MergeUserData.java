package registrationInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MergeUserData {
	
	private static String cardPath = "/home/pq/inspect/registrationInfo/card";
	private static String vehiclePath = "/home/pq/inspect/registrationInfo/vehicle";
	
	private static String userInfoPath = "/home/pq/inspect/cardSta.csv";
	private static String vehicleInfoPath = "/home/pq/inspect/vehicleSta.csv";
	
	public static void mergeVehicleInfo(String path, String outPath) {

		File file = new File(path);
		List<String> list = Arrays.asList(file.list());
		Map<String, LinkedList<String>> cardIDMap = new HashMap<>();

		
		for (int i = 0; i < list.size(); i++) {
			// 依次处理每一个文件
			String pathIn = path + "/" + list.get(i);
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				String line = "";
				String[] data;
				while ((line = reader.readLine()) != null) {
					data = line.split(",", 8);
					
					//String cardId = data[0];// 用户卡编号
					String vehId = data[0];// 车辆id

					if (cardIDMap.containsKey(vehId)) {
						LinkedList<String> listTrace = cardIDMap.get(vehId);
						listTrace.add(line);
						cardIDMap.put(vehId, listTrace);
					} else {
						LinkedList<String> listTrace = new LinkedList<>();
						listTrace.add(line);
						cardIDMap.put(vehId, listTrace);

					}
					
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(pathIn + " read finish!");


			}
		

		writePreExData(outPath, cardIDMap);
	}
	
	
	
	public static void preProcess(String path, String outPath) {

		File file = new File(path);
		List<String> list = Arrays.asList(file.list());
		Map<String, LinkedList<String>> cardIDMap = new HashMap<>();

		
		for (int i = 0; i < list.size(); i++) {
			// 依次处理每一个文件
			String pathIn = path + "/" + list.get(i);
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				String line = "";
				String[] data;
				while ((line = reader.readLine()) != null) {
					data = line.split(",", 15);
					
					//String cardId = data[0];// 用户卡编号
					String vehId = data[6];// 车辆id

					if (cardIDMap.containsKey(vehId)) {
						LinkedList<String> listTrace = cardIDMap.get(vehId);
						listTrace.add(line);
						cardIDMap.put(vehId, listTrace);
					} else {
						LinkedList<String> listTrace = new LinkedList<>();
						listTrace.add(line);
						cardIDMap.put(vehId, listTrace);

					}
					
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(pathIn + " read finish!");


			}
		

		writePreExData(outPath, cardIDMap);
	}

	public static void writePreExData(String outPath, Map<String, LinkedList<String>> cardIDMap){
	
		try {
			OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(outPath),"utf-8");
			BufferedWriter writer = new BufferedWriter(writerStream);
			for (String cardId : cardIDMap.keySet()) {
				LinkedList<String> listTrace = cardIDMap.get(cardId);
				for (int j = 0; j < listTrace.size(); j++) {
					if(j == listTrace.size()-1){
						writer.write(listTrace.get(j));
						break;
					}
					writer.write(listTrace.get(j) + "|");			
				}
				writer.write("\n");
				writer.flush();
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		
		//preProcess(cardPath, userInfoPath);
		mergeVehicleInfo(vehiclePath, vehicleInfoPath);
	}

}
