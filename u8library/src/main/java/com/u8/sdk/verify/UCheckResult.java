package com.u8.sdk.verify;

public class UCheckResult {
    private int suc;
    private int state;

    public UCheckResult(int suc, int state) {
        this.suc = suc;
        this.state = state;
    }


    public int getSuc() {
        return this.suc;
    }


    public void setSuc(int suc) {
        this.suc = suc;
    }


    public int getState() {
        return this.state;
    }


    public void setState(int state) {
        this.state = state;
    }
}


