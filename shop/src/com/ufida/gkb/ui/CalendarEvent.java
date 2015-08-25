package com.ufida.gkb.ui;

import android.graphics.Paint;

public class CalendarEvent {
	private String title;
	private boolean eventFlag = false;
	private Paint paint;

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean getEventFlag() {
		return eventFlag;
	}

	public void setEventFlag(boolean eventFlag) {
		this.eventFlag = eventFlag;
	}

}
