package dealByProDemand;

import java.io.*;
import java.util.HashMap;

public class CheckDataByGuangdong {

    protected String excelALLPath = "E:/inspect/���Ͳ�һ�»�����Ϣ/���Ͳ�һ�����ͳ��.csv";
    protected String excelGdPath  = "E:/inspect/�㶫���ݺ˶�/�쳣�շ����ݺ˲���-�㶫.csv";

    protected String resultSamePath      = "E:/inspect/�㶫���ݺ˶�/����ļ�/����-����ȫ����ͬ.csv";
    protected String resultDifferentPath = "E:/inspect/�㶫���ݺ˶�/����ļ�/������ͬ-���Ʋ�ͬ.csv";

    protected HashMap<String, String> allContent, gdContent;

    public CheckDataByGuangdong() {
        this.allContent = new HashMap<>();
        this.gdContent = new HashMap<>();
    }

    /**
     * ��ȡȫ����Ϣ
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
     * ��ȡ�㶫ʡ����Ϣ
     *
     * @param path
     * @throws IOException
     */
    public void initGdContent(String path) throws IOException {
        FileInputStream fis            = new FileInputStream(new File(path));
        BufferedReader  bufferedReader = new BufferedReader(new InputStreamReader(fis));

        String line;
        bufferedReader.readLine();
        while (true) {
            if ((line = bufferedReader.readLine()) == null) {
                break;
            }

            String[] tmp    = line.split(",");
            String   cardID = tmp[1];

            this.gdContent.put(cardID, line);
        }
        bufferedReader.close();
    }

    public static void main(String[] args) {
    	CheckDataByGuangdong gd = new CheckDataByGuangdong();

        try {

            // д�ļ�
            OutputStreamWriter writerStream = new OutputStreamWriter(new FileOutputStream(gd.resultSamePath), "utf-8");
			BufferedWriter FileWriterSame = new BufferedWriter(writerStream);
			OutputStreamWriter writerStream1 = new OutputStreamWriter(new FileOutputStream(gd.resultDifferentPath), "utf-8");
			BufferedWriter FileWriterDiff = new BufferedWriter(writerStream1);

            // ��ȡexcel��Ϣ
            gd.initAllContent(gd.excelALLPath);
            gd.initGdContent(gd.excelGdPath);

            for (String key : gd.gdContent.keySet()) {

                // �㶫��
                String   gdLine       = gd.gdContent.get(key);
                String[] gdTmp        = gdLine.split(",");
                String   gdCardID     = gdTmp[1];
                String   gdCardNumber = gdTmp[0];
                String   gdCardType   = "";
                String   gdDunWei     = "";
                if (gdTmp.length == 4) {
                    gdCardType = gdTmp[2];
                    gdDunWei = gdTmp[3];
                } else {
                    System.out.println(gdLine);
                }

                // ȫ����
                String allLine = gd.allContent.get(key);
                if (allLine.isEmpty()) {
                    continue;
                }

                String[] allTmp        = allLine.split(",");
                String   allCardID     = allTmp[0].substring(5);
                String   allCardNumber = allTmp[1];
                allCardNumber = allCardNumber.substring(0, allCardNumber.length() - 2);

                // �Ƚϳ����Ƿ�һ��
                if (allCardID.equals(gdCardID) && allCardNumber.equals(gdCardNumber)) {
                    String sameLine = allLine + ',' + gdCardType + ',' + gdDunWei + "\n";
                    FileWriterSame.write(sameLine);
                } else {
                    System.out.println(allCardID + "       " + gdCardID);
                    System.out.println(allCardNumber + "       " + gdCardNumber);
                    String sameLine = allLine + ',' + gdCardNumber + ',' + gdCardType + ',' + gdDunWei + "\n";
                    FileWriterDiff.write(sameLine);
                }
            }

            FileWriterSame.close();
            FileWriterDiff.close();

        } catch (IOException e) {
            System.out.println("error");
            e.printStackTrace();
        }
    }

}
