package inspect;

import java.security.Signature;
import java.util.LinkedList;

public class Main {
	// 相似度计算
		public static double xiangsidu(String a, String b) {
			// 相似度公式：1 - 最小编辑距离/两个串中较长的长度
			int n = a.length();
			int m = b.length();
			if (n < 1 && m < 1)
				return 0; // 两个空串
	 
			return 1 - minEditDist(a, b) / Math.max(n, m);
		}
	 
		// 最小编辑距离计算
		public static double minEditDist(String a, String b) {
			int n = a.length();
			int m = b.length();
			if (n < 1 || m < 1)
				return (n + m) * 1.00; // 两个空串
	 
			double[][] d = new double[n + 1][m + 1];
			double cost;
	 
			for (int i = 0; i <= n; ++i)
				d[i][0] = i;
			for (int i = 0; i <= m; ++i)
				d[0][i] = i;
	 
			for (int i = 1; i <= n; i++) {
				for (int j = 1; j <= m; j++) {
					if (a.charAt(i - 1) == b.charAt(j - 1))
						cost = 0;
					else
						cost = 1;
					d[i][j] = Minimum(d[i - 1][j] + 1, d[i][j - 1] + 1,
							d[i - 1][j - 1] + cost);
				}
			}
			return d[n][m];
		}
	 
		private static double Minimum(double a, double b, double c) {
			double mi = a;
			if (b < mi) {
				mi = b;
			}
			if (c < mi) {
				mi = c;
			}
			return mi;
		}

	

	public static void main(String[] args) {
		
		String a = "粤D96932_9";
		String b = "粤D90876_9";

		System.out.println(a.substring(0,7));
	}

}
