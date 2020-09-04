package com.zhdj.zhdj.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @ClassName MessageModel
 * @Author dongxueqiang
 * @Date 2020/7/14 21:50
 * @Title
 */
public class MessageModel implements Parcelable {
    /**
     * id : 1
     * resources_name : 测试媒资1
     * resources_type : 1
     * imgs_url : http://dj.qsmedia.org.cn/uploads/file/284062beccfd54a80a968a1ff1730847.jpg
     * resources_url : http://dj.qsmedia.org.cn/uploads/file/284062beccfd54a80a968a1ff1730847.jpg
     * add_time : 2020-07-13 21:51:44
     * edit_time : 2020-07-14 10:42:27
     * tenant_id : 1
     * tenant_name :
     * status : 1
     * audit_time : 2020-07-14 14:14:41
     * running_state : 1
     * user_id : 1
     * resources_origin : 租户媒资库
     * is_delete : 0
     * ext : doc
     * file_name : 12312
     * imgs_name : 321312
     * department : 设计部
     * is_self : 1
     * num : 7
     *
     */

    private int id;
    private String resources_name;
    private int resources_type;
    private String imgs_url;
    private String resources_url;
    private String add_time;
    private String edit_time;
    private int tenant_id;
    private String tenant_name;
    private int status;
    private String audit_time;
    private int running_state;
    private int user_id;
    private String resources_origin;
    private int is_delete;
    private String ext;
    private String file_name;
    private String imgs_name;
    private String department;
    private long startTime;
    private long endTime;
    private int is_self;
    private int num;

    public int getId() {
        return id;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getResources_name() {
        return resources_name;
    }

    public void setResources_name(String resources_name) {
        this.resources_name = resources_name;
    }

    public int getResources_type() {
        return resources_type;
    }

    public void setResources_type(int resources_type) {
        this.resources_type = resources_type;
    }

    public String getImgs_url() {
        return imgs_url;
    }

    public void setImgs_url(String imgs_url) {
        this.imgs_url = imgs_url;
    }

    public String getResources_url() {
        return resources_url;
    }

    public void setResources_url(String resources_url) {
        this.resources_url = resources_url;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getEdit_time() {
        return edit_time;
    }

    public void setEdit_time(String edit_time) {
        this.edit_time = edit_time;
    }

    public int getTenant_id() {
        return tenant_id;
    }

    public void setTenant_id(int tenant_id) {
        this.tenant_id = tenant_id;
    }

    public String getTenant_name() {
        return tenant_name;
    }

    public void setTenant_name(String tenant_name) {
        this.tenant_name = tenant_name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAudit_time() {
        return audit_time;
    }

    public void setAudit_time(String audit_time) {
        this.audit_time = audit_time;
    }

    public int getRunning_state() {
        return running_state;
    }

    public void setRunning_state(int running_state) {
        this.running_state = running_state;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getResources_origin() {
        return resources_origin;
    }

    public void setResources_origin(String resources_origin) {
        this.resources_origin = resources_origin;
    }

    public int getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(int is_delete) {
        this.is_delete = is_delete;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getFile_name() {
        return file_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getImgs_name() {
        return imgs_name;
    }

    public void setImgs_name(String imgs_name) {
        this.imgs_name = imgs_name;
    }

    @Override
    public String toString() {
        return "MessageModel{" +
                "id=" + id +
                ", resources_name='" + resources_name + '\'' +
                ", resources_type=" + resources_type +
                ", imgs_url='" + imgs_url + '\'' +
                ", resources_url='" + resources_url + '\'' +
                ", add_time='" + add_time + '\'' +
                ", edit_time='" + edit_time + '\'' +
                ", tenant_id=" + tenant_id +
                ", tenant_name='" + tenant_name + '\'' +
                ", status=" + status +
                ", audit_time='" + audit_time + '\'' +
                ", running_state=" + running_state +
                ", user_id=" + user_id +
                ", resources_origin='" + resources_origin + '\'' +
                ", is_delete=" + is_delete +
                ", ext='" + ext + '\'' +
                ", file_name='" + file_name + '\'' +
                ", imgs_name='" + imgs_name + '\'' +
                '}';
    }

    public MessageModel() {
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getIs_self() {
        return is_self;
    }

    public void setIs_self(int is_self) {
        this.is_self = is_self;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.resources_name);
        dest.writeInt(this.resources_type);
        dest.writeString(this.imgs_url);
        dest.writeString(this.resources_url);
        dest.writeString(this.add_time);
        dest.writeString(this.edit_time);
        dest.writeInt(this.tenant_id);
        dest.writeString(this.tenant_name);
        dest.writeInt(this.status);
        dest.writeString(this.audit_time);
        dest.writeInt(this.running_state);
        dest.writeInt(this.user_id);
        dest.writeString(this.resources_origin);
        dest.writeInt(this.is_delete);
        dest.writeString(this.ext);
        dest.writeString(this.file_name);
        dest.writeString(this.imgs_name);
        dest.writeString(this.department);
        dest.writeLong(this.startTime);
        dest.writeLong(this.endTime);
        dest.writeInt(this.is_self);
        dest.writeInt(this.num);
    }

    protected MessageModel(Parcel in) {
        this.id = in.readInt();
        this.resources_name = in.readString();
        this.resources_type = in.readInt();
        this.imgs_url = in.readString();
        this.resources_url = in.readString();
        this.add_time = in.readString();
        this.edit_time = in.readString();
        this.tenant_id = in.readInt();
        this.tenant_name = in.readString();
        this.status = in.readInt();
        this.audit_time = in.readString();
        this.running_state = in.readInt();
        this.user_id = in.readInt();
        this.resources_origin = in.readString();
        this.is_delete = in.readInt();
        this.ext = in.readString();
        this.file_name = in.readString();
        this.imgs_name = in.readString();
        this.department = in.readString();
        this.startTime = in.readLong();
        this.endTime = in.readLong();
        this.is_self = in.readInt();
        this.num = in.readInt();
    }

    public static final Creator<MessageModel> CREATOR = new Creator<MessageModel>() {
        @Override
        public MessageModel createFromParcel(Parcel source) {
            return new MessageModel(source);
        }

        @Override
        public MessageModel[] newArray(int size) {
            return new MessageModel[size];
        }
    };
}
