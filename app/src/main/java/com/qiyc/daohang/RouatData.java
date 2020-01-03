package com.qiyc.daohang;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @Description: java类作用描述
 * @Author: rand
 * @Date: 2019-12-30 19:13
 */
@Entity
public class RouatData {
    @Id(autoincrement = true)
    private Long id;

    @Property(nameInDb = "from_add")
    @Index(unique = true)
    private String from_add;

    @Property(nameInDb = "to_add")
    private String to_add;

    @Property(nameInDb = "start_dis")
    private String start_dis;
    @Property(nameInDb = "end_dis")
    private String end_dis;
    @Property(nameInDb = "trans_dis1")
    private String trans_dis1;

    @Property(nameInDb = "trans_dis2")
    private String trans_dis2;

    @Property(nameInDb = "trans_dis4")
    private String trans_dis3;

    @Property(nameInDb = "trans_dis4")
    private String trans_dis4;

    @Property(nameInDb = "trans_dis5")
    private String trans_dis5;
    @Property(nameInDb = "trans_dis6")
    private String trans_dis6;
    @Generated(hash = 1026103934)
    public RouatData(Long id, String from_add, String to_add, String start_dis,
            String end_dis, String trans_dis1, String trans_dis2, String trans_dis3,
            String trans_dis4, String trans_dis5, String trans_dis6) {
        this.id = id;
        this.from_add = from_add;
        this.to_add = to_add;
        this.start_dis = start_dis;
        this.end_dis = end_dis;
        this.trans_dis1 = trans_dis1;
        this.trans_dis2 = trans_dis2;
        this.trans_dis3 = trans_dis3;
        this.trans_dis4 = trans_dis4;
        this.trans_dis5 = trans_dis5;
        this.trans_dis6 = trans_dis6;
    }
    @Generated(hash = 807243717)
    public RouatData() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFrom_add() {
        return this.from_add;
    }
    public void setFrom_add(String from_add) {
        this.from_add = from_add;
    }
    public String getTo_add() {
        return this.to_add;
    }
    public void setTo_add(String to_add) {
        this.to_add = to_add;
    }
    public String getStart_dis() {
        return this.start_dis;
    }
    public void setStart_dis(String start_dis) {
        this.start_dis = start_dis;
    }
    public String getEnd_dis() {
        return this.end_dis;
    }
    public void setEnd_dis(String end_dis) {
        this.end_dis = end_dis;
    }
    public String getTrans_dis1() {
        return this.trans_dis1;
    }
    public void setTrans_dis1(String trans_dis1) {
        this.trans_dis1 = trans_dis1;
    }
    public String getTrans_dis2() {
        return this.trans_dis2;
    }
    public void setTrans_dis2(String trans_dis2) {
        this.trans_dis2 = trans_dis2;
    }
    public String getTrans_dis3() {
        return this.trans_dis3;
    }
    public void setTrans_dis3(String trans_dis3) {
        this.trans_dis3 = trans_dis3;
    }
    public String getTrans_dis4() {
        return this.trans_dis4;
    }
    public void setTrans_dis4(String trans_dis4) {
        this.trans_dis4 = trans_dis4;
    }
    public String getTrans_dis5() {
        return this.trans_dis5;
    }
    public void setTrans_dis5(String trans_dis5) {
        this.trans_dis5 = trans_dis5;
    }
    public String getTrans_dis6() {
        return this.trans_dis6;
    }
    public void setTrans_dis6(String trans_dis6) {
        this.trans_dis6 = trans_dis6;
    }


}
