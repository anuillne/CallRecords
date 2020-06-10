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


/**
 * @author： AnuilLne
 * @date：   2020/5/3 11:26
 * @param: null
 * @return：
 * @description： 用于展示获取通话记录的Activity
 */
public class GetCallRecords extends AppCompatActivity {

    private TextView mTextViewOfGetResult;
    ArrayList<ContactInfo> contactInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_call_records);
        initView();
    }
    /**
     * @author： AnuilLne
     * @date：   2020/5/2 12:47
     * @param: view
     * @return： void
     * @description： 按钮拨出电话功能 获取拨出的电话记录并展示
     */
    public void buttonOfOutGoingCall(View view){
        getContactInfoAndShow(2);
    }

    /**
     * @author： AnuilLne
     * @date：   2020/5/3 11:28
     * @param: view
     * @return： void
     * @description： 按钮未接电话功能 获取未接的电话记录并展示
     */
    public void buttonOfMissedCall(View view){
        getContactInfoAndShow(3);
    }

    /**
     * @author： AnuilLne
     * @date：   2020/5/3 11:28
     * @param: view
     * @return： void
     * @description： 按钮已接电话功能 获取已接的电话记录并展示
     */
    public void buttonOfReceivedCall(View view){
        getContactInfoAndShow(1);
    }

    /**
     * @author： AnuilLne
     * @date：   2020/5/3 11:29
     * @param: showType int 根据按钮确定需要展示的内容
     * @return： void
     * @description： 获取通话记录并展示
     */
    private void getContactInfoAndShow(int showType){
        // 获取内容接收对象
        ContentResolver resolver = getContentResolver();
        //获取系统的通话记录权限
        if (checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "不能访问通讯记录", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(GetCallRecords.this, MyConstantUtil.PERMISSIONS_STORAGE, 1);
        }
        // 获取童话记录
        Cursor cursor = resolver.query(MyConstantUtil.CONTACT_URI, new String[]{"name", "number", "type", "date", "duration"},
                null,null,null);
        //遍历集合
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
        StringBuilder builder = new StringBuilder();
        //遍历通话记录集合
        for (ContactInfo contactInfo : contactInfoList) {
            // 判断
            int type = contactInfo.getType();
            String typeName;
            if (type==showType){
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
            }
        }
        mTextViewOfGetResult.scrollTo(0,0);
        mTextViewOfGetResult.setText(builder.toString());
    }

    private void initView(){
        mTextViewOfGetResult = findViewById(R.id.textViewOfGetResult);
        mTextViewOfGetResult.setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
