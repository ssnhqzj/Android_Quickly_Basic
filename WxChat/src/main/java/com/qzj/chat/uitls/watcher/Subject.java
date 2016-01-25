package com.qzj.chat.uitls.watcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qzj on 2015/12/17.
 */
public abstract class Subject {
    /**
     * 用来保存注册的观察者对象
     */
    private List<Observer> list = new ArrayList<Observer>();

    /**
     * 注册观察者对象
     *
     * @param observer 观察者对象
     */
    public void attach(Observer observer) {
        list.add(observer);
    }

    /**
     * 删除观察者对象
     *
     * @param observer 观察者对象
     */
    public void detach(Observer observer) {

        list.remove(observer);
    }

    /**
     * 通知所有注册的观察者对象
     */
    public void notifyObservers(int what,Object obj) {
        for (Observer observer : list) {
            observer.update(what,obj);
        }
    }
}
