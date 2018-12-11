package inspect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dao.io;

public class preComparison {

	//private static String oriExData = "H:/pangqian/data/oriExData";// 原始出口数据
	//private static String preOriExDataRes = "H:/pangqian/data/preOriDataRes.csv";// 初步结果

	private static String oriExData = "/home/highwaytransaction/extransaction/2018-08";// 原始出口数据
	private static String preProExData = "/home/pq/inspect/intermediateData/sumDataBySever/2018-08";// 初步结果(划分为五部分)
	private static String preProExDataDir = "/home/pq/inspect/intermediateData/sumDataBySever";// 初步结果(划分为五部分)
	private static String prefiltExData = "/home/pq/inspect/intermediateData/comPerData/comPerData_2018-08";// 对8月份数据单条比对得到的不一致数据
	
	
	/**
	 * 对原始出口数据进行处理，初步找出异常数据
	 * 
	 * @param path
	 *            原始出口数据路径
	 * @param out
	 *            处理结果存放位置
	 */
	public static void preProcess(String path, String out) {

		File file = new File(path);
		List<String> list = Arrays.asList(file.list());
		Map<String, LinkedList<String>> cardIDMap = new HashMap<>();

		int etcCarNum = 0;
		//int count = 0;
		
		for (int i = 0; i < list.size(); i++) {
			// 依次处理每一个文件
			String pathIn = path + "/" + list.get(i);
			
			//count++;
			//if(count < 124) continue;
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				String line = "";
				String[] data;
				while ((line = reader.readLine()) != null) {
					data = line.split(",", 26);
					
					if(data.length != 26) continue;
					
					String type = data[0];// 交易类型，现金or非现金
					String id = data[1];// 交易编号
					String fee = data[3];// 交易金额
					String cardId = data[8];// 用户卡编号
					String OBUId = data[10];// OBU编号
					String enVehId = data[11];// 入口实际收费车牌号码
					String exVehId = data[12];// 出口实际收费车牌号码
					String idenVehId = data[13];// 出口识别收费车牌号码
					String enVehType = data[14];// 入口收费车型
					String exVehType = data[15];// 出口收费车型
					String payType = data[25];// 支付类型

					if (type.equals("1") && ((enVehType!=null && Integer.valueOf(enVehType) < 5) || (exVehType!=null && Integer.valueOf(exVehType) < 5))) {
						if (cardIDMap.containsKey(cardId)) {
							LinkedList<String> listTrace = cardIDMap.get(cardId);
							listTrace.add(id + ","+ cardId +"," + fee + "," + OBUId + "," + enVehId + "," + exVehId + "," + idenVehId
									+ "," + enVehType + "," + exVehType + "," + payType);
							cardIDMap.put(cardId, listTrace);
						} else {
							LinkedList<String> listTrace = new LinkedList<>();
							listTrace.add(id + ","+ cardId +"," + fee + "," + OBUId + "," + enVehId + "," + exVehId + "," + idenVehId
									+ "," + enVehType + "," + exVehType + "," + payType);
							cardIDMap.put(cardId, listTrace);

							etcCarNum++;
						}
					}
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(pathIn + " read finish!");
			
			int index = pathIn.indexOf("success");
			if(pathIn.substring(index-3,index-1).equals("31")){
				
				String sub = pathIn.substring(index + 7, index + 11);
				String outPath = out + sub + ".csv";
				writePreExData(outPath, cardIDMap);
				
				cardIDMap = new HashMap<>();
				System.out.println(sub.substring(1) + "部分共找到" + etcCarNum + "张etc卡。");
				etcCarNum = 0;
			}
		}
		
	}
	
	public static void writePreExData(String outPath, Map<String, LinkedList<String>> cardIDMap){

		try {
			OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(outPath),"utf-8");
			BufferedWriter writer = new BufferedWriter(writerStream);
			for (String cardId : cardIDMap.keySet()) {
				LinkedList<String> listTrace = cardIDMap.get(cardId);
				for (int j = 0; j < listTrace.size(); j++) {
					if(j == listTrace.size()-1){
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
	
	public static void preFilter(String in, String out) {

		File file = new File(in);
		List<String> list = Arrays.asList(file.list());
		Map<String, LinkedList<String>> cardIDMap = new HashMap<>();

		int etcCarNum = 0;
		
		for (int i = 0; i < list.size(); i++) {
			// 依次处理每一个文件
			String pathIn = in + "/" + list.get(i);
		
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				String line = "";
				String[] data;
				String[] perData;
				
				while ((line = reader.readLine()) != null) {
					data = line.split("\\|");
					String cardId;
					for(int j = 0; j < data.length; j++){
						
						perData = data[j].split(",", 10);
						cardId = perData[1];
						if(!(perData[4].equals(perData[5]) && perData[5].equals(perData[6]) && perData[7].equals(perData[8]))){
							
							if (cardIDMap.containsKey(cardId)) {
								LinkedList<String> listTrace = cardIDMap.get(cardId);
								listTrace.add(line);
								cardIDMap.put(cardId, listTrace);
							} else {
								LinkedList<String> listTrace = new LinkedList<>();
								listTrace.add(line);
								cardIDMap.put(cardId, listTrace);

								etcCarNum++;
							}
							
							break;
						}
					}
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println(pathIn + " read finish!");
			String outPath = out + "_00" + i + ".csv";
			writePreExData(outPath, cardIDMap);
			System.out.println("第" + i + "部分共找到" + etcCarNum + "张etc卡存在单条数据不一致情况。");
			etcCarNum = 0;
		}
		
		
	}
	
	
	
	
	
	public static void main(String[] args) {

		//preProcess(oriExData, preProExData);
		preFilter(preProExDataDir, prefiltExData);
		System.out.println("end");
	}

}
