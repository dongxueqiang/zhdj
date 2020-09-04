package com.zhdj.zhdj.utils;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.zhdj.zhdj.global.SpConstant;
import com.zhdj.zhdj.model.LiveMessageModel;

/**
 * @ClassName MessageUtils
 * @Author dongxueqiang
 * @Date 2020/9/1 19:10
 * @Title
 */
public class MessageUtils {

    public static LiveMessageModel getMessage() {
        LiveMessageModel um = new LiveMessageModel();
        String userInfo = SPUtils.getInstance().getString(SpConstant.MESSAGE_MODEL);
        if (userInfo != null && !"".equals(userInfo)) {
            try {
                Gson gson = new Gson();
                um = gson.fromJson(userInfo, LiveMessageModel.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return um;
    }

    public static void saveMessage(LiveMessageModel model){
        SPUtils.getInstance().put(SpConstant.MESSAGE_MODEL, new Gson().toJson(model));
    }
}
