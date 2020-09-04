package com.zhdj.zhdj.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @ClassName LiveMessageModel
 * @Author dongxueqiang
 * @Date 2020/7/20 9:52
 * @Title
 */
public class LiveMessageModel implements Parcelable {
    private List<MessageModel> list;
    private int is_change;
    private int rotation_time;
    private int running_state;

    public int getRotation_time() {
        return rotation_time;
    }

    public int getRunning_state() {
        return running_state;
    }

    public void setRunning_state(int running_state) {
        this.running_state = running_state;
    }

    public void setRotation_time(int rotation_time) {
        this.rotation_time = rotation_time;
    }

    public List<MessageModel> getList() {
        return list;
    }

    public void setList(List<MessageModel> list) {
        this.list = list;
    }

    public int getIs_change() {
        return is_change;
    }

    public void setIs_change(int is_change) {
        this.is_change = is_change;
    }

    public LiveMessageModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.list);
        dest.writeInt(this.is_change);
        dest.writeInt(this.rotation_time);
        dest.writeInt(this.running_state);
    }

    protected LiveMessageModel(Parcel in) {
        this.list = in.createTypedArrayList(MessageModel.CREATOR);
        this.is_change = in.readInt();
        this.rotation_time = in.readInt();
        this.running_state = in.readInt();
    }

    public static final Creator<LiveMessageModel> CREATOR = new Creator<LiveMessageModel>() {
        @Override
        public LiveMessageModel createFromParcel(Parcel source) {
            return new LiveMessageModel(source);
        }

        @Override
        public LiveMessageModel[] newArray(int size) {
            return new LiveMessageModel[size];
        }
    };
}
