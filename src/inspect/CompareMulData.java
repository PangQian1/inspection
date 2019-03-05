package inspect;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import dao.textSimilar;
import dao.regularExpression;
import divideByReleaser.DividedByCardId;
import inspect.InspectByType;

public class CompareMulData {
	
	private static String sumDataPath = "/home/pq/inspect/intermediateData/sumDataBySever";
	private static String comMulDataPath = "/home/pq/inspect/intermediateData/comMulData/";
	
	private static String userInfoPath = "/home/pq/inspect/registrationInfo/userInfo.csv";
	private static String typeNotConsistSumAllPath = "/home/pq/inspect/resData/typeNotConsistSummaryAll.csv";
	
	/**
	 * 対预处理文件进一步分析，找到每一用户ID对应有不同出口车型以及出口车牌的车辆记录，分别放入不同结果文件
	 * @param path 预处理过的文件路径
	 * @param outPath 结果文件路径
	 */
	public static void compareMulData(String path, String outPath) {

		File file = new File(path);
		List<String> list = Arrays.asList(file.list());
		Map<String, LinkedList<String>> exTypeMap = new HashMap<>();
		//Map<String, LinkedList<String>> exIdMap = new HashMap<>();

		int exTypeNum = 0;
		//int exIdNum = 0;
		
		for (int i = 0; i < list.size(); i++) {
			// 依次处理每一个文件
			String pathIn = path + "/" + list.get(i);
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				String line = "";
				String[] data;
				String exIdSub = "";
				String exVehIdSub = "";
				
				while ((line = reader.readLine()) != null) {
					data = line.split("\\|");
					
					boolean f1 = true;
					boolean exTypeFlag = false;
					String exType = "-1";
					boolean f2 = true;
					boolean exIdFlag = false;
					String exId = "-1";
					
					for(int j=0; j<data.length; j++){
						String[] trace = data[j].split(",", 10);
						if(trace.length == 10){
							String cardId = trace[1];// 用户卡编号
							String enVehId = trace[4];// 入口实际收费车牌号码
							String exVehId = trace[5];// 出口实际收费车牌号码
							String idenVehId = trace[6];// 出口识别收费车牌号码
							String enVehType = trace[7];// 入口收费车型
							String exVehType = trace[8];// 出口收费车型
							
						/*	if(enVehType.length()>4 || exVehType.length()>4){
								break;
							}
							
							if((!enVehType.equals("null") && Integer.valueOf(enVehType) > 4) || (!exVehType.equals("null") && Integer.valueOf(exVehType) > 4)){
								break;
							}*/
							
							if(exVehType.length()>4){
								continue;
							}
							
							//进一步筛选，若出现大于4型车的情况，则pass掉
						/*	if(!exVehType.equals("null") && Integer.valueOf(exVehType) > 4){
								break;
							}*/
							
							//比对出口车型不一致情况
							if(!exTypeFlag && !exVehType.equals("null") && !exVehType.equals("0")){
								exTypeFlag = true;
								exType = exVehType;
							}
								
	
							if (exTypeFlag && f1 && !exVehType.equals("null") && !exType.equals(exVehType) && !exVehType.equals("0")) {
								if (exTypeMap.containsKey(cardId)) {
									LinkedList<String> listTrace = exTypeMap.get(cardId);
									listTrace.add(line);
									exTypeMap.put(cardId, listTrace);
								} else {
									LinkedList<String> listTrace = new LinkedList<>();
									listTrace.add(line);
									exTypeMap.put(cardId, listTrace);
	
									exTypeNum++;
								}
								f1 = false;
							}
							
							
							/*//比对出口实际车牌不一致情况
							if(!exIdFlag && exVehId.length()==9 && !exVehId.substring(2, 7).equals("00000") &&
									!exVehId.substring(2, 7).equals("12345") && !exVehId.substring(0, 2).equals("苏W") &&
									regularExpression.isLetterDigitOrChinese(exVehId.substring(0, 7))){
								exIdFlag = true;
								exId = exVehId;
							}
							
							if(exIdFlag && f2 && exVehId.length()==9 && !exVehId.substring(2, 7).equals("00000") && 
									!exVehId.substring(2, 7).equals("12345") && !exVehId.substring(0, 2).equals("苏W") &&
									regularExpression.isLetterDigitOrChinese(exVehId.substring(0, 7))){
								
								exIdSub = exId.substring(0, 7);
								exVehIdSub = exVehId.substring(0, 7);
								
								if(textSimilar.xiangsidu(exIdSub, exVehIdSub)<0.8){
									if (exIdMap.containsKey(cardId)) {
										LinkedList<String> listTrace = exIdMap.get(cardId);
										listTrace.add(line);
										exIdMap.put(cardId, listTrace);
									} else {
										LinkedList<String> listTrace = new LinkedList<>();
										listTrace.add(line);
										exIdMap.put(cardId, listTrace);
	
										exIdNum++;
									}
									f2 = false;
								}
							}*/
						
						}
					}
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(pathIn + " read finish!");
		}
		
		System.out.println("出口车型不一致情况共找到" + exTypeNum + "张etc卡。");
		//System.out.println("出口实际车牌不一致情况共找到" + exIdNum + "张etc卡。");
		
		//写结果
		String exVehTypeMulPath = outPath + "exVehTypeMulRes.csv";
		//String exVehIdMulPath = outPath + "exVehIdMulRes.csv";
		
		ComparePerData.writeData(exVehTypeMulPath, exTypeMap);
		//ComparePerData.writeData(exVehIdMulPath, exIdMap);
		
		System.out.println("******************多条数据比对完毕*************");
	}
	
	public static void generateSummaryTabAll(String sumDataPath, String userInfoPath, String outPath){
		
		File file = new File(sumDataPath);
		List<String> list = Arrays.asList(file.list());

		File userInfoFile = new File(userInfoPath);
		
		Map<String, ArrayList<String>> userInfoMap = new HashMap<>();
		Map<String, ArrayList<String>> summaryTalMap = new HashMap<>();
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(userInfoFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				
				String cardId = data[0];// 用户卡编号
				String exId = data[1];// 注册车牌
				String exType = data[2];// 注册车型
				
				if(!exType.equals("null")){
					ArrayList<String> userList = new ArrayList<>();
					userList.add(exId);
					userList.add(exType);
					userInfoMap.put(cardId, userList);
				}
			}
			reader.close();
			
			System.out.println(userInfoPath + " read finish!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		

		for (int i = 0; i < list.size(); i++) {
			// 依次处理每一个文件
			String pathIn = sumDataPath + "/" + list.get(i);
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				String line = "";
				String[] data;
				
				while ((line = reader.readLine()) != null) {
					//针对每个id进行判断
					data = line.split("\\|");
					//String OBUId_1 = "";
					//String OBUId_2 = "";
					
					for(int j=0; j<data.length; j++){
						String[] trace = data[j].split(",", 10);
	
						String cardId = trace[1];// 用户卡编号
						String OBUId = trace[3];//OBU编号
						String exVehType = trace[8];// 出口收费车型	
						
						if(userInfoMap.containsKey(cardId)){
	
							if (!exVehType.equals("null")) {
								
								ArrayList<String> info = userInfoMap.get(cardId);
								String vehId = info.get(0);//注册车牌
								int r_type = Integer.parseInt(info.get(1));//注册车型
								int a_type = Integer.parseInt(exVehType);//实际车型
								
								String proNum = cardId.substring(0, 2);
								String proName = DividedByCardId.getCardIdRealeaser(proNum);
								
								if(summaryTalMap.containsKey(cardId)){
									ArrayList<String> content = summaryTalMap.get(cardId);

									if(a_type == r_type){//注册车型和实际车型一致的情况
										int c = Integer.parseInt(content.get(4));
										c++;
										content.set(4, c + "");
									}else{
										int c = Integer.parseInt(content.get(5));
										c++;
										content.set(5, c + "");
									}
									
									if(!OBUId.equals("null") && !OBUId.equals("")){
										
										if(content.get(6).equals(OBUId)){
											int c = Integer.parseInt(content.get(7));
											c++;
											content.set(7, c + "");
										}else if(!content.get(6).equals(OBUId) && content.get(6).equals("")){
											content.set(6, OBUId);
											int c = Integer.parseInt(content.get(7));
											c++;
											content.set(7, c + "");
										}else if(content.get(8).equals(OBUId)){
											int c = Integer.parseInt(content.get(9));
											c++;
											content.set(9, c + "");
										}else if(!content.get(8).equals(OBUId) && content.get(8).equals("")){
											content.set(8, OBUId);
											int c = Integer.parseInt(content.get(9));
											c++;
											content.set(9, c + "");
										}
										
									}
											
									summaryTalMap.put(cardId, content);
								}else{
									ArrayList<String> content = new ArrayList<>();
									content.add("'" + cardId);
									content.add(vehId);
									content.add(proName);
									content.add(r_type + "");
									
									if(a_type == r_type){//注册车型和实际车型一致的情况
										content.add("1");
										content.add("0");
									}else{
										content.add("0");
										content.add("1");
									}	
									
									if(OBUId.equals("null") || OBUId.equals("")){
										content.add("");
										content.add("0");
										content.add("");
										content.add("0");
									}else{
										content.add(OBUId);
										content.add("1");
										content.add("");
										content.add("0");
									}
									summaryTalMap.put(cardId, content);
								}

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
		
		writeTab(outPath, summaryTalMap);
	}
	
	public static void generateSummaryTab(String mulDataPath, String userInfoPath, String outPath){
		
		File mulDataFile = new File(mulDataPath);
		File userInfoFile = new File(userInfoPath);
		
		Map<String, ArrayList<String>> userInfoMap = new HashMap<>();
		Map<String, String> summaryTalMap = new HashMap<>();
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(userInfoFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			while ((line = reader.readLine()) != null) {
				data = line.split(",");
				
				String cardId = data[0];// 用户卡编号
				String exId = data[1];// 注册车牌
				String exType = data[2];// 注册车型
				
				if(!exType.equals("null")){
					ArrayList<String> userList = new ArrayList<>();
					userList.add(exId);
					userList.add(exType);
					userInfoMap.put(cardId, userList);
				}
			}
			reader.close();
			
			System.out.println(userInfoPath + " read finish!");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		try {		
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(mulDataFile), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);
			
			String line = "";
			String[] data;
			
			while ((line = reader.readLine()) != null) {
				//针对每个id进行判断
				data = line.split("\\|");
				
				int consistCount = 0;
				int notConsistCount = 0;
				String str = "";
				
				for(int j=0; j<data.length; j++){
					String[] trace = data[j].split(",", 10);

					String cardId = trace[1];// 用户卡编号
					String exVehType = trace[8];// 出口收费车型	
					
					if(userInfoMap.containsKey(cardId)){

						if (!exVehType.equals("null")) {
							
							ArrayList<String> list = userInfoMap.get(cardId);
							String vehId = list.get(0);//注册车牌
							int r_type = Integer.parseInt(list.get(1));//注册车型
							int a_type = Integer.parseInt(exVehType);//实际车型
							
							if(a_type == r_type){//注册车型和实际车型一致的情况
								consistCount++;
							}else{
								notConsistCount++;
							}

							String proNum = cardId.substring(0, 2);
							String proName = DividedByCardId.getCardIdRealeaser(proNum);
							
							if((j == data.length-1)){
								str = cardId + "," + vehId + "," + proName + "," + r_type + "," + consistCount + "," + notConsistCount;
								summaryTalMap.put(cardId, str);
							}			
						}
					}else{
						break;
					}
	
					
				}
			}
			
			reader.close();
			
			InspectByType.writeData(outPath, summaryTalMap);
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
	
	public static void writeTab(String outPath, Map<String, ArrayList<String>> dataMap) {
		// 写文件
		System.out.println(outPath + "  writing !");
		try {
			OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(outPath), "utf-8");
			BufferedWriter writer = new BufferedWriter(writerStream);
			for (String cardId : dataMap.keySet()) {
				ArrayList<String> listTrace = dataMap.get(cardId);
				if(!listTrace.get(5).equals("0")){
					for (int j = 0; j < listTrace.size(); j++) {
						if (j == listTrace.size() - 1) {
							writer.write(listTrace.get(j));
							break;
						}
						writer.write(listTrace.get(j) + ",");
					}
				}else{
					continue;
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
		
		//compareMulData(sumDataPath, comMulDataPath);
		generateSummaryTabAll(sumDataPath, userInfoPath, typeNotConsistSumAllPath);
		
	}

}
