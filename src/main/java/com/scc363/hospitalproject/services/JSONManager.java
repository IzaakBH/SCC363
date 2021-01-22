package com.scc363.hospitalproject.services;

import com.scc363.hospitalproject.utils.Pair;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;


public class JSONManager
{
    private Pair[] keyValues;

    public JSONManager(Pair[] keyValues)
    {
        this.keyValues = keyValues;
    }

    public JSONManager(){}

    public JSONObject generateJSONObject()
    {
        JSONObject object = new JSONObject();
        for (Pair keyValue : keyValues)
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
        this.keyValues = new Pair[]{new Pair("result", String.valueOf(value))};
        return generateJSONObject().toString();
    }
}
