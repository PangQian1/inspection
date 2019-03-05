package divideByReleaser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import escapeAuditByType.MatchUserInfo;

public class DividedByCardId {
	
	private static String inPath = "I:/����/exVehTypeMulRes.csv";
	private static String outPath = "I:/����/���û����Ż���ʡ��/";
	
	public static void main(String[] args) {
		//splitProByCardId(inPath, outPath);
	}
	
	public static String getCardIdRealeaser(String ProvinceCode) {
		switch (ProvinceCode) {
		case "05":
			return "����";
		case "11":
			return "������";
		case "12":
			return "�����";
		case "31":
			return "�Ϻ���";
		case "50":
			return "������";
		case "64":
			return "���Ļ���������";
		case "65":
			return "�½�ά���������";
		case "15":
			return "���ɹ�������";
		case "45":
			return "����׳��������";
		case "13":
			return "�ӱ�ʡ";
		case "14":
			return "ɽ��ʡ";
		case "21":
			return "����ʡ";
		case "22":
			return "����ʡ";
		case "23":
			return "������ʡ";
		case "32":
			return "����ʡ";
		case "33":
			return "�㽭ʡ";
		case "34":
			return "����ʡ";
		case "35":
			return "����ʡ";
		case "36":
			return "����ʡ";
		case "37":
			return "ɽ��ʡ";
		case "41":
			return "����ʡ";
		case "42":
			return "����ʡ";
		case "43":
			return "����ʡ";
		case "44":
			return "�㶫ʡ";
		case "51":
			return "�Ĵ�ʡ";
		case "52":
			return "����ʡ";
		case "53":
			return "����ʡ";
		case "61":
			return "����ʡ";
		case "62":
			return "����ʡ";
		case "63":
			return "�ຣʡ";
		default:
			return "";
		}
	}
	
	/**
	 * �Խ���ļ�����ʡ�ݻ��ִ���
	 */
	public static void splitProByCardId(String userInfoPath, String inPath, String outPath) {
		Map<String, ArrayList<String>> ProvinceMap = new HashMap<>();
		Map<String, ArrayList<String>> userInfoMap = MatchUserInfo.matchUserInfo(userInfoPath);

		// ���ļ�
		try {
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(inPath), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);

			String line = "";
			String[] lineArray;
			while ((line = reader.readLine()) != null) {
				lineArray = line.split("\\|");

				String[] data  = lineArray[0].split(",");
				String cardId  = data[1];// �û������
				String provinceCode = cardId.substring(0, 2);
				ArrayList<String> userInfo;
				
				if(userInfoMap.containsKey(cardId)){
					userInfo = userInfoMap.get(cardId);
				}else{
					continue;
				}
				
				//System.out.println(provinceCode);
				String province = getCardIdRealeaser(provinceCode);
				
				if (province.isEmpty()) {
					System.out.println("�����쳣ʡ�ݣ���");
					continue;
				}

				if (ProvinceMap.containsKey(province)) {
					ArrayList<String> listTrace = ProvinceMap.get(province);
					int count = Integer.parseInt(listTrace.get(0));
					count++;
					listTrace.set(0, count+"");
					String info = "," + userInfo.get(0) + "," + userInfo.get(1) + "\\|";
					listTrace.add(line.replaceAll("\\|", info) + "," + userInfo.get(0) + "," + userInfo.get(1));
					ProvinceMap.put(province, listTrace);
				} else {
					ArrayList<String> listTrace = new ArrayList<>();
					listTrace.add("1");
					String info = "," + userInfo.get(0) + "," + userInfo.get(1) + "\\|";
					listTrace.add(line.replaceAll("\\|", info) + "," + userInfo.get(0) + "," + userInfo.get(1));
					ProvinceMap.put(province, listTrace);
				}				
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(inPath + " read finish!");

		// д����
		writeData(outPath, ProvinceMap);
		
		System.out.println("*************��ʡ����*************");
	}

	public static void writeData(String outPath, Map<String, ArrayList<String>> dataMap) {
		// д�ļ�
		System.out.println(outPath + "  writing !");
		int sum = 0;
		try {		
			for (String province : dataMap.keySet()) {
				
				ArrayList<String> listTrace = dataMap.get(province);
				String path = outPath + province + "_" + listTrace.get(0) + ".csv";
				OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(path), "utf-8");
				BufferedWriter writer = new BufferedWriter(writerStream);
			
				for (int j = 1; j < listTrace.size(); j++) {
					String[] data = listTrace.get(j).split("\\|");
					for(int i = 0; i < data.length; i++) {
						writer.write(data[i]);
						writer.write("\n");
					}
					sum++;
				}
			
				writer.flush();
				writer.close();

				System.out.println(path + " write end.");
			}
			System.out.println("������" + sum + "����¼");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


}
