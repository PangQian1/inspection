package inspect;

import java.io.BufferedWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import dao.io;

public class test {

	public static void main(String[] args) {
		int count = 1;
		System.out.println(count%31==0);
	}

}

/*try{
	Map<String,LinkedList<String>> map=new HashMap<>();
	while((line=reader.readLine())!=null){
	
		String[] data=line.split(",");
		if(data.length==6){
			String id=data[0];
			String time=data[1];
			String lat=data[2];
			String lng=data[3];
			String v=data[4];
			String d=data[5];
			if(map.containsKey(id)){
				LinkedList<String> listTrace=map.get(id);
				listTrace.add(time+","+lat+","+lng+","+v+","+d);
				map.put(id, listTrace);
			}else{ 
				LinkedList<String> listTrace=new LinkedList<>();
				listTrace.add(time+","+lat+","+lng+","+v+","+d);
				map.put(id, listTrace);
			}
		}
	}
	reader.close();
	for(String id:map.keySet()){
		LinkedList<String> listTrace=map.get(id);
		String outPath=outDir+"/"+id+".csv";
		BufferedWriter writer=io.getWriter(outPath, "gbk");
		for(int j=0;j<listTrace.size();j++){
			writer.write(listTrace.get(j)+"\n");
		}
		writer.flush();
		writer.close();
	}
}catch(Exception e){
	e.printStackTrace();
} finally {
	
}*/