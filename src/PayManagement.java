import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class PayManagement{
    static DBManager db;


    static ArrayList<Stock> selectedStocks = new ArrayList<>();
    static ArrayList<Stock> stocks;
    static int selectNumber;


    PayManagement(DBManager db) {
        this.db = db;
        selectNumber = 0;
    }

    public void link(){
        stocks = StockManagement.stocks;
    }

    public void subMenu() {
        int selectMenu;

        //initList();
        //loadList();
        while (true) {
            int totalPrice = 0;
            System.out.println("#########################################");
            System.out.println("################### 결제 #################");
            System.out.println("#########################################");
            System.out.println("제품번호    제품명     수량      가격");
            for (int i = 0; i < selectedStocks.size(); i++) {
                System.out.println(selectedStocks.get(i).stockNumber + "\t" + selectedStocks.get(i).name + "\t" + selectedStocks.get(i).count + "\t" + selectedStocks.get(i).price);
                totalPrice = Integer.parseInt(selectedStocks.get(i).price) * Integer.parseInt(selectedStocks.get(i).count);
            }
            System.out.println("총액 : " + totalPrice + "\t회원 번호 : ");
            System.out.println("1) 추가");
            System.out.println("2) 결제");
            System.out.println("3) 결제취소");

            Scanner sc = new Scanner(System.in);
            selectMenu = sc.nextInt();
            System.out.println();

            switch (selectMenu) {
                case 1:
                    Payment.addList(selectedStocks, stocks);
                    break;
                case 2:
                    Payment.doPayment(selectedStocks);
                    break;
                case 3:
                    return;
            }
        }

    }
}