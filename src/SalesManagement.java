import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;

public class SalesManagement {
    private final static String CLEAR_WINDOW = "\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n\r\n";
    private final static int SEARCH_BY_YEAR = 0;
    private final static int SEARCH_BY_MONTH = 1;
    private final static int SEARCH_BY_DAY = 2;
    static DBManager dbManager;

    static ArrayList<Integer> years = new ArrayList<>();
    static ArrayList<Integer> months = new ArrayList<>();
    static ArrayList<Integer> days = new ArrayList<>();
    static ArrayList<String> times = new ArrayList<>();
    static ArrayList<Integer> profits = new ArrayList<>();

    int length = 0;

    SalesManagement(DBManager dbManager){
        this.dbManager = dbManager;

        String readData = dbManager.readFile("sales");
        //날짜 입력 양식 yyyy-mm-dd-HH:mm:ss

        String[] allLines = readData.split("\n");
        this.length = allLines.length - 2;
        for (int lineIdx = 2; lineIdx < this.length + 2; lineIdx++) {
            String line = allLines[lineIdx];
            String[] data = line.split("\t");

            String ymd = data[0];
            String[] ymds = ymd.split("-");

            this.years.add(Integer.parseInt(ymds[0]));
            this.months.add(Integer.parseInt(ymds[1]));
            this.days.add(Integer.parseInt(ymds[2]));
            this.times.add(ymds[3]);

            this.profits.add(Integer.parseInt(data[1]));
        }
    }

    public void subMenu(){
        System.out.println("##### 매출 관리 #####");
        System.out.println("1) 연간조회\t 2) 월간조회\t 3) 일간조회\t 4) 종료\t");
        Scanner scanner = new Scanner(System.in);
        int userSelect = scanner.nextInt();

        switch (userSelect) {
            case 1:
                searchByYear();
                break;
            case 2:
                searchByMonth();
                break;
            case 3:
                searchByDay();
                break;
            case 4:
                break;
            default:
                System.out.println("메뉴 중 하나를 선택해 주세요");
                subMenu();
        }
    }

    private void searchByYear(){
        System.out.println(CLEAR_WINDOW);
        System.out.println("##### 매출 관리 - 연간 조회 #####");
        System.out.println("연도\t총판매금액");
        ArrayList<Integer> yearGroup = groupBy(SEARCH_BY_YEAR);
        int profit = 0;

        if(yearGroup == null){
            System.out.println("데이터가 존재하지 않습니다.");
            subMenu();
        }
        for(int idx=0; idx<yearGroup.size(); idx++){
            int year = yearGroup.get(idx);
            for(int j=0; j<this.length; j++){
                if(years.get(j) == year){ //해당 연도일 경우 profit에 더함
                    profit += profits.get(j);
                }
            }
            System.out.println(year+"\t"+profit);
        }
        subMenu();
    }

    private void searchByMonth(){
        System.out.println(CLEAR_WINDOW);
        System.out.println("##### 매출 관리 - 월별 조회 #####");
        System.out.println("월\t총판매금액");
        ArrayList<Integer> yearGroup = groupBy(SEARCH_BY_YEAR);
        ArrayList<Integer> monthGroup = groupBy(SEARCH_BY_MONTH);
        int profit = 0;

        if(monthGroup == null){
            System.out.println("데이터가 존재하지 않습니다.");
            subMenu();
        }
        for(int yidx=0; yidx<yearGroup.size();yidx++){
            int y = yearGroup.get(yidx);
            for(int idx=0; idx<monthGroup.size(); idx++){
                int month = monthGroup.get(idx);
                for(int j=0; j<this.length; j++){
                    if(months.get(j) == month){
                        profit += profits.get(j);
                    }
                }
                System.out.println(y+"-"+month+"\t"+profit);
            }
        }
        subMenu();
    }

    private void searchByDay(){
        System.out.println(CLEAR_WINDOW);
        System.out.println("##### 매출 관리 - 일별 조회 #####");
        System.out.println("일자\t총판매금액\t");

        ArrayList<Integer> mGroup = groupBy(SEARCH_BY_MONTH);
        ArrayList<Integer> dGroup = groupBy(SEARCH_BY_DAY);
        int profit = 0;

        if(dGroup == null){
            System.out.println("데이터가 존재하지 않습니다.");
            subMenu();
        }
        for(int yidx=0; yidx<mGroup.size();yidx++){
            int m = mGroup.get(yidx);
            for(int idx=0; idx<dGroup.size(); idx++){
                int day = dGroup.get(idx);
                for(int j=0; j<this.length; j++){
                    if(days.get(j) == day){
                        profit += profits.get(j);
                    }
                }
                System.out.println(m+"-"+day+"\t"+profit);
            }
        }
        subMenu();
    }

    static void addSales(String price){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss", Locale.KOREA);
        Date nowDate = new Date();
        String date = simpleDateFormat.format(nowDate);

        //리스트에 추가
        String[] tempArr = date.split("-");
        SalesManagement.years.add(Integer.parseInt(tempArr[0]));
        SalesManagement.months.add(Integer.parseInt(tempArr[1]));
        SalesManagement.days.add(Integer.parseInt(tempArr[2]));
        SalesManagement.times.add(tempArr[3]);
        SalesManagement.profits.add(Integer.parseInt(price));

        //파일 갱신
        StringBuffer msg = new StringBuffer();
        msg.append("일자\t총판매금액");
        msg.append("\r\n----------------------------------------\r\n");
        dbManager.writeFile("sales",msg.toString(),false);
        for(int j=0; j<times.size(); j++){
            String dayTime = years.get(j)+"-"+months.get(j)+"-"+days.get(j)+"-"+times.get(j);
            String data = dayTime+"\t"+profits.get(j);
            dbManager.writeFile("sales",data+"\r\n",true);
        }
    }

    private ArrayList<Integer> groupBy(int standard){
        //데이터 없으면 null반환
        //standard 기준으로 묶음 반환
        ArrayList<Integer> retunrList = new ArrayList<>();
        ArrayList<Integer> stdList = null;

        switch(standard){
            case SEARCH_BY_YEAR :
                stdList = years;
                break;
            case SEARCH_BY_MONTH :
                stdList = months;
                break;
            case SEARCH_BY_DAY :
                stdList = days;
                break;
        }

        try{
            retunrList.add(stdList.get(0)); //맨 처음꺼 넣음
        }catch(Exception e){
            return null;
        }

        for(int idx = 1; idx < years.size() ; idx++){
            //이전 날짜와 같은 경우 continue
            if(retunrList.get(retunrList.size()-1).equals(stdList.get(idx))) continue;
            //이전 날짜와 다른 경우 add
            else retunrList.add(stdList.get(idx));
        }
        return retunrList;
    }

    private void stop(){
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
    }
}
