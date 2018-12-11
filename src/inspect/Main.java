package inspect;

public class Main {

	public static void main(String[] args) {
		
		String a = "G000444003023042302402018080110184955,44011534223208208019,4606,860300612019B73A,‘¡SR6B29_0,‘¡SR6B29_0,null,1,1,3|S008144001008020101602018073009385471,44011534223208208019,6076,860300612019B73A,‘¡SR6B29_0,‘¡SR6B29_0,null,1,1,3";
		
		String[] data = a.split("\\|");
		System.out.println(data.length);
		for(int i = 0; i < data.length; i++){
			System.out.println(data[i]);
		}

	}

}
