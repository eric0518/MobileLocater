package com.example.xiaog.test5;

import android.content.Context;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;

import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.System.*;

public class CellInfoRequester {

    CellLocator cellLocator = new CellLocator();
    Context context = null;

    CellInfoRequester(Context context) {
        this.context = context;
    }

    public CellLocator readCellInfo() {
        return readCellLocation(this.context);
    }

    public void cellInfoReportReportFixRate(int ms) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                cellInfoReportReport();
           }
         },0,ms);
    }

    public void cellInfoReportReport() {
        CellLocator cellInfo = readCellInfo();
        Log.d("--->Read cell info :", cellInfo.toJson());
    }

    private CellLocator readCellLocation(Context context) {
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
