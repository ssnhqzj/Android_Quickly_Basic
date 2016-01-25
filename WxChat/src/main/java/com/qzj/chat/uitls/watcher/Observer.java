package com.qzj.chat.uitls.watcher;

/**
 * Created by qzj on 2015/12/17.
 */
public interface Observer {
    /**
     * 更新接口
     * @param obj
     */
    public void update(int what,Object obj);
}
