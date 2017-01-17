package com.shaba.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.shaba.app.R;
import com.shaba.app.been.MapListEntity;
import com.shaba.app.fragment.base.BaseFragment;
import com.shaba.app.global.Const;
import com.shaba.app.overlay.SchemeDriveOverlay;
import com.shaba.app.utils.SBLog;
import com.shaba.app.utils.SchemeUtil;
import com.shaba.app.utils.ToastUtil;
import com.shaba.app.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

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
public class MapFragment extends BaseFragment implements LocationSource, AMapLocationListener, View.OnClickListener, RouteSearch.OnRouteSearchListener {

    @Bind(R.id.map)
    MapView map;
    @Bind(R.id.tv_bank_name)
    TextView tvBankName;
    @Bind(R.id.bank_tel)
    TextView tvBankTel;
    @Bind(R.id.iv_choseGPS)
    ImageView ivChoseGPS;


    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isFirst = true;
    private LatLng latlng;
    private LatLng addressLngLat;
    private Marker marker;
    private String addressName, address, tel;
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private RouteSearch mRouteSearch;

    @Override
    public View initViews() {
        Log.e("MapFragment", "initViews: 执行");
        View view = inflater.inflate(R.layout.fragment_map, null);
        ButterKnife.bind(this, view);
        Bundle arguments = getArguments();
        MapListEntity mapInfo = arguments.getParcelable("mapInfo");
        addressLngLat = new LatLng(
                Double.valueOf(mapInfo.getLat()), Double.valueOf(mapInfo.getLng()));
        addressName = mapInfo.getName();
        address = mapInfo.getAddress();
        tvBankName.setText(address);
        tel = mapInfo.getTel();
        tvBankTel.setText(tel);
        ivChoseGPS.setOnClickListener(this);
        initAmap();
        mEndPoint = new LatLonPoint(addressLngLat.latitude, addressLngLat.longitude);
        return view;
    }

    public void initAmap() {
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = map.getMap();
        }
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                .fromResource(R.drawable.image_emoticon25));// 设置小蓝点的图标
        myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(Color.TRANSPARENT);// 设置圆形的填充颜色
// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(0.1f);// 设置圆形的边框粗细
        aMap.setMyLocationStyle(myLocationStyle);

        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置默认定位按钮是否显示
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);

        aMap.getUiSettings().setScaleControlsEnabled(true);

        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setCompassEnabled(true);
//        AMapNavi.getInstance()
        addMarkersToMap();

        mRouteSearch = new RouteSearch(mActivity);
        mRouteSearch.setRouteSearchListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        Log.e("MapFragment", "onCreateView: 执行");
        //创建地图
        map.onCreate(savedInstanceState);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.e("MapFragment", "onDestroyView: 执行");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁地图
        map.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
        ButterKnife.unbind(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("MapFragment", "onResume: 执行");
        //在执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        map.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("MapFragment", "onPause: 执行");
        //在执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        map.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.e("MapFragment", "onSaveInstanceState: 执行");
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        map.onSaveInstanceState(outState);
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        this.mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            //初始化定位
            mlocationClient = new AMapLocationClient(mActivity);
            //初始化定位参数
            mLocationOption = new AMapLocationClientOption();
            //设置定位回调监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();//启动定位
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (mListener != null && aMapLocation != null) {
            if (aMapLocation != null
                    && aMapLocation.getErrorCode() == 0) {
                // 定位成功回调信息，设置相关消息
//                aMapLocation.getLocationType();// 获取当前定位结果来源，如网络定位结果，详见定位类型表
//                aMapLocation.getLatitude();// 获取纬度
//                aMapLocation.getLongitude();// 获取经度
//                aMapLocation.getAccuracy();// 获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat(
//                        "yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(aMapLocation.getTime());
//                df.format(date);// 定位时间
//                aMapLocation.getAddress();// 地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                aMapLocation.getCountry();// 国家信息
//                aMapLocation.getProvince();// 省信息
//                aMapLocation.getCity();// 城市信息
//                aMapLocation.getDistrict();// 城区信息
//                aMapLocation.getStreet();// 街道信息
//                aMapLocation.getStreetNum();// 街道门牌号信息
//                aMapLocation.getCityCode();// 城市编码
//                aMapLocation.getAdCode();// 地区编码
//                aMapLocation.getAoiName();// 获取当前定位点的AOI信息
                mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点

                Double geoLat = aMapLocation.getLatitude();
                Double geoLng = aMapLocation.getLongitude();

                mStartPoint = new LatLonPoint(geoLat, geoLng);
                SBLog.d(mStartPoint.toString());
                latlng = new LatLng(geoLat,geoLng);
                if (isFirst) {
                    LatLngBounds bounds = new LatLngBounds.Builder()
                            .include(addressLngLat).include(latlng).build();
                    aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds,
                            150));
                    isFirst = false;
                }
            } else {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                ToastUtils.showToast(errText);
                Log.e("AmapErr", errText);
            }
        }
    }

    private void addMarkersToMap() {
        MarkerOptions markerOption = new MarkerOptions()
                .anchor(0.5f, 0.5f)
                .position(addressLngLat)
                .title(addressName)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .draggable(true).period(50);

        ArrayList<MarkerOptions> markerOptionlst = new ArrayList<MarkerOptions>();
        markerOptionlst.add(markerOption);
        List<Marker> markerlst = aMap.addMarkers(markerOptionlst, true);
        marker = markerlst.get(0);
        marker.showInfoWindow();
    }

    @Override
    public void onClick(View v) {
        searchRouteResult(2, RouteSearch.DRIVING_SINGLE_DEFAULT);
    }

    /**
     * 开始搜索路径规划方案
     */
    public void searchRouteResult(int routeType, int mode) {
        if (mStartPoint == null) {
            ToastUtils.showToast("定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            ToastUtils.showToast("终点未设置");
        }
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        //发送路线计算请求
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null,
                null, "");// 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
//        DrivingRouteOverlay s =
    }

    private DriveRouteResult mDriveRouteResult;


    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == 1000) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    aMap.clear();// 清理地图上的所有覆盖物
                    SchemeDriveOverlay drivingRouteOverlay = new SchemeDriveOverlay(
                            mActivity, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
//                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = SchemeUtil.getBusRouteTitle(dur, dis);
                    Toast.makeText(mActivity,des,Toast.LENGTH_LONG).show();
//                    mRotueTimeDes.setText(des);
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    /*SpannableStringBuilder spanabledes = SchemeUtil
                            .getRouteDes(this.getApplication(), taxiCost);
                    mRouteDetailDes.setText(spanabledes);
                    mBottomLayout.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mActivity,
                                    DriveRouteDetailActivity.class);
                            intent.putExtra(BundleFlag.DRIVE_PATH, drivePath);
                            intent.putExtra(BundleFlag.DRIVE_RESULT,
                                    mDriveRouteResult);
                            intent.putExtra(BundleFlag.DRIVE_TARGET_NAME,
                                    mCloudItem.getTitle());
                            startActivity(intent);
                        }
                    });*/
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mActivity, R.string.route_suggestion_walk);
                }

            } else {
                ToastUtil.show(mActivity, R.string.error_route_result_drive);
            }
        } else if (errorCode == Const.ERROR_CODE_SOCKE_TIME_OUT) {
            ToastUtil.show(mActivity.getApplicationContext(),
                    R.string.error_socket_timeout);
        } else if (errorCode == Const.ERROR_CODE_UNKNOW_HOST) {
            ToastUtil
                    .show(mActivity.getApplicationContext(), R.string.error_network);
        } else if (errorCode == Const.ERROR_CODE_FAILURE_AUTH) {
            ToastUtil.show(mActivity.getApplicationContext(), R.string.error_key);
        } else if (errorCode == 33) {
            ToastUtil.show(mActivity.getApplicationContext(),
                    R.string.error_route_result_drive);
        } else if (errorCode == Const.ERROR_CODE_TABLEID) {
            ToastUtil.show(mActivity.getApplicationContext(),
                    R.string.error_table_id);
        } else {
            ToastUtil.show(mActivity.getApplicationContext(),
                    getString(R.string.error_other) + errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
}
