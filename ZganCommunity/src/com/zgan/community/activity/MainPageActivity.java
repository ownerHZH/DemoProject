package com.zgan.community.activity;

import com.zgan.community.R;
import com.zgan.community.tools.MainAcitivity;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainPageActivity extends MainAcitivity {

	private Button communityService; // 社区服务
	//private ImageButton communityCheap; // 好便宜
	private Button communityNotification; // 社区通知
	private Button communityPay; // 充值缴费
	private Button communityTrade; // 社区商圈
	private Button communityPolitical; // 阳光政务
	private Button communityRecruit; // 招工信息
	private Button communityLivelihood; // 生活百事
	private Button communityHouseShelter; // 家庭卫士
	private Button communitySettings; // 我的设置
	private ImageView setting;

	/*private TextView name; // 姓名
	private TextView integral; // 积分
	private TextView communityName; // 加入的社区名称
*/
	private Context con;

	int im[] = new int[] { R.drawable.galler_one, R.drawable.galler_two,R.drawable.galler_three
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_page);

		communityService = (Button) findViewById(R.id.communityService);
		//communityCheap = (ImageButton) findViewById(R.id.communityCheap);
		communityNotification = (Button) findViewById(R.id.communityNotification);
		communityPay = (Button) findViewById(R.id.communityPay);
		communityTrade = (Button) findViewById(R.id.communityTrade);
		communityPolitical = (Button) findViewById(R.id.communityPolicital);
		communityRecruit = (Button) findViewById(R.id.communityRecruit);
		communityLivelihood = (Button) findViewById(R.id.communityLivelihood);
		communityHouseShelter = (Button) findViewById(R.id.communityHouseShelter);
		communitySettings = (Button) findViewById(R.id.communitySettings);
		setting=(ImageView) findViewById(R.id.headIco);
		/*name = (TextView) findViewById(R.id.textViewName); // 显示的姓名
		integral = (TextView) findViewById(R.id.textViewIntegral); // 显示积分
		communityName = (TextView) findViewById(R.id.textViewCommunityName); // 显示加入的社区名称
*/
		/*Gallery gallery = (Gallery) findViewById(R.id.advertising);
		ImagAdapter adapter=new ImagAdapter(con);
		gallery.setAdapter(adapter);*/
		con = MainPageActivity.this; // 初始化一个全局的Context

		ButtonClickListener l = new ButtonClickListener(); // 按钮点击监听器初始化

		communityService.setOnClickListener(l); // 按钮注册监听器
		//communityCheap.setOnClickListener(l);
		communityNotification.setOnClickListener(l);
		communityPay.setOnClickListener(l);
		communityTrade.setOnClickListener(l);
		communityPolitical.setOnClickListener(l);
		communityRecruit.setOnClickListener(l);
		communityLivelihood.setOnClickListener(l);
		communityHouseShelter.setOnClickListener(l);
		setting.setOnClickListener(l);
		//communitySmartHousing.setOnClickListener(l);
		//communityNeighbor.setOnClickListener(l);
		communitySettings.setOnClickListener(l);

	}

	public class ButtonClickListener implements View.OnClickListener {

		Intent intent = null;

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.communityService:
				// 社区服务点击响应事件
				intent = new Intent(con, CommunityServiceActivity.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;

			/*case R.id.communityCheap:
				// 好便宜点击响应事件
				intent = new Intent(con, CommunityCheapActivity.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;*/
			case R.id.communityNotification:
				// 社区通知点击响应事件
				intent = new Intent(con, CommunityNewNotificationActivity.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communityPay:
				// 充值缴费点击响应事件
				intent = new Intent(con, CommunityNewPayActivity.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communityTrade:
				// 社区商圈点击响应事件
				intent = new Intent(con, CommunityTradeActivity.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communityPolicital:
				// 阳光政务点击响应事件
				intent = new Intent(con, CommunityPolicitalActivity.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communityRecruit:
				// 招工信息点击响应事件
				intent = new Intent(con, RecruitmentInfo.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communityLivelihood:
				// 生活百事点击响应事件
				intent = new Intent(con, Life_Pepsi_son.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			case R.id.communityHouseShelter:
				// 家庭卫士点击响应事件
				intent = new Intent(con, AQWSAppActivity.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;
			/*case R.id.communitySmartHousing:
				// 智能家居点击响应事件
				intent = new Intent(con, AQWSAppActivity.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;*/
			/*case R.id.communityNeighbour:
				// 邻里圈点击响应事件
				Toast.makeText(MainPageActivity.this, "对不起该功能已屏蔽",
						Toast.LENGTH_LONG);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				break;*/
			case R.id.communitySettings:
				// 我的设置点击响应事件
				intent = new Intent(con, CommunityContactProperty.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				//finish();
				break;
			case R.id.headIco:
				// 我的设置点击响应事件
				intent = new Intent(con, CommunitySetting.class);
				startActivity(intent);
				// Acticity切换动画
				overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
				//finish();
				break;

			default:
				break;
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.activity_main_page, menu);
		return true;
	}

	/*class ImagAdapter extends BaseAdapter {
		private Context context;// 用于接收传递过来的Context对象

		public ImagAdapter(Context context) {
			super();
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			Log.i("im.length", ""+im.length);
			return im.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Log.i("magc", ""+position);

			ImageView iv = new ImageView(MainPageActivity.this);// 针对外面传递过来的Context变量，
			//内存防止溢出
			BitmapFactory.Options options = new BitmapFactory.Options();
			options.inSampleSize = 1 ;
						BitmapFactory.Options opt = new BitmapFactory.Options();  
	        opt.inPreferredConfig = Bitmap.Config.RGB_565;  
	        opt.inPurgeable = true;  
	        opt.inInputShareable = true;  
	        // 获取资源图片  
	        Bitmap bitmap = BitmapFactory.decodeResource(MainPageActivity.this.getResources(), im[position]);

			iv.setImageBitmap(bitmap);
	        return iv;
		}*/


}
