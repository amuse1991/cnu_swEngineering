import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Payment {
    String name;
    String price;
    String stockNumber;
    String amount;
    static ArrayList<Payment> payList = new ArrayList<>();

    Payment(String name, String price, String stockNumber, String amount){
        this.name = name;
        this.price = price;
        this.stockNumber = stockNumber;
        this.amount = amount;
    }

    static void addList(ArrayList<Stock> selectedStocks, ArrayList<Stock> stocks) {
        String currentStockNumber,
                currentName, currentPrice;
        int targetStNumber, amount;

        Iterator<Stock> stockItor = stocks.iterator();

        Scanner sc = new Scanner(System.in);
        PayManagement.db.printFile("goodsList");

        System.out.print("추가할 제품번호를 입력해 주세요 : ");
        targetStNumber = sc.nextInt();
        System.out.print("수량을 입력해 주세요 : ");
        amount = sc.nextInt();
        System.out.println();


        while (stockItor.hasNext()) {
            Stock currentStockObj = stockItor.next();
            currentStockNumber = currentStockObj.stockNumber;
            currentName = currentStockObj.name;
            currentPrice = currentStockObj.price;

            if (currentStockNumber.equals(String.valueOf(targetStNumber))) {

                selectedStocks.add(new Stock(currentStockNumber,currentName,String.valueOf(amount),currentPrice));

                break;
            }
        }
    }

    static void doPayment(ArrayList<Stock> selectedStock){
        System.out.println("회원이면 1, 비회원이면 2를 입력하세요");
        Scanner scanner = new Scanner(System.in);
        int isCustomer = scanner.nextInt();
        scanner.nextLine();

        double discount = 1.0; // 할인X

        if(isCustomer == 1){ //회원인 경우
            Customer.search(CustomerManager.customers);
            System.out.println("결제를 진행할 회원번호를 입력하세요 : ");
            int id = scanner.nextInt();
            scanner.nextLine();
            for(int idx=0; idx<CustomerManager.customers.size(); idx++){
                Customer customer = CustomerManager.customers.get(idx);
                if(customer.customerId == id){
                    discount = 0.8;
                    System.out.println("20% 할인 적용");
                }
            }
        }
        //결제 (결제객체 생성해서 페이 리스트에 삽입)
        String name;
        int totalPrice;
        String stockNumber;
        for(int idx=0; idx<selectedStock.size(); idx++){
            name = selectedStock.get(idx).name;
            int count = Integer.parseInt(selectedStock.get(idx).count);
            int price = Integer.parseInt(selectedStock.get(idx).price);
            stockNumber = selectedStock.get(idx).stockNumber;
            totalPrice = (int)((double)count*(double)price*discount);
            System.out.println("실결제금액 :"+ totalPrice);
            payList.add(new Payment(name,String.valueOf(totalPrice),stockNumber, String.valueOf(count)));
        }
        //재고 테이블에 반영 (페이 리스트 참조해서 재고 차감)
        //매출 테이블에 반영 (페이 리스트 참조해서 매출 테이블 추가)
        for(int idx=0; idx<payList.size();idx++){
            Payment curPay = payList.get(idx);
            Stock.stockOut(curPay.stockNumber,Integer.parseInt(curPay.amount));
            SalesManagement.addSales(curPay.price);
        }
        System.out.println("결제가 완료되었습니다.");
        payList.clear();
        PayManagement.selectedStocks.clear();
        //payList 초기화
    }
}
