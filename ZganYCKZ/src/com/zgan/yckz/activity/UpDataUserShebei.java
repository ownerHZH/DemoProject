package com.zgan.yckz.activity;

import java.util.ArrayList;
import java.util.List;

import com.zgan.yckz.R;
import com.zgan.yckz.adapter.KaiGuanAdapter;
import com.zgan.yckz.tools.SheBei;
import com.zgan.yckz.tools.YCKZ_SQLHelper;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class UpDataUserShebei extends Activity {
	/**
	 * 添加按钮
	 */
	TextView update;
	/**
	 * 
	 */
	Button back;
	ListView listView;
	TextView textView;
	SheBei data;
	

	YCKZ_SQLHelper yckz_SQLHelper;

	SQLiteDatabase db;
	
	String mac=null;
	
	String subname=null;
	
	List<SheBei> list = new ArrayList<SheBei>();

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if(db!=null){
			db.close();
		}
		super.onDestroy();
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.update);
		
		yckz_SQLHelper = new YCKZ_SQLHelper(this, "yckz.db3", 1);
		db = yckz_SQLHelper.getReadableDatabase();
		
		
		textView=(TextView)findViewById(R.id.title);
		back = (Button) findViewById(R.id.back);
		listView = (ListView) findViewById(R.id.listview);
		
		update.setText("");
		textView.setText("添加设置");
		update.setOnClickListener(listener);
		back.setOnClickListener(listener);
		
		Select();
		
		KaiGuanAdapter adapter = new KaiGuanAdapter(this, list);
		listView.setAdapter(adapter);

	}

	private void Select() {
		// TODO Auto-generated method stub
		Cursor c = db.rawQuery("select *from table_SubDev",new String[]{});

		while (c.moveToNext()) {
			mac = c.getString(c.getColumnIndex("_MAC"));
			subname=c.getString(c.getColumnIndex("_DeviceName"));
			Log.i("mac", mac);
			Log.i("subname", subname);
			SheBei  sheBei=new SheBei();
			sheBei.setMAC(mac);
			sheBei.setDeviceName(subname);
			list.add(sheBei);
		}
		c.close();
	}

	OnClickListener listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			finish();
		}
	};
}
