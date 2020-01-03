package com.qiyc.daohang;

import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: rand
 * @Date: 2020-01-02 17:32
 */

public class ExcelEntity {
    int nums;
    List<ExcelItemEntity> lists;

    public ExcelEntity(int nums, List<ExcelItemEntity> lists) {
        this.nums = nums;
        this.lists = lists;
    }

    public int getNums() {
        return nums;
    }

    public void setNums(int nums) {
        this.nums = nums;
    }

    public List<ExcelItemEntity> getLists() {
        return lists;
    }

    public void setLists(List<ExcelItemEntity> lists) {
        this.lists = lists;
    }
}
