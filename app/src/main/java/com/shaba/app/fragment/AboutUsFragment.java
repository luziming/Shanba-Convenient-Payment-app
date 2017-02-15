package com.shaba.app.fragment;

import android.view.View;
import android.widget.TextView;

import com.shaba.app.R;
import com.shaba.app.fragment.base.BaseFragment;

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
public class AboutUsFragment extends BaseFragment {
    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_about_us, null);
        try {
            String versionName = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), 0).versionName;
            TextView tv_version_code = (TextView) view.findViewById(R.id.tv_version_code);
            tv_version_code.setText("版本号：" + versionName);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return view;
    }
}
