package com.zhdj.zhdj.model;

/**
 * @ClassName SkinModel
 * @Author dongxueqiang
 * @Date 2020/7/14 19:06
 * @Title
 */
public class SkinModel {
    /**
     * id : 1
     * back_imgs_url : 31231233333
     * logo_url : 213123
     * add_time : 0
     * edit_time : 2020
     */

    private int id;
    private String back_imgs_url;
    private String logo_url;
    private int add_time;
    private int edit_time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBack_imgs_url() {
        return back_imgs_url;
    }

    public void setBack_imgs_url(String back_imgs_url) {
        this.back_imgs_url = back_imgs_url;
    }

    public String getLogo_url() {
        return logo_url;
    }

    public void setLogo_url(String logo_url) {
        this.logo_url = logo_url;
    }

    public int getAdd_time() {
        return add_time;
    }

    public void setAdd_time(int add_time) {
        this.add_time = add_time;
    }

    public int getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(int edit_time) {
        this.edit_time = edit_time;
    }
}
