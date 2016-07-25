package com.gank.demo.net.bean;

import java.util.List;

public class PrintStatus {
    private boolean ok;

    private List<Table1> Table1;

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public boolean getOk() {
        return this.ok;
    }

    public void setTable1(List<Table1> Table1) {
        this.Table1 = Table1;
    }

    public List<Table1> getTable1() {
        return this.Table1;
    }

}