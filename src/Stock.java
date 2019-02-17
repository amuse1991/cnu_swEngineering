import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class Stock {
    String name;
    String price;
    String count;
    String stockNumber;

    Stock(String stockNumber, String name, String count, String price){
        this.name = name;
        this.price = price;
        this.count = count;
        this.stockNumber = stockNumber;
    }


    public static boolean regist(ArrayList<Stock> stocks) {
        try {
            String newName;
            String newPrice;
            String newCount;

            Scanner sc = new Scanner(System.in);

            //loadTable();
            System.out.println("#########################################");
            System.out.println("################# 재고 등록 ###############");
            System.out.println("#########################################");
            StockManagement.db.printFile("goodsList");
            System.out.println("등록할 제품의 정보를 입력해주세요. (제품명, 수량, 가격)");

            newName = sc.next();
            newCount = sc.next();
            newPrice = sc.next();

            //객체 생성 및 리스트에 삽입
            Stock newStockObj = new Stock(String.valueOf(++StockManagement.currentStockNumber),newName,newCount,newPrice);
            stocks.add(newStockObj);

//            System.out.println("Current : " + StockManagement.currentStockNumber);
            System.out.println("등록이 완료되었습니다.");

            // DB에 기록
            //파일에 쓰기
            StringBuilder sb = new StringBuilder();
            sb.append("제품번호\t제품명\t수량\t가격");
            sb.append("\r\n----------------------------------------\n");
            sb.append("\t\t\t\n");
            StockManagement.db.writeFile("goodsList",sb.toString(),false);
            for(int j = 0; j<StockManagement.stocks.size(); j++){
                Stock curStk = StockManagement.stocks.get(j);
                String data = curStk.stockNumber+"\t"+curStk.name+"\t"+curStk.count+"\t"+curStk.price;
                StockManagement.db.writeFile("goodsList",data+"\r\n",true);
            }

            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void edit(ArrayList<Stock> stocks) {
        Stock currentStockObj;
        String editName;
        String editPrice;
        String editCount;
        String currentNumber;
        String currentName;
        String currentPrice;
        String currentCount;

        String targetNumber;
        System.out.println("#########################################");
        System.out.println("################# 재고 수정 ###############");
        System.out.println("#########################################");
        StockManagement.db.printFile("goodsList");
        //loadTable();

        Scanner sc = new Scanner(System.in);
        System.out.println("수정할 제품 번호를 입력해 주세요.");
        targetNumber = sc.nextLine();
        System.out.println("수정할 제품의 정보를 입력해주세요. (제품명, 가격 수량)");
        editName = sc.next();
        editPrice = sc.next();
        editCount = sc.next();


        for(int idx = 0 ; idx<stocks.size(); idx++){
            if(stocks.get(idx).stockNumber.equals(targetNumber)) {
                //수정할 제품 객체 가져오기
                Stock targetObj = stocks.get(idx);
                //수정 사항 반영
                targetObj.name = editName;
                targetObj.price = editPrice;
                targetObj.count = editCount;
                stocks.remove(idx);
                stocks.add(idx,targetObj);
                //파일에 기록
                StringBuilder sb = new StringBuilder();
                sb.append("제품번호\t제품명\t수량\t가격");
                sb.append("\r\n----------------------------------------\n");
                sb.append("\t\t\t\n");
                StockManagement.db.writeFile("goodsList",sb.toString(),false);
                for(int j = 0; j<StockManagement.stocks.size(); j++){
                    Stock curStk = StockManagement.stocks.get(j);
                    String data = curStk.stockNumber+"\t"+curStk.name+"\t"+curStk.count+"\t"+curStk.price;
                    StockManagement.db.writeFile("goodsList",data+"\r\n",true);
                }
                break;
            }
        }
        /*
        Iterator<Stock> stockIterator = stocks.iterator();

        StringBuilder sb = new StringBuilder();
        sb.append("제품번호\t제품명\t수량\t가격");
        sb.append("\r\n----------------------------------------\n");
        sb.append("\t\t\t\n");

        while (stockIterator.hasNext()) {
            currentStockObj = stockIterator.next();

            currentNumber = currentStockObj.stockNumber;
            currentName = currentStockObj.name;
            currentPrice = currentStockObj.price;
            currentCount = currentStockObj.count;

            sb.append(currentNumber);
            sb.append("\t");
            if (currentName.length() == 0)
                continue;
            if (currentNumber.equals(String.valueOf(targetNumber))) {
                sb.append(editName);
                sb.append("\t");
                sb.append(editPrice);
                sb.append("\t");
                sb.append(editCount);
                sb.append("\n");
            } else {
                sb.append(currentName);
                sb.append("\t");
                sb.append(currentPrice);
                sb.append("\t");
                sb.append(currentCount);
                sb.append("\n");
            }
        }
        StockManagement.db.writeFile("goodsList", sb.toString(), false);
        StockManagement.db.printFile("goodsList");
        */
    }

    static void delete(ArrayList<Stock> stocks) {
        Stock currentStockObj;

        int targetNumber;
        StockManagement.db.printFile("goodsList");
        //loadTable();

        Scanner sc = new Scanner(System.in);
        System.out.println("삭제할 제품 번호를 입력해 주세요.");
        targetNumber = sc.nextInt();

        for(int idx = 0 ; idx < StockManagement.stocks.size(); idx++){
            currentStockObj = StockManagement.stocks.get(idx);
            if(currentStockObj.stockNumber.equals(String.valueOf(targetNumber))){
                //리스트에서 제거
                StockManagement.stocks.remove(idx);
                StockManagement.stocks.trimToSize();
                //파일에 쓰기
                StringBuilder sb = new StringBuilder();
                sb.append("제품번호\t제품명\t수량\t가격");
                sb.append("\r\n----------------------------------------\n");
                sb.append("\t\t\t\n");
                StockManagement.db.writeFile("goodsList",sb.toString(),false);
                for(int j = 0; j<StockManagement.stocks.size(); j++){
                    Stock curStk = StockManagement.stocks.get(j);
                    String data = curStk.stockNumber+"\t"+curStk.name+"\t"+curStk.count+"\t"+curStk.price;
                    StockManagement.db.writeFile("goodsList",data+"\r\n",true);
                }
            }
        }
        StockManagement.db.printFile("goodsList");
    }

    String getList() {
        return StockManagement.db.readFile("goodsList");
    }

    static boolean stockOut(String stockNum, int amount){
        Stock curStcok;
        for(int idx = 0; idx < StockManagement.stocks.size(); idx++){
            curStcok = StockManagement.stocks.get(idx);
            if(curStcok.stockNumber.equals(stockNum)){
                int lastAmount = Integer.parseInt(curStcok.count);
                int resultAmount = lastAmount-amount;
                //리스트 갱신
                StockManagement.stocks.remove(idx); //삭제
                StockManagement.stocks.trimToSize();
                StockManagement.stocks.add(idx,new Stock(curStcok.stockNumber,curStcok.name,String.valueOf(resultAmount),curStcok.price)); //삭제한 위치에 다시 삽입
                //파일 갱신
                StringBuffer msg = new StringBuffer();
                msg.append("제품번호\t제품명\t수량\t가격");
                msg.append("\r\n----------------------------------------\r\n\t\t\t\r\n");
                for(int j=0; j<StockManagement.stocks.size(); j++){
                    Stock stock = StockManagement.stocks.get(j);
                    String s = stock.stockNumber+"\t"+stock.name+"\t"+stock.count+"\t"+stock.price+"\r\n";
                    msg.append(s);
                }
                StockManagement.db.writeFile("goodsList",msg.toString(),false);

                return true;
            }
        }
        System.out.println("해당 재고를 찾을 수 없습니다."); //***********예외흐름
        return false;
    }

    static void search(ArrayList<Stock> stocks){

        StockManagement.db.printFile("goodsList");

    }
}
