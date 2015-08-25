package com.zhongcw.ui.widget.dialog;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class ImgFactory {
	public static String defaul = "default.png";
	public static Map<String, Fbitmap> bitmaps = new HashMap<String, Fbitmap>();
	private static ImgFactory img=new ImgFactory();
	
	public static Bitmap getImg(String name, boolean bol) {
		String path = "/img/" + name;
		InputStream bitmapin = img.getClass().getResourceAsStream(path);
		Bitmap bitmap=null;
		if(bitmaps.get(name)!=null){
			bitmap=bitmaps.get(name).bitmap;
		}else{
			bitmap = BitmapFactory.decodeStream(bitmapin);
			bitmaps.put(name, new Fbitmap(bitmap,bol));
		}
		return bitmap;
	}

	public static Bitmap getImg(String name) {
		String path = "/cstio/ufdia/commons/res/" + name;
		InputStream bitmapin = img.getClass().getResourceAsStream(path);
		return BitmapFactory.decodeStream(bitmapin);
	}
	
	public static Drawable getDraw(String name) {
		Bitmap bitmap = getImg(name);
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}
	
	public static Drawable getDraw(String name, boolean bol) {
		Bitmap bitmap = getImg(name, bol);
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}

	public static Drawable toDraw(Bitmap bitmap) {
		Drawable drawable = new BitmapDrawable(bitmap);
		return drawable;
	}
	
	public static void recyclebitmap(){
		List<String> list=mapKey(bitmaps);
		for(int i=0;i<list.size();i++){
			Fbitmap bitmap=bitmaps.get(list.get(i));
			if(bitmap==null || bitmap.bitmap==null || bitmap.bitmap.isRecycled()){
				bitmaps.remove(list.get(i));
			}else if(bitmap.autore=false){
				bitmap.bitmap.recycle();
				bitmaps.remove(list.get(i));
			}
		}
	}
	
	public static void clearn(){
		List<String> list=mapKey(bitmaps);
		try{
		for(int i=0;i<list.size();i++){
			Fbitmap bitmap=bitmaps.get(list.get(i));
			if(bitmap==null ||  bitmap.bitmap==null || bitmap.bitmap.isRecycled()){
				bitmaps.remove(list.get(i));
			}
		}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static List<String> mapKey(Map<?, ?> map) {
		List<String> retn = new ArrayList<String>();
		for (Object o : map.keySet()) {
			retn.add(String.valueOf(o));
		}
		return retn;
	}
	
	public static class Fbitmap{
		public Bitmap bitmap;
		public boolean autore=true;
		
		public Fbitmap(Bitmap bitmap,boolean autore){
			this.bitmap=bitmap;
			this.autore=autore;
		}
	}
}
