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

	private static String originPath = "I:/����/resData/origin";
	private static String abnormalRecordTabPath = "I:/����/resData/���⳵��ͨ�м�¼";
	private static String abnormalCarDataPath = "I:/����/resData/���⳵�����ݱ�";
	
	public static void main(String[] args) {
		abnormalRecordTab(originPath, abnormalRecordTabPath, abnormalCarDataPath);
	}
	
	public static void abnormalRecordTab(String originPath, String abnormalRecordTabPath, String abnormalCarDataPath) {
		File file = new File(originPath);
		List<String> list = Arrays.asList(file.list());
		
		for (int i = 0; i < list.size(); i++) {
			// ���δ���ÿһ��ʡ��
			String pathIn = originPath + "/" + list.get(i);
			String pathOut1 = abnormalRecordTabPath + "/" + list.get(i);
			String pathOut2 = abnormalCarDataPath + "/" + list.get(i).substring(0,list.get(i).indexOf("_")) + ".csv";
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				//��һ���ļ���д�����ļ�			
				OutputStreamWriter writerStream1 = new OutputStreamWriter(new FileOutputStream(pathOut1), "utf-8");
				BufferedWriter writer1 = new BufferedWriter(writerStream1);		
				OutputStreamWriter writerStream2 = new OutputStreamWriter(new FileOutputStream(pathOut2), "utf-8");
				BufferedWriter writer2 = new BufferedWriter(writerStream2);
				
				writer1.write("ETC ID,���г���(��������),���г���(��������),"
						+ "����ID,ETC ID,���׽��,OBU ID,���ʵ���շѳ��ƺ�,����ʵ���շѳ��ƺ�,����ʶ���ƺ�,����շѳ���,�����շѳ���,֧������\n");
				writer2.write("���,ETC ID,����(����������Ϣ),���г���(����������Ϣ),OBU ID(����������Ϣ),1�ͳ��ִ���,2�ͳ��ִ���,3�ͳ��ִ���,4�ͳ��ִ���,��������\n");
				
				String line = "";
				String[] data;
				int serial_num = 0;
				
				while ((line = reader.readLine()) != null) {
					//���ÿ��id�����ж�
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
