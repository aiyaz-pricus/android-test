package pricus.aiyaz.exampreparation;




import java.util.ArrayList;
import java.util.zip.Inflater;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import pricus.aiyaz.exampreparation.data_adapter.Temp_ques_answer;
import pricus.aiyaz.exampreparation.database_helper.Database_helper;
import pricus.aiyaz.exampreparation.network.Json_data;
import android.app.Activity;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	ListView lv;
	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;

	//Database Helper object
	public Database_helper db_helper;
	public SQLiteDatabase sql_db;

	Animation animation_noti;
	
	ImageView home_icon;
	ImageView icon_drawer;	
	String cond_parent_id;
	String a,p_id;
	String parent_exam_name;
	TextView noti;

	ImageButton img_btn_bell;
	ArrayList<String> nav_bar_item;
	ArrayList<String> nav_sub_item;

	SharedPreferences local_settings;

	ProgressDialog pdialog;
	// object for getting and setting question locally
	Temp_ques_answer set_get_q_a;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		local_settings=getApplicationContext().getSharedPreferences("Local_setting", MODE_PRIVATE);
		
		int noti_flag=local_settings.getInt("notifications", -1);
		if(noti_flag==-1)
		{
			Editor edit=local_settings.edit();
			edit.putInt("notifications", 0);
			edit.commit();
		}
		
		animation_noti=new TranslateAnimation(0,10,0,0); 

		set_get_q_a=new Temp_ques_answer();
		
		//set column name and datatype to create table.
		String[] columnname_menu={"id","title","parentid"};
		String[] datatype_menu={"varchar PRIMARY KEY","varchar","varchar"};
		db_helper=new Database_helper(getApplicationContext());
		sql_db=db_helper.getWritableDatabase();
		db_helper.create_table(sql_db, "ms_product", columnname_menu, datatype_menu);

		pdialog=new ProgressDialog(MainActivity.this);
		pdialog.setMessage("Processing");
		pdialog.setCancelable(false);

		mTitle = mDrawerTitle = getTitle();
		Cursor tmp_ms_product=sql_db.rawQuery("select count(*) from ms_product;",null); 
		tmp_ms_product.moveToFirst();
		int is_ms_product_empty=tmp_ms_product.getInt(0);

		ActionBar mActionBar = getActionBar();
		mActionBar.setDisplayShowHomeEnabled(false);
		mActionBar.setDisplayShowTitleEnabled(false);
		LayoutInflater mInflater = LayoutInflater.from(this);

		View mCustomView = mInflater.inflate(R.layout.custom_actionbar, null);
		noti=(TextView)mCustomView.findViewById(R.id.notify);
		icon_drawer=(ImageView)mCustomView.findViewById(R.id.imageView1);
		img_btn_bell=(ImageButton)mCustomView.findViewById(R.id.imageButton1);
		home_icon=(ImageView)mCustomView.findViewById(R.id.home_image);
		mActionBar.setCustomView(mCustomView);
		mActionBar.setDisplayShowCustomEnabled(true);
		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
		navDrawerItems = new ArrayList<NavDrawerItem>();	

		boolean net_available=haveNetworkConnection();
		if(net_available==true)
		{
			System.out.println("internet is available..........=============");
			new get_notification().execute("asd");
			if(is_ms_product_empty==0)
			{
				pdialog.show();
				new download_menu().execute();
			}
			else
			{
				Toast.makeText(getApplicationContext(), "already downloaded database", Toast.LENGTH_LONG).show();
				Cursor load_parent=sql_db.rawQuery("select * from ms_product where parentid='0'", null);
				load_parent.moveToFirst();
				nav_bar_item=new ArrayList<String>();
				for(int i=0;i<load_parent.getCount();i++)
				{
					nav_bar_item.add(load_parent.getString(1));
					System.out.println(load_parent.getString(1));
					load_parent.moveToNext();
				}
				for (int i = 0; i < nav_bar_item.size(); i++) {

					navDrawerItems.add(new NavDrawerItem(nav_bar_item.get(i)));
				}
			}

		}
		else
		{
			System.out.println("internet is not available..........=============");


			Toast.makeText(getApplicationContext(), "Internet Connection Not Available", Toast.LENGTH_LONG).show();
			Cursor load_parent=sql_db.rawQuery("select * from ms_product where parentid='0'", null);
			if(load_parent != null)
			{
				load_parent.moveToFirst();
				nav_bar_item=new ArrayList<String>();
				for(int i=0;i<load_parent.getCount();i++)
				{
					nav_bar_item.add(load_parent.getString(1));
					System.out.println(load_parent.getString(1));
					load_parent.moveToNext();
				}
				for (int i = 0; i < nav_bar_item.size(); i++) {
					navDrawerItems.add(new NavDrawerItem(nav_bar_item.get(i)));
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Please Connect to internet to download data.", Toast.LENGTH_LONG).show();
			}
		}



		if(noti.getVisibility()==noti.VISIBLE)
		{
			//	ScaleAnimation scale = new ScaleAnimation(0, 1, 0, 1, ScaleAnimation.RELATIVE_TO_SELF, .5f, ScaleAnimation.RELATIVE_TO_SELF, .5f);
			//	scale.setDuration(300);
			//	scale.setInterpolator(new OvershootInterpolator());


		}


		home_icon.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				/*	 FragmentManager fm = getFragmentManager();
				   String tag; //= fm.getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1).getName();
				 FragmentTransaction ft = fm.beginTransaction();

			        Fragment f=getFragmentManager().findFragmentByTag(tag);
			        ft.remove(f);
			        ft.commit(); */
			}
		});
		img_btn_bell.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("img clicked..........");
				if(noti.getVisibility()==noti.VISIBLE)
				{
					noti.setVisibility(noti.INVISIBLE);
					Notification_fragment noti_frag=new Notification_fragment();
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
					.replace(R.id.frame_container, noti_frag).commit();
					animation_noti.cancel();
					animation_noti.reset();
				}
				else
				{
					System.out.println("img clicked..........");
					Notification_fragment noti_frag=new Notification_fragment();
					FragmentManager fragmentManager = getFragmentManager();
					fragmentManager.beginTransaction()
					.replace(R.id.frame_container, noti_frag).commit();
				}
			}
		});
		icon_drawer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(mDrawerLayout.isDrawerOpen(mDrawerList))
				{
					Animation myRotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation_back);
					icon_drawer.startAnimation(myRotation);
					mDrawerLayout.closeDrawers();
				}
				else
				{
					Animation myRotation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotation);
					icon_drawer.startAnimation(myRotation);
					mDrawerLayout.openDrawer(mDrawerList);
				}
			}
		});

		// nav drawer icons from resources


		// adding nav drawer items to array
		// Home

		// Find People

		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1), true, "50+"));


		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		//getActionBar().setHomeButtonEnabled(true);
		getActionBar().setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_shape));
		getActionBar().setDisplayShowTitleEnabled(false);
		//getActionBar().setDisplayUseLogoEnabled(false);
		//	getActionBar().setIcon(android.R.color.transparent);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
				) {
			public void onDrawerClosed(View view) {

				// calling onPrepareOptionsMenu() to show action bar icons

				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons

				System.out.println("drawer opened-------=-=--=-=-=--=-===-=-=-=-=-=-=-=-====-=-=-=-=-=-=-=-=");
				invalidateOptionsMenu();
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);
		if (savedInstanceState == null) {
			// on first time display view for first nav item
			displayView(0);
		}
	}

	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
	ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			//displayView(position);

			nav_sub_item=new ArrayList<String>();
			a=nav_bar_item.get(position);
			parent_exam_name=a;
			Cursor data=sql_db.rawQuery("select * from ms_product where title='"+a+"' and parentid='0'", null);
			data.moveToFirst();
			p_id=data.getString(0);
			Cursor sub_menu=sql_db.rawQuery("select * from ms_product where parentid='"+p_id+"'", null);
			sub_menu.moveToFirst();
			for (int i = 0; i < sub_menu.getCount(); i++) {
				System.out.println(sub_menu.getString(1));
				nav_sub_item.add(sub_menu.getString(1));
				sub_menu.moveToNext();
			}
			cond_parent_id=p_id;
			get_menu("0",p_id);
		}
		void get_menu(String title,String parentid)
		{

			p_id=parentid;
			ArrayAdapter<String> sub_adapter;
			final Dialog dialog = new Dialog(MainActivity.this);
			dialog.setContentView(R.layout.dialog_box);
			lv=(ListView)dialog.findViewById(R.id.listView1);

			if(title=="0")
			{
				sub_adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nav_sub_item);

				dialog.setTitle(a);
				LinearLayout ll=(LinearLayout)dialog.findViewById(R.id.lin_layout_dialog);
				ll.setAlpha(1.0f);
				lv.setAdapter(sub_adapter);
				// set the custom dialog components - text, image and button
				dialog.show();
			}
			else if(title=="1")
			{
				parent_exam_name=parent_exam_name+"-"+a;

				dialog.setTitle(a);
				Cursor check_if_parent=sql_db.rawQuery("select * from ms_product where parentid='"+parentid+"'", null);
				int temp_sub_count=check_if_parent.getCount();
				if(temp_sub_count>0)
				{
					for (int i = 0; i < temp_sub_count; i++) {
						check_if_parent.moveToPosition(i);
						nav_sub_item.add(check_if_parent.getString(1));
						System.out.println(check_if_parent.getString(1));

					}
					sub_adapter=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, nav_sub_item);
					lv.setAdapter(sub_adapter);
					dialog.show();
				}
				else
				{
					String[] tab_name=cond_parent_id.split("_");
					System.out.println(tab_name[0]);
					Cursor cursor = sql_db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+tab_name[0]+"'", null);
					if(cursor!=null) {
						if(cursor.getCount()>0) {
							//Toast.makeText(getApplicationContext(), "table existed", Toast.LENGTH_LONG).show();
							get_data_sqlite();

						}
						else
						{
							System.out.println("table name to download:::------- "+ tab_name[0]);
							pdialog.show();
							new download_q_a().execute(tab_name[0]);
						}

					}
				}

			}
			// if button is clicked, close the custom dialog

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					String temp_sub_menu=lv.getItemAtPosition(position).toString();
					Cursor check_parent=sql_db.rawQuery("select * from ms_product where title='"+temp_sub_menu+"' and parentid='"+p_id+"'", null);
					check_parent.moveToFirst();
					System.out.println(temp_sub_menu);
					cond_parent_id=cond_parent_id+"_"+temp_sub_menu;
					System.out.println(cond_parent_id.toLowerCase());
					String sub_p_id=check_parent.getString(0);					
					nav_sub_item.clear();
					a=temp_sub_menu;
					dialog.dismiss();
					get_menu("1", sub_p_id);
				}

			});
			
		

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_settings:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		//	boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		//	menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			//	fragment = new HomeFragment();
			//	Toast.makeText(getApplicationContext(),"hello", Toast.LENGTH_LONG).show();
			break;
		default:
			break;
		}

	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	public class download_menu extends AsyncTask<String, Integer, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Json_data jdata=new Json_data("http://192.168.1.51/henal/webservices-demo/webservices-select1.php?exam=all","ms_product",getApplicationContext());
			jdata.http_req_function();
			return null;

		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			Cursor load_parent=sql_db.rawQuery("select * from ms_product where parentid='0'", null);
			load_parent.moveToFirst();
			nav_bar_item=new ArrayList<String>();
			for(int i=0;i<load_parent.getCount();i++)
			{
				nav_bar_item.add(load_parent.getString(1));
				System.out.println(load_parent.getString(1));
				load_parent.moveToNext();
			}
			for (int i = 0; i < nav_bar_item.size(); i++) {

				navDrawerItems.add(new NavDrawerItem(nav_bar_item.get(i)));
			}
			navMenuIcons.recycle();

			mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

			// setting the nav drawer list adapter
			adapter = new NavDrawerListAdapter(getApplicationContext(),
					navDrawerItems);
			mDrawerList.setAdapter(adapter);
			pdialog.hide();
		}
	}
	public class download_q_a extends AsyncTask<String, Integer, String>
	{

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			get_data_sqlite();
			pdialog.cancel();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			System.out.println("downloading data from server....");
			Json_data jdata=new Json_data("http://192.168.1.51/henal/webservices-demo/webservices-select-questiontype-all.php?exam="+params[0],"'"+params[0]+"'",getApplicationContext());
			jdata.http_req_crete_table();
			return null;
		}
	}
	void get_data_sqlite()
	{
		String[] temp=cond_parent_id.split("_");

		System.out.println("select * from '"+temp[0]+"' where question_type='"+cond_parent_id+"'");
		Cursor c_table=sql_db.rawQuery("select * from `"+temp[0]+"` where question_type='"+cond_parent_id.toLowerCase()+"'",null);
		if(c_table!=null)
		{
			for(int i=0;i<c_table.getCount();i++)
			{	c_table.moveToPosition(i);
			System.out.println(c_table.getString(0)+ c_table.getString(2)+ c_table.getString(3)+ c_table.getString(4));
			set_get_q_a.set_values(c_table.getString(0), c_table.getString(1), c_table.getString(2), c_table.getString(3), c_table.getString(4), c_table.getString(5), c_table.getString(6), c_table.getString(7));
			}
		}
		Fragment fragment=new HomeFragment(set_get_q_a,c_table.getCount(),parent_exam_name);

		if (fragment != null) {
			mDrawerLayout.closeDrawers();
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
			.replace(R.id.frame_container, fragment).commit();
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	public class get_notification extends AsyncTask<String, Integer, String>
	{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
		
			
			Cursor get_noti_local=sql_db.rawQuery("select max(notification_id) from notification_table", null);
			System.out.println("inside getting notification.......");
			String noti;
			if(get_noti_local!=null)
			{
				get_noti_local.moveToFirst();
				String notification_last=get_noti_local.getString(0);
				if(notification_last!=null){
				Editor edit=local_settings.edit();
				edit.putInt("notifications", Integer.parseInt(notification_last));
				edit.commit();
				}
				System.out.println("notification_last:-----"+notification_last);
				String link= "http://192.168.1.51/henal/webservices-demo/webservices-select-notification-table.php?method=notification&id="+local_settings.getInt("notifications", 0);			
				Json_data jdata=new Json_data(link,"notification_table",getApplicationContext());
				noti=jdata.http_notification_function();
			}
			else
			{
				System.out.println("no local table detected..............");
				String link= "http://192.168.1.51/henal/webservices-demo/webservices-select-notification-table.php?method=notification&id="+0;			
				Json_data jdata=new Json_data(link,"notification_table",getApplicationContext());
				noti=jdata.http_notification_function();
			}
			return noti;

		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String[] split_noti=result.split("-");
			String no_of_notifications=split_noti[1];
			System.out.println("no of notification:----"+no_of_notifications);
			if(Integer.parseInt(no_of_notifications)>0)
			{
				noti.setVisibility(noti.VISIBLE);
				noti.setText(no_of_notifications);

				
				animation_noti.setDuration(300);
				
				animation_noti.setRepeatMode(Animation.RESTART);

				animation_noti.setRepeatCount(Animation.INFINITE);

				img_btn_bell.startAnimation(animation_noti);
			}
		}

	}


	private boolean haveNetworkConnection() {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					haveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					haveConnectedMobile = true;
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

}