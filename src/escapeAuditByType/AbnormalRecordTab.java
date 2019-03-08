package escapeAuditByType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;

public class AbnormalRecordTab {

	private static String originPath = "I:/稽查/resData/origin";
	private static String abnormalRecordTabPath = "I:/稽查/resData/问题车辆通行记录";
	private static String abnormalCarDataPath = "I:/稽查/resData/问题车辆数据表";
	
	public static void main(String[] args) {
		abnormalRecordTab(originPath, abnormalRecordTabPath, abnormalCarDataPath);
	}
	
	public static void abnormalRecordTab(String originPath, String abnormalRecordTabPath, String abnormalCarDataPath) {
		File file = new File(originPath);
		List<String> list = Arrays.asList(file.list());
		
		for (int i = 0; i < list.size(); i++) {
			// 依次处理每一个省份
			String pathIn = originPath + "/" + list.get(i);
			String pathOut1 = abnormalRecordTabPath + "/" + list.get(i);
			String pathOut2 = abnormalCarDataPath + "/" + list.get(i).substring(0,list.get(i).indexOf("_")) + ".csv";
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				//读一个文件，写两个文件			
				OutputStreamWriter writerStream1 = new OutputStreamWriter(new FileOutputStream(pathOut1), "utf-8");
				BufferedWriter writer1 = new BufferedWriter(writerStream1);		
				OutputStreamWriter writerStream2 = new OutputStreamWriter(new FileOutputStream(pathOut2), "utf-8");
				BufferedWriter writer2 = new BufferedWriter(writerStream2);
				
				writer1.write("ETC ID,发行车牌(国家中心),发行车型(国家中心),"
						+ "交易ID,ETC ID,交易金额,OBU ID,入口实际收费车牌号,出口实际收费车牌号,出口识别车牌号,入口收费车型,出口收费车型,支付类型\n");
				writer2.write("序号,ETC ID,车牌(国家中心信息),发行车型(国家中心信息),OBU ID(国家中心信息),1型出现次数,2型出现次数,3型出现次数,4型出现次数,其它次数\n");
				
				String line = "";
				String[] data;
				int serial_num = 0;
				
				while ((line = reader.readLine()) != null) {
					//针对每个id进行判断
					data = line.split("\\|");
					
					int type_1_num = 0;
					int type_2_num = 0;
					int type_3_num = 0;
					int type_4_num = 0;
					int type_other_num = 0;
					serial_num++;
					
					for(int j=1; j<data.length; j++){
						writer1.write(data[0] + "," + data[j] + "\n");	
						
						String trace[] = data[j].split(",", 10);
						String exType = trace[8].trim();
						
						switch (exType) {
						case "1":
							type_1_num++;
							break;
						case "2":
							type_2_num++;													
							break;
						case "3":
							type_3_num++;
							break;
						case "4":
							type_4_num++;
							break;
						default:
							type_other_num++;
							break;
						}							
					}
					
					writer2.write(serial_num + "," + data[0] + ",," + type_1_num + "," + type_2_num + "," + type_3_num + "," + type_4_num + "," + type_other_num + "\n");
				}
			
				reader.close();
				writer1.flush();
				writer1.close();
				writer2.flush();
				writer2.close();
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			System.out.println(pathIn + " read finish!");
			System.out.println(pathOut1 + " write finish!");
			System.out.println(pathOut2 + " write finish!");
			
		}
			
	}


}
