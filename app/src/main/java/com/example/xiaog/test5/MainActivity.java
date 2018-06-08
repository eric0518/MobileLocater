package com.example.xiaog.test5;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.Activity;
import android.os.Bundle;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.ServiceState;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private class CellStateListener extends PhoneStateListener {

        private static final int EVENTS = LISTEN_CELL_LOCATION
                | LISTEN_SERVICE_STATE;

        @Override
        public void onCellLocationChanged(CellLocation location) {
            updateCellLocation(location);
        }

        @Override
        public void onServiceStateChanged(ServiceState serviceState) {
            updateServiceState(serviceState.getOperatorNumeric());
        }
    }

    private CellStateListener listener = new CellStateListener();
    Context context = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        getjizhaninfo(context);

    }

    @Override
    protected void onPause() {
        super.onPause();
        listen(CellStateListener.LISTEN_NONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        listen(CellStateListener.EVENTS);
    }

    private void listen(int events) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //telephonyManager.listen(listener, events);
    }

    private static GsmCellLocation gsmCellLocation(CellLocation location) {
        try {
            return (GsmCellLocation) location;
        } catch (ClassCastException e) {
            return null;
        }
    }

    void updateCellLocation(CellLocation location) {
        GsmCellLocation gsmLocation = gsmCellLocation(location);
        int lac = gsmLocation != null ? gsmLocation.getLac() : -1;
        int cid = gsmLocation != null ? gsmLocation.getCid() : -1;

        setText(R.id.lac, R.string.lac, lac);
        setText(R.id.rnc, R.string.rnc, cid >= 0 ? cid >> 16 : -1);
        setText(R.id.cid, R.string.cid, cid >= 0 ? cid & 0xffff : -1);
    }

    void updateServiceState(String operator) {
        String mcc = operator != null && operator.length() >= 3 ? operator
                .substring(0, 3) : "";
        String mnc = operator != null && operator.length() >= 3 ? operator
                .substring(3) : "";

        setText(R.id.mcc, R.string.mcc, mcc);
        setText(R.id.mnc, R.string.mnc, mnc);
    }

    private void setText(int id, int label, String string) {
        TextView view = (TextView) findViewById(id);
    }

    private void setText(int id, int label, int number) {
        setText(id, label, number >= 0 ? String.valueOf(number) : "");
    }

    public int[] getjizhaninfo(Context context) {

        Log.d("getjizhaninfo", "started------------------------");
        startLocation();
        int[] info = new int[4];
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

                mcc = Integer.parseInt(operator.substring(0, 3));
                mnc = Integer.parseInt(operator.substring(3));
            }
            // 中国移动和中国联通获取LAC、CID的方式
            if (phoneType == TelephonyManager.PHONE_TYPE_CDMA) {
                // mnctype = "cdma";
                CdmaCellLocation location = (CdmaCellLocation) mTelephonyManager.getCellLocation();

                lac = location.getNetworkId(); // getLac();
                cellId = location.getBaseStationId(); // getCid();
            } else if (phoneType == TelephonyManager.PHONE_TYPE_GSM) {
                // mnctype = "gsm";
                // GsmCellLocation location = (GsmCellLocation)
                // mTelephonyManager
                // .getCellLocation();
                GsmCellLocation location = null;
                CellLocation cellLocation = mTelephonyManager.getCellLocation();
                if (cellLocation != null) {
                    location = (GsmCellLocation) cellLocation;
                }
                if (location != null) {

                    lac = location.getLac();
                    cellId = location.getCid();
                }

            } else {
                // mnctype = "none";
            }
            info[0] = cellId;
            info[1] = lac;
            info[2] = mcc;
            info[3] = mnc;

            Log.d("getjizhaninfo", "started------------------------cid = " + cellId);
            Log.d("getjizhaninfo", "started------------------------lac = " + lac);

            setText(R.id.lac, R.string.lac, lac);
            setText(R.id.rnc, R.string.rnc, cellId >= 0 ? cellId >> 16 : -1);
            setText(R.id.cid, R.string.cid, cellId >= 0 ? cellId & 0xffff : -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }

    private void startLocation() {
        int checkPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (checkPermission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            Log.d("TTTT", "弹出提示");
            return;
        } else {

        }
    }
}