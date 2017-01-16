package com.shaba.app.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.shaba.app.R;
import com.shaba.app.fragment.BankMapFragment;
import com.shaba.app.fragment.FarmersPointMapFragment;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
*/
public class SecondActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private TextView toolbar_title;
    private String title = "陕坝缴费宝";

    @Override
    protected int getLayoutID() {
        return R.layout.activity_second;
    }

    public void onEvent(String title) {
        toolbar_title.setText(title);
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        String type = getIntent().getStringExtra("type");
        Fragment fragment = null;
        Bundle bundle = new Bundle();
        switch(type){
            case "bank-map":
                title = "网点地图";
                bundle.putInt("MAP_TYPE",1);
                fragment = new BankMapFragment();
                fragment.setArguments(bundle);
                break;
            case "atm-map":
                title = "ATM地图";
                bundle.putInt("MAP_TYPE",2);
                fragment = new BankMapFragment();
                break;
            case "farmers-point-map":
                title = "助农点地图";
                fragment = new FarmersPointMapFragment();
                fragment.setArguments(bundle);
                break;
        }
        getFragmentManager().beginTransaction()
                .replace(R.id.fl_container_content,fragment)
                .commit();
        //初始化Toolbar
        initToolbar();
    }

    private void initToolbar() {
        toolbar.setTitle("");//设置Toolbar标题
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText(title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true ;
        }
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            toolbar_title.setText(title);
        }
        overridePendingTransition(R.anim.activity_enter,R.anim.activity_exit);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
