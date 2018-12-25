package Main;

import inspect.*;

public class Main {

	private static String comPerDataExcNullPath = "/home/pq/inspect/intermediateData/comPerDataExcNull/comPerDataExcNull_2018-08.csv";
	private static String comPerDataClassPath = "/home/pq/inspect/intermediateData/comPerDataClass/";
	
	private static String sumDataPath = "/home/pq/inspect/intermediateData/sumDataBySever";
	private static String comMulDataPath = "/home/pq/inspect/intermediateData/comMulData/";
	
	private static String exVehTypeMulPath   = "/home/pq/inspect/intermediateData/comMulData/exVehTypeMulRes.csv";
	private static String splitProPath = "/home/pq/inspect/intermediateData/comMulData/·ÖÊ¡.csv";
	
	public static void main(String[] args) {
		
		ComparePerData.comparePerData(comPerDataExcNullPath, comPerDataClassPath);
		CompareMulData.compareMulData(sumDataPath, comMulDataPath);

		SpiltProvince.splitPro(exVehTypeMulPath, splitProPath);
	}

}
