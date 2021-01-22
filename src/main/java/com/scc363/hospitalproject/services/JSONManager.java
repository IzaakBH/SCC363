package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.datamodels.KeyValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONAware;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class JSONManager
{
    private KeyValue[] keyValues;

    public JSONManager(KeyValue[] keyValues)
    {
        this.keyValues = keyValues;
    }

    public JSONManager(){}

    public JSONObject generateJSONObject()
    {
        JSONObject object = new JSONObject();
        for (KeyValue keyValue : keyValues)
        {
            object.put(keyValue.getKey(), keyValue.getValue());
        }
        return object;
    }


    public JSONArray convertToJSONObject(String data)
    {
        Object dataObj = (Object) JSONValue.parse(data);
        return (JSONArray) dataObj;
    }

    public String getResponseObject(boolean value)
    {
        this.keyValues = new KeyValue[]{new KeyValue("result", String.valueOf(value))};
        return generateJSONObject().toString();
    }
}
