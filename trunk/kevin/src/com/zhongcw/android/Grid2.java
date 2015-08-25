///*
// * Copyright (C) 2007 The Android Open Source Project
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.zhongcw.android;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.GridView;
//import android.widget.ImageView;
//import android.widget.ListView;
//
///**
// * A grid that displays a set of framed photos.
// *
// */
//public class Grid2 extends Activity {
//	Button b1;
//	Button b2;
//	Button b3;
//	
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.grid_2);
//        
////        b1 = new Button(this);
////        b1.setText("A");
////        b1.setOnClickListener(new View.OnClickListener() {
////			public void onClick(View v) {
////				String str = b1.getText().toString();
////				int a = 1;
////			}
////		});
////        
////        b2 = new Button(this);
////        b2.setText("B");
////        b3 = new Button(this);
////        b3.setText("C");
//        
//    	List list = new ArrayList();
//    	list.add("a");
//    	list.add("b");
//    	list.add("c");
//    	list.add("d");
//    	list.add("e");
//    	list.add("f");
//    	list.add("g");
//    	list.add("h");
//    	list.add("i");
//    	list.add("j");
//    	
////    	list.add(b1);
////    	list.add(b2);
////    	list.add(b3);
//    	ArrayAdapter adapter = new ArrayAdapter(this, R.layout.main_1, list);
//        GridView g = (GridView) findViewById(R.id.myGrid);
//        g.setAdapter(adapter);
//    }
//
//}
