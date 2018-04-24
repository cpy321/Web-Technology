package com.example.pengyuchen.hw91;

/**
 * Created by pengyuchen on 4/19/18.
 */

public class entity2 {
    //第一列表头
    private String sheetRow1;
    //第二列表头
    private String sheetRow2;
    //第三列表头
    private String sheetRow3;
    private String sheetRow4;
    private String sheetRow5;


    public entity2(String sheetRow1, String sheetRow2, String sheetRow3,String sheetRow4,String sheetRow5) {
        this.sheetRow1 = sheetRow1;
        this.sheetRow2 = sheetRow2;
        this.sheetRow3 = sheetRow3;
        this.sheetRow4 = sheetRow4;
        this.sheetRow5 = sheetRow5;
    }

    public String getItem1() {
        return sheetRow1;
    }

    public void setItem1(String sheetRow1) {
        this.sheetRow1 = sheetRow1;
    }

    public String getItem2() {
        return sheetRow2;
    }

    public void setItem2(String sheetRow2) {
        this.sheetRow2 = sheetRow2;
    }

    public String getItem3() {
        return sheetRow3;
    }

    public void setItem3(String sheetRow3) {
        this.sheetRow3 = sheetRow3;
    }

    public void setItem4(String sheetRow4) {
        this.sheetRow4 = sheetRow4;
    }
    public String getItem4() {
        return sheetRow4;
    }

    public void setItem5(String sheetRow5) {
        this.sheetRow5 = sheetRow5;
    }
    public String getItem5() {
        return sheetRow5;
    }


}