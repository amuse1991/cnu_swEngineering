import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class StockManagement {
    static DBManager db;
    Scanner sc;
    static ArrayList<Stock> stocks = new ArrayList<>();
    static int length = 0;
    static int currentStockNumber = -1;

    public StockManagement(DBManager db) {
        this.db = db;
        this.sc = new Scanner(System.in);
        loadTable();

    }

    private void loadTable() {
        String readData = db.readFile("goodsList");
        String columns[] = readData.split("\n");
        for (int i = 3; i < columns.length; i++) {
            String[] attributes = columns[i].split("\t");
            if (attributes[0].length() == 0)
                continue;
            stocks.add(new Stock(attributes[0], attributes[1], attributes[2], attributes[3]));

            if (currentStockNumber < Integer.parseInt(attributes[0]))
                currentStockNumber = Integer.parseInt(attributes[0]);
        }
    }

    public void subMenu() {
        int select;
        while (true) {
            System.out.println("#########################################");
            System.out.println("################# 재고 관리 ###############");
            System.out.println("#########################################");
            System.out.println("1) 등록");
            System.out.println("2) 수정");
            System.out.println("3) 삭제");
            System.out.println("4) 조회");
            System.out.println("5) 취소");
            select = sc.nextInt();
            System.out.println();
            switch (select) {
                case 1:
                    if(Stock.regist(stocks)){

                    }
                    break;
                case 2:
                    Stock.edit(stocks);
                    break;
                case 3:
                    Stock.delete(stocks);
                    break;
                case 4:
                    Stock.search(stocks);
                    break;
                case 5:
                    return;
            }
        }
    }
}