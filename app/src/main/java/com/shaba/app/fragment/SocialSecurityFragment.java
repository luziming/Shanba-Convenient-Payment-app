package com.shaba.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.shaba.app.R;
import com.shaba.app.activity.LoginActivity;
import com.shaba.app.been.SocialSecurity;
import com.shaba.app.been.SocialSecurityResult;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.fragment.base.FragmentUtils;
import com.shaba.app.utils.RegexUtil;
import com.shaba.app.utils.StringUtil;
import com.shaba.app.utils.ToastUtil;
import com.shaba.app.utils.UnionPayUtils;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

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
public class SocialSecurityFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {


    @Bind(R.id.autoComplete_peopleID)
    AutoCompleteTextView auto_IDCard;
    @Bind(R.id.spn_city_cd_choose)
    Spinner spinner_city;
    @Bind(R.id.spn_product_tp_choose)
    Spinner spinner_products;
    @Bind(R.id.spn_product_tp_choose2)
    Spinner spinner_type;
    @Bind(R.id.btn_query)
    Button bt_query;
    @Bind(R.id.ll_secondChooser)
    LinearLayout llSecondChooser;

    private String[] mProductNames = new String[]{"医疗保险", "养老保险", "工伤保险", "失业保险", "生育保险"};
    private int[] mProductNameIds = new int[]{-1, -2, 21, 41, 51};
    private List<SocialSecurity> mProductNameList = new ArrayList<SocialSecurity>() {
        {
            for (int i = 0; i < mProductNames.length; i++) {
                add(new SocialSecurity(mProductNameIds[i], mProductNames[i]));
            }
        }
    };

    private String[] mCityNames = new String[]{
            "呼和浩特", "包头", "乌海", "赤峰", "通辽", "鄂尔多斯",
            "呼伦贝尔", "巴彦淖尔", "乌兰察布", "兴安盟", "锡盟二连",
            "阿拉善盟", "满洲里", "自治区养老", "自治区医疗"};

    private int[] mCityIDs = new int[]{21053, 21054, 21055, 21056, 21057, 21058, 21059,
            210510, 210511, 210512, 210513, 210514, 210515, 21051, 21052};

    private List<SocialSecurity> mCityNameList = new ArrayList<SocialSecurity>() {
        {
            for (int i = 0; i < mCityNames.length; i++) {
                add(new SocialSecurity(mCityIDs[i], mCityNames[i]));
            }
        }
    };
    //"城市职工医疗保险","城市居民医疗保险"  31,38
    private String[] mMedicalTreatmentNames = new String[]{"城市职工医疗保险", "城市居民医疗保险"};
    private int[] mMedicalTreatmentIDs = new int[]{31, 38};
    private List<SocialSecurity> mMedicalTreatmentList = new ArrayList<SocialSecurity>() {
        {
            for (int i = 0; i < mMedicalTreatmentNames.length; i++) {
                add(new SocialSecurity(mMedicalTreatmentIDs[i], mMedicalTreatmentNames[i]));
            }
        }
    };
    // 样老保险 11 ,"城乡养老","机关失业单位养老" ,150,12
    private String[] mPensionNames = new String[]{"企业职工养老保险", "城乡养老保险", "机关事业单位养老保险"};
    private int[] mPensionIDs = new int[]{11, 150, 12};
    private List<SocialSecurity> mPensionList = new ArrayList<SocialSecurity>() {
        {
            for (int i = 0; i < mPensionNames.length; i++) {
                add(new SocialSecurity(mPensionIDs[i], mPensionNames[i]));
            }
        }
    };
    //默认为城市职工医疗保险
    private String mProductTypeID = "31";
    //默认地区为呼和浩特
    private String mCityID = "21053";

    @Override
    public View initViews() {
        View view = inflater.inflate(R.layout.fragment_social_security, null);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void initData() {
        bt_query.setOnClickListener(this);
        initAutoComplete("social-security", auto_IDCard);
        //初始化保险类型Spinner
        initSpinner(mProductNameList, spinner_products);
        spinner_products.setOnItemSelectedListener(this);
        //初始化地区Spinner
        initSpinner(mCityNameList, spinner_city);
    }

    /**
     * 初始化Spinner
     * @param list
     */
    private void initSpinner(List<SocialSecurity> list, Spinner spinner) {
        ArrayAdapter<SocialSecurity> spinnerAdapter = new ArrayAdapter<>(mActivity, android.R.layout.simple_spinner_item, list);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
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
    public void onClick(View v) {
        if (StringUtil.isFastClick())
            return;
        //提交查询
        String IDCard = auto_IDCard.getText().toString().trim();
        if (TextUtils.isEmpty(IDCard) || !RegexUtil.IdCardRegex(IDCard.replace("x", "X"))) {
            ToastUtil.show(mActivity, R.string.error_id_num);
            return;
        }
        Map<Object, Object> params = new HashMap<>();
        params.put("usr_num", IDCard);
        params.put("product_tp", mProductTypeID);
        params.put("city_cd", mCityID);
        appUtil.querySocialSecuritys(params, new QuerySocialSecurityHandler(), token);
        UnionPayUtils.getInstance().showDialog(mActivity);
        saveHistory("social-security", IDCard);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.spn_product_tp_choose:
                int ids = ((SocialSecurity) spinner_products.getSelectedItem()).getId();
                if (ids == -1 || ids == -2) {
                    llSecondChooser.setVisibility(View.VISIBLE);
                    switch (ids) {
                        case -1://医疗保险
                            initSpinner(mMedicalTreatmentList, spinner_type);
                            break;
                        case -2://养老保险
                            initSpinner(mPensionList, spinner_type);
                            break;
                    }
                    spinner_type.setOnItemSelectedListener(this);
                } else {
                    llSecondChooser.setVisibility(View.GONE);
                    mProductTypeID = String.valueOf(ids);
                }
                break;
            case R.id.spn_product_tp_choose2:
                int pcId = ((SocialSecurity) spinner_type.getSelectedItem()).getId();
                mProductTypeID = String.valueOf(pcId);
                break;
            case R.id.spn_city_cd_choose:
                int city_id = ((SocialSecurity) spinner_city.getSelectedItem()).getId();
                mCityID = String.valueOf(city_id);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class QuerySocialSecurityHandler extends JsonHttpResponseHandler {
        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            UnionPayUtils.getInstance().disMissDialog();
            try {
                if (!response.getBoolean("success")) {
                    if (!response.isNull("error")) {
                        if ("noauth".equals(response.getString("error"))) {
                            ToastUtil.show(context, R.string.re_login);
                            startActivity(new Intent(mActivity, LoginActivity.class));
                            mActivity.overridePendingTransition(R.anim.activity_open, R.anim.activity_close);
                            mActivity.finish();
                            return;
                        }
                    }
                    if (!response.isNull("message")) {
                        ToastUtil.show(context, response.getString("message"));
                        return;
                    }
                }
                if (response.getBoolean("success")) {
                    JSONObject obj = response.getJSONObject("obj");
                    SocialSecurityResult result = new SocialSecurityResult();
                    result.setState(obj.getString("state"));
                    result.setCurr_balance(obj.getString("curr_balance"));
                    result.setTotal_unit_pay(obj.getString("total_unit_pay"));
                    result.setPay_month_num(obj.getString("pay_month_num"));
                    result.setCustomer_id(obj.getString("customer_id"));
                    result.setTotal_person_pay(obj.getString("total_person_pay"));
                    SocialSecurityDetailFragment fragment = new SocialSecurityDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("result", result);
                    fragment.setArguments(bundle);
                    FragmentUtils.startCoolFragment(mActivity, fragment, R.id.fl_container_content);
                }
            } catch (JSONException e) {

                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(int statusCode, Header[] headers,
                              Throwable throwable, JSONObject errorResponse) {
            UnionPayUtils.getInstance().disMissDialog();
        }

        @Override
        public void onFinish() {

        }
    }
}
