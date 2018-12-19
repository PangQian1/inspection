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
import dao.textSimilar;
import dao.regularExpression;

public class ComparePerData {

	public static String dataPath = "/home/pq/inspect/intermediateData/comPerDataExcNull/comPerDataExcNull_2018-08.csv";

	public static String EnExIDPath = "/home/pq/inspect/intermediateData/comPerDataClass/EnExIdMatchRes.csv";
	public static String ExIdenIDPath = "/home/pq/inspect/intermediateData/comPerDataClass/ExIdenIdMatchRes.csv";
	public static String VehicleTypePath = "/home/pq/inspect/intermediateData/comPerDataClass/vehicleTypeMatchRes.csv";

	/**
	 * 对原始出口数据进行处理，初步找出异常数据
	 */
	public static void comparePerData() {

		Map<String, LinkedList<String>> EnExIDMap      = new HashMap<>();
		Map<String, LinkedList<String>> ExIdenIDMap    = new HashMap<>();
		Map<String, LinkedList<String>> VehicleTypeMap = new HashMap<>();

		int EnExNumber = 0;
		int ExIdenNumber = 0;
		int VehicleTypeNumber = 0;
		
		// 读文件
		try {
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(dataPath), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);

			String line = "";
			String[] lineArray;
			String exVehIdSub = "";
			String enVehIdSub = "";
			String idenVehIdSub = "";
			
			while ((line = reader.readLine()) != null) {
				lineArray = line.split("\\|");
				for (String item : lineArray) {

					String[] data = item.split(",");
					String cardId = data[1];// 用户卡编号
					String enVehId = data[4];// 入口实际收费车牌号码
					String exVehId = data[5];// 出口实际收费车牌号码
					String idenVehId = data[6];// 出口识别收费车牌号码
					String enVehType = data[7];// 入口收费车型
					String exVehType = data[8];// 出口收费车型

					if(enVehType.length()>4 || exVehType.length()>4){
						break;
					}
					
					if((enVehType!=null && Integer.valueOf(enVehType) > 4) || (exVehType!=null && Integer.valueOf(exVehType) > 4)){
						break;
					}					
					
					// 比较入口收费车型和出口收费车型
					if (!enVehType.equals(exVehType) && !enVehType.equals("0") && !exVehType.equals("0")) {
						if (VehicleTypeMap.containsKey(cardId)) {
							LinkedList<String> listTrace = VehicleTypeMap.get(cardId);
							listTrace.add(item);
							VehicleTypeMap.put(cardId, listTrace);
						} else {
							LinkedList<String> listTrace = new LinkedList<>();
							listTrace.add(item);
							VehicleTypeMap.put(cardId, listTrace);
							VehicleTypeNumber++;
						}
					}

					// 出口实际收费车牌情况判断
					if (exVehId.equals("null") || exVehId.length() < 9 || exVehId.substring(2, 7).equals("00000") || 
							!regularExpression.isLetterDigitOrChinese(exVehId.substring(0, 7))) { 
						continue;
					}
					
					// 入口实际收费车牌，出口实际收费车牌
					if (!enVehId.equals("null") && enVehId.length() > 8 && !enVehId.substring(2, 7).equals("00000") && 
							regularExpression.isLetterDigitOrChinese(enVehId.substring(0, 7))) {
						// 排除 NULL 默认车牌
						
						enVehIdSub = enVehId.substring(0, 7);
						exVehIdSub = exVehId.substring(0, 7);
						
						if (textSimilar.xiangsidu(enVehIdSub, exVehIdSub)<0.8) {
							if (EnExIDMap.containsKey(cardId)) {
								LinkedList<String> listTrace = EnExIDMap.get(cardId);
								listTrace.add(item);
								EnExIDMap.put(cardId, listTrace);
							} else {
								LinkedList<String> listTrace = new LinkedList<>();
								listTrace.add(item);
								EnExIDMap.put(cardId, listTrace);
								EnExNumber++;
							}
						}
					}

					// 出口实际收费车牌，出口识别收费车牌
					if (!idenVehId.equals("null") && idenVehId.length() > 8 && !idenVehId.substring(2, 7).equals("00000") && 
							regularExpression.isLetterDigitOrChinese(idenVehId.substring(0, 7))) {
						// 排除 NULL 默认车牌
						
						exVehIdSub = exVehId.substring(0, 7);
						idenVehIdSub = idenVehId.substring(0, 7);
						
						if (textSimilar.xiangsidu(exVehIdSub,idenVehIdSub)<0.8) {
							if (ExIdenIDMap.containsKey(cardId)) {
								LinkedList<String> listTrace = ExIdenIDMap.get(cardId);
								listTrace.add(item);
								ExIdenIDMap.put(cardId, listTrace);
							} else {
								LinkedList<String> listTrace = new LinkedList<>();
								listTrace.add(item);
								ExIdenIDMap.put(cardId, listTrace);
								ExIdenNumber++;
							}
						}
					}
				}
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(dataPath + " read finish!");

		System.out.println("入口收费车牌 != 出口收费车牌 数量： " + EnExNumber);
		System.out.println("出口收费车牌 != 出口识别车牌 数量： " + ExIdenNumber);
		System.out.println("入口收费车型 != 出口收费车型 数量： " + VehicleTypeNumber);
		
		// 写数据
		writeData(EnExIDPath, EnExIDMap);
		writeData(ExIdenIDPath, ExIdenIDMap);
		writeData(VehicleTypePath, VehicleTypeMap);
	}

	public static void writeData(String outPath, Map<String, LinkedList<String>> dataMap) {
		// 写文件
		System.out.println(outPath + "  writing !");
		try {
			OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(outPath), "utf-8");
			BufferedWriter writer = new BufferedWriter(writerStream);
			for (String cardId : dataMap.keySet()) {
				LinkedList<String> listTrace = dataMap.get(cardId);
				for (int j = 0; j < listTrace.size(); j++) {
					if (j == listTrace.size() - 1) {
						writer.write(listTrace.get(j));
						break;
					}
					writer.write(listTrace.get(j) + "|");
				}
				writer.write("\n");
				writer.flush();
			}
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		comparePerData();
	}
}
