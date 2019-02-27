package dealByProDemand;

import java.io.*;
import java.util.HashMap;

import org.omg.IOP.TAG_MULTIPLE_COMPONENTS;

public class CheckDataByJiangSu {

    protected String excelALLPath = "E:/inspect/车型不一致汇总信息/车型不一致情况统计.csv";
    protected String exceljsPath  = "E:/inspect/江苏数据核对/异常收费数据核查结果-江苏.csv";

    protected String resultSamePath      = "E:/inspect/江苏数据核对/结果文件/卡号-车牌全都相同.csv";
    protected String resultDifferentPath = "E:/inspect/江苏数据核对/结果文件/卡号相同-车牌不同.csv";

    protected HashMap<String, String> allContent, jsContent;

    public CheckDataByJiangSu() {
        this.allContent = new HashMap<>();
        this.jsContent = new HashMap<>();
    }

    /**
     * 获取全部信息
     *
     * @param path
     * @throws IOException
     */
    public void initAllContent(String path) throws IOException {
    	File userFile = new File(path);
    	InputStreamReader inStream = new InputStreamReader(new FileInputStream(userFile), "UTF-8");
		BufferedReader reader = new BufferedReader(inStream);

        String line;
        while (true) {
            if ((line = reader.readLine()) == null) {
                break;
            }

            String[] tmp    = line.split(",");
            String   cardID = tmp[0].substring(5);

            this.allContent.put(cardID, line);
        }
        reader.close();
    }

    /**
     * 获取江苏省份信息
     *
     * @param path
     * @throws IOException
     */
    public void initJsContent(String path) throws IOException {
        FileInputStream fis            = new FileInputStream(new File(path));
        BufferedReader  bufferedReader = new BufferedReader(new InputStreamReader(fis));

        String line;
        bufferedReader.readLine();

        while (true) {
            if ((line = bufferedReader.readLine()) == null) {
                break;
            }
    
            String[] tmp    = line.split(",");
            String   cardID = tmp[2];
            
            if(cardID.length() == 20){
            	cardID = cardID.substring(4);
            }
            
            if(this.jsContent.containsKey(cardID)){
            	String info1 = this.jsContent.get(cardID);
            	info1 = info1.substring((info1.indexOf(",") + 1));
            	String info2 = line.substring((line.indexOf(",") + 1));
            	if(info1.equals(info2)){
            		
            	}else{
            		System.out.println(this.jsContent.get(cardID));
            		System.out.println(line);
            		System.out.println();
            		this.jsContent.remove(cardID);
            	}
            }else {
            	this.jsContent.put(cardID, line);
			}
            
        }
        bufferedReader.close();

    }

    public static void main(String[] args) {
    	CheckDataByJiangSu js = new CheckDataByJiangSu();

        try {

            // 写文件
            OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(js.resultSamePath), "utf-8");
			BufferedWriter FileWriterSame = new BufferedWriter(writerStream);
			OutputStreamWriter writerStream1 = new OutputStreamWriter(new FileOutputStream(js.resultDifferentPath), "utf-8");
			BufferedWriter FileWriterDiff = new BufferedWriter(writerStream1);

            // 读取excel信息
            js.initAllContent(js.excelALLPath);
            js.initJsContent(js.exceljsPath);
            
            int notConsistNum = 0;
            int consistNum = 0;
            int cardNumNotConsist = 0;

            for (String key : js.jsContent.keySet()) {

                // 江苏的
                String   jsLine       = js.jsContent.get(key);
                String[] jsTmp        = jsLine.split(",");
                String   jsCardID     = jsTmp[2];//卡号
                String   jsCardNumber = jsTmp[1].trim();//车牌
                String   jsCardType   = "";
                String   jsOBU        = "";
                
                if(jsCardNumber.length() == 9){
                	jsCardNumber = jsCardNumber.substring(0, 7);
                }
                if (jsTmp.length == 6) {
                    jsCardType = jsTmp[5];
                    jsOBU = "'" + jsTmp[3];
                } else {
                    System.out.println(jsLine);
                }

                // 全部的
                if(js.allContent.containsKey(key)){
                	String allLine = js.allContent.get(key);
                	
                    String[] allTmp        = allLine.split(",");
                    String   allCardID     = allTmp[0].substring(5);//卡号
                    String   allCardNumber = allTmp[1];//车牌
                    allCardNumber = allCardNumber.substring(0, allCardNumber.length() - 2).trim();

                    // 比较车牌是否一样
                    if (allCardID.equals(key) && allCardNumber.equals(jsCardNumber)) {
                        String sameLine = allLine + ',' + jsCardType + ',' + jsOBU + "\n";
                        FileWriterSame.write(sameLine);
                        consistNum++;
                    } else {
                    	//System.out.println(allCardID + "       " + key);
                        //System.out.println(allCardNumber + "       " + jsCardNumber);
                        String sameLine = allLine + ',' + jsTmp[1] + ',' + jsCardType + ',' + jsOBU + "\n";
                        FileWriterDiff.write(sameLine);
                        cardNumNotConsist++;
                    }
                }else{
                	//System.out.println(key);
                	notConsistNum++;
                }
            }
            
            System.out.println(js.jsContent.size());
            System.out.println(notConsistNum + "  " + consistNum + "  " + cardNumNotConsist);
            
            FileWriterSame.close();
            FileWriterDiff.close();

        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

}
