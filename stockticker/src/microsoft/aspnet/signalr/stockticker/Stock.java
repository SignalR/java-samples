package microsoft.aspnet.signalr.stockticker;

import java.util.Calendar;

public class Stock {

	@com.google.gson.annotations.SerializedName("Price")
	private double mPrice;

	@com.google.gson.annotations.SerializedName("Symbol")
	private String mSymbol;
	
	@com.google.gson.annotations.SerializedName("DayOpen")
	private double mDayOpen;
	
	@com.google.gson.annotations.SerializedName("DayLow")
	private double mDayLow;

	@com.google.gson.annotations.SerializedName("DayHigh")
	private double mDayHigh;
	
	@com.google.gson.annotations.SerializedName("LastChange")
	private double mLastChange;
	
	@com.google.gson.annotations.SerializedName("Change")
	private double mChange;
	
	@com.google.gson.annotations.SerializedName("PercentChange")
	private double mPercentChange;
	
	private Calendar mLastUpdateTime;
	
	public Stock() {
		mLastUpdateTime = Calendar.getInstance();
	}

	public double getPrice() {
		return mPrice;
	}

	public String getSymbol() {
		return mSymbol;
	}

	public double getDayOpen() {
		return mDayOpen;
	}

	public double getDayLow() {
		return mDayLow;
	}

	public double getDayHigh() {
		return mDayHigh;
	}

	public double getLastChange() {
		return mLastChange;
	}

	public double getChange() {
		return mChange;
	}

	public double getPercentChange() {
		return mPercentChange * 100;
	}

	public Calendar getLastUpdateTime() {
		return mLastUpdateTime;
	}
}