package inspect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dao.regularExpression;
import dao.textSimilar;

public class InspectByType {
	
	/*private static String userInfoPath = "/home/pq/inspect/registrationInfo/userInfo.csv";
	private static String exVehTypeMulPath = "/home/pq/inspect/intermediateData/comMulData/exVehTypeMulRes.csv";
	private static String matchResPath = "/home/pq/inspect/resData/exTypeMatchUserRes.csv";*/
	
	private static String userInfoPath = "H:/pangqian/userInfo.csv";
	private static String exVehTypeMulPath = "H:/pangqian/exVehTypeMulRes.csv";
	private static String matchResPath = "H:/pangqian/res/exTypeMatchUserRes.csv";
	
	public static void typeMatchUser(String userPath, String typePath, String outPath) {

		File userFile = new File(userPath);
		File typeFile = new File(typePath);
		Map<String, String> matchResMap = new HashMap<>();
		Map<String, ArrayList<String>> userInfoMap = new HashMap<>();
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(userFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				
				String cardId = data[0];// �û������
				String exId = data[1];// ע�ᳵ��
				String exType = data[2];// ע�ᳵ��
				
				if(!exType.equals("null")){
					ArrayList<String> userList = new ArrayList<>();
					userList.add(exId);
					userList.add(exType);
					userInfoMap.put(cardId, userList);
				}
			}
			reader.close();
			
			System.out.println(userPath + " read finish!");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {			
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(typeFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			while ((line = reader.readLine()) != null) {
				//���ÿ��id�����ж�
				data = line.split("\\|");
				
				int countT = 0;
				int countF = 0;
				int type = 100;
				
				for(int j=0; j<data.length; j++){
					String[] trace = data[j].split(",", 10);
					
					String cardId = trace[1];// �û������
					//String exVehId = trace[5];// ����ʵ���շѳ��ƺ���
					String exVehType = trace[8];// �����շѳ���				

					if (!exVehType.equals("null") && userInfoMap.containsKey(cardId)) {
						
						ArrayList<String> list = userInfoMap.get(cardId);
						String vehId = list.get(0);
						int r_type = Integer.parseInt(list.get(1));
						int a_type = Integer.parseInt(exVehType);
						
						if(a_type == r_type) countT++;
						
						if(a_type > r_type){
							if(type == 100) type = a_type;
							if(type != a_type) break;
							countF++;
						}
						
						if((j == data.length-1) && countF>0 && countT>0){
							matchResMap.put(cardId, cardId + "," + vehId + "," + r_type + "|" + line);
						}			
					}
					
				}
			}
			
			System.out.println(typePath + " read finish!");
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		
		System.out.println("���ҵ�" + matchResMap.size() + "�����ݡ�");
		
		//д���
		writeData(outPath, matchResMap);

		System.out.println("**************�����û����ݳ��ͻ������*************");
	}
	
	
	public static void writeData(String outPath, Map<String, String> dataMap) {
		// д�ļ�
		System.out.println(outPath + "  writing !");
		try {
			OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(outPath), "utf-8");
			BufferedWriter writer = new BufferedWriter(writerStream);
			for (String cardId : dataMap.keySet()) {
				String str = dataMap.get(cardId);
				writer.write(str);
				
				writer.write("\n");
				writer.flush();
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		typeMatchUser(userInfoPath, exVehTypeMulPath, matchResPath);
	}

}
