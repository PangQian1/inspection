package inspect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class TypeSpiltByPro {
	
	private static String matchResPath = "/home/pq/inspect/resData/exTypeMatchUserRes.csv";
	private static String splitProPath = "/home/pq/inspect/resData/splitByPro/";
	
/*	private static String matchResPath = "H:/pangqian/res/exTypeMatchUserRes.csv";
	private static String splitProPath = "H:/pangqian/res/splitByPro/";*/

	/**
	 * �Խ���ļ�����ʡ�ݻ��ִ���
	 */
	public static void splitPro(String inPath, String outPath) {
		Map<String, ArrayList<String>> ProvinceMap = new HashMap<>();

		// ���ļ�
		try {
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(inPath), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);

			String line = "";
			String[] lineArray;
			while ((line = reader.readLine()) != null) {
				lineArray = line.split("\\|");

				String[] data  = lineArray[0].split(",");
				String cardId  = data[0];// �û������
				String exVehId = data[1];// ����ʵ���շѳ��ƺ���
				String provinceShort = exVehId.substring(0, 1);
				
				String province = getProvinceName(provinceShort);
				
				if (province.isEmpty()) {
					System.out.println("�����쳣ʡ�ݣ���");
					continue;
				}

				if (ProvinceMap.containsKey(province)) {
					ArrayList<String> listTrace = ProvinceMap.get(province);
					int count = Integer.parseInt(listTrace.get(0));
					count++;
					listTrace.set(0, count+"");
					listTrace.add(line);
					ProvinceMap.put(province, listTrace);
				} else {
					ArrayList<String> listTrace = new ArrayList<>();
					listTrace.add("1");
					listTrace.add(line);
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
					writer.write(listTrace.get(j));
					writer.write("\n");
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

	public static String getProvinceName(String ProvinceShort) {
		switch (ProvinceShort) {
		case "��":
			return "������";
		case "��":
			return "�����";
		case "��":
			return "�Ϻ���";
		case "��":
			return "������";
		case "��":
			return "���Ļ���������";
		case "��":
			return "�½�ά���������";
		case "��":
			return "���ɹ�������";
		case "��":
			return "����׳��������";
		case "��":
			return "����������";
		case "��":
			return "����ر�������";
		case "��":
			return "�����ر�������";
		case "��":
			return "�ӱ�ʡ";
		case "��":
			return "ɽ��ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "������ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "�㽭ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "����ʡ";
		case "³":
			return "ɽ��ʡ";
		case "ԥ":
			return "����ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "�㶫ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "�Ĵ�ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "�ຣʡ";
		case "̨":
			return "̨��";
		default:
			return "";
		}
	}


	public static void main(String[] args) {
		
		splitPro(matchResPath, splitProPath);
		
	}

}
