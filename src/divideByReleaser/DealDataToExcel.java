package divideByReleaser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import inspect.InspectByType;

public class DealDataToExcel{

	public static void main(String[] args) {
		String userPath = "I:/稽查/根据卡号划分省份/广东省_23796.csv";
		
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
				
				String id = data[0];
				String cardId = data[1];
				String OBU = data[3];
			
				
				if(!OBU.equals("null")){
					OBU = "'" + OBU; 
					data[3] = OBU;
				}
				
				cardId = "'" + cardId;
				data[1] = cardId;
				
				for(int i=0; i<data.length; i++){
					if(i == data.length-1){
						str += data[i];
						break;
						}
					str += data[i] + ",";
				}
				
				matchResMap.put(id, str);
			}
			reader.close();
			
			System.out.println(userPath + " read finish!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		InspectByType.writeData("I:/稽查/广东省_23796.csv", matchResMap);
	}

	

}

