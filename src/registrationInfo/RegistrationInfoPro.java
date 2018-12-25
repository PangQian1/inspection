package registrationInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Date;

import dao.regularExpression;
import dao.textSimilar;
import inspect.ComparePerData;

public class RegistrationInfoPro {
	
	/*private static String cardPath = "/home/pq/inspect/registrationInfo/card";
	private static String vehiclePath = "/home/pq/inspect/registrationInfo/vehicle";
	
	private static String userInfoPath = "/home/pq/inspect/registrationInfo/userInfo.csv";*/
	
	private static String cardPath = "H:/pangqian/card";
	private static String vehiclePath = "H:/pangqian/vehicle";
	
	private static String userInfoPath = "H:/pangqian/userInfo.csv";
	
	public static void regisInfoPro(String cardPath, String vehPath, String outPath){

		File file = new File(cardPath);
		List<String> list = Arrays.asList(file.list());
		Map<String,ArrayList<String>> userInfoMap = new HashMap<>();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//���û�����Ϣ
		for (int i = 0; i < list.size(); i++) {
			// ���δ���ÿһ���ļ�
			String pathIn = cardPath + "/" + list.get(i);
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				String line = "";
				String[] data;
				
				while ((line = reader.readLine()) != null) {
					data = line.split(",", 15);
					
					String cardId = data[0];// �û������
					String vehId = data[6];// ����id
					String time = data[13];// �û���״̬���ʱ��
					String op = data[14];// ������1.���� 2.��� 3.ɾ��
					
					String staChTime = time.substring(0, 10) + " " + time.substring(11);
					
					if (userInfoMap.containsKey(vehId)) {
					
						ArrayList<String> userList = userInfoMap.get(vehId);
						String currTime = userList.get(1);
						
						Date currTimeS = sdf.parse(currTime);
						Date staChTimeS = sdf.parse(staChTime);
						
						if(currTimeS.before(staChTimeS)){
							
							if(Integer.parseInt(op) == 3){
								userInfoMap.remove(vehId);
								break;
							}
							
							userList.set(0, cardId);
							userList.set(1, staChTime);	
							userInfoMap.put(vehId, userList);
						}
						
					}else {			
						if(Integer.parseInt(op) != 3){
							ArrayList<String> userList = new ArrayList<>();
							userList.add(cardId);
							userList.add(staChTime);
							userList.add("null");
							userInfoMap.put(vehId, userList);
						}
					}

				}
				
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(pathIn + " read finish!");
		}
		
		//��������Ϣ
		File file2 = new File(vehPath);
		List<String> list2 = Arrays.asList(file2.list());
		
		for (int i = 0; i < list2.size(); i++) {
			// ���δ���ÿһ���ļ�
			String pathIn = vehPath + "/" + list2.get(i);
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				String line = "";
				String[] data;
				
				while ((line = reader.readLine()) != null) {
					data = line.split(",", 8);
					
					String vehId = data[0];// ����id
					String type = data[1];// �շѳ���
					String op = data[7];// ������1.���� 2.��� 3.ɾ��
					
					if (userInfoMap.containsKey(vehId)) {
						
						ArrayList<String> userList = userInfoMap.get(vehId);		
						userList.set(2, type);
						userInfoMap.put(vehId, userList);
					}		
				}
				
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(pathIn + " read finish!");
		}
		
		System.out.println("���ҵ�" + userInfoMap.size() + "��etc����");
		
		//д���
		writeData(outPath, userInfoMap);
		
		System.out.println("******************�û���Ϣ�������*************");
		
		
	}
	
	public static void writeData(String outPath, Map<String, ArrayList<String>> dataMap) {
		// д�ļ�
		System.out.println(outPath + "  writing !");
		try {
			OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(outPath), "utf-8");
			BufferedWriter writer = new BufferedWriter(writerStream);
			for (String vehId : dataMap.keySet()) {
				ArrayList<String> list = dataMap.get(vehId);
				
				String str = list.get(0) + "," + vehId + "," + list.get(2);
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
		
		regisInfoPro(cardPath, vehiclePath, userInfoPath);
	}

}
