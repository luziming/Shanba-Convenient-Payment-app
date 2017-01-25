package com.shaba.app.been;

import java.io.Serializable;

public class BillListEntity implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4508635934487346938L;
	private String payment_time;
	private String payment_amount;
	private String type_id;
	private String order_id;
	private String url;
	private int status;
	
	
	public String getPayment_time() {
		return payment_time;
	}
	public void setPayment_time(String payment_time) {
		this.payment_time = payment_time;
	}
	public String getPayment_amount() {
		return payment_amount;
	}
	public void setPayment_amount(String payment_amount) {
		this.payment_amount = payment_amount;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "BillListEntity [payment_time=" + payment_time
				+ ", payment_amount=" + payment_amount + ", type_id=" + type_id
				+ ", order_id=" + order_id + ", url=" + url + ", status="
				+ status + "]";
	}
	
	
	
}
