package dealByProDemand;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FindAppointCar {
	
	private static String GDAbnormalCarInfoPath = "I:/稽查/resData/问题车辆通行记录/广东省_16348.csv";
	private static String laneTypePath = "I:/稽查/lanetype.csv";
	private static String escapeCarPath = "I:/稽查/resData/";
	
	public static void main(String[] args) {
		gdAbnormalCarInfo(GDAbnormalCarInfoPath, laneTypePath, escapeCarPath, "粤T03F72_0");
		gdAbnormalCarInfo(GDAbnormalCarInfoPath, laneTypePath, escapeCarPath, "粤AU4S77_0");

	}
	
	public static Map<String, String> matchLaneType(String laneTypePath) {

		File laneTypeFile = new File(laneTypePath);
		
		Map<String, String> laneTypeMap = new HashMap<>();
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(laneTypeFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				
				String laneId = data[0];// 
				String laneType = data[1];//
				
				laneTypeMap.put(laneId, laneType);
			}
			reader.close();
			
			System.out.println(laneTypePath + " read finish!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return laneTypeMap;
	}
	
	public static void gdAbnormalCarInfo(String GDAbnormalCarInfoPath, String laneTypePath, String escapeCarPath, String plate) {
		
		Map<String, String> laneTypeMap = matchLaneType(laneTypePath);
		
		File abnormalCarInfoFile = new File(GDAbnormalCarInfoPath);
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(abnormalCarInfoFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String outPath = escapeCarPath + plate + ".csv";
			OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(outPath), "utf-8");
			BufferedWriter writer = new BufferedWriter(writerStream);
			
			String line = "";
			String[] data;
			
			line = reader.readLine();
			writer.write(line + ",出口收费车道类型" + "\n");
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				
				String cardId = data[1];//车牌
				String laneId = data[3].substring(0, 21);
				String laneType = laneTypeMap.get(laneId);
				
				data[0] = "'" + data[0];
				data[4] = "'" + data[4];
				data[6] = "'" + data[6];
				
				String new_line = "";
				for(int i=0;i<data.length;i++) {
					if(i == data.length-1) {
						new_line += data[i];
						break;
					}
					new_line += data[i] + ",";
				}
				
				if(cardId.equals(plate)) {
					writer.write(new_line + "," + laneType + "\n");
				}
				
			}
			reader.close();
			writer.flush();
			writer.close();
			System.out.println(GDAbnormalCarInfoPath + " read finish!");
			System.out.println(escapeCarPath + " write finish!");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
