package com.zhongcw.android;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class CalendarView extends View {

	static String TAG = "CalendarView";
	float cellHeight;
	float cellWidth;
	private CalendarEvent[] array;

	int year;
	int month;

	private CalendarActivity calendarActivity;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public CalendarView(Context context) {
		super(context);
		this.calendarActivity = (CalendarActivity) context;
		setFocusable(true);
		setFocusableInTouchMode(true);

		array = fillArrayWithCalendar();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		draw6x7Grid(canvas);
	}

	/**
	 * 6x7 grid
	 */
	void draw6x7Grid(Canvas canvas) {
		Paint background = new Paint();
		background.setColor(Color.WHITE);
		canvas.drawRect(0, 0, getWidth(), getHeight(), background);

		Paint row = new Paint();
		row.setColor(Color.LTGRAY); // red

		cellHeight = getHeight() / 6f; // 6行
		cellWidth = getWidth() / 7f; // 7列

		// draw row
		for (int i = 0; i < 6; i++) {
			canvas.drawLine(0, i * cellHeight, getWidth(), i * cellHeight, row);
		}
		Paint column = new Paint();
		column.setColor(Color.LTGRAY); // blue
		// draw column
		for (int i = 0; i < 7; i++) {
			canvas.drawLine(i * cellWidth, 0, i * cellWidth, getHeight(),
					column);
		}

		// fill string to cell
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 7; j++) {
				drawCell(canvas, i, j);
			}
		}

	}

	private void drawCell(Canvas canvas, int i, int j) {
		float x = cellWidth / 2;
		float y = cellHeight / 2;

		CalendarEvent event = getCell(i, j);
		if (event.getEventFlag()) {
			Paint paint = new Paint();
			paint.setColor(Color.RED);
			canvas.drawPoint(j * cellWidth + cellWidth - 2, i * cellHeight + 2,
					paint); // 原始点
			canvas.drawPoint(j * cellWidth + cellWidth - 2, i * cellHeight + 3,
					paint); // height +1
			canvas.drawPoint(j * cellWidth + cellWidth - 2, i * cellHeight + 4,
					paint); // height +2
			canvas.drawPoint(j * cellWidth + cellWidth - 3, i * cellHeight + 2,
					paint); // width -1
			canvas.drawPoint(j * cellWidth + cellWidth - 4, i * cellHeight + 2,
					paint); // width -2
		} else {
		}

		canvas.drawText(event.getTitle(), j * cellWidth + x,
				i * cellHeight + y, event.getPaint());
	}

	private CalendarEvent[] fillArrayWithCalendar() {
		CalendarEvent[] ary = new CalendarEvent[42];
		for (int i = 0; i < ary.length; i++) {
			ary[i] = new CalendarEvent();
		}

		setYear(2010);
		setMonth(03);
		int dayOfMonth = CalendarActivity.getNumberOfDaysInMonth(getYear(),
				getMonth()); // 某月多少天？（31）
		int weekday = CalendarActivity.getWeekdayOfDateTime(getYear() + "-"
				+ getMonth() + "-1"); // 某天是周几？默认取该月1号。周日为0，周一为1
		if (weekday == 0) {
			weekday = 7;
		}
		int first = 1;
		int start = weekday - first;
		for (int i = 0; i < start; i++) {
			ary[i].setTitle("#");
			Paint paint = new Paint();
			paint.setColor(Color.GRAY);
			paint.setTextAlign(Paint.Align.CENTER);
			ary[i].setPaint(paint);
		}
		int day = 1;
		for (int j = start; j < dayOfMonth + start; j++) {
			ary[j].setTitle(String.valueOf(day));
			Paint paint = new Paint();
			paint.setColor(Color.BLACK);
			paint.setTextAlign(Paint.Align.CENTER);
			ary[j].setPaint(paint);
			day++;
		}
		int after = 1;
		for (int k = dayOfMonth + start; k < ary.length; k++) {
			ary[k].setTitle(String.valueOf(after));
			Paint paint = new Paint();
			paint.setColor(Color.GRAY);
			paint.setTextAlign(Paint.Align.CENTER);
			ary[k].setPaint(paint);
			after++;
		}

		// 事件列表
		String[] event = { "3", "5", "8" };

		for (int i = 0; i < ary.length; i++) {
			for (int j = 0; j < event.length; j++) {
				if (ary[i].getTitle().equals(event[j])) {
					ary[i].setEventFlag(true);
				} else {

				}
			}
		}

		return ary;
	}

	private CalendarEvent getCell(int i, int j) {
		return array[i * 7 + j];
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() != MotionEvent.ACTION_DOWN)
			return super.onTouchEvent(event);
		float x = event.getX();
		float y = event.getY();
		int column = (int) Math.floor(x / cellWidth);
		int row = (int) Math.floor(y / cellHeight);
		int chooseNum = row * 7 + column;
		String day = array[chooseNum].getTitle();
		String month;
		if(getMonth() < 10){
			month = "0"+String.valueOf(getMonth());
		}else{
			month = String.valueOf(getMonth());
		}
		String date = getYear() + "-" + month + "-" + day;
		calendarActivity.queryEvent(date);
		return true;
	}
}
