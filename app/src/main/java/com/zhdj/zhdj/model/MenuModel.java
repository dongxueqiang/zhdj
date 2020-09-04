package com.zhdj.zhdj.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName MenuModel
 * @Author dongxueqiang
 * @Date 2020/7/14 19:29
 * @Title
 */
public class MenuModel implements Parcelable {
    private int id;
    private String module_name;
    private String back_imgs_url;
    private int running_state;

    public MenuModel(String module_name) {
        this.module_name = module_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModule_name() {
        return module_name;
    }

    public void setModule_name(String module_name) {
        this.module_name = module_name;
    }

    public String getBack_imgs_url() {
        return back_imgs_url;
    }

    public void setBack_imgs_url(String back_imgs_url) {
        this.back_imgs_url = back_imgs_url;
    }

    public int getRunning_state() {
        return running_state;
    }

    public void setRunning_state(int running_state) {
        this.running_state = running_state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.module_name);
        dest.writeString(this.back_imgs_url);
        dest.writeInt(this.running_state);
    }

    protected MenuModel(Parcel in) {
        this.id = in.readInt();
        this.module_name = in.readString();
        this.back_imgs_url = in.readString();
        this.running_state = in.readInt();
    }

    public static final Parcelable.Creator<MenuModel> CREATOR = new Parcelable.Creator<MenuModel>() {
        @Override
        public MenuModel createFromParcel(Parcel source) {
            return new MenuModel(source);
        }

        @Override
        public MenuModel[] newArray(int size) {
            return new MenuModel[size];
        }
    };
}
