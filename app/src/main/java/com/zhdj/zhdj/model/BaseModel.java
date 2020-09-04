package com.zhdj.zhdj.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author : johnny
 * @date :   2019-05-13
 * @desc :
 */
public class BaseModel<T> implements Parcelable {
    private int ret;
    private String msg;
    private boolean ok;
    private T data;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isOk() {
        return ok;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ret);
        dest.writeString(this.msg);
        dest.writeByte(this.ok ? (byte) 1 : (byte) 0);
    }

    public BaseModel() {
    }

    protected BaseModel(Parcel in) {
        this.ret = in.readInt();
        this.msg = in.readString();
        this.ok = in.readByte() != 0;
    }

    public static final Creator<BaseModel> CREATOR = new Creator<BaseModel>() {
        @Override
        public BaseModel createFromParcel(Parcel source) {
            return new BaseModel(source);
        }

        @Override
        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }
    };
}
