package com.shaba.app.http;

import android.text.TextUtils;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.shaba.app.global.ConstantUtil;

import java.util.Map;

public class AppUtil {





    /**
     * 登陆
     * @param params          参数
     * @param responseHandler 返回消息
     */
    public void login(Map<String, String> params, JsonHttpResponseHandler responseHandler) {
        // http://192.168.1.17:300/api/login
        // ApiUrl.BIZ_LOGIN_API.toString()
        // TODO
        HttpClientUtils.postJson("http://115.28.138.217:3000/api/login", params, responseHandler);
    }

    /**
     * 获取短信
     * @param params
     * @param responseHandler
     */
    public void getSMS(Map<Object, Object> params, JsonHttpResponseHandler responseHandler) {
        // TODO http://115.28.138.217:3000/api/send-code
        HttpClientUtils.postJson("http://115.28.138.217:3000/api/send-code", params, responseHandler);
    }

    /**
     * 注册
     */
    public void getRegInfo(Map<Object, Object> params, JsonHttpResponseHandler responseHandler) {
        // TODO http://115.28.138.217:3000/api/register
        HttpClientUtils.postJson("http://115.28.138.217:3000/api/register", params, responseHandler);
    }

    /**
     * 忘记密码
     */
    public void findPassword(Map<Object, Object> params, JsonHttpResponseHandler responseHandler) {
        // TODO http://115.28.138.217:3000/api/register
        HttpClientUtils.postJson("http://115.28.138.217:3000/api/forgot-password", params, responseHandler);
    }

    /**
     * 修改密码
     *
     * @param token
     */
    public void getUpdataPwd(Map<Object, Object> params, JsonHttpResponseHandler responseHandler, String token) {
        // TODO http://115.28.138.217:3000/api/edit-password
        HttpClientUtils.post("http://115.28.138.217:3000/api/edit-password", params, token, responseHandler);
    }

    // http://192.168.1.20:8080/bmjf/api/sysPush/list

//    /**
//     * 获取消息列表
//     */
//    public void getMeassage(Map<Object, Object> params, JsonHttpResponseHandler responseHandler) {
//        HttpClientUtils.postJson(ApiUrl.GRTMEASSAGE.toString(), params, responseHandler);
//    }

    /**
     * 取得银联返回TN号，调用银联支付控件
     * @param params
     * @param responseHandler
     * @param companyId
     * @param token
     */
    public void getTnFromUnionPay(Map<Object, Object> params, JsonHttpResponseHandler responseHandler, String companyId,
                                  String token) {
        if (companyId.equals("1")) {
            HttpClientUtils.post("http://115.28.138.217:3000/api/power/pay", params, token, responseHandler);
        } else {
            // TODO
            HttpClientUtils.post("http://115.28.138.217:3000/api/elec/pay", params, token, responseHandler);
        }
    }

    /**
     * 查询电费账单
     * @param params
     * @param responseHandler
     * @param companyId
     */
    public void queryBillElec2(Map<Object, Object> params, JsonHttpResponseHandler responseHandler, String companyId) {
        if (companyId.equals("1")) {
            params.put("area_buss_id", ConstantUtil.AREA_ID);
            //HttpClientUtils.postJson(ApiUrl.QUERY_BILL_ELEC.toString(), params, responseHandler);
        } else {
            HttpClientUtils.postJson("http://115.28.138.217:3000/api/elec/query", params, responseHandler);
        }

    }

    public void queryBillElec(Map<Object, Object> params, String token, JsonHttpResponseHandler responseHandler,
                              String companyId) {

        if (companyId.equals("1")) {
            HttpClientUtils.post("http://115.28.138.217:3000/api/power/query", params, token, responseHandler);
        } else {
            // TODO
            HttpClientUtils.post("http://115.28.138.217:3000/api/elec/query", params, token, responseHandler);
        }

    }

    /**
     * 交完电费后，更新后台订单状态
     * @param params
     * @param responseHandler
     */
    public void updatePayStatus(Map<Object, Object> params, JsonHttpResponseHandler responseHandler) {
//        HttpClientUtils.postJson(ApiUrl.UPDATE_PAY_STATUS.toString(), params, responseHandler);
    }


    /**
     * 获取新闻列表
     * @param newListResponseHandler
     */
    public void getNewListMore(AsyncHttpResponseHandler newListResponseHandler, RequestParams params, String token) {
        // TODO http://115.28.138.217:3000/api/news-list
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/api/articles-list", params, newListResponseHandler, token);
    }

    /**
     * 获取新闻列表
     */
    public void getNewList(AsyncHttpResponseHandler mapListResponseHandler, String token) {
        // TODO http://115.28.138.217:3000/api/news-list
        RequestParams params = new RequestParams();
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/api/articles-list", params, mapListResponseHandler,
                token);
    }

    /**
     * 查询账单
     */
    public void getBillList(AsyncHttpResponseHandler responseHandler, RequestParams params, String token) {
        // TODO http://115.28.138.217:3000/api/deal-list?current-page=0
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/api/deal-list", params, responseHandler, token);
    }

    /**
     * 获取地址列表
     *
     */
    public void getMapist(AsyncHttpResponseHandler newListResponseHandler, String token) {
        // TODO http://115.28.138.217:3000/common/api/branch-list
        RequestParams params = new RequestParams();
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/common/api/branch-list", params,
                newListResponseHandler, token);
    }

    /**
     * ATM\
     * @param newListResponseHandler
     * @param token
     */
    public void getATMList(AsyncHttpResponseHandler newListResponseHandler, String token) {
        // TODO http://115.28.138.217:3000/common/api/atm-list
        RequestParams params = new RequestParams();
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/common/api/atm-list", params,
                newListResponseHandler, token);
    }


    /**
     * 助农点
     * @param newListResponseHandler
     */
    public void getBankPoint(AsyncHttpResponseHandler newListResponseHandler, String token) {
        // TODO http://115.28.138.217:3000/common/branchs
        RequestParams params = new RequestParams();
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/common/api/pos-list", params, newListResponseHandler, token);
    }

    /**
     * 手机流量 2,208 全国移动10M 移动 10M 全国 即时生效 全国 特殊套餐不能充值 3
     * @param token
     */
    public void getLiuliang(Map<Object, Object> params, JsonHttpResponseHandler responseHandler, String token) {
        // TODO http://115.28.138.217:3000/api/news-list
        HttpClientUtils.post("http://115.28.138.217:3000/api/rain/traffic/pay", params, token, responseHandler);
    }

    /**
     * 手机/固话充值
     *
     * @param params
     * @param responseHandler
     * @param phoneType
     * @param token
     */
    public void phoneRecharge(Map<Object, Object> params, JsonHttpResponseHandler responseHandler, String phoneType,
                              String token) {
        String url = null;
        // TODO
        if (phoneType.equals("phone")) {
            url = "http://115.28.138.217:3000/api/rain/phone/pay";
        } else if (phoneType.equals("tel") || phoneType.equals("broadband")) {
            url = "http://115.28.138.217:3000/api/rain/tel/pay";
        }
        HttpClientUtils.post(url, params, token, responseHandler);
    }

    /**
     * 取得银联返回TN号，调用银联支付控件
     *
     * @param params
     * @param responseHandler
     */
    //public void getTnToPayRecharge(Map<Object, Object> params, JsonHttpResponseHandler responseHandler) {
//        params.put("area_buss_id", ConstantUtil.AREA_ID_PHONE_RECHARGE);
//        HttpClientUtils.postJson(ApiUrl.GET_TN_TO_PAY_RECHARGE.toString(), params, responseHandler);
    //}

    /**
     * 充值手机费成功后，更新后台订单状态
     *
     * @param params
     * @param responseHandler
     */
//    public void updateRechargePhoneStatus(Map<Object, Object> params, JsonHttpResponseHandler responseHandler) {
//        HttpClientUtils.postJson(ApiUrl.UPDATE_RECHARGE_PHONE_STATUS.toString(), params, responseHandler);
//    }

    /**
     * 查询电视费用订单
     * @param params
     * @param responseHandler
     * @param token
     */
    public void queryBillTV(Map<Object, Object> params,
                            // TODO http://115.28.138.217:3000/api/tv/query
                            JsonHttpResponseHandler responseHandler, String token) {
        HttpClientUtils.post("http://115.28.138.217:3000/api/tv/query", params, token, responseHandler);
    }

    /**
     * 查询电视费tn
     *
     * @param params
     * @param responseHandler
     */
    public void getTVTnFromUnionPay(Map<Object, Object> params, String token, JsonHttpResponseHandler responseHandler) {
        // TODO
        HttpClientUtils.post("http://115.28.138.217:3000/api/tv/pay", params, token, responseHandler);
    }

    /**
     * 充值手机费成功后，更新后台订单状态
     *
     * @param params
     * @param responseHandler
     */
//    public void updateTVPayStatus(Map<Object, Object> params, JsonHttpResponseHandler responseHandler) {
//        HttpClientUtils.postJson(ApiUrl.UPDATE_TV_STATUS.toString(), params, responseHandler);
//    }

    /**
     * 验证手机号码
     * @param mobiles
     * @return
     */
    public boolean isMobileNO(String mobiles) {
        String telRegex = "[1][3458]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }

    /**
     * 更新app 版本
     *
     * @param params
     */
    public void checkAppVersion(Map<Object, Object> params, JsonHttpResponseHandler responseHandler) {
        // TODO
        HttpClientUtils.postJson("http://115.28.138.217:3000/api/version", params, responseHandler);

    }

    /**
     * 社保状态查询
     *
     * @param params
     * @param responseHandler
     * @param token
     */
    public void querySocialSecurity(Map<Object, Object> params, JsonHttpResponseHandler responseHandler, String token) {
        // TODO
        HttpClientUtils.post("http://115.28.138.217:3000/api/secstate", params, token, responseHandler);

    }

    /**
     * 社保查询
     *
     * @param params
     * @param responseHandler
     * @param token
     */
    public void querySocialSecuritys(Map<Object, Object> params, JsonHttpResponseHandler responseHandler,
                                     String token) {
        HttpClientUtils.post("http://115.28.138.217:3000/api/secfee", params, token, responseHandler);

    }

    /**
     * 验证码 验证
     * @param params
     * @param responseHandler
     */

    public void checkVerificationCode(Map<Object, Object> params, JsonHttpResponseHandler responseHandler) {
        // TODO http://115.28.138.217:3000/api/check-code
        HttpClientUtils.postJson("http://115.28.138.217:3000/api/check-code", params, responseHandler);

    }

    /**
     * 获取账单详情
     *
     * @param token
     * @param url
     */
    public void getBillDetail(String url, String token, AsyncHttpResponseHandler responseHandler) {
        RequestParams params = new RequestParams();
        HttpClientUtils.getJsonForToken(url, params, responseHandler, token);
    }

    public void changeUser(Map<Object, Object> params, JsonHttpResponseHandler responseHandler, String token) {
        HttpClientUtils.post("http://115.28.138.217:3000/api/change-mobile", params, token, responseHandler);

    }

    /**
     * 新闻轮播图
     *
     * @param responseHandler
     * @param token
     */
    public void getNewsTopPic(RequestParams params, AsyncHttpResponseHandler responseHandler, String token) {
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/api/banners", params, responseHandler, token);
    }


    /**
     * 检查登录状态
     * http://115.28.138.217:3000/api/check-login
     */
    public void checkLoginState(AsyncHttpResponseHandler responseHandler, String token) {
        RequestParams params = new RequestParams();
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/api/check-login", params, responseHandler, token);
    }

    /**
     * 全行业缴费分类查询
     */
    public void selectFromType(AsyncHttpResponseHandler responseHandler, RequestParams params,String token) {
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/api/merchant/select-products",params,responseHandler,token);
    }

    /**
     * 全行业缴费项目用户查询
     */
    public void selectUserFromProduct(AsyncHttpResponseHandler responseHandler, RequestParams params,String token) {
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/api/merchant/participants",params,responseHandler,token);
    }
    /**
     * 全行业缴费搜索项目
     */
    public void searchProduct(AsyncHttpResponseHandler responseHandler, RequestParams params,String token) {
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/api/merchant/find-products",params,responseHandler,token);
    }

    /**
     * 全行业缴费获取tn
     */
    public void getIndustryTN(AsyncHttpResponseHandler responseHandler, RequestParams params,String token) {
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/api/merchant/pay",params,responseHandler,token);
    }
    /**
     * 全行业缴费缴费记录
     */
    public void getPaymentRecord(AsyncHttpResponseHandler responseHandler, RequestParams params,String token) {
        HttpClientUtils.getJsonForToken("http://115.28.138.217:3000/api/merchant/trades",params,responseHandler,token);
    }

}
