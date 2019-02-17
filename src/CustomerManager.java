import java.util.ArrayList;
import java.util.Scanner;

public class CustomerManager {
    private final static String CLEAR_WINDOW = "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n";
    static DBManager dbManager;
    static int length = 0;

    public static ArrayList<Customer> customers = new ArrayList<>();

    CustomerManager(DBManager dbManager){
        this.dbManager = dbManager;

        String readData = dbManager.readFile("customer");

        String[] allLines = readData.split("\n");
        this.length = allLines.length - 2;
        for (int lineIdx = 2; lineIdx < this.length + 2; lineIdx++) {
            String line = allLines[lineIdx];
            String[] data = line.split("\t");

            customers.add(new Customer(Integer.parseInt(data[0]),data[1],data[2]));
        }
    }


    public void subMenu() {
        System.out.println("##### 회원관리 #####");
        System.out.println("1) 조회\t 2) 가입\t 3) 탈퇴\t 4) 종료\t");
        Scanner scanner = new Scanner(System.in);
        int userSelect = scanner.nextInt();

        switch (userSelect) {
            case 1:
                Customer.search(customers);
                subMenu();
                break;
            case 2:
                if(Customer.register(customers))
                    length++;
                subMenu();
                break;
            case 3:
                if(Customer.deleteId(customers))
                    length--;
                subMenu();
                break;
            case 4:
                break;
            default:
                System.out.println("메뉴 중 하나를 선택해 주세요");
                subMenu();
        }
    }

}