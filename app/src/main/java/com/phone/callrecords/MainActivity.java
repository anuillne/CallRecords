package com.phone.callrecords;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import com.phone.callrecords.activity.GetCallRecords;
import com.phone.callrecords.activity.StaCallRecords;
import com.phone.callrecords.utils.MyConstantUtil;


/**
 * @author： Yin Jingyu
 * @date：   2020/5/3 11:36
 * @param: null
 * @return：
 * @description： 主Activity
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(MainActivity.this, MyConstantUtil.PERMISSIONS_STORAGE, 1);
    }

    public void buttonOne(View view) {
        //界面跳转
        Intent intent=new Intent(MainActivity.this, GetCallRecords.class);
        startActivity(intent);
    }

    public void buttonTwo(View view) {
        Intent intent=new Intent(MainActivity.this, StaCallRecords.class);
        startActivity(intent);
    }

}
