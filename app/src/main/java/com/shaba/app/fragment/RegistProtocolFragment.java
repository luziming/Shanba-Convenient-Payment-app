package com.shaba.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.shaba.app.R;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.fragment.base.FragmentUtils;
import com.shaba.app.utils.ToastUtil;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

import static com.shaba.app.global.MyApplication.context;

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
public class RegistProtocolFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    @Bind(R.id.rg_agreeMent)
    RadioGroup rgAgreeMent;
    @Bind(R.id.tv_agree)
    TextView tvAgree;

    private boolean isArgee = false;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_protocol, null);
        ButterKnife.bind(this, view);
        rgAgreeMent.setOnCheckedChangeListener(this);
        tvAgree.setOnClickListener(this);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rbtn_agree:
                isArgee = true;
                break;
            case R.id.rbtn_unagree:
                isArgee = false;
                break;
        }
    }

    @Override
    public void onClick(View v) {
        if (isArgee) {
//            startActivity(new Intent(this, RegSMSActivity.class));
            RegistFragment fragment = new RegistFragment();
            FragmentUtils.startFragment(mActivity,fragment,R.id.fl_container_content);
            EventBus.getDefault().post("手机号注册");
        }else {
            ToastUtil.show(context, "您未同意《缴费宝 － 用户注册协议》");
        }
    }
}
