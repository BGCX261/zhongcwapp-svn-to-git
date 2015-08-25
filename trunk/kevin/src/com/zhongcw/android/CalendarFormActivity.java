package com.zhongcw.android;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class CalendarFormActivity extends Activity{
	
	private Button saveButton;
	private Spinner spinner;
	private CheckBox checkBox;
	private Button startDate;
	private Button startTime;
	private Button endDate;
	private Button endTime;
	
    // date and time
    private int start_mYear;
    private int start_mMonth;
    private int start_mDay;
    private int start_mHour;
    private int start_mMinute;
    private int end_mYear;
    private int end_mMonth;
    private int end_mDay;
    private int end_mHour;
    private int end_mMinute;

    static final int START_TIME = 0;
    static final int START_DATE = 1;
    static final int END_TIME = 2;
    static final int END_DATE = 3;
    
    private DbAdapterCalendar dbAdapterCalendar;
    private String subject=""; //主题（字段）
    private String note=""; //备注（字段）
    private String start_time; //开始时间（字段）
    private String end_time; //结束时间（字段）
    private String repeatType; //重复（字段）
    
    private EditText calendar_subjectEditText;
    private EditText calendar_noteEditText;
    Bundle bundle;
    private long rowId;
	
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calendar_form);
		
		Intent intent = this.getIntent();
		bundle = intent.getExtras();
		dbAdapterCalendar = new DbAdapterCalendar(this);
		if(bundle == null){ //insert
		}else{ //update
			rowId = bundle.getLong("rowid");
			dbAdapterCalendar.open();
			Cursor cursor = dbAdapterCalendar.fetch(rowId);
			subject = cursor.getString(10);
			note = cursor.getString(12);
		}
		
		
		calendar_subjectEditText = (EditText)findViewById(R.id.calendar_subjectEditText);
		calendar_subjectEditText.setText(subject);
		calendar_noteEditText = (EditText)findViewById(R.id.calendar_noteEditText);
		calendar_noteEditText.setText(note);
		
		spinner = (Spinner) findViewById(R.id.spinner_repeat_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.repeat_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        
        checkBox = (CheckBox)findViewById(R.id.checkbox_all_day);
        checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener(){
        	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
        		if(isChecked){
//        			startTime.setText("00:00");
//        			startTime.setClickable(false);
        			startTime.setEnabled(false);
        			endTime.setEnabled(false);
        		}else{
        			startTime.setEnabled(true);
        			endTime.setEnabled(true);
        		}
            }
        });
        
        saveButton = (Button) findViewById(R.id.calendar_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				long id = spinner.getSelectedItemId();
//				String value = spinner.getSelectedItem().toString();
				boolean allDay =  checkBox.isChecked();
				if(allDay){ //全天日程
					start_time =  startDate.getText() + "00:00:00";
					end_time =      endDate.getText() + "23:59:59";
				}else{ //非全天日程
					start_time =  startDate.getText() + " " + startTime.getText();
					end_time =      endDate.getText() + " " + endTime.getText();
					
					switch ((int) id) {
					case 0: //不重复
						break;
					case 1: //每天
//						JSONObject jsonObj = new JSONObject();
						JSONObject value_jsonObj = new JSONObject();
						try {
								value_jsonObj.put("rtype", "day");
								value_jsonObj.put("intervalSlot", 1);
								value_jsonObj.put("dspan", 0);
								value_jsonObj.put("beginDay", startDate.getText());
								value_jsonObj.put("endDay", "no");
//								jsonObj.put("repeatType", value_jsonObj);
								repeatType = value_jsonObj.toString();
							} catch (JSONException e) {
								e.printStackTrace();
							}
						break;
					case 2: //每周
						break;
					case 3: //每月
						break;
					case 4: //每年
						break;
					default:
						break;
					}
				}
				
				subject =  calendar_subjectEditText.getText().toString();
				note =  calendar_noteEditText.getText().toString();
				
				if(bundle == null){ //insert
					dbAdapterCalendar.open();
					dbAdapterCalendar.insert(repeatType,null,null,null,null,null,null,start_time,end_time,subject,null,note,null);
					dbAdapterCalendar.close();
				}else{ //update
					dbAdapterCalendar.open();
					dbAdapterCalendar.update(rowId, repeatType,null,null,null,null,null,null,start_time,end_time,subject,null,note);
					dbAdapterCalendar.close();
				}
				
				finish();
			}
		});
        
        startDate = (Button) findViewById(R.id.startDate);
        startDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(START_DATE);
            }
        });
        startTime = (Button) findViewById(R.id.startTime);
        startTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(START_TIME);
            }
        });
        
        endDate = (Button) findViewById(R.id.endDate);
        endDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(END_DATE);
            }
        });
        endTime = (Button) findViewById(R.id.endTime);
        endTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(END_TIME);
            }
        });
        /**
         * 
         */

        final Calendar c = Calendar.getInstance();
        start_mYear = c.get(Calendar.YEAR);
        start_mMonth = c.get(Calendar.MONTH);
        start_mDay = c.get(Calendar.DAY_OF_MONTH);
        start_mHour = c.get(Calendar.HOUR_OF_DAY);
        start_mMinute = c.get(Calendar.MINUTE);
      
        end_mYear = c.get(Calendar.YEAR);
        end_mMonth = c.get(Calendar.MONTH);
        end_mDay = c.get(Calendar.DAY_OF_MONTH);
        end_mHour = c.get(Calendar.HOUR_OF_DAY);
        end_mMinute = c.get(Calendar.MINUTE);

        updateDisplay();
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case START_TIME:
                return new TimePickerDialog(this,
                        mTimeSetListener, start_mHour, start_mMinute, false);
            case START_DATE:
                return new DatePickerDialog(this,
                            mDateSetListener,
                            start_mYear, start_mMonth, start_mDay);
            case END_TIME:
                return new TimePickerDialog(this,
                        endTimeSetListener, end_mHour, end_mMinute, false);
            case END_DATE:
                return new DatePickerDialog(this,
                            endDateSetListener,
                            end_mYear, end_mMonth, end_mDay);    
        }
        return null;
    }

    @Override
    protected void onPrepareDialog(int id, Dialog dialog) {
        switch (id) {
            case START_TIME:
                ((TimePickerDialog) dialog).updateTime(start_mHour, start_mMinute);
                break;
            case START_DATE:
                ((DatePickerDialog) dialog).updateDate(start_mYear, start_mMonth, start_mDay);
                break;
            case END_TIME:
                ((TimePickerDialog) dialog).updateTime(end_mHour, end_mMinute);
                break;
            case END_DATE:
                ((DatePickerDialog) dialog).updateDate(end_mYear, end_mMonth, end_mDay);
                break;
        }
    }    

    private void updateDisplay() {
    	String sMonth;
    	if(start_mMonth + 1 < 10){
    		sMonth = "0"+String.valueOf(start_mMonth + 1);
    	}else{
    		sMonth = String.valueOf(start_mMonth + 1);
    	}
     	String eMonth;
    	if(end_mMonth + 1 < 10){
    		eMonth = "0"+String.valueOf(end_mMonth + 1);
    	}else{
    		eMonth = String.valueOf(end_mMonth + 1);
    	}
    	
        startDate.setText(new StringBuffer()
        		.append(start_mYear).append("-")
        		.append(sMonth).append("-")
                .append(start_mDay)
                );
        startTime.setText(new StringBuffer()
        		.append(pad(start_mHour)).append(":00:00")
//        		.append(pad(start_mMinute))
        		);
        endDate.setText(new StringBuffer()
				.append(end_mYear).append("-")
				.append(eMonth).append("-")
				.append(end_mDay)
        );
        endTime.setText(new StringBuffer()
				.append(pad(end_mHour)).append(":00:00")
//				.append(pad(end_mMinute))
		);
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                        int dayOfMonth) {
                	start_mYear = year;
                	start_mMonth = monthOfYear;
                	start_mDay = dayOfMonth;
                    updateDisplay();
                }
            };
    private DatePickerDialog.OnDateSetListener endDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                            int dayOfMonth) {
                    	end_mYear = year;
                    	end_mMonth = monthOfYear;
                    	end_mDay = dayOfMonth;
                        updateDisplay();
                    }
                };        

    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                	start_mHour = hourOfDay;
                	start_mMinute = minute;
                    updateDisplay();
                }
            };
    private TimePickerDialog.OnTimeSetListener endTimeSetListener =
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    	end_mHour = hourOfDay;
                    	end_mMinute = minute;
                        updateDisplay();
                    }
                };        

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

}
