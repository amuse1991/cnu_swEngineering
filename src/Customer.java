import java.util.ArrayList;
import java.util.Scanner;


public class Customer {
    private final static String CLEAR_WINDOW = "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n";
    int customerId;
    String name;
    String phone;
    //String rank;

    Customer(int customerId, String name, String phone /*String rank*/){
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        //this.rank = rank;
    }

    Customer(String name, String phone){
        this.name = name;
        this.phone = phone;
    }

    public static void search(ArrayList<Customer> customers) {
        System.out.println("##### 회원관리 - 조회 #####");
        boolean isFind;

        isFind = findCustomer(customers);

        if(!isFind){
            System.out.println("존재하지 않는 회원입니다.");
        }
    }

    public static boolean register(ArrayList<Customer> customers) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("추가할 회원의 이름을 입력해 주세요 :");
            String newName = scanner.nextLine();
            System.out.println("추가할 회원의 전화번호를 입력해 주세요 :");
            String newPhone = scanner.nextLine();
            //String newRank = "브론즈";
            int newId = getNewCustomerId();

            //파일에 기록
            String userData = "\r\n" + newId + "\t" + newName + "\t" + newPhone + "\t" /*+ newRank*/;
            CustomerManager.dbManager.writeFile("customer", userData, true);

            //리스트에 기록
            Customer newCustomer = new Customer(newId, newName, newPhone/*, newRank*/);
            customers.add(newCustomer);

            return true;
        }catch (Exception e){
            return false;
        }
    }

    public static boolean deleteId(ArrayList<Customer> customers) {
        Scanner scanner = new Scanner(System.in);
        boolean isFind;

        System.out.println("##### 회원관리 - 탈퇴 #####");
        //회원 조회 수행
        isFind = findCustomer(customers);
        //탐색 실패시
        if(!isFind){
            System.out.println("존재하지 않는 회원입니다.");
            return false;
        }

        System.out.println("탈퇴할 회원의 회원번호를 입력하세요 :");
        int id = scanner.nextInt();
        scanner.nextLine();

        for(int i=0; i<CustomerManager.length; i++){
            Customer customer = customers.get(i);
            if(id == customer.customerId){
                customers.remove(i);
                customers.trimToSize();

                CustomerManager.dbManager.writeFile("customer","회원번호\t이름\t전화번호",false);
                CustomerManager.dbManager.writeFile("customer","\r\n----------------------------------------\r\n",true);
                for (int idx = 0; idx<CustomerManager.length-1;idx++) {
                    String s = customers.get(idx).customerId + "\t" + customers.get(idx).name + "\t" + customers.get(idx).phone + "\t" /*+ customers.get(idx).rank*/+"\r\n";
                    CustomerManager.dbManager.writeFile("customer",s,true);
                }
                break;
            }
        }
        return true;
    }

    private static int getNewCustomerId() {
        return CustomerManager.length + 1;
    }

    private static boolean findCustomer(ArrayList<Customer> customers){
        System.out.println("1) 이름으로 검색 \r\n2) 전화번호로 검색");
        Scanner scanner = new Scanner(System.in);
        int userSelect = scanner.nextInt();
        scanner.nextLine(); // 개행문자 제거용
        boolean isFind = false;

        switch (userSelect) {
            case 1:
                System.out.println(CLEAR_WINDOW);
                System.out.println("이름(전화번호)를 입력해 주세요 : ");
                String targetName = scanner.nextLine();
                System.out.println(CLEAR_WINDOW);
                System.out.println("회원번호\t이름\t전화번호");
                System.out.println("-----------------------------");

                for (int idx = 0; idx < CustomerManager.length; idx++) {
                    Customer customer = customers.get(idx);
                    if (targetName.equals(customer.name)) {
                        StringBuffer outData = new StringBuffer();
                        outData.append(customer.customerId + "\t");
                        outData.append(customer.name + "\t");
                        outData.append(customer.phone + "\t");
                        //outData.append(customer.rank+ "\t");
                        System.out.println(outData.toString());
                        isFind = true;
                    }
                }
                break;

            case 2:
                System.out.println(CLEAR_WINDOW);
                System.out.println("이름(전화번호)를 입력해 주세요 : ");
                String targetPhone = scanner.nextLine();
                System.out.println(CLEAR_WINDOW);
                System.out.println("회원번호\t이름\t전화번호");
                System.out.println("----------------------------------------");
                for (int idx = 0; idx <CustomerManager.length; idx++) {
                    Customer customer = customers.get(idx);
                    if (targetPhone.equals(customer.phone)) {
                        StringBuffer outData = new StringBuffer();
                        outData.append(customer.customerId + "\t");
                        outData.append(customer.name + "\t");
                        outData.append(customer.phone + "\t");
                        //outData.append(customer.rank+ "\t");
                        System.out.println(outData.toString());
                        isFind = true;
                    }
                }
                break;
        }
        return isFind;
    }
}
