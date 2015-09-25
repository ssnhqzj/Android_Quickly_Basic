package com.gjt.common.bean;

import java.util.Map;

/**
 * 序列化map供Bundle传递map使用
 * Created  on 13-12-9.
 */
public class SerializableMap extends Basebean {
 
    private Map<String,Object> map;
 
    public Map<String, Object> getMap() {
        return map;
    }
 
    public void setMap(Map<String, Object> map) {
        this.map = map;
    }
}