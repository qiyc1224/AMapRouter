package com.qiyc.daohang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
//        getRoad();
//        getRoat();

        mText5.setMovementMethod(ScrollingMovementMethod.getInstance());

        mText1.setOnClickListener(this);
        mText2.setOnClickListener(this);
        mText3.setOnClickListener(this);
        mText4.setOnClickListener(this);
        mText5.setOnClickListener(this);

    }

    private void getRoat() {
        try {
            OkGo.<String>get(url + "&" + originStr + "=" + mStartPoint.getLongitude() + "," + mStartPoint.getLatitude() +
                    "&" + destinationStr + "=" + mEndPoint.getLongitude() + "," + mEndPoint.getLatitude() +
                    "&" + cityStr + "=" + "010").execute(new StringCallback() {
                @Override
                public void onSuccess(Response<String> response) {
                    Log.d("====", "onSuccess: "+response.body());
                    try {
                        String busStr="";

                        JSONObject jsonObject = new JSONObject(response.body());
                        if (jsonObject.has("route")){
                            JSONObject rout= jsonObject.optJSONObject("route");
                            if (rout.has("transits")){
                                JSONArray transits=rout.optJSONArray("transits");
                                JSONObject transitsObj=null;
                                if (transits!=null && transits.length()>0){
                                    transitsObj=new JSONObject(transits.get(1).toString());
                                }
                                if (transitsObj !=null &&transitsObj.has("distance")){
                                    busStr = "总路程"+transitsObj.optString("distance")+"米&&&";
                                }
                                if (transitsObj !=null && transitsObj.has("segments")){
                                    JSONArray segments = transitsObj.optJSONArray("segments");
                                    if (segments!=null && segments.length()>0){
                                        for (int i = 0; i < segments.length(); i++) {
                                            if (segments.get(i)!= null){
                                                JSONObject  jsonObject1 = new JSONObject(segments.get(i).toString());
                                                if (jsonObject1.has("walking")){
                                                    JSONObject walking = jsonObject1.optJSONObject("walking");
                                                    String distance="0";
                                                    if (walking !=null &&walking.has("distance")){
                                                        distance = walking.optString("distance");
                                                    }
                                                    if (i==0){//第0个  当做起点步行距离
                                                        busStr+="起点步行距离"+distance+"米&&&";
                                                    }else if (i ==segments.length()-1 && i!=1){
                                                        busStr+="终点步行距离"+distance+"米";
                                                    }else if (i==segments.length() -1 && i==1){//只有一个数据
                                                        busStr+="终点步行距离"+0+"米";
                                                    }else{
                                                        busStr += "换乘点步行距离" + distance + "米&&&";
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
                                                }else{
                                                    busStr += "换乘点步行距离" + 0 + "米&&&";
                                                }
                                            }else{
                                                busStr += "换乘点步行距离" + 0 + "米&&&";
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Log.d("===", "onSuccess:     "+busStr);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("===", "Exception:     "+e.getMessage());
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
    private ArrayList<PointModule> getXlsData(String xlsName, int index,String type) {
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

                countryModel.setId(sheet.getCell(0, i).getContents().replace("数据",""));
                countryModel.setBianhao(sheet.getCell(1, i).getContents().replace("数据",""));
                countryModel.setLonPoint(sheet.getCell(2, i).getContents().replace("数据",""));
                countryModel.setLatPoint(sheet.getCell(3, i).getContents().replace("数据",""));
                pointList.add(countryModel);
            }

            workbook.close();
            if (type.equals("0")){
                DataListManager.getInstance().StartPoi.clear();
                DataListManager.getInstance().StartPoi.addAll(pointList);
            }else{
                DataListManager.getInstance().EndPoi.clear();
                DataListManager.getInstance().EndPoi.addAll(pointList);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_1:
                new Thread(){
                    @Override
                    public void run() {
                        new ExcelDataLoader().execute("StartPoi.xls","0");
                    }
                }.start();
                break;
            case R.id.tv_2:
                new Thread(){
                    @Override
                    public void run() {
                        new ExcelDataLoader().execute("EndPoi.xls","1");
                    }
                }.start();
                break;
            case R.id.tv_3:
                mText5.setText("");
                for (int i = 0; i < DataListManager.getInstance().StartPoi.size(); i++) {
                   mText5.setText(mText5.getText().toString()+ DataListManager.getInstance().StartPoi.get(i).toString());
                }
                break;
                case R.id.tv_4:
                    mText5.setText("");
                    for (int i = 0; i < DataListManager.getInstance().EndPoi.size(); i++) {
                        mText5.setText(mText5.getText().toString()+ DataListManager.getInstance().EndPoi.get(i).toString());
                    }
                break;
                case R.id.tv_5:

                break;
        }
    }


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
            return getXlsData(params[0], 0,params[1]);
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

    }
