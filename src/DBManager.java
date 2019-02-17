import java.io.*;
import java.util.HashMap;

public class DBManager {
    HashMap<String, String> nameConst = new HashMap<>();

    DBManager() {
        init();
    }

    //파일 새로 생성
    void init() {
        nameConst.put("회원", "customer");
        nameConst.put("결제목록", "orderList");
        nameConst.put("매출목록", "goods");
        nameConst.put("제품목록", "goodsList");
        try {
            String dir = null;
            BufferedWriter writer;
            String content = null;

            for (String key : nameConst.keySet()) {
                switch (key) {
                    case "회원":
                        dir = System.getProperty("user.dir") + File.separator + "src" + File.separator + nameConst.get(key) + ".txt";
                        content = "회원번호\t이름\t전화번호\t등급";
                        break;
                    case "제품목록":
                        dir = System.getProperty("user.dir") + File.separator + "src" + File.separator + nameConst.get(key) + ".txt";
                        content = "제품번호\t제품명\t수량\t가격";
                        break;
                    case "결제목록":
                        dir = System.getProperty("user.dir") + File.separator + "src" + File.separator + nameConst.get(key) + ".txt";
                        content = "제품번호\t제품명\t수량\t가격";
                        break;
                    case "매출목록":
                        dir = System.getProperty("user.dir") + File.separator + "src" + File.separator + nameConst.get(key) + ".txt";
                        content = "일자\t총판매금액";
                        break;
                    default:
                        dir = null;
                        break;
                }
                if (dir != null) {

                    int nLast = dir.lastIndexOf(File.separator);
                    String strDir = dir.substring(0, nLast);
                    String strFile = dir.substring(nLast + 1, dir.length());

                    File dirFolder = new File(strDir);
                    if (!dirFolder.exists())
                        dirFolder.mkdirs();
                    File f = new File(dirFolder, strFile);
                    if (!f.exists()) {
                        f.createNewFile();

                        writer = new BufferedWriter(new FileWriter(dir));
                        writer.write(content.toString());

                        writer.write("\r\n----------------------------------------\n\t\t\t");
                        writer.close();
                    }
                } else {
                    System.out.println("파일 init 오류 :: dir이 null입니다.");
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 파일 읽어서 콘솔에 출력하는 메소드
    void printFile(String fileName) {
        try {
            String dir = System.getProperty("user.dir") + File.separator + "src" + File.separator + fileName + ".txt";
            BufferedReader reader = new BufferedReader(new FileReader(dir));
            String line = reader.readLine();
            while (line != null) {
                if (line.length() > 5)
                    System.out.println(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 파일 읽어서 콘솔에 출력하는 메소드
    String readFile(String fileName) {
        StringBuilder sb = new StringBuilder();
        try {
            String dir = System.getProperty("user.dir") + File.separator + "src" + File.separator + fileName + ".txt";
            BufferedReader reader = new BufferedReader(new FileReader(dir));
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /* 쓰기 메소드
        isUpdate = true (기존 파일 지우지 않음, 업데이트 개념)
        isUpdate = false (기존 파일 지움, 쓰기 개념)
     */
    void writeFile(String fileName, String content, boolean isUpdate) {
        try {
            String dir = System.getProperty("user.dir") + File.separator + "src" + File.separator + fileName + ".txt";

            FileWriter fw = new FileWriter(dir, isUpdate);
            BufferedWriter w = new BufferedWriter(fw);

            w.write(content);

            w.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}