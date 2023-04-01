package com.oracle.oracle.training.services;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

@Service
@Slf4j
public class JSONParseService {

    public List<Object> parseJSONArray(String json){
        try{
           JSONArray jsonArray = new JSONArray(json);
           return  parseArray(jsonArray,"");
        }catch(JSONException e){
            log.error("Cannot parse json");
        }
        return null;
    }
    public Map<String,Object> parseJSONObject(String json){
        try{
            JSONObject jsonObject = new JSONObject(json);
            return  parseObject(jsonObject,"");
        }catch(JSONException e){
            log.error("Cannot parse json");
        }
        return null;
    }
    private Map parseObject(JSONObject jsonObject, String prefix){
        Map<String,Object> map = new HashMap<>();
        for(String key: jsonObject.keySet()){
            Object ob = jsonObject.get(key);
            if(ob==null) continue;
            if(ob instanceof JSONArray)
                map.put(key,parseArray(jsonObject.getJSONArray(key),key));
            else if(ob instanceof JSONObject)
                map.put(key,parseObject(jsonObject.getJSONObject(key),key+" "));
            else map.put(key,jsonObject.get(key));
        }
        return  map;
    }
     private List parseArray(JSONArray jsonArray, String key){
        List<Object> list =new ArrayList<>();
        for(int i = 0 ; i < jsonArray.length();i++){
            Object ob = jsonArray.get(i);
            if(ob == null) continue;
            if(ob instanceof JSONArray)
                list.add(parseArray(jsonArray.getJSONArray(i),""));
            else if(ob instanceof  JSONObject)
                list.add(parseObject(jsonArray.getJSONObject(i), key+" "));
            else list.add(ob);
        }
        return list;
    }
}
