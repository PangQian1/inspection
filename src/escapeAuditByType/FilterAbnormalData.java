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

public class FilterAbnormalData {
	
	private static String userInfoPath = "/home/pq/inspect/registrationInfo/userInfo.csv";
	private static String originPath = "/home/pq/inspect/resData/dividedByCardId/origin/";
	private static String sumDataPath = "/home/pq/inspect/intermediateData/sumDataBySever";
	
	public static void main(String[] args) {
		filterAbnormalData(sumDataPath, userInfoPath, originPath);
	}
	
	public static void filterAbnormalData(String sumDataPath, String userInfoPath, String outPath) {
		
		File file = new File(sumDataPath);
		List<String> list = Arrays.asList(file.list());
		
		Map<String, Map<String, String>> abnormalRecordMap = new HashMap<>();//ʡ��-cardId-ע����Ϣ+ͨ�м�¼
		Map<String, ArrayList<String>> userInfoMap = MatchUserInfo.matchUserInfo(userInfoPath);
		
		for (int i = 0; i < list.size(); i++) {
			// ���δ���ÿһ���ļ�
			String pathIn = sumDataPath + "/" + list.get(i);
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				String line = "";
				String[] data;
				
				while ((line = reader.readLine()) != null) {
					//���ÿ��id�����ж�
					data = line.split("\\|");
					
					for(int j=0; j<data.length; j++){
						String[] trace = data[j].split(",", 10);
	
						String cardId = trace[1];// �û������
						String exVehType = trace[8];// �����շѳ���	
						
						if(userInfoMap.containsKey(cardId)){
	
							if (!exVehType.equals("null")) {
								
								ArrayList<String> info = userInfoMap.get(cardId);
								String vehId = info.get(0);//ע�ᳵ��
								int r_type = Integer.parseInt(info.get(1));//ע�ᳵ��
								int a_type = Integer.parseInt(exVehType);//ʵ�ʳ���
								
								if(r_type == a_type) {
									continue;
								}
								
								if(r_type > 4) {
									break;
								}
								
								String proNum = cardId.substring(0, 2);
								String province = DividedByCardId.getCardIdRealeaser(proNum);
								
								if (abnormalRecordMap.containsKey(province)) {
									Map<String, String> traceMap = abnormalRecordMap.get(province);
									
									if(traceMap.containsKey(cardId)) {
										String traceList = traceMap.get(cardId);
										traceList += "|" + line;
										traceMap.put(cardId, traceList);
									}else {
										String traceList = cardId + "," + vehId + "," + r_type + "|" + line;		
										traceMap.put(cardId, traceList);
									}
									
									abnormalRecordMap.put(province, traceMap);
								} else {
									Map<String, String> traceMap = new HashMap<>();		
									String traceList = cardId + "," + vehId + "," + r_type + "|" + line;		
									traceMap.put(cardId, traceList);
									abnormalRecordMap.put(province, traceMap);
								}	
								
								break;

							}
						}else{
							break;
						}	
					}
				}
			
				reader.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			System.out.println(pathIn + "read finish!");
		}
		
		// д����
		writeRecordDividePro(abnormalRecordMap, outPath);
		
		System.out.println("*************��ʡ����*************");
		
	}

	public static void writeRecordDividePro(Map<String, Map<String, String>> dataMap, String outPath) {
		// д�ļ�
		System.out.println(outPath + "  writing !");
		try {

			for (String province : dataMap.keySet()) {
				
				Map<String, String> traceMap = dataMap.get(province);
				String path = outPath + province + "_" + traceMap.size() + ".csv";
				OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(path), "utf-8");
				BufferedWriter writer = new BufferedWriter(writerStream);
				
				for (String cardId : traceMap.keySet()) {
					String traceList = traceMap.get(cardId);
					writer.write(traceList);
					writer.write("\n");	
				}
			
				writer.flush();
				writer.close();

				System.out.println(path + " write end.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


}
