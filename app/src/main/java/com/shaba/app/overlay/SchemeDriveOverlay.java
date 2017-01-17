package com.shaba.app.overlay;

import android.content.Context;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DrivePath;
import com.shaba.app.R;

import java.util.List;

public class SchemeDriveOverlay extends DrivingRouteOverlay {


	/**
	 * 根据给定的参数，构造一个导航路线图层类对象。
	 * @param context          当前的activity对象。
	 * @param amap             地图对象。
	 * @param path             导航路线规划方案。
	 * @param start
	 * @param end
	 * @param throughPointList
	 */
	public SchemeDriveOverlay(Context context, AMap amap, DrivePath path, LatLonPoint start, LatLonPoint end, List<LatLonPoint> throughPointList) {
		super(context, amap, path, start, end, throughPointList);
	}

	@Override
	protected BitmapDescriptor getStartBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.route_start);
	}

	@Override
	protected BitmapDescriptor getEndBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.route_end);
	}

	@Override
	protected BitmapDescriptor getDriveBitmapDescriptor() {
		return BitmapDescriptorFactory.fromResource(R.drawable.cloud_car);
	}


}
