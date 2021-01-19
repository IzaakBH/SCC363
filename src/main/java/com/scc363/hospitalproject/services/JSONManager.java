package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.KeyValue;
import org.json.simple.JSONObject;


public class JSONManager
{
    private KeyValue[] keyValues;
    public JSONManager(KeyValue[] keyValues)
    {
        this.keyValues = keyValues;
    }


    public JSONObject generateJSONObject()
    {
        JSONObject object = new JSONObject();
        for (KeyValue keyValue : keyValues)
        {
            object.put(keyValue.getKey(), keyValue.getValue());
        }
        return object;
    }

}
