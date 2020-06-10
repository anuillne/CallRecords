package com.phone.callrecords.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.phone.callrecords.R;
import com.phone.callrecords.service.ContactInfo;
import com.phone.callrecords.utils.MyConstantUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author： AnuilLne
 * @date：   2020/5/3 11:32
 * @param: null
 * @return：
 * @description： 用于展示统计通话记录的Activity
 */
public class StaCallRecords extends AppCompatActivity {

    private TextView mTextViewOfStaResult;
    ArrayList<ContactInfo> contactInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sta_call_records);
        initView();
    }

    /**
     * @author： AnuilLne
     * @date：   2020/5/3 11:32
     * @param: view
     * @return： void
     * @description： 按钮 获取最大通话时间，这里直接从大到小排序
     */
    public void buttonOfMaxTime(View view){
        getContactInfo();
        // 排序
        MyConstantUtil.myContactSort(contactInfoList);
        StringBuilder builder = new StringBuilder();
        for (ContactInfo contactInfo : contactInfoList) {
            String typeName;
            switch (contactInfo.getType()){
                case 1:
                    typeName = "接听";
                    break;
                case 2:
                    typeName = "拨出";
                    break;
                case 3:
                    typeName = "未接";
                    break;
                default:    typeName = "其他";
            }
            String contactName = contactInfo.getName() != null ? contactInfo.getName():"未知";
            String contact = "联系人："+contactName
                    +"\n电话："+contactInfo.getNumber()
                    +"\n通话类型："+typeName
                    +"\n通话时间："+MyConstantUtil.getDateTime(contactInfo.getDate())
                    +"\n通话时长："+MyConstantUtil.getDurationTime(contactInfo.getDuration())+"\n\n";
            builder.append(contact);
        }
        mTextViewOfStaResult.scrollTo(0,0);
        mTextViewOfStaResult.setText(builder.toString());
    }

    /**
     * @author： AnuilLne
     * @date：   2020/5/3 11:34
     * @param: view
     * @return： void
     * @description： 按钮 获取最多通话次数，这里直接从大到小排序
     */
    public void buttonOfMostTimes(View view){
        getContactInfo();
        ArrayList<String> numberList = new ArrayList<>();
        for (ContactInfo contactInfo : contactInfoList) {
            String number = contactInfo.getNumber();
            numberList.add(number);
        }
        HashMap<String, Integer> map = new HashMap<>();
        for (String number : numberList){
            int count = Collections.frequency(numberList,number);
            map.put(number,count);
        }
        List<Map.Entry<String,Integer>> list = new ArrayList<>(map.entrySet());
        list.sort(Comparator.comparingInt(Map.Entry::getValue));
        Collections.reverse(list);
        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Integer> temp:list){
            builder.append("Tel：").append(temp.getKey()).append("\n");
            builder.append("次数：").append(temp.getValue()).append("\n\n");
        }
        mTextViewOfStaResult.scrollTo(0,0);
        mTextViewOfStaResult.setText(builder.toString());
    }

    /**
     * @author： AnuilLne
     * @date：   2020/5/3 11:34
     * @param: view
     * @return： void
     * @description： 按钮 获取最近5次通话记录
     */
    public void buttonOfLastFive(View view){
        getContactInfo();
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (ContactInfo contactInfo : contactInfoList) {
            if (i==5){
                break;
            }
            int type = contactInfo.getType();
            String typeName;
            switch (type){
                case 1:
                    typeName = "接听";
                    break;
                case 2:
                    typeName = "拨出";
                    break;
                case 3:
                    typeName = "未接";
                    break;
                default:    typeName = "其他";
            }
            String contactName = contactInfo.getName() != null ? contactInfo.getName():"未知";
            String contact = "联系人："+contactName
                    +"\n电话："+contactInfo.getNumber()
                    +"\n通话类型："+typeName
                    +"\n通话时间："+MyConstantUtil.getDateTime(contactInfo.getDate())
                    +"\n通话时长："+MyConstantUtil.getDurationTime(contactInfo.getDuration())+"\n\n";
            builder.append(contact);
            i = i+1;
            }
        mTextViewOfStaResult.scrollTo(0,0);
        mTextViewOfStaResult.setText(builder.toString());
    }

    private void initView(){
        mTextViewOfStaResult = findViewById(R.id.textViewOfStaResult);
        mTextViewOfStaResult.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    /**
     * @author： AnuilLne
     * @date：   2020/5/3 11:35
     * @param:
     * @return： void
     * @description： 获取通话记录
     */
    private void getContactInfo(){
        // 获取内容接收对象
        ContentResolver resolver = getContentResolver();
        //获取系统的通话记录权限
        if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "不能访问通讯记录", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(StaCallRecords.this, MyConstantUtil.PERMISSIONS_STORAGE, 1);
            return;
        }
        Cursor cursor = resolver.query(MyConstantUtil.CONTACT_URI,
                new String[]{"name", "number", "type", "date", "duration"},
                null,null,null);
        if (cursor != null && cursor.getCount() > 0){
            if (contactInfoList != null){
                contactInfoList.clear();
            }
            while (cursor.moveToNext()){
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String number = cursor.getString(cursor.getColumnIndex("number"));
                int type = cursor.getInt(cursor.getColumnIndex("type"));
                long date = cursor.getLong(cursor.getColumnIndex("date"));
                long duration = cursor.getLong(cursor.getColumnIndex("duration"));
                ContactInfo contactInfo = new ContactInfo(name, number, type, date, duration);
                contactInfoList.add(contactInfo);
            }
            cursor.close();
        }
        Collections.reverse(contactInfoList);
    }

}
