package com.scc363.hospitalproject.datamodels;

public class KeyValue
{
    private String key;
    private Object value;

    public KeyValue(String key, Object value)
    {
        this.key = key;
        this.value = value;
    }

    public String getKey()
    {
        return this.key;
    }

    public Object getValue()
    {
        return this.value;
    }
}
