package com.zgan.community.adapter;

import com.zgan.community.activity.AQWSAppActivityFragment;
import com.zgan.community.activity.CommunityContactPropertyFragment;
import com.zgan.community.activity.CommunityNewNotificationActivityFragment;
import com.zgan.community.activity.CommunityNewPayActivityFragment;
import com.zgan.community.activity.CommunityPolicitalActivityFragment;
import com.zgan.community.activity.CommunityServiceActivityFragment;
import com.zgan.community.activity.CommunityTradeActivityFragment;
import com.zgan.community.activity.Life_Pepsi_sonFragment;
import com.zgan.community.activity.MainFragmentActivity;
import com.zgan.community.activity.RecruitmentInfoFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter {

	public final static int TAB_COUNT = 9;
	public FragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int id) {
		switch (id) {
		case MainFragmentActivity.TAB0:
			CommunityServiceActivityFragment communityServiceActivityFragment = new CommunityServiceActivityFragment();
			return communityServiceActivityFragment;
		case MainFragmentActivity.TAB1:
			CommunityTradeActivityFragment communityTradeActivityFragment = new CommunityTradeActivityFragment();
			return communityTradeActivityFragment;
		case MainFragmentActivity.TAB2:
			CommunityNewNotificationActivityFragment communityNewNotificationActivityFragment = new CommunityNewNotificationActivityFragment();
			return communityNewNotificationActivityFragment;
		case MainFragmentActivity.TAB3:
			RecruitmentInfoFragment recruitmentInfoFragment = new RecruitmentInfoFragment();
			return recruitmentInfoFragment;
		case MainFragmentActivity.TAB4:
			Life_Pepsi_sonFragment life_Pepsi_sonFragment = new Life_Pepsi_sonFragment();
			return life_Pepsi_sonFragment;
		case MainFragmentActivity.TAB5:
			CommunityPolicitalActivityFragment communityPolicitalActivityFragment = new CommunityPolicitalActivityFragment();
			return communityPolicitalActivityFragment;
		case MainFragmentActivity.TAB6:
			CommunityNewPayActivityFragment communityNewPayActivityFragment = new CommunityNewPayActivityFragment();
			return communityNewPayActivityFragment;
		case MainFragmentActivity.TAB7:
			CommunityContactPropertyFragment communityContactPropertyFragment = new CommunityContactPropertyFragment();
			return communityContactPropertyFragment;
		case MainFragmentActivity.TAB8:
			AQWSAppActivityFragment aQWSAppActivityFragment = new AQWSAppActivityFragment();
			return aQWSAppActivityFragment;
		}
		return null;
	}

	@Override
	public int getCount() {
		return TAB_COUNT;
	}

}
