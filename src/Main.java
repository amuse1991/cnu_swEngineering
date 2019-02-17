import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        //객체들 나열하고
        DBManager db = new DBManager();

        StockManagement stockManager = new StockManagement(db);
        SalesManagement salesManager = new SalesManagement(db);
        CustomerManager customerManager = new CustomerManager(db);
        PayManagement payManager = new PayManagement(db);

        linkAll(stockManager,payManager,salesManager,stockManager);

        Scanner sc = new Scanner(System.in);
        int select;
        System.out.println("");
        while (true) {
            System.out.println("#########################################");
            System.out.println("############# 인쇄소 POS 시스템 ############");
            System.out.println("#########################################");
            System.out.println("1) 재고 관리");
            System.out.println("2) 결제");
            System.out.println("3) 매출관리");
            System.out.println("4) 회원관리");
            select = sc.nextInt();
            System.out.println();
            switch (select) {
                case 1:
                    stockManager.subMenu();
                    break;
                case 2:
                    payManager.subMenu();
                    break;
                case 3:
                    salesManager.subMenu();
                    break;
                case 4:
                    customerManager.subMenu();
                    break;
            }
        }
    }

    private static void linkAll(StockManagement st, PayManagement pm, SalesManagement sm, StockManagement stm){
        pm.link();
    }
}
