package com.zhdj.zhdj.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName TimeModel
 * @Author dongxueqiang
 * @Date 2020/7/19 19:54
 * @Title
 */
public class TimeModel implements Parcelable {
    /**
     * id : 1
     * function_type : 1
     * group_id : 2
     * group_name : 测试分组2
     * type : 1
     * day : 0
     * function_time : 21:15:18
     * add_time : 2020-07-13 21:15:49
     * range_type : 1
     * status : 1
     * edit_time : 2020-07-13 21:25:49
     * is_delete : 0
     * user_id : 1
     * department : 学院1
     */

    private int id;
    /**
     * 1定时开机 0定时关机
     */
    private int function_type;
    private int group_id;
    private String group_name;
    /**
     * 1每天 2每星期 3每月
     */
    private int type;
    private String day;
    /**
     * 时间点
     */
    private String function_time;
    private String add_time;
    private int range_type;
    private int status;
    private String edit_time;
    private int is_delete;
    private int user_id;
    private String department;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFunction_type() {
        return function_type;
    }

    public void setFunction_type(int function_type) {
        this.function_type = function_type;
    }

    public int getGroup_id() {
        return group_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getFunction_time() {
        return function_time;
    }

    public void setFunction_time(String function_time) {
        this.function_time = function_time;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public int getRange_type() {
        return range_type;
    }

    public void setRange_type(int range_type) {
        this.range_type = range_type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(String edit_time) {
        this.edit_time = edit_time;
    }

    public int getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.function_type);
        dest.writeInt(this.group_id);
        dest.writeString(this.group_name);
        dest.writeInt(this.type);
        dest.writeString(this.day);
        dest.writeString(this.function_time);
        dest.writeString(this.add_time);
        dest.writeInt(this.range_type);
        dest.writeInt(this.status);
        dest.writeString(this.edit_time);
        dest.writeInt(this.is_delete);
        dest.writeInt(this.user_id);
        dest.writeString(this.department);
    }

    public TimeModel() {
    }

    protected TimeModel(Parcel in) {
        this.id = in.readInt();
        this.function_type = in.readInt();
        this.group_id = in.readInt();
        this.group_name = in.readString();
        this.type = in.readInt();
        this.day = in.readString();
        this.function_time = in.readString();
        this.add_time = in.readString();
        this.range_type = in.readInt();
        this.status = in.readInt();
        this.edit_time = in.readString();
        this.is_delete = in.readInt();
        this.user_id = in.readInt();
        this.department = in.readString();
    }

    public static final Parcelable.Creator<TimeModel> CREATOR = new Parcelable.Creator<TimeModel>() {
        @Override
        public TimeModel createFromParcel(Parcel source) {
            return new TimeModel(source);
        }

        @Override
        public TimeModel[] newArray(int size) {
            return new TimeModel[size];
        }
    };
}
