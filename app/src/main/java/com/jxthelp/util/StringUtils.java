package com.jxthelp.util;


/**
 * Created by idisfkj on 15-9-28 14:54.
 * Email: idisfkj@qq.com
 */
public class StringUtils {
    public static boolean isValue(String tokenizer,String value){
        if(tokenizer.indexOf(value)!=-1){
            return true;
        }
        return false;
    }
    public static String getValue(String tokenizer,String startString,String endString,int unStart){
        int start=tokenizer.indexOf(startString);
        int end=tokenizer.length();
        //截取所需字符串
        String value=tokenizer.substring(start+unStart,end);
        end=value.indexOf(endString,unStart);
        if(end==-1){
            end=value.length();
        }

        return value.substring(0,end);
    }
}
