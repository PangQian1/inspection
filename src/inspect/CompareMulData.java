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
import dao.textSimilar;
import dao.regularExpression;

public class CompareMulData {
	
	private static String sumDataPath = "/home/pq/inspect/intermediateData/sumDataBySever";
	private static String comMulDataPath = "/home/pq/inspect/intermediateData/comMulData/";
	
	public static void compareMulData(String path, String outPath) {

		File file = new File(path);
		List<String> list = Arrays.asList(file.list());
		Map<String, LinkedList<String>> exTypeMap = new HashMap<>();
		Map<String, LinkedList<String>> exIdMap = new HashMap<>();

		int exTypeNum = 0;
		int exIdNum = 0;
		
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
						String cardId = trace[1];// 用户卡编号
						String enVehId = trace[4];// 入口实际收费车牌号码
						String exVehId = trace[5];// 出口实际收费车牌号码
						String idenVehId = trace[6];// 出口识别收费车牌号码
						String enVehType = trace[7];// 入口收费车型
						String exVehType = trace[8];// 出口收费车型
						
						if(enVehType.length()>4 || exVehType.length()>4){
							break;
						}
						
						if((!enVehType.equals("null") && Integer.valueOf(enVehType) > 4) || (!exVehType.equals("null") && Integer.valueOf(exVehType) > 4)){
							break;
						}
						
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
						
						
						//比对出口实际车牌不一致情况
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
		System.out.println("出口实际车牌不一致情况共找到" + exIdNum + "张etc卡。");
		
		//写结果
		String exVehTypeMulPath = outPath + "exVehTypeMulRes.csv";
		String exVehIdMulPath = outPath + "exVehIdMulRes.csv";
		
		ComparePerData.writeData(exVehTypeMulPath, exTypeMap);
		ComparePerData.writeData(exVehIdMulPath, exIdMap);
		
		System.out.println("******************多条数据比对完毕*************");
	}
	
	
	public static void main(String[] args) {
		
		compareMulData(sumDataPath, comMulDataPath);
		
	}

}
