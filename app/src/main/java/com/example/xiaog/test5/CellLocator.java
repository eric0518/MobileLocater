package com.example.xiaog.test5;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
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

    public CellLocator readCellLocation(Context context) {
        CellLocator cellLocator = new CellLocator();
        try {
            // String mnctype = "gsm";
            int lac = 0;
            int cellId = 0;
            int mcc = 0;
            int mnc = 0;
            TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int phoneType = mTelephonyManager.getPhoneType();
            // 返回值MCC + MNC
            String operator = mTelephonyManager.getNetworkOperator();

            if (operator != null && operator.length() > 3) {

                cellLocator.setMcc(Integer.parseInt(operator.substring(0, 3)));
                cellLocator.setMnc(Integer.parseInt(operator.substring(3)));
            }

            // 中国移动和中国联通获取LAC、CID的方式
            if (phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
                // mnctype = "cdma";
                CdmaCellLocation location = (CdmaCellLocation) mTelephonyManager.getCellLocation();

                cellLocator.setLac(location.getNetworkId());
                cellLocator.setCid(location.getBaseStationId());

            } else if (phoneType == TelephonyManager.PHONE_TYPE_GSM) {

                GsmCellLocation location = null;
                CellLocation cellLocation = mTelephonyManager.getCellLocation();
                if (cellLocation != null) {
                    location = (GsmCellLocation) cellLocation;
                }
                if (location != null) {
                    cellLocator.setLac(location.getLac());
                    cellLocator.setCid(location.getCid());
                }

            } else {
                // mnctype = "none";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return cellLocator;
    }
}
