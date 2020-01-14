package com.qiyc.daohang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.CheckedOutputStream;

import jxl.NumberCell;
import jxl.Sheet;
import jxl.Workbook;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "=====   ";
    private LatLonPoint mStartPoint = new LatLonPoint(39.942295, 116.335891);//起点，116.335891,39.942295
    private LatLonPoint mEndPoint = new LatLonPoint(39.995576, 116.481288);//终点，116.481288,39.995576
    private TextView mText1;
    private TextView mText2;
    private TextView mText3;
    private TextView mText4;
    private TextView mText5;
    private TextView mText6;
    private TextView mText7;
    private TextView mText8;
    private TextView mText9;

    List<PointModule> mStartList = new ArrayList<>();
    List<PointModule> mEndList = new ArrayList<>();
    String key = "6556860d6cee3cb1d53dc7c4323aeb19";
    String url = "https://restapi.amap.com/v3/direction/transit/integrated?output=json&key=6556860d6cee3cb1d53dc7c4323aeb19";
    String sss = "https://restapi.amap.com/v3/direction/transit/integrated?key=您的key&origin=116.481028,39.989643&destination=116.434446,39.90816&city=北京&cityd=北京&strategy=0&nightflag=0";

    String originStr = "origin";
    String destinationStr = "destination";
    String cityStr = "city";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mText1 = (TextView) findViewById(R.id.tv_1);
        mText2 = (TextView) findViewById(R.id.tv_2);
        mText3 = (TextView) findViewById(R.id.tv_3);
        mText4 = (TextView) findViewById(R.id.tv_4);
        mText5 = (TextView) findViewById(R.id.tv_5);
        mText6 = (TextView) findViewById(R.id.tv_6);
        mText7 = (TextView) findViewById(R.id.tv_7);
        mText8 = (TextView) findViewById(R.id.tv_8);
        mText9 = (TextView) findViewById(R.id.tv_9);
//        getRoad();
//        getRoat();

        mText5.setMovementMethod(ScrollingMovementMethod.getInstance());
        mText1.setOnClickListener(this);
        mText2.setOnClickListener(this);
        mText3.setOnClickListener(this);
        mText4.setOnClickListener(this);
        mText5.setOnClickListener(this);
        mText6.setOnClickListener(this);
        mText7.setOnClickListener(this);
        mText8.setOnClickListener(this);
        mText9.setOnClickListener(this);

    }

    int index=0;
    private void getRoat(final SuperPointsModule spm) {
        try {
            OkGo.<String>get(url + "&" + originStr + "=" + spm.getStartLon() + "," + spm.getStartLat() +
                    "&" + destinationStr + "=" + spm.getEndLon() + "," + spm.getEndLat() +
                    "&" + cityStr + "=" + "0411").execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.d("====", "onSuccess: " + response.body());
                    try {
                        String busStr = "";

                        SuperPointsModule superPointsModule = new SuperPointsModule();
                        superPointsModule.setId(spm.getId());
                        superPointsModule.setBianhao(spm.getBianhao());
                        superPointsModule.setStartLon(spm.getStartLon());
                        superPointsModule.setStartLat(spm.getStartLat());
                        superPointsModule.setEndLon(spm.getEndLon());
                        superPointsModule.setEndLat(spm.getEndLat());
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.has("route")) {
                            JSONObject rout = jsonObject.optJSONObject("route");
                            if (rout.has("transits")) {
                                JSONArray transits = rout.optJSONArray("transits");
                                JSONObject transitsObj = null;
                                if (transits != null && transits.length() > 0) {
                                    transitsObj = new JSONObject(transits.get(0).toString());
                                }
                                if (transitsObj != null && transitsObj.has("distance")) {
                                    busStr = "总路程" + transitsObj.optString("distance") + "米&&&";
                                    superPointsModule.setTotalDis(transitsObj.optString("distance"));
                                }
                                if (transitsObj != null && transitsObj.has("segments")) {
                                    JSONArray segments = transitsObj.optJSONArray("segments");
                                    if (segments != null && segments.length() > 0) {
                                        for (int i = 0; i < segments.length(); i++) {
                                            if (segments.get(i) != null) {
                                                JSONObject jsonObject1 = new JSONObject(segments.get(i).toString());
                                                if (jsonObject1.has("walking")) {
                                                    JSONObject walking = jsonObject1.optJSONObject("walking");
                                                    String distance = "0";
                                                    if (walking != null && walking.has("distance")) {
                                                        distance = walking.optString("distance");
                                                    }
                                                    if (i == 0) {//第0个  当做起点步行距离
                                                        busStr += "起点步行距离" + distance + "米&&&";
                                                        superPointsModule.setStartDis(distance);
                                                    } else if (i == segments.length() - 1 && i != 0){
//                                                           && (jsonObject1.optJSONObject("walking").has("bus") && jsonObject1.optJSONObject("walking").optJSONObject("bus").has("buslines") &&jsonObject1.optJSONObject("walking").optJSONObject("bus").optJSONObject("buslines") ==null)) {
                                                        busStr += "终点步行距离" + distance + "米";
                                                        superPointsModule.setEndDis(distance);
                                                    }/* else if (i == segments.length() - 1 && i == 0) {//只有一个数据
                                                        busStr += "起点步行距离" + distance + "米";
                                                    }*/ else {
                                                        busStr += "换乘点步行距离"+i +"   " + distance + "米&&&";
                                                        if (i==1){
                                                            superPointsModule.setTranDis1(distance);
                                                        }else if (i==2){
                                                            superPointsModule.setTranDis2(distance);
                                                        }else if (i==3){
                                                            superPointsModule.setTranDis3(distance);
                                                        }else if (i==4){
                                                            superPointsModule.setTranDis4(distance);
                                                        }else if (i==5){
                                                            superPointsModule.setTranDis5(distance);
                                                        }
                                                    }
//                                                    if (walking !=null &&walking.has("distance")){
//                                                        String distance1 = walking.optString("distance");
//                                                        if (i==0){
//                                                            busStr+="起点步行距离"+distance+"米&&&";
//                                                        }else if (i ==segments.length()-1){
//                                                            busStr+="终点步行距离"+distance+"米";
//                                                        }else {
//                                                            busStr += "换乘点步行距离" + distance + "米&&&";
//                                                        }
//                                                    }else{
//                                                        busStr += "换乘点步行距离" + 0 + "米&&&";
//                                                    }
                                                } else {
                                                    busStr += "换乘点步行距离" + 0 + "米&&&";
                                                }
                                            } else {
                                                busStr += "换乘点步行距离" + 0 + "米&&&";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Log.d(TAG, "onSuccess:当前count是"+count+"  第" +index+"次加载成功");
                        Log.d("===", "onSuccess:     " + busStr);
                        DataListManager.getInstance().m1Rr.add(superPointsModule);
                        if (count==1||count==2||count==3||count==4||count==5){
                            if (index==DataListManager.getInstance().m1.size()-1){
                                generateFinalExcel();
                                return;
                            }
                        }else if (count==6){
                            if (index==DataListManager.getInstance().m6.size()-1){
                                generateFinalExcel();
                                return;
                            }
                        }else if (count==7){
                            if (index==DataListManager.getInstance().m7.size()-1){
                                generateFinalExcel();
                                return;
                            }
                        }
                        index++;
                        if (count==1){
                            getRoat(DataListManager.getInstance().m1.get(index));
                        }else if (count==2){
                            getRoat(DataListManager.getInstance().m2.get(index));
                        }else if (count==3){
                            getRoat(DataListManager.getInstance().m3.get(index));
                        }else if (count==4){
                            getRoat(DataListManager.getInstance().m4.get(index));
                        }else if (count==5){
                            getRoat(DataListManager.getInstance().m5.get(index));
                        }else if (count==6){
                            getRoat(DataListManager.getInstance().m6.get(index));
                        }else if (count==7){
                            getRoat(DataListManager.getInstance().m7.get(index));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("===", "Exception:     " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getRoat1(final SuperPointsModule spm) {
        try {
            OkGo.<String>get("https://restapi.amap.com/v3/direction/transit/integrated?output=json&key=6556860d6cee3cb1d53dc7c4323aeb19&origin=121.531303,38.883768&destination=121.594141,38.915004&city=0411").execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.d("====", "onSuccess: " + response.body());
                    try {
                        String busStr = "";

                        SuperPointsModule superPointsModule = new SuperPointsModule();
                        superPointsModule.setId(spm.getId());
                        superPointsModule.setBianhao(spm.getBianhao());
                        superPointsModule.setStartLon(spm.getStartLon());
                        superPointsModule.setStartLat(spm.getStartLat());
                        superPointsModule.setEndLon(spm.getEndLon());
                        superPointsModule.setEndLat(spm.getEndLon());
                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.has("route")) {
                            JSONObject rout = jsonObject.optJSONObject("route");
                            if (rout.has("transits")) {
                                JSONArray transits = rout.optJSONArray("transits");
                                JSONObject transitsObj = null;
                                if (transits != null && transits.length() > 0) {
                                    transitsObj = new JSONObject(transits.get(0).toString());
                                }
                                if (transitsObj != null && transitsObj.has("distance")) {
                                    busStr = "总路程" + transitsObj.optString("distance") + "米&&&";
                                    superPointsModule.setTotalDis(transitsObj.optString("distance"));
                                }
                                if (transitsObj != null && transitsObj.has("segments")) {
                                    JSONArray segments = transitsObj.optJSONArray("segments");
                                    if (segments != null && segments.length() > 0) {
                                        for (int i = 0; i < segments.length(); i++) {
                                            if (segments.get(i) != null) {
                                                JSONObject jsonObject1 = new JSONObject(segments.get(i).toString());
                                                if (jsonObject1.has("walking")) {
                                                    JSONObject walking = jsonObject1.optJSONObject("walking");
                                                    String distance = "0";
                                                    if (walking != null && walking.has("distance")) {
                                                        distance = walking.optString("distance");
                                                    }
                                                    if (i == 0) {//第0个  当做起点步行距离
                                                        busStr += "起点步行距离" + distance + "米&&&";
                                                        superPointsModule.setStartDis(distance);
                                                    } else if (i == segments.length() - 1 && i != 0){
//                                                           && (jsonObject1.optJSONObject("walking").has("bus") && jsonObject1.optJSONObject("walking").optJSONObject("bus").has("buslines") &&jsonObject1.optJSONObject("walking").optJSONObject("bus").optJSONObject("buslines") ==null)) {
                                                        busStr += "终点步行距离" + distance + "米";
                                                        superPointsModule.setEndDis(distance);
                                                    }/* else if (i == segments.length() - 1 && i == 0) {//只有一个数据
                                                        busStr += "起点步行距离" + distance + "米";
                                                    }*/ else {
                                                        busStr += "换乘点步行距离"+i +"   " + distance + "米&&&";
                                                        if (i==1){
                                                            superPointsModule.setTranDis1(distance);
                                                        }else if (i==2){
                                                            superPointsModule.setTranDis2(distance);
                                                        }else if (i==3){
                                                            superPointsModule.setTranDis3(distance);
                                                        }else if (i==4){
                                                            superPointsModule.setTranDis4(distance);
                                                        }else if (i==5){
                                                            superPointsModule.setTranDis5(distance);
                                                        }
                                                    }
//                                                    if (walking !=null &&walking.has("distance")){
//                                                        String distance1 = walking.optString("distance");
//                                                        if (i==0){
//                                                            busStr+="起点步行距离"+distance+"米&&&";
//                                                        }else if (i ==segments.length()-1){
//                                                            busStr+="终点步行距离"+distance+"米";
//                                                        }else {
//                                                            busStr += "换乘点步行距离" + distance + "米&&&";
//                                                        }
//                                                    }else{
//                                                        busStr += "换乘点步行距离" + 0 + "米&&&";
//                                                    }
                                                } else {
                                                    busStr += "换乘点步行距离" + 0 + "米&&&";
                                                }
                                            } else {
                                                busStr += "换乘点步行距离" + 0 + "米&&&";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Log.d("===", "onSuccess:     " + busStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("===", "Exception:     " + e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取 excel 表格中的数据,不能在主线程中调用
     *
     * @param xlsName excel 表格的名称
     * @param index   第几张表格中的数据
     */
    private ArrayList<PointModule> getXlsData(String xlsName, int index, String type) {
        ArrayList<PointModule> pointList = new ArrayList<PointModule>();
        AssetManager assetManager = getAssets();

        try {
            Workbook workbook = Workbook.getWorkbook(assetManager.open(xlsName));
            Sheet sheet = workbook.getSheet(index);

            int sheetNum = workbook.getNumberOfSheets();
            int sheetRows = sheet.getRows();
            int sheetColumns = sheet.getColumns();

            Log.d(TAG, "the num of sheets is " + sheetNum);
            Log.d(TAG, "the name of sheet is  " + sheet.getName());
            Log.d(TAG, "total rows is 行=" + sheetRows);
            Log.d(TAG, "total cols is 列=" + sheetColumns);

            for (int i = 0; i < sheetRows; i++) {
                PointModule countryModel = new PointModule();

                countryModel.setId(sheet.getCell(0, i).getContents());
                countryModel.setBianhao(sheet.getCell(1, i).getContents());
                countryModel.setLonPoint(((NumberCell) sheet.getCell(2, i)).getValue() + "");
                countryModel.setLatPoint(((NumberCell) sheet.getCell(3, i)).getValue() + "");
                pointList.add(countryModel);
            }

            workbook.close();
            if (type.equals("0")) {
                DataListManager.getInstance().StartPoi.clear();
                DataListManager.getInstance().StartPoi.addAll(pointList);
                mStartList.addAll(pointList);
            } else {
                DataListManager.getInstance().EndPoi.clear();
                DataListManager.getInstance().EndPoi.addAll(pointList);
                mEndList.addAll(pointList);
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mText5.setText("加载成功");
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "read error=" + e, e);
        }

        return pointList;
    }

    private ArrayList<SuperPointsModule> getExcelData(String xlsName, int index, String type) {
        ArrayList<SuperPointsModule> pointList = new ArrayList<SuperPointsModule>();
        AssetManager assetManager = getAssets();

        try {
            Workbook workbook = Workbook.getWorkbook(assetManager.open(xlsName));
            Sheet sheet = workbook.getSheet(index);

            int sheetNum = workbook.getNumberOfSheets();
            int sheetRows = sheet.getRows();
            int sheetColumns = sheet.getColumns();

            Log.d(TAG, "the num of sheets is " + sheetNum);
            Log.d(TAG, "the name of sheet is  " + sheet.getName());
            Log.d(TAG, "total rows is 行=" + sheetRows);
            Log.d(TAG, "total cols is 列=" + sheetColumns);

            for (int i = 0; i < sheetRows; i++) {
                SuperPointsModule countryModel = new SuperPointsModule();
                countryModel.setId(sheet.getCell(0, i).getContents());
                countryModel.setBianhao(sheet.getCell(1, i).getContents());
                countryModel.setStartLon((sheet.getCell(2, i)).getContents() + "");
                countryModel.setStartLat((sheet.getCell(3, i)).getContents() + "");
                countryModel.setEndLon((sheet.getCell(4, i)).getContents() + "");
                countryModel.setEndLat((sheet.getCell(5, i)).getContents() + "");
                pointList.add(countryModel);
            }

            workbook.close();
            DataListManager.getInstance().m7.addAll(pointList);
            for (int i = 0; i < DataListManager.getInstance().m7.size(); i++) {
                Log.d(TAG, "getExcelData: "+i +"     "+DataListManager.getInstance().m7.get(i).getId()+"   "+DataListManager.getInstance().m7.get(i).getBianhao());
            }
//
//            for (int i = 0; i < 5000; i++) {
//                DataListManager.getInstance().m1.add(pointList.get(i));
//            }
//            for (int i = 5000; i < 10000; i++) {
//                DataListManager.getInstance().m2.add(pointList.get(i));
//            }
//            for (int i = 10000; i < 15000; i++) {
//                DataListManager.getInstance().m3.add(pointList.get(i));
//            }
//            for (int i = 15000; i < 20000; i++) {
//                DataListManager.getInstance().m4.add(pointList.get(i));
//            }
//            for (int i = 20000; i < 25000; i++) {
//                DataListManager.getInstance().m5.add(pointList.get(i));
//            }
//            for (int i = 25000; i < pointList.size(); i++) {
//                DataListManager.getInstance().m6.add(pointList.get(i));
//            }

            Log.d(TAG, "getExcelData:   "+DataListManager.getInstance().m1.size()+" "+DataListManager.getInstance().m2.size()+" "+DataListManager.getInstance().m3.size()+" "
                    +DataListManager.getInstance().m4.size()+" "+DataListManager.getInstance().m5.size()+" "+DataListManager.getInstance().m6.size()+" "+DataListManager.getInstance().m7.size());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mText5.setText("加载成功");
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "read error=" + e, e);
        }

        return pointList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_1:

                generateFinalExcel();

               /* new Thread() {
                    @Override
                    public void run() {
                        new ExcelDataLoader().execute("StartPoi.xls", "0");
                    }
                }.start();*/
                break;
            case R.id.tv_2:
               /* new Thread() {
                    @Override
                    public void run() {
                        new ExcelDataLoader().execute("EndPoi.xls", "1");
                    }
                }.start();*/
               getRoat1(DataListManager.getInstance().m1.get(0));
                break;
            case R.id.tv_3:
                mText5.setText("");
                for (int i = 0; i < DataListManager.getInstance().StartPoi.size(); i++) {
                    mText5.setText(mText5.getText().toString() + DataListManager.getInstance().StartPoi.get(i).toString());
                }
                break;
            case R.id.tv_4:
                mText5.setText("");
                for (int i = 0; i < DataListManager.getInstance().EndPoi.size(); i++) {
                    mText5.setText(mText5.getText().toString() + DataListManager.getInstance().EndPoi.get(i).toString());
                }
                break;
            case R.id.tv_5:

                break;
            case R.id.tv_6:
                String filePath = Environment.getExternalStorageDirectory() + "/$MuMu共享文件夹";
                File file = new File(filePath);
                if (!file.exists()) {
                    file.mkdirs();
                }
                String excelFileName = "/demo.xls";
                filePath = filePath + excelFileName;
                String[] strArr = {"序号", "编号", "起点经度", "起点纬度", "终点经度", "终点纬度", "起点步行距离", "换乘步行距离1", "换乘步行距离2", "换乘步行距离3", "换乘步行距离4", "换乘步行距离5", "终点步行距离"};
                List<SuperPointsModule> list = new ArrayList<>();
                for (int i = 0; i < mStartList.size(); i++) {
                    for (int j = 0; j < mEndList.size(); j++) {
                        SuperPointsModule superPointsModule = new SuperPointsModule();
                        superPointsModule.id = mStartList.get(i).getBianhao();
                        superPointsModule.bianhao = mEndList.get(j).getBianhao();
                        superPointsModule.startLon = mStartList.get(i).getLonPoint();
                        superPointsModule.startLat = mStartList.get(i).getLatPoint();
                        superPointsModule.endLon = mEndList.get(j).getLonPoint();
                        superPointsModule.endLat = mEndList.get(j).getLatPoint();
                        list.add(superPointsModule);

                    }
                }
                ExcelUtil.initExcel(filePath, strArr);
                ExcelUtil.writeObjListToExcel(list, filePath, this);
                break;
            case R.id.tv_7:
                new Thread() {
                    @Override
                    public void run() {
                        new ExcelDataLoaderMore().execute("m7.xls", "0");
                    }
                }.start();
                break;
            case R.id.tv_8:
                String filePath1 = Environment.getExternalStorageDirectory() + "/$MuMu共享文件夹";
                File file1 = new File(filePath1);
                if (!file1.exists()) {
                    file1.mkdirs();
                }
                String excelFileName1 = "/m" + count + ".xls";
                filePath1 = filePath1 + excelFileName1;
                String[] strArr1 = {"序号", "编号", "起点经度", "起点纬度", "终点经度", "终点纬度", "起点步行距离", "换乘步行距离1", "换乘步行距离2", "换乘步行距离3", "换乘步行距离4", "换乘步行距离5", "终点步行距离"};

                switch (count) {
                    case 1:
                        ExcelUtil.initExcel(filePath1, strArr1);
                        Log.d(TAG, "onClick:    "+DataListManager.getInstance().m1.size());
                        ExcelUtil.writeObjListToExcel(DataListManager.getInstance().m1, filePath1, this);
                        count++;
                        break;
                    case 2:
                        ExcelUtil.initExcel(filePath1, strArr1);
                        Log.d(TAG, "onClick:    "+DataListManager.getInstance().m2.size());
                        ExcelUtil.writeObjListToExcel(DataListManager.getInstance().m2, filePath1, this);
                        count++;
                        break;
                    case 3:
                        ExcelUtil.initExcel(filePath1, strArr1);
                        Log.d(TAG, "onClick:    "+DataListManager.getInstance().m3.size());
                        ExcelUtil.writeObjListToExcel(DataListManager.getInstance().m3, filePath1, this);
                        count++;
                        break;
                    case 4:
                        ExcelUtil.initExcel(filePath1, strArr1);
                        Log.d(TAG, "onClick:    "+DataListManager.getInstance().m4.size());
                        ExcelUtil.writeObjListToExcel(DataListManager.getInstance().m4, filePath1, this);
                        count++;
                        break;
                    case 5:
                        ExcelUtil.initExcel(filePath1, strArr1);
                        Log.d(TAG, "onClick:    "+DataListManager.getInstance().m5.size());
                        ExcelUtil.writeObjListToExcel(DataListManager.getInstance().m5, filePath1, this);
                        count++;
                        break;
                    case 6:
                        ExcelUtil.initExcel(filePath1, strArr1);
                        Log.d(TAG, "onClick:    "+DataListManager.getInstance().m6.size());
                        ExcelUtil.writeObjListToExcel(DataListManager.getInstance().m6, filePath1, this);
                        count =1;
                        break;
                }
                break;
            case R.id.tv_9:
                //读取m1  加载网络数据
                if (count==1){
                    getRoat(DataListManager.getInstance().m1.get(index));
                }else if (count==2){
                    getRoat(DataListManager.getInstance().m2.get(index));
                }else if (count==3){
                    getRoat(DataListManager.getInstance().m3.get(index));
                }else if (count==4){
                    getRoat(DataListManager.getInstance().m4.get(index));
                }else if (count==5){
                    getRoat(DataListManager.getInstance().m5.get(index));
                }else if (count==6){
                    getRoat(DataListManager.getInstance().m6.get(index));
                }else if (count==7){
                    getRoat(DataListManager.getInstance().m7.get(index));
                }
                break;
        }
    }

    private void generateFinalExcel() {
        String filePath0 = Environment.getExternalStorageDirectory() + "/$MuMu共享文件夹";
        File file0 = new File(filePath0);
        if (!file0.exists()) {
            file0.mkdirs();
        }
        String excelFileName0 = "/m"+count+"rr.xls";
        filePath0 = filePath0 + excelFileName0;
        String[] strArr0 = {"序号", "编号", "起点经度", "起点纬度", "终点经度", "终点纬度","路线总长度","起点步行距离", "换乘步行距离1", "换乘步行距离2", "换乘步行距离3", "换乘步行距离4", "换乘步行距离5", "终点步行距离"};
        ExcelUtil.initExcel(filePath0, strArr0);
        ExcelUtil.writeObjListToExcel(DataListManager.getInstance().m1Rr, filePath0, this);
    }

    int count = 7;


    //在异步方法中 调用
    private class ExcelDataLoader extends AsyncTask<String, Void, ArrayList<PointModule>> {

        @Override
        protected void onPreExecute() {
//            progressDialog.setMessage("加载中,请稍后......");
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
        }

        @Override
        protected ArrayList<PointModule> doInBackground(String... params) {
            return getXlsData(params[0], 0, params[1]);
        }

        @Override
        protected void onPostExecute(ArrayList<PointModule> countryModels) {
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//
//            if (countryModels != null && countryModels.size() > 0) {
//                //存在数据
//                sortByName(countryModels);
//                setupData(countryModels);
//            } else {
//                //加载失败
//
//
//            }

        }
    }

    //在异步方法中 调用
    private class ExcelDataLoaderMore extends AsyncTask<String, Void, ArrayList<SuperPointsModule>> {

        @Override
        protected void onPreExecute() {
//            progressDialog.setMessage("加载中,请稍后......");
//            progressDialog.setCanceledOnTouchOutside(false);
//            progressDialog.show();
        }

        @Override
        protected ArrayList<SuperPointsModule> doInBackground(String... params) {
            return getExcelData(params[0], 0, params[1]);
        }

        @Override
        protected void onPostExecute(ArrayList<SuperPointsModule> countryModels) {
//            if (progressDialog.isShowing()) {
//                progressDialog.dismiss();
//            }
//
//            if (countryModels != null && countryModels.size() > 0) {
//                //存在数据
//                sortByName(countryModels);
//                setupData(countryModels);
//            } else {
//                //加载失败
//
//
//            }

        }
    }

}
