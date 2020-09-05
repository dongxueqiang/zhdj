package com.zhdj.zhdj.dao;

import android.content.Context;
import android.util.Log;

import com.zhdj.greendao.gen.MessageModelDao;
import com.zhdj.zhdj.model.MessageModel;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * @ClassName MessageDao
 * @Description TODO
 * @Author dongxueqiang
 * @Date 2020/9/5 1:59 PM
 */
public class MessageDao {

    private static final String TAG = MessageDao.class.getSimpleName();
    private DaoManager mManager;

    public MessageDao(Context context) {
        mManager = DaoManager.getInstance();
        mManager.init(context);
    }

    /**
     * 完成MessageModel记录的插入，如果表未创建，先创建MessageModel表
     *
     * @param messageModel
     * @return
     */
    public boolean insertMessageModel(MessageModel messageModel) {
        boolean flag = false;
        flag = mManager.getDaoSession().getMessageModelDao().insert(messageModel) == -1 ? false : true;
        Log.i(TAG, "insert MessageModel :" + flag + "-->" + messageModel.toString());
        return flag;
    }

    /**
     * 插入多条数据，在子线程操作
     *
     * @param messageModelList
     * @return
     */
    public boolean insertMultMessageModel(final List<MessageModel> messageModelList) {
        boolean flag = false;
        try {
            mManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (MessageModel MessageModel : messageModelList) {
                        mManager.getDaoSession().insertOrReplace(MessageModel);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改一条数据
     *
     * @param messageModel
     * @return
     */
    public boolean updateMessageModel(MessageModel messageModel) {
        boolean flag = false;
        try {
            mManager.getDaoSession().update(messageModel);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除单条记录
     *
     * @param messageModel
     * @return
     */
    public boolean deleteMessageModel(MessageModel messageModel) {
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().delete(messageModel);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有记录
     *
     * @return
     */
    public boolean deleteAll() {
        boolean flag = false;
        try {
            //按照id删除
            mManager.getDaoSession().deleteAll(MessageModel.class);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 查询所有记录
     *
     * @return
     */
    public List<MessageModel> queryAllMessageModel() {
        return mManager.getDaoSession().loadAll(MessageModel.class);
    }

    /**
     * 根据主键id查询记录
     *
     * @param key
     * @return
     */
    public MessageModel queryMessageModelById(long key) {
        return mManager.getDaoSession().load(MessageModel.class, key);
    }

    /**
     * 使用native sql进行查询操作
     */
    public List<MessageModel> queryMessageModelByNativeSql(String sql, String[] conditions) {
        return mManager.getDaoSession().queryRaw(MessageModel.class, sql, conditions);
    }

    /**
     * 使用queryBuilder进行查询
     *
     * @return
     */
    public List<MessageModel> queryMessageModelByQueryBuilder(long id) {
        QueryBuilder<MessageModel> queryBuilder = mManager.getDaoSession().queryBuilder(MessageModel.class);
        return queryBuilder.where(MessageModelDao.Properties.Id.eq(id)).list();
//        return queryBuilder.where(MessageModelDao.Properties._id.ge(id)).list();
    }

}
