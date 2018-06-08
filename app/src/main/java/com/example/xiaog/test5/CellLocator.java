package com.example.xiaog.test5;

import android.util.Log;

public class CellLocator {

    enum NumberType { Hex, Dec }

    int cid = 0;
    int lac = 0;
    int mcc = 0;
    int mnc = 0;
    int rnc = 0;

    public int getMcc() {
        return mcc;
    }

    public int getMnc() {
        return mnc;
    }

    public int getRnc() {
        return rnc;
    }

    public void setMcc(int mcc) {
        this.mcc = mcc;
    }

    public void setMnc(int mnc) {
        this.mnc = mnc;
    }

    public void setRnc(int rnc) {
        this.rnc = rnc;
    }

    public void setLac(int lac) {
        this.lac = lac;
    }

    public int getLac() {
        return lac;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public int getCid() {
        return cid;
    }

    public String toJson() {
        String json = "{" +
                      "\"cid\":" + cid + "," +
                      "\"lac\":" + lac + "," +
                      "\"mcc\":" + mcc + "," +
                      "\"mnc\":" + mnc + "," +
                      "\"rnc\":" + rnc+
                      "}";

        return json;
    }
}
