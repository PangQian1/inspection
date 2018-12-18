package inspect;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class CompareMulData {
	
	public static String dataPath = "/home/pq/inspect/intermediateData/sumDataBySever";

	public static String exVehTypeMulPath = "/home/pq/inspect/intermediateData/comMulData/exVehTypeMulRes.csv";
	
	public static void compareMulData(String path, String out) {

		File file = new File(path);
		List<String> list = Arrays.asList(file.list());
		Map<String, LinkedList<String>> cardIDMap = new HashMap<>();

		int exTypeNum = 0;
		
		for (int i = 0; i < list.size(); i++) {
			// ���δ���ÿһ���ļ�
			String pathIn = path + "/" + list.get(i);
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				String line = "";
				String[] data;
				while ((line = reader.readLine()) != null) {
					data = line.split("|");
					
					boolean exTypeFlag = false;
					String exType = "-1";
					
					for(int j=0; j<data.length; j++){
						String[] trace = data[j].split(",", 10);

						String cardId = data[1];// �û������
						String enVehId = data[4];// ���ʵ���շѳ��ƺ���
						String exVehId = data[5];// ����ʵ���շѳ��ƺ���
						String idenVehId = data[6];// ����ʶ���շѳ��ƺ���
						String enVehType = data[7];// ����շѳ���
						String exVehType = data[8];// �����շѳ���
						
						//�ȶԳ��ڳ��Ͳ�һ�����
						if(!exTypeFlag && !exVehType.equals("null")){
							exTypeFlag = true;
							exType = exVehType;
						}
							

						if (exTypeFlag && !exType.equals(exVehType)) {
							if (cardIDMap.containsKey(cardId)) {
								LinkedList<String> listTrace = cardIDMap.get(cardId);
								listTrace.add(line);
								cardIDMap.put(cardId, listTrace);
							} else {
								LinkedList<String> listTrace = new LinkedList<>();
								listTrace.add(line);
								cardIDMap.put(cardId, listTrace);

								exTypeNum++;
							}
						}
						
						
						//�ȶԳ���ʵ�ʳ�Ƣ��һ�����
					}
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(pathIn + " read finish!");
			
			/*int index = pathIn.indexOf("success");
			if(pathIn.substring(index-3,index-1).equals("31")){
				
				String sub = pathIn.substring(index + 7, index + 11);
				String outPath = out + "/2018-08" + sub + ".csv";
				preComparison.writePreExData(outPath, cardIDMap);
				
				cardIDMap = new HashMap<>();
				System.out.println(sub.substring(1) + "���ֹ��ҵ�" + etcCarNum + "��etc����");
				etcCarNum = 0;
			}*/
		}
		
		
		ComparePerData.writeData(out, cardIDMap);
		
	}
	
	
	
	public static void main(String[] args) {
		
		compareMulData(dataPath, exVehTypeMulPath);
		System.out.println("end");
	}

}
