package com.shaba.app.been;

public class PaymentCompany {
	private int id;
	private String value = "";

	public PaymentCompany(int id, String value) {
		this.id = id;
		this.value = value;
	}

	@Override
	public String toString() {
		// 为什么要重写toString()呢？因为适配器在显示数据的时候，如果传入适配器的对象不是字符串的情况下，直接就使用对象.toString()
		return value;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
