package inspect;

public class Main {

	public static void main(String[] args) {
		
		String a = "2018-08-21_success_003.csv";
		int index = a.indexOf("success");
		
		String sub = a.substring(index + 7, index + 11);
		System.out.println(a.substring(index-3,index-1));

	}

}
