package com.shaba.app.utils;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.RouteBusLineItem;
import com.shaba.app.R;
import com.shaba.app.global.Const;

import java.text.DecimalFormat;
import java.util.List;

public class SchemeUtil {
	public static String getBusPathTitle(BusPath busPath) {
		if (busPath == null) {
			return String.valueOf("");
		}
		List<BusStep> busSetps = busPath.getSteps();
		if (busSetps == null) {
			return String.valueOf("");
		}
		StringBuffer sb = new StringBuffer();
		for (BusStep busStep : busSetps) {
			RouteBusLineItem busline = busStep.getBusLine();
			if (busline == null) {
				continue;
			}
			String buslineName = getSimpleBusLineName(busline.getBusLineName());
			sb.append(buslineName);
			sb.append(">");
		}
		return sb.substring(0, sb.length() - 1);
	}

	public static String getBusPathDes(BusPath busPath) {
		if (busPath == null) {
			return String.valueOf("");
		}
		long second = busPath.getDuration();
		String time = getFriendlyTime((int) second);
		float subDistance = busPath.getDistance();
		String subDis = getFriendlyLength((int) subDistance);
		float walkDistance = busPath.getWalkDistance();
		String walkDis = getFriendlyLength((int) walkDistance);
		return String.valueOf(time + " | " + subDis + " | 步行" + walkDis);
	}

	public static String getFriendlyLength(int lenMeter) {
		if (lenMeter > 10000) { // 10 km
			int dis = lenMeter / 1000;
			return dis + Const.Kilometer;
		}
		if (lenMeter > 1000) {
			float dis = (float) lenMeter / 1000;
			DecimalFormat fnum = new DecimalFormat("##0.0");
			String dstr = fnum.format(dis);
			return dstr + Const.Kilometer;
		}
		if (lenMeter > 100) {
			int dis = lenMeter / 50 * 50;
			return dis + Const.Meter;
		}
		int dis = lenMeter / 10 * 10;
		if (dis == 0) {
			dis = 10;
		}
		return dis + Const.Meter;
	}

	public static String getFriendlyTime(int second) {
		if (second > 3600) {
			int hour = second / 3600;
			int miniate = (second % 3600) / 60;
			return hour + "小时" + miniate + "分钟";
		}
		if (second >= 60) {
			int miniate = second / 60;
			return miniate + "分钟";
		}
		return second + "秒";
	}

	public static String getSimpleBusLineName(String busLineName) {
		if (busLineName == null) {
			return String.valueOf("");
		}
		return busLineName.replaceAll("\\(.*?\\)", "");
	}

	public static int getDriveActionID(String actionName) {
		if (actionName == null || actionName.equals("")) {
			return R.drawable.dir3;
		}
		if ("左转".equals(actionName)) {
			return R.drawable.dir2;
		}
		if ("右转".equals(actionName)) {
			return R.drawable.dir1;
		}
		if ("向左前方行驶".equals(actionName) || "靠左".equals(actionName)) {
			return R.drawable.dir6;
		}
		if ("向右前方行驶".equals(actionName) || "靠右".equals(actionName)) {
			return R.drawable.dir5;
		}
		if ("向左后方行驶".equals(actionName) || "左转调头".equals(actionName)) {
			return R.drawable.dir7;
		}
		if ("向右后方行驶".equals(actionName)) {
			return R.drawable.dir8;
		}
		if ("直行".equals(actionName)) {
			return R.drawable.dir3;
		}
		if ("减速行驶".equals(actionName)) {
			return R.drawable.dir4;
		}
		return R.drawable.dir3;
	}

	public static int getWalkActionID(String actionName) {
		if (actionName == null || actionName.equals("")) {
			return R.drawable.dir13;
		}
		if ("左转".equals(actionName)) {
			return R.drawable.dir2;
		}
		if ("右转".equals(actionName)) {
			return R.drawable.dir1;
		}
		if ("向左前方".equals(actionName) || "靠左".equals(actionName)) {
			return R.drawable.dir6;
		}
		if ("向右前方".equals(actionName) || "靠右".equals(actionName)) {
			return R.drawable.dir5;
		}
		if ("向左后方".equals(actionName)) {
			return R.drawable.dir7;
		}
		if ("向右后方".equals(actionName)) {
			return R.drawable.dir8;
		}
		if ("直行".equals(actionName)) {
			return R.drawable.dir3;
		}
		if ("通过人行横道".equals(actionName)) {
			return R.drawable.dir9;
		}
		if ("通过过街天桥".equals(actionName)) {
			return R.drawable.dir11;
		}
		if ("通过地下通道".equals(actionName)) {
			return R.drawable.dir10;
		}

		return R.drawable.dir13;
	}

	public static int calculateLineDistance(LatLonPoint start, LatLonPoint end) {
		int distance = 0;
		if (start == null || end == null) {
			return distance;
		}
		double startLat = start.getLatitude();
		double startLon = start.getLongitude();
		double endLat = end.getLatitude();
		double endLon = end.getLongitude();
		LatLng amapStart = new LatLng(startLat, startLon);
		LatLng amapEnd = new LatLng(endLat, endLon);
		return (int) AMapUtils.calculateLineDistance(amapStart, amapEnd);
	}

	public static SpannableStringBuilder getRouteDes(Context context,
			int taxiCost) {
		SpannableStringBuilder stringBuilder = new SpannableStringBuilder();
		stringBuilder.append(Const.TAXI_TIP);
		String costStr = String.valueOf(taxiCost);
		Spannable spCost = new SpannableString(costStr);
		int costColor = context.getResources().getColor(R.color.red);
		spCost.setSpan(new ForegroundColorSpan(costColor), 0, spCost.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		stringBuilder.append(spCost);
		stringBuilder.append(Const.YUAN);
		return stringBuilder;
	}

	public static String getBusRouteTitle(int duration,int distance) {
		String dur = SchemeUtil.getFriendlyTime(duration);
		String dis = SchemeUtil.getFriendlyLength(distance);
		return dur + "(" + dis + ")";
	}
}
