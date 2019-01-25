package Main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import inspect.InspectByType;

public class AdaptExcel {

	public static void main(String[] args) {
		String userPath = "E:/inspect/车型不一致汇总信息/typeNotConsist.csv";
		
		File userFile = new File(userPath);

		Map<String, String> matchResMap = new HashMap<>();

		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(userFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			String str = "";
			
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				str = "";
				
				String cardId = data[0];
				String OBU1 = data[6];
				String OBU2 = data[8];
				
				if(!OBU1.equals("")){
					OBU1 = "'" + OBU1;
					data[6] = OBU1;
				}
				if(!OBU2.equals("")){
					OBU2 = "'" + OBU2;
					data[8] = OBU2;
				}
				
				for(int i=0; i<data.length; i++){
					if(i == data.length-1){
						str += data[i];
						break;
						}
					str += data[i] + ",";
				}
				
				matchResMap.put(cardId, str);
			}
			reader.close();
			
			System.out.println(userPath + " read finish!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		InspectByType.writeData("E:/inspect/车型不一致汇总信息/type.csv", matchResMap);
	}

	

}
