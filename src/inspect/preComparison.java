package inspect;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import dao.io;

public class preComparison {

	//private static String oriExData = "H:/pangqian/data/oriExData";// ԭʼ��������
	//private static String preOriExDataRes = "H:/pangqian/data/preOriDataRes.csv";// �������
	
	private static String oriExData = "/home/highwaytransaction/extransaction";// ԭʼ��������
	//private static String oriExData = "/home/highwaytransaction/extransaction/2018-08";// ԭʼ��������
	private static String preProExData = "/home/pq/inspect/intermediateData/sumDataBySever";// �������(����Ϊ�岿��)
	private static String prefiltExData = "/home/pq/inspect/intermediateData/comPerData";// ��8�·����ݵ����ȶԵõ��Ĳ�һ������
	private static String prefiltExDataExcNull = "/home/pq/inspect/intermediateData/comPerDataExcNull";// ��8�·����ݵ����ȶԵõ��Ĳ�һ������,�ų�NULLֵ
	
	
	/**
	 * ��ԭʼ�������ݽ��д��������ҳ��쳣����--�ҵ��ͳ�etc���ײ�����Ϣ,ͬһ�û�id����Ϣ�ŵ�ͬһ���У��������·�����
	 * 
	 * @param path
	 *            ԭʼ��������·��
	 * @param out
	 *            ���������λ��
	 */
	public static void preProcess(String path, String out) {
		
		File file=new File(path);
		List<String> list = Arrays.asList(file.list());
		Map<String, LinkedList<String>> cardIDMap = new HashMap<>();

		int etcCarNum = 0;
		//int count = 0;
	
		for(int i=0;i<list.size();i++){
			//���嵽ÿ����
			String p=path+"/"+list.get(i);
			File fileIn=new File(p);
			List<String> listIn=Arrays.asList(fileIn.list());
			int round = 0;
			
			if(listIn.size() < 3) continue;
			
			for(int j=0;j<listIn.size();j++){
				try {
					String pathIn=p+"/"+listIn.get(j);
					
					if(p.substring(0, 7).equals("2018-03")) break;
					
					BufferedReader reader=io.getReader(pathIn,"utf-8");
	
					String line = "";
					String[] data;
					while ((line = reader.readLine()) != null) {
						data = line.split(",", 26);
						
						if(data.length != 26) continue;
						
						String type = data[0];// �������ͣ��ֽ�or���ֽ�
						String id = data[1];// ���ױ��
						String fee = data[3];// ���׽��
						String cardId = data[8];// �û������
						String OBUId = data[10];// OBU���
						String enVehId = data[11];// ���ʵ���շѳ��ƺ���
						String exVehId = data[12];// ����ʵ���շѳ��ƺ���
						String idenVehId = data[13];// ����ʶ���շѳ��ƺ���
						String enVehType = data[14];// ����շѳ���
						String exVehType = data[15];// �����շѳ���
						String payType = data[25];// ֧������
						
						if(enVehType.length()>4 || exVehType.length()>4){
							continue;
						}
	
						if (type.equals("1") && ((enVehType!=null && Integer.valueOf(enVehType) < 5 && Integer.valueOf(enVehType) != 0) || 
								(exVehType!=null && Integer.valueOf(exVehType) < 5) && Integer.valueOf(exVehType) != 0)) {
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
					
					
					System.out.println(pathIn + " read finish!");

					if((j!=0) && (j%31 == 0 || j == listIn.size()-1)){
						round++;

						String outPath = out + "/" + listIn.get(0).substring(0, 7) + "_00" + round + ".csv";
						writePreExData(outPath, cardIDMap);
						
						cardIDMap = new HashMap<>();
						System.out.println(listIn.get(0).substring(0, 7) + "�·ݵ�" +round + "���ֹ��ҵ�" + etcCarNum + "��etc����");
						etcCarNum = 0;
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			
			}
		}
		
	}
	
	
	
	/**
	 * ��ԭʼ�������ݽ��д��������ҳ��쳣����--�ҵ��ͳ�etc���ײ�����Ϣ,ͬһ�û�id����Ϣ�ŵ�ͬһ���У�����һ���·ݣ�8�·ݣ�����
	 * 
	 * @param path
	 *            ԭʼ��������·��
	 * @param out
	 *            ���������λ��
	 */
	public static void preProcess08(String path, String out) {

		File file = new File(path);
		List<String> list = Arrays.asList(file.list());
		Map<String, LinkedList<String>> cardIDMap = new HashMap<>();

		int etcCarNum = 0;
		//int count = 0;
		
		for (int i = 0; i < list.size(); i++) {
			// ���δ���ÿһ���ļ�
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
					
					String type = data[0];// �������ͣ��ֽ�or���ֽ�
					String id = data[1];// ���ױ��
					String fee = data[3];// ���׽��
					String cardId = data[8];// �û������
					String OBUId = data[10];// OBU���
					String enVehId = data[11];// ���ʵ���շѳ��ƺ���
					String exVehId = data[12];// ����ʵ���շѳ��ƺ���
					String idenVehId = data[13];// ����ʶ���շѳ��ƺ���
					String enVehType = data[14];// ����շѳ���
					String exVehType = data[15];// �����շѳ���
					String payType = data[25];// ֧������
					
					if(enVehType.length()>4 || exVehType.length()>4){
						continue;
					}

					if (type.equals("1") && ((enVehType!=null && Integer.valueOf(enVehType) < 5 && Integer.valueOf(enVehType) != 0) || 
							(exVehType!=null && Integer.valueOf(exVehType) < 5) && Integer.valueOf(exVehType) != 0)) {
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
				String outPath = out + "/2018-08" + sub + ".csv";
				writePreExData(outPath, cardIDMap);
				
				cardIDMap = new HashMap<>();
				System.out.println(sub.substring(1) + "���ֹ��ҵ�" + etcCarNum + "��etc����");
				etcCarNum = 0;
			}
		}
		
	}
	
	public static void preFilter(String in, String out) {

		File file = new File(in);
		List<String> list = Arrays.asList(file.list());
		Map<String, LinkedList<String>> cardIDMap = new HashMap<>();

		int etcCarNum = 0;
		
		for (int i = 0; i < list.size(); i++) {
			// ���δ���ÿһ���ļ�
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
								listTrace.add(data[j]);
								cardIDMap.put(cardId, listTrace);
							} else {
								LinkedList<String> listTrace = new LinkedList<>();
								listTrace.add(data[j]);
								cardIDMap.put(cardId, listTrace);

								etcCarNum++;
							}
						}
					}
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println(pathIn + " read finish!");
			String outPath = out + "/comPerData_2018-08_00" + (i+1) + ".csv";
			writePreExData(outPath, cardIDMap);
			System.out.println("��" + (i+1) + "���ֹ��ҵ�" + etcCarNum + "��etc�����ڵ������ݲ�һ�������");
			cardIDMap = new HashMap<>();
			etcCarNum = 0;
		}
		
		
	}
	
	public static void preFilterExcNull(String in, String out) {

		File file = new File(in);
		List<String> list = Arrays.asList(file.list());
		Map<String, LinkedList<String>> cardIDMap = new HashMap<>();

		int etcCarNum = 0;
		
		for (int i = 0; i < list.size(); i++) {
			// ���δ���ÿһ���ļ�
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
						
						if(
								((!perData[4].equals("null")) && (!perData[5].equals("null")) && !(perData[4].equals(perData[5]))) ||
								((!perData[5].equals("null")) && (!perData[6].equals("null")) && !(perData[5].equals(perData[6]))) ||
								((!perData[7].equals("null")) && (!perData[8].equals("null")) && !(perData[7].equals(perData[8])))
							){	
							if (cardIDMap.containsKey(cardId)) {
								LinkedList<String> listTrace = cardIDMap.get(cardId);
								listTrace.add(data[j]);
								cardIDMap.put(cardId, listTrace);
							} else {
								LinkedList<String> listTrace = new LinkedList<>();
								listTrace.add(data[j]);
								cardIDMap.put(cardId, listTrace);

								etcCarNum++;
							}
						}
					}
				}
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println(pathIn + " read finish!");
			//String outPath = out + "/comPerDataExcNull_2018-08_00" + (i+1) + ".csv";
			//writePreExData(outPath, cardIDMap);
			//System.out.println("��" + (i+1) + "���ֹ��ҵ�" + etcCarNum + "��etc�����ڵ������ݲ�һ�������");
			//cardIDMap = new HashMap<>();
			//etcCarNum = 0;
		}
		
		String outPath = out + "/comPerDataExcNull_2018-08.csv";
		writePreExData(outPath, cardIDMap);
		System.out.println("���ҵ�" + etcCarNum + "��etc�����ڵ������ݲ�һ�������(�ų���ֵ)");
		
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
	
	public static void count(String in){
		
		Map<String, String> countMap = new HashMap<>();
		File file = new File(in);
		List<String> list = Arrays.asList(file.list());
		int etcCarNum = 0;
		for (int i = 0; i < list.size(); i++) {
			// ���δ���ÿһ���ļ�
			String pathIn = in + "/" + list.get(i);
		
			try {
				InputStreamReader inStream = new InputStreamReader(new FileInputStream(pathIn), "UTF-8");
				BufferedReader reader = new BufferedReader(inStream);
				
				String line = "";
				String[] data;
				String cardId;
				
				while ((line = reader.readLine()) != null) {
					data = line.split(",", 3);
					cardId = data[1];
					if (countMap.containsKey(cardId)) {
						continue;
					} else {
						String v = "1";
						countMap.put(cardId, v);
						etcCarNum++;
					}
				}
				
				
				reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(pathIn + " read finish!");
		}
		
		System.out.println("���ҵ�" + etcCarNum + "������");
		
	/*	//String outPath = out + "/2018-08.csv";
		String outPath = out + "/comPerData_2018-08.csv";
		
		try {
			OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(outPath),"utf-8");
			BufferedWriter writer = new BufferedWriter(writerStream);
			for (String cardId : countMap.keySet()) {
				String v = countMap.get(cardId);
				writer.write(cardId + "," + v + "\n");			
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}

	public static void main(String[] args) {

		//preProcess(oriExData, preProExData);
		//preFilter(preProExData, prefiltExData);
		//preFilterExcNull(prefiltExData, prefiltExDataExcNull);
		
		count(preProExData);
		//count(prefiltExData);
		
		System.out.println("**********Ԥ�������**************");
	}

}
