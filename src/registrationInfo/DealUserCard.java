package registrationInfo;

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
import java.util.List;
import java.util.Map;

public class DealUserCard {
	
	private static String cardPath = "/home/pq/inspect/registrationInfo/card";
	
	private static String userCardInfoPath = "/home/pq/inspect/registrationInfo/userCardInfo.csv";
	
	public static void userCardInfo(String cardPath, String outPath){

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
					
					if(time.length() != 19)continue;
					String staChTime = time.substring(0, 10) + " " + time.substring(11);
					
					
					if (userInfoMap.containsKey(vehId)) {
					
						ArrayList<String> userList = userInfoMap.get(vehId);
						String currTime = userList.get(1);
						
						Date currTimeS = sdf.parse(currTime);
						Date staChTimeS = sdf.parse(staChTime);
						
						if(!currTimeS.after(staChTimeS)){							
							userList.set(0, cardId);
							userList.set(1, staChTime);	
							userInfoMap.put(vehId, userList);
						}
						
					}else {								
						ArrayList<String> userList = new ArrayList<>();
						userList.add(cardId);
						userList.add(staChTime);
						userInfoMap.put(vehId, userList);		
					}

				}
				
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			//System.out.println(pathIn + " read finish!");
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
				
				String str = list.get(0) + "," + vehId;
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
		userCardInfo(cardPath, userCardInfoPath);
	}

}
