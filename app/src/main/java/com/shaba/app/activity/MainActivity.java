package com.shaba.app.activity;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shaba.app.R;
import com.shaba.app.fragment.HomeFragment;
import com.shaba.app.fragment.ReplacePhoneNumerFragment;
import com.shaba.app.fragment.ResetPasswordFragment;
import com.shaba.app.fragment.base.FragmentFactory;
import com.shaba.app.utils.PrefUtils;
import com.shaba.app.utils.ToastUtils;
import com.tencent.bugly.Bugly;

import butterknife.Bind;
import cn.carbs.android.library.MDDialog;

public class MainActivity extends BaseActivity {

    @Bind(R.id.toolbar)
    public Toolbar toolbar;
    @Bind(R.id.dl_left)
    public DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view)
    NavigationView navigation_view;
    @Bind(R.id.fl_container)
    FrameLayout fl_container;
    @Bind(R.id.ll_left)
    LinearLayout ll_left;

    public ActionBarDrawerToggle mDrawerToggle;
    private TextView toolbar_title;
    private long mExitTime;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        //bugly自动更新
        Bugly.init(getApplicationContext(), "999e8dfed4", false);

        String usernmae = PrefUtils.getString(this, "username", "未登录");
        View headerView = navigation_view.getHeaderView(0);
        TextView tv_username = (TextView) headerView.findViewById(R.id.tv_statu_username);
        tv_username.setText(usernmae);
        boolean isFirst = PrefUtils.getBoolean(this, "isFirst", true);
        if (isFirst)
            firStLoginDialog();
        //初始化Toolbar
        initToolbar();
        //设置菜单列表
        setOnRightMenuClick();
//        EventBus.getDefault().register(this);
//        6F:1E:F9:98:29:53:D3:8D:CF:B8:CF:2F:E7:12:01:4A:D3:7C:0F:0D
    }

    @Override
    protected void elseView() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //将侧边栏顶部延伸至status bar
            mDrawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar;虽默认为false,但经测试,DrawerLayout需显示设置
            mDrawerLayout.setClipToPadding(false);
        }
    }


    private void setOnRightMenuClick() {
        /**设置MenuItem的字体颜色**/
        Resources resource = (Resources) getBaseContext().getResources();
        ColorStateList csl = (ColorStateList) resource.getColorStateList(R.color.navigation_menu_item_color);
        navigation_view.setItemTextColor(csl);
        /**设置MenuItem默认选中项**/
        navigation_view.getMenu().getItem(0).setChecked(true);
        initFragment(new HomeFragment());
        navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        fragment = FragmentFactory.create(0);
                        break;
                    case R.id.about_us:
                        fragment = FragmentFactory.create(1);
                        break;
                    case R.id.reset_pass:   //这个需要每次创建新的Fragment
                        fragment = new ResetPasswordFragment();
                        break;
                    case R.id.record_sele:
                        fragment = FragmentFactory.create(2);
                        break;
                    case R.id.update_phone:
                        fragment = new ReplacePhoneNumerFragment();
                        break;
                    case R.id.exit:
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                        finish();
                        break;
                }
                if (fragment != null) {
                    initFragment(fragment);
                    item.setChecked(true);
                    toolbar_title.setText(item.getItemId() == R.id.home ? "陕坝缴费宝" : item.getTitle());
                }
                mDrawerLayout.closeDrawer(ll_left);
                return true;
            }
        });
    }

    private void initFragment(Fragment fragment) {
        getFragmentManager()
                .beginTransaction().
                replace(R.id.fl_container, fragment).commit();
    }

    private void initToolbar() {
        toolbar.setTitle("");//设置Toolbar标题
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        toolbar_title.setText("陕坝缴费宝");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setBackEnable();
    }

    public void setBackEnable() {
        //创建返回键，并实现打开关/闭监听
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, 0, 0) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() != 0) {
            super.onBackPressed();
        } else {
            if (mDrawerLayout.isDrawerOpen(ll_left)) {
                mDrawerLayout.closeDrawer(ll_left);
            } else {
                if ((System.currentTimeMillis() - mExitTime) > 2000) {
                    ToastUtils.showToast("再按一次退出程序");
                    mExitTime = System.currentTimeMillis();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    /**
     * 首次登陆弹出对话框
     */
    protected void firStLoginDialog() {
        new MDDialog.Builder(MainActivity.this)
                .setContentView(R.layout.alertdialog_info)
                .setPositiveButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PrefUtils.putBoolean(MainActivity.this, "isFirst", false);
                    }
                })
                .setWidthMaxDp(600)
                .setShowTitle(false)//default is true
                .setShowNegativeButton(false)
                .setShowPositiveButton(true)
                .create()
                .show();
    }
}
