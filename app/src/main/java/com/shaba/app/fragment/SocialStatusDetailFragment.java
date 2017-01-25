package com.shaba.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.shaba.app.R;
import com.shaba.app.been.SocialStatusResult;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.utils.StringUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

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
public class SocialStatusDetailFragment extends BaseFragment {

    @Bind(R.id.tv_socialSecurityName)
    TextView tvSocialSecurityName;
    @Bind(R.id.tv_socialSecurityId)
    TextView tvSocialSecurityId;
    @Bind(R.id.tv_socialSecurityState)
    TextView tvSocialSecurityState;
    @Bind(R.id.btn_back)
    Button btnBack;

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_social_statu_detail, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        Bundle arguments = getArguments();
        SocialStatusResult result = arguments.getParcelable("result");
        tvSocialSecurityState.setText(result.getState());
        tvSocialSecurityId.setText(result.getCustomer_id());
        tvSocialSecurityName.setText(result.getCustomer_name());
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtil.isFastClick())
                    return;
                mActivity.onBackPressed();
            }
        });
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
}
