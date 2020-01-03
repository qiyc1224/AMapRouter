package com.qiyc.daohang;

/**
 * @Description: java类作用描述
 * @Author: rand
 * @Date: 2020-01-03 18:39
 */
public class PointModule {
    public String id;
    public String bianhao;
    public String lonPoint;
    public String latPoint;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBianhao() {
        return bianhao;
    }

    public void setBianhao(String bianhao) {
        this.bianhao = bianhao;
    }

    public String getLonPoint() {
        return lonPoint;
    }

    public void setLonPoint(String lonPoint) {
        this.lonPoint = lonPoint;
    }

    public String getLatPoint() {
        return latPoint;
    }

    public void setLatPoint(String latPoint) {
        this.latPoint = latPoint;
    }

    @Override
    public String toString() {
        return "PointModule{" +
                "id='" + id + '\'' +
                ", bianhao='" + bianhao + '\'' +
                ", lonPoint='" + lonPoint + '\'' +
                ", latPoint='" + latPoint + '\'' +
                '}';
    }
}
