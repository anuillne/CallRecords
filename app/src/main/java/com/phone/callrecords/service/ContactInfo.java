package com.phone.callrecords.service;

/**
 *
 * @author： AnuilLne
 * @date： 2020/5/2 15:22
 * @description:
 */
public class ContactInfo {
    private String name;
    private String number;
    private int type;
    private long date;
    private long duration;

    /**
     * @author： AnuilLne
     * @date：   2020/5/2 16:25
     * @param: name
     * @param: number
     * @param: type 1 接听    2 拨出    3 未接
     * @param: date
     * @param: duration 持续时间
     * @return：
     * @description：
     */
    public ContactInfo(String name, String number, int type, long date, long duration){
        this.name = name;
        this.number = number;
        this.type = type;
        this.date = date;
        this.duration = duration;
    }
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
