package com.phone.callrecords.utils;

import android.Manifest;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.provider.CallLog;
import com.phone.callrecords.service.ContactInfo;

import java.util.ArrayList;


/**
 *
 * @author： AnuilLne
 * @date： 2020/5/2 15:13
 * @description: 工具类 排序，时间格式化，常量存放
 */
public class MyConstantUtil {
    public static final Uri CONTACT_URI = CallLog.Calls.CONTENT_URI;
    public static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_CALL_LOG};

    public static String getDateTime(long date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        return simpleDateFormat.format(date);
    }
    public static String getDurationTime(long date){
        if (date < 60) {
            return date+"秒";
        }else if (date> 60 &&date< 3600) {
            long m = date/60;
            long s = date%60;
            return m+"分"+s+"秒";
        }else {
            long h = date/3600;
            long m = (date%3600)/60;
            long s = (date%3600)%60;
            return h+"小时"+m+"分"+s+"秒";
        }
    }

    public static void myContactSort(ArrayList<ContactInfo> contactInfoArrayList){
        for(int i=1; i < contactInfoArrayList.size(); i++){
            ContactInfo temp = contactInfoArrayList.get(i);
            int j;
            for(j=i; j>0&&contactInfoArrayList.get(j-1).getDuration() < temp.getDuration(); j--){
                contactInfoArrayList.set(j,contactInfoArrayList.get(j-1));
            }
            contactInfoArrayList.set(j, temp);
        }
    }
}
