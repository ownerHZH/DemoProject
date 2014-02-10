package com.hiibox.houseshelter.activity;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.hiibox.houseshelter.ShaerlocActivity;
import com.hiibox.houseshelter.R;
import com.hiibox.houseshelter.adapter.FamilyMembersAdapter;
import com.hiibox.houseshelter.core.MianActivity;
import com.hiibox.houseshelter.net.MembersInfoResult;

    
  
  
  
  
  
  
  
public class FamilyMembersActivity extends ShaerlocActivity {

    @ViewInject(id = R.id.family_members_lv, itemClick = "onItemClick") ListView memberLV;
    
    private FamilyMembersAdapter adapter = null;
    private List<String> list = null;
    private List<String> rfidCardList = null;
    private int flag = 0;
    private List<MembersInfoResult> membersList = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        flag = getIntent().getIntExtra("selectedIndex", 0);
        setContentView(R.layout.activity_family_members_layout);
        
        list = new ArrayList<String>();
        rfidCardList = new ArrayList<String>();
        if (flag == 0) {
        	membersList = HomepageActivity.membersList;
        	if (null != membersList) {
        		int len = membersList.size();
        		for (int i = 0; i < len; i ++) {
        			list.add(membersList.get(i).nickname.trim());
        			rfidCardList.add(membersList.get(i).cardNum.trim());
        		}
        		list.add(getString(R.string.negative));
        		list.add(0, getString(R.string.all));
        	} else {
        		Intent data = new Intent();
                data.putExtra("member", "");
                setResult(RESULT_OK, data );
        		MianActivity.getScreenManager().exitActivity(mActivity);
        	}
        } else if (flag == 1) {
        	list.add(getString(R.string.all));
            list.add(getString(R.string.defence));
            list.add(getString(R.string.cancel_defence));
            list.add(getString(R.string.negative));
        } else if (flag == 2) {
        	list.add(getString(R.string.all));
            list.add(getString(R.string.temperature_abnormal));
            list.add(getString(R.string.invade));
            list.add(getString(R.string.negative));
        } else if (flag == 3) {
        	list.add(getString(R.string.all));
        	list.add(getString(R.string.invade));
        	list.add(getString(R.string.capture));
        	list.add(getString(R.string.negative));
        }
        
        adapter = new FamilyMembersAdapter(mContext, list);
        memberLV.setAdapter(adapter);
    }
    
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        if (position != adapter.getCount()-1) {
            Intent data = new Intent();
            data.putExtra("member", list.get(position));
            if (flag == 0 && position > 0) {
            	data.putExtra("rfidCard", rfidCardList.get(position-1));
            }
            setResult(RESULT_OK, data);
        }
        MianActivity.getScreenManager().exitActivity(mActivity);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
