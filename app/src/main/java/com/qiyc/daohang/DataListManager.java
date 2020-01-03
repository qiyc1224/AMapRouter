package com.qiyc.daohang;

import java.util.ArrayList;

/**
 * @Description: java类作用描述
 * @Author: rand
 * @Date: 2020-01-03 18:56
 */
public class DataListManager {
    private static DataListManager INSTANCE;

    private DataListManager() {
    }

    public static DataListManager getInstance() {
        if (INSTANCE == null) {
            synchronized ((DataListManager.class)) {
                if (INSTANCE == null) {
                    INSTANCE = new DataListManager();
                }
            }
        }
        return INSTANCE;
    }

    public ArrayList<PointModule> StartPoi = new ArrayList<>();
    public ArrayList<PointModule> EndPoi = new ArrayList<>();
    public int codeName = 0;
    public int codeNameCache = 0;



    public void clean() {
        INSTANCE = null;
    }

}

