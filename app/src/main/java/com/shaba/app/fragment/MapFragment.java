package com.shaba.app.fragment;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Circle;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
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
import com.shaba.app.utils.AMapUtil;
import com.shaba.app.utils.StringUtil;
import com.shaba.app.utils.ToastUtils;
import com.shaba.app.view.DrivingRouteOverLay;

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
public class MapFragment extends BaseFragment implements LocationSource, AMapLocationListener,
        View.OnClickListener ,RouteSearch.OnRouteSearchListener{

    @Bind(R.id.map)
    MapView map;
    @Bind(R.id.tv_bank_name)
    TextView tvBankName;
    @Bind(R.id.bank_tel)
    TextView tvBankTel;
    @Bind(R.id.fab_daohang)
    FloatingActionButton fab;

    private AMap aMap;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private LatLng addressLngLat;
    private String addressName, address, tel;
    private Marker mLocMarker;
    //    private SensorEventHelper mSensorHelper;
    private Circle mCircle;
    private static final int STROKE_COLOR = Color.argb(180, 3, 145, 255);
    private static final int FILL_COLOR = Color.argb(10, 0, 0, 180);
    private boolean mFirstFix = false;
    public static final String LOCATION_MARKER_FLAG = "我";
    private LatLng location;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private RouteSearch mRouteSearch;
    private DriveRouteResult mDriveRouteResult;
    private boolean isShowRoute = false;
    private DrivingRouteOverLay drivingRouteOverlay;


    @Override
    public View initViews() {
        Log.e("MapFragment", "initViews: 执行");
        View view = inflater.inflate(R.layout.fragment_map, null);
        ButterKnife.bind(this,view);
        Bundle arguments = getArguments();
        MapListEntity mapInfo = arguments.getParcelable("mapInfo");
        addressLngLat = new LatLng(
                Double.valueOf(mapInfo.getLat()), Double.valueOf(mapInfo.getLng()));
        addressName = mapInfo.getName();
        address = mapInfo.getAddress();
        Log.e("MapFragment", "地址: " + addressName);
        tvBankName.setText(address);
        tel = mapInfo.getTel();
        tvBankTel.setText(tel);
        fab.setOnClickListener(this);
        initAmap();
        mRouteSearch = new RouteSearch(getActivity());
        mRouteSearch.setRouteSearchListener(this);
        return view;
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
    public void initAmap() {
        //初始化地图控制器对象
        if (aMap == null) {
            aMap = map.getMap();
            setUpMap();
        }
//        if (mSensorHelper != null) {
//            mSensorHelper.registerSensorListener();
//        }
        addMarkersToMap();
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        aMap.setLocationSource(this);// 设置定位监听
        aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
        aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.getUiSettings().setScaleControlsEnabled(true);//设置触摸缩放可见
        aMap.getUiSettings().setZoomControlsEnabled(false);//设置缩放按钮不可见
        aMap.getUiSettings().setCompassEnabled(true);//设置指南针可见
        addMyLocaltion();
    }

    private void addMyLocaltion() {
        // 自定义系统定位蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        // 自定义定位蓝点图标
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.
                fromResource(R.drawable.gps_point));
        // 自定义精度范围的圆形边框颜色
        myLocationStyle.strokeColor(STROKE_COLOR);
        //自定义精度范围的圆形边框宽度
        myLocationStyle.strokeWidth(5);
        // 设置圆形的填充颜色
        myLocationStyle.radiusFillColor(FILL_COLOR);
        // 将自定义的 myLocationStyle 对象添加到地图上
        aMap.setMyLocationStyle(myLocationStyle);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //销毁地图
        map.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("MapFragment", "onResume: 执行");
        //在执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        map.onResume();
//        if (mSensorHelper != null) {
//            mSensorHelper.registerSensorListener();
//        }
    }

    @Override
    public void onPause() {
        super.onPause();
//        if (mSensorHelper != null) {
//            mSensorHelper.unRegisterSensorListener();
//            mSensorHelper.setCurrentMarker(null);
//            mSensorHelper = null;
//        }
        Log.e("MapFragment", "onPause: 执行");
        //在执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        map.onPause();
        mFirstFix = false;
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
        mListener = onLocationChangedListener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(getActivity());
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
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
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null
                    && amapLocation.getErrorCode() == 0) {
                location = new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude());
                if (!mFirstFix) {
                    mFirstFix = true;
//                    addCircle(location, amapLocation.getAccuracy());//添加定位精度圆
//                    addMarker(location);//添加定位图标
//                    mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转

                } else {
                    mCircle.setCenter(location);
                    mCircle.setRadius(amapLocation.getAccuracy());
                    mLocMarker.setPosition(location);
                }
                mListener.onLocationChanged(amapLocation);
//                mSensorHelper.setCurrentMarker(mLocMarker);//定位图标旋转
                LatLngBounds bounds = LatLngBounds.builder().include(addressLngLat).include(location).build();
                aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
                ToastUtils.showToast(errText);
            }
        }
    }

    private void addMarkersToMap() {
        MarkerOptions markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                .position(addressLngLat)
                .title(addressName)
                .draggable(true);
        aMap.addMarker(markerOption);
    }


    @Override
    public void onClick(View v) {
        if (StringUtil.isFastClick())
            return;
        if (!isShowRoute) {
            fab.setImageResource(R.drawable.ic_clear_black_24dp);
            searchRoute();
        } else {
            fab.setImageResource(R.drawable.ic_directions_car_black_24dp);
            aMap.clear();
            addMarkersToMap();
            isShowRoute = false;
        }
    }

    /**
     * 规划路线
     */
    private void searchRoute() {
        showProgressDialog();
        if (location == null) {
            ToastUtils.showToast("定位中，稍后再试...");
            return;
        }
        if (addressLngLat == null) {
            ToastUtils.showToast("重点未设置");
            return;
        }
        LatLonPoint mStartPoint = new LatLonPoint(location.latitude, location.longitude);
        LatLonPoint mEndPoint = new LatLonPoint(addressLngLat.latitude, addressLngLat.longitude);
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(mStartPoint, mEndPoint);
        // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DrivingDefault, null,
                null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(getActivity());
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int errorCode) {
        aMap.clear();// 清理地图上的所有覆盖物
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (driveRouteResult != null && driveRouteResult.getPaths() != null) {
                if (driveRouteResult.getPaths().size() > 0) {
                    mDriveRouteResult = driveRouteResult;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    drivingRouteOverlay = new DrivingRouteOverLay(
                            mActivity, aMap, drivePath,
                            mDriveRouteResult.getStartPos(),
                            mDriveRouteResult.getTargetPos(), null);
                    drivingRouteOverlay.setNodeIconVisibility(false);//设置节点marker是否显示
                    drivingRouteOverlay.setIsColorfulline(true);//是否用颜色展示交通拥堵情况，默认true
                    drivingRouteOverlay.removeFromMap();
                    drivingRouteOverlay.addToMap();
                    drivingRouteOverlay.zoomToSpan();
//                    mBottomLayout.setVisibility(View.VISIBLE);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    String des = "开车约" + AMapUtil.getFriendlyTime(dur) + "(" + AMapUtil.getFriendlyLength(dis) + ")";
                    int taxiCost = (int) mDriveRouteResult.getTaxiCost();
                    drivingRouteOverlay.setStartSnippet(des);
                    drivingRouteOverlay.setEndSnippet(addressName);
                    isShowRoute = true;
                    dissmissProgressDialog();
                } else if (driveRouteResult != null && driveRouteResult.getPaths() == null) {
                    ToastUtils.showToast("没有结果");
                }

            } else {
                ToastUtils.showToast("没有结果");
            }
        } else {
            ToastUtils.showToast("" + errorCode);
        }
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

}
