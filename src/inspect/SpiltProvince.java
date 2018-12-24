package inspect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class SpiltProvince {

	public static String DataPath   = "/home/pq/inspect/intermediateData/comMulData/exVehTypeMulRes.csv";
	public static String ResultPath = "/home/pq/inspect/intermediateData/comMulData/��ʡ.csv";

	/**
	 * ��ԭʼ�������ݽ��д��������ҳ��쳣����
	 */
	public static void preProcess() {
		Map<String, LinkedList<String>> ProvinceMap = new HashMap<>();

		// ���ļ�
		try {
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(DataPath), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);

			String line = "";
			String[] lineArray;
			while ((line = reader.readLine()) != null) {
				lineArray = line.split("\\|");
				for (String item : lineArray) {
					String[] data  = item.split(",");
					String cardId  = data[1];// �û������
					String exVehId = data[5];// ����ʵ���շѳ��ƺ���
					String provinceShort = exVehId.substring(0, 1);
					
					String province = getProvinceName(provinceShort);
					if (province.isEmpty()) {
						continue;
					}

					if (ProvinceMap.containsKey(provinceShort)) {
						LinkedList<String> listTrace = ProvinceMap.get(provinceShort);
						listTrace.add(cardId);
						ProvinceMap.put(provinceShort, listTrace);
					} else {
						LinkedList<String> listTrace = new LinkedList<>();
						listTrace.add(cardId);
						ProvinceMap.put(provinceShort, listTrace);
					}
					break;
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(DataPath + " read finish!");

		// д����
		writeData(ResultPath, ProvinceMap);
	}

	public static void writeData(String outPath, Map<String, LinkedList<String>> dataMap) {
		// д�ļ�
		System.out.println(outPath + "  writing !");
		try {
			OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(outPath), "utf-8");
			BufferedWriter writer = new BufferedWriter(writerStream);
			for (String provinceShort : dataMap.keySet()) {
				LinkedList<String> listTrace = dataMap.get(provinceShort);
				String str = getProvinceName(provinceShort) + "," + listTrace.size() + ",";
				System.out.println(provinceShort);
				writer.write(str);
				System.out.println(str);
				for (int j = 0; j < listTrace.size(); j++) {
					writer.write(listTrace.get(j) + ",");
				}
				writer.write("\n");
				writer.flush();
			}
			writer.close();
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
			return "�ӱ�ʡ";
		case "��":
			return "ɽ��ʡ";
		case "��":
			return "���ɹ�������";
		case "��":
			return "����ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "������ʡ";
		case "��":
			return "�Ϻ���";
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
			return "����׳��������";
		case "��":
			return "����ʡ";
		case "��":
			return "������";
		case "��":
			return "�Ĵ�ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "����������";
		case "��":
			return "����ʡ";
		case "��":
			return "����ʡ";
		case "��":
			return "�ຣʡ";
		case "��":
			return "���Ļ���������";
		case "��":
			return "�½�ά���������";
		case "��":
			return "����ر�������";
		case "��":
			return "�����ر�������";
		case "̨":
			return "̨��ʡ";
		default:
			return "";
		}
	}

	public static void main(String[] args) {
		preProcess();
	}
}
