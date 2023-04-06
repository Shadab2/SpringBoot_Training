package com.oracle.oracle.training.services.functional;

import com.oracle.oracle.training.entity.post.Comments;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class PostUtilityService {
    public Set<Integer> parseStringToInteger(String data) {
        if(data==null || data.length()==0)  return new HashSet<>();
        Set<Integer> dataSet = new HashSet<>();
        String[] dataArray = data.split(",");
        for(String currentData:dataArray){
            dataSet.add(Integer.parseInt(currentData));
        }
        return dataSet;
    }

    public  String parseSetToString(Set<Integer> set){
        StringBuilder sb =new StringBuilder();
        for(Integer x:set){
            sb.append(x).append(',');
        }
        return sb.toString();
    }

    public byte[] getBytes(List<Comments> list){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos;
        try {
            oos = new ObjectOutputStream(baos);
            oos.writeObject(list);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }

    public List<Comments> getComments(byte[] commentsData) {
        List<Comments> list = new ArrayList<>();
        try{
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(commentsData));
        try {
            list = (ArrayList<Comments>) ois.readObject();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            ois.close();
        }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }
    public String getDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("EE MMM d y H:m:s ZZZ");
        String dateString = formatter.format(new Date());
        return dateString;
    }
}
