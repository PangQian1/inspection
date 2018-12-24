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
	public static String ResultPath = "/home/pq/inspect/intermediateData/comMulData/分省.csv";

	/**
	 * 对原始出口数据进行处理，初步找出异常数据
	 */
	public static void preProcess() {
		Map<String, LinkedList<String>> ProvinceMap = new HashMap<>();

		// 读文件
		try {
			InputStreamReader inStream = new InputStreamReader(new FileInputStream(DataPath), "UTF-8");
			BufferedReader reader = new BufferedReader(inStream);

			String line = "";
			String[] lineArray;
			while ((line = reader.readLine()) != null) {
				lineArray = line.split("\\|");
				for (String item : lineArray) {
					String[] data  = item.split(",");
					String cardId  = data[1];// 用户卡编号
					String exVehId = data[5];// 出口实际收费车牌号码
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

		// 写数据
		writeData(ResultPath, ProvinceMap);
	}

	public static void writeData(String outPath, Map<String, LinkedList<String>> dataMap) {
		// 写文件
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
		case "京":
			return "北京市";
		case "津":
			return "天津市";
		case "冀":
			return "河北省";
		case "晋":
			return "山西省";
		case "蒙":
			return "内蒙古自治区";
		case "辽":
			return "辽宁省";
		case "吉":
			return "吉林省";
		case "黑":
			return "黑龙江省";
		case "沪":
			return "上海市";
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
		case "桂":
			return "广西壮族自治区";
		case "琼":
			return "海南省";
		case "渝":
			return "重庆市";
		case "川":
			return "四川省";
		case "贵":
			return "贵州省";
		case "云":
			return "云南省";
		case "藏":
			return "西藏自治区";
		case "陕":
			return "陕西省";
		case "甘":
			return "甘肃省";
		case "青":
			return "青海省";
		case "宁":
			return "宁夏回族自治区";
		case "新":
			return "新疆维吾尔自治区";
		case "港":
			return "香港特别行政区";
		case "澳":
			return "澳门特别行政区";
		case "台":
			return "台湾省";
		default:
			return "";
		}
	}

	public static void main(String[] args) {
		preProcess();
	}
}
