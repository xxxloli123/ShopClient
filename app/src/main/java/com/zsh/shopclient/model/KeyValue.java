package com.zsh.shopclient.model;

/**
 * Created by Administrator on 2017/12/22.
 */

public class KeyValue<Key,Value> extends Struct {
    private Key key;
    private Value value;

    public KeyValue(Key key, Value value) {
        this.key = key;
        this.value = value;
    }

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }
}
