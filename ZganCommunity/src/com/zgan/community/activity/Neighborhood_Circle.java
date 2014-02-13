package com.zgan.community.activity;

import java.util.ArrayList;
import java.util.List;

import com.zgan.community.R;
import com.zgan.community.tools.MainAcitivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class Neighborhood_Circle extends MainAcitivity {
	Button back;

	TextView top_title;
	
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.line_recinfo_son);
		back = (Button) findViewById(R.id.back);
		listView = (ListView) findViewById(R.id.list_reinfo);

		top_title = (TextView) findViewById(R.id.title);

		top_title.setText("¡⁄¿Ô»¶");

		back.setOnClickListener(l);
		List<String> data = new ArrayList<String>();
		for (int i = 0; i < 10; i++) {
			data.add("" + i);
			listView.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_expandable_list_item_1, data));
		}
	}
	OnClickListener l = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.back:
				finish();
				break;

			default:
				break;
			}
		}
	};
}
