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
	 * 对结果文件进行省份划分处理
	 */
	public static void splitPro(String inPath, String outPath) {
		Map<String, ArrayList<String>> ProvinceMap = new HashMap<>();

		// 读文件
		try {
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(inPath), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);

			String line = "";
			String[] lineArray;
			while ((line = reader.readLine()) != null) {
				lineArray = line.split("\\|");

				String[] data  = lineArray[0].split(",");
				String cardId  = data[0];// 用户卡编号
				String exVehId = data[1];// 出口实际收费车牌号码
				String provinceShort = exVehId.substring(0, 1);
				
				String province = getProvinceName(provinceShort);
				
				if (province.isEmpty()) {
					System.out.println("出现异常省份！！");
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

		// 写数据
		writeData(outPath, ProvinceMap);
		
		System.out.println("*************分省结束*************");
	}

	public static void writeData(String outPath, Map<String, ArrayList<String>> dataMap) {
		// 写文件
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
			System.out.println("共包括" + sum + "条记录");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getProvinceName(String ProvinceShort) {
		switch (ProvinceShort) {
		case "京":
			return "北京市";
		case "津":
			return "天津市";
		case "沪":
			return "上海市";
		case "渝":
			return "重庆市";
		case "宁":
			return "宁夏回族自治区";
		case "新":
			return "新疆维吾尔自治区";
		case "蒙":
			return "内蒙古自治区";
		case "桂":
			return "广西壮族自治区";
		case "藏":
			return "西藏自治区";
		case "港":
			return "香港特别行政区";
		case "澳":
			return "澳门特别行政区";
		case "冀":
			return "河北省";
		case "晋":
			return "山西省";
		case "辽":
			return "辽宁省";
		case "吉":
			return "吉林省";
		case "黑":
			return "黑龙江省";
		case "苏":
			return "江苏省";
		case "浙":
			return "浙江省";
		case "皖":
			return "安徽省";
		case "闽":
			return "福建省";
		case "赣":
			return "江西省";
		case "鲁":
			return "山东省";
		case "豫":
			return "河南省";
		case "鄂":
			return "湖北省";
		case "湘":
			return "湖南省";
		case "粤":
			return "广东省";
		case "琼":
			return "海南省";
		case "川":
			return "四川省";
		case "贵":
			return "贵州省";
		case "云":
			return "云南省";
		case "陕":
			return "陕西省";
		case "甘":
			return "甘肃省";
		case "青":
			return "青海省";
		case "台":
			return "台湾";
		default:
			return "";
		}
	}


	public static void main(String[] args) {
		
		splitPro(matchResPath, splitProPath);
		
	}

}
