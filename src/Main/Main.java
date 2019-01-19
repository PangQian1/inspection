package Main;

import inspect.*;

public class Main {

	private static String comPerDataExcNullPath = "/home/pq/inspect/intermediateData/comPerDataExcNull/comPerDataExcNull_2018-08.csv";
	private static String comPerDataClassPath = "/home/pq/inspect/intermediateData/comPerDataClass/";
	
	private static String sumDataPath = "/home/pq/inspect/intermediateData/sumDataBySever";
	private static String comMulDataPath = "/home/pq/inspect/intermediateData/comMulData/";
	
	private static String userInfoPath = "/home/pq/inspect/registrationInfo/userInfo.csv";
	private static String exVehTypeMulPath = "/home/pq/inspect/intermediateData/comMulData/exVehTypeMulRes.csv";
	
	private static String matchResPath = "/home/pq/inspect/resData/exTypeMatchUserRes.csv";
	private static String splitProPath = "/home/pq/inspect/resData/splitByPro/";
	
	public static void main(String[] args) {
		
		//ComparePerData.comparePerData(comPerDataExcNullPath, comPerDataClassPath);//单条数据比对
		CompareMulData.compareMulData(sumDataPath, comMulDataPath);//多条数据比对
		
		InspectByType.typeMatchUser(userInfoPath, exVehTypeMulPath, matchResPath);//比对注册信息找出车型不一致车辆交易

		TypeSpiltByPro.splitPro(matchResPath, splitProPath);//划分省份
	}

}
