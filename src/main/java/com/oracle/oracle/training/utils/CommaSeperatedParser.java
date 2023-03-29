package com.oracle.oracle.training.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CommaSeperatedParser{
    public Set<Integer> parseStringToInteger(String data) {
        if(data==null || data.length()==0)  return new HashSet<>();
        Set<Integer> dataSet = new HashSet<>();
        String[] dataArray = data.split(",");
        for(String currentData:dataArray){
            dataSet.add(Integer.parseInt(currentData));
        }
        return dataSet;
    }
}
