package com.shaba.app.utils;

import com.shaba.app.global.ConstantUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {

	public static final String TEL_REGEX = "(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$";
	public static final String TEL_REGEX2="\\d{3}\\d{8}|\\d{3}\\d{7}|\\d{4}\\d{8}|\\d{4}\\d{7}";  
    public static final String IdCard_REgex= "([0-9]{17}([0-9]|X))|([0-9]{15})";
    /** 
     * 中国电信号码格式验证 手机段： 133,153,180,181,189,177,1700 
      * **/  
     private static final String CHINA_TELECOM_PATTERN = "(^1(33|53|77|8[019])\\d{8}$)|(^1700\\d{7}$)";  
   
     /** 
      * 中国联通号码格式验证 手机段：130,131,132,155,156,185,186,145,176,1709 
      * **/  
     private static final String CHINA_UNICOM_PATTERN = "(^1(3[0-2]|4[5]|5[56]|7[6]|8[56])\\d{8}$)|(^1709\\d{7}$)";     
     /** 
      * 中国移动号码格式验证 
      * 手机段：134,135,136,137,138,139,150,151,152,157,158,159,182,183,184 
      * ,187,188,147,178,1705 
      * **/  
     private static final String CHINA_MOBILE_PATTERN = "(^1(3[4-9]|4[7]|5[0-27-9]|7[8]|8[2-478])\\d{8}$)|(^1705\\d{7}$)";  
	/**
	 * 手机验证
	 * @param telNum
	 * @return
	 */
	public static boolean telRegex(String telNum) {
		if (StringUtil.isEmpty(telNum)) {
			return false;
		}
		String telNumText = telNum
				.replaceAll(ConstantUtil.SPACE_STRING, ConstantUtil.NULL_STRING)
				.trim().toString();
		Pattern p = Pattern.compile(TEL_REGEX);
		Matcher m = p.matcher(telNumText);
		return m.matches();
	}
	/**
	 * 身份证验证
	 * @return
	 */
	public static boolean IdCardRegex(String idcard) {
		if (StringUtil.isEmpty(idcard)) {
			return false;
		}
		Pattern p = Pattern.compile(IdCard_REgex);
		Matcher m = p.matcher(idcard);
		return m.matches();
	}
	
	/**
	 * 固话验证
	 * @param telNum
	 * @return
	 */
	public static boolean telRegex2(String telNum) {
		if (StringUtil.isEmpty(telNum)) {
			return false;
		}
		String telNumText = telNum
				.replaceAll(ConstantUtil.SPACE_STRING, ConstantUtil.NULL_STRING)
				.trim().toString();
		Pattern p = Pattern.compile(TEL_REGEX2);
		Matcher m = p.matcher(telNumText);
		return m.matches();
	}
	public static int matchesPhoneNumber(String phone_number) { 
 
		int flag = 0; 
		if (phone_number.matches(CHINA_TELECOM_PATTERN)) { 
		flag = 1; 
		} else if (phone_number.matches(CHINA_UNICOM_PATTERN)) { 
		flag = 2; 
		} else if (phone_number.matches(CHINA_MOBILE_PATTERN)) { 
		flag = 3; 
		} else { 
		flag = 4; 
		} 
		return flag; 

		} 
}
