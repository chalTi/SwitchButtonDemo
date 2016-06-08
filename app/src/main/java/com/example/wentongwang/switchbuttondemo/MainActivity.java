package com.example.wentongwang.switchbuttondemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


public class MainActivity extends Activity {

    private SwitchButton mswitchbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
    }

    private void initView() {
        mswitchbtn = (SwitchButton) findViewById(R.id.sbtn);
    }
    private void initData() {
        mswitchbtn.setSwitchBackground(R.drawable.switch_background);
        mswitchbtn.setSwitchSlide(R.drawable.slide_button_background);
        mswitchbtn.setOnSwitchListener(new SwitchButton.OnSwitchListenr() {
            @Override
            public void onSwitchChanged(boolean isOpened) {
                if(!isOpened){
                    Toast.makeText(MainActivity.this,"处于关闭状态",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(MainActivity.this,"处于打开状态",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
