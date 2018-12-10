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

	//private static String oriExData = "H:/pangqian/data/oriExData";// ԭʼ��������
	//private static String preOriExDataRes = "H:/pangqian/data/preOriDataRes.csv";// �������

	private static String oriExData = "/home/highwaytransaction/extransaction/2018-08";// ԭʼ��������
	private static String preOriExDataRes = "/home/pq/dataRes/2018-08";// �������(����Ϊ�岿��)
	
	
	
	/**
	 * ��ԭʼ�������ݽ��д��������ҳ��쳣����
	 * 
	 * @param path
	 *            ԭʼ��������·��
	 * @param out
	 *            ���������λ��
	 */
	public static void preFilter(String path, String out) {

		File file = new File(path);
		List<String> list = Arrays.asList(file.list());
		Map<String, LinkedList<String>> cardIDMap = new HashMap<>();

		int etcCarNum = 0;

		for (int i = 0; i < list.size(); i++) {
			// ���δ���ÿһ���ļ�
			String pathIn = path + "/" + list.get(i);
			
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				String line = "";
				String[] data;
				while ((line = reader.readLine()) != null) {
					data = line.split(",", 26);
					
					String type = data[0];// �������ͣ��ֽ�or���ֽ�
					String id = data[1];// ���ױ��
					String fee = data[3];// ���׽��
					String cardId = data[8];// �û������
					String OBUId = data[10];// OBU���
					String enVehId = data[11];// ���ʵ���շѳ��ƺ���
					String exVehId = data[12];// ����ʵ���շѳ��ƺ���
					String idenVehId = data[13];// ����ʶ���շѳ��ƺ���
					int enVehType = Integer.valueOf(data[14]);// ����շѳ���
					int exVehType = Integer.valueOf(data[15]);// �����շѳ���
					String payType = data[25];// ֧������

					if (type.equals("1") && enVehType < 5 && exVehType < 5) {
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
				System.out.println(sub.substring(1) + "���ֹ��ҵ�" + etcCarNum + "��etc����");
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

	
	
	public static void main(String[] args) {

		preFilter(oriExData, preOriExDataRes);
		System.out.println("end");
	}

}
