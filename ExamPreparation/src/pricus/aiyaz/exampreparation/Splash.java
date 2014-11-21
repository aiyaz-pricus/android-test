package pricus.aiyaz.exampreparation;

import pricus.aiyaz.exampreparation.database_helper.Database_helper;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class Splash extends Activity {
	 Thread t;
	 SQLiteDatabase sqdb;
	 Database_helper db_helper;
	 String activity;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		db_helper=new Database_helper(getApplicationContext());
		
		setContentView(R.layout.splash);
		
		sqdb=db_helper.getWritableDatabase();
		String[] login_db_columns={"username","email","phone_no"};
		String[] datatype={"TEXT","TEXT","TEXT"};
		String[] statistics_column={"name_test","success_ratio","time_test","succ_time","succ_answer","total_question","attempted","right_ans","wrong_ans"};
		String[] statistics_datatype={"TEXT","TEXT","TEXT","TEXT","TEXT","TEXT","TEXT","TEXT","TEXT"};
		db_helper.create_table(sqdb, "user_statistics", statistics_column, statistics_datatype);
		db_helper.create_table(sqdb, "login", login_db_columns, datatype);
		String[] column_name={"notification_id","notification_msg"};
		String[] datatype_noti={"NUMBER PRIMARY KEY","TEXT"};
		db_helper.create_table(sqdb, "notification_table", column_name, datatype_noti);
		new waiting().execute("3211");
		
	}
	private void animatedStartActivity() {
		// we only animateOut this activity here.
		// The new activity will animateIn from its onResume() - be sure to implement it.
		final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		// disable default animation for new intent
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		ActivitySwitcher.animationOut(findViewById(R.id.container), getWindowManager(), new ActivitySwitcher.AnimationFinishedListener() {
			@Override
			public void onAnimationFinished() {
				startActivity(intent);
				Splash.this.finish();
			}
		});
	}
	
	
	public class waiting extends AsyncTask<String, Integer, String>
	{

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			animatedStartActivity();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			for(int i=0;i<2;i++)
			{
				try {
					Thread.sleep(1000);
					System.out.println("waiting");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(i==1)
				{
					
					
					Cursor data = db_helper.select_data(sqdb, "login");
						System.out.println("data is not null");
						if(data.getCount()>0)
						{	
						
						
						//	Intent intent=new Intent(Splash.this,MainActivity.class);
						//	startActivity(intent);
						}
						else
						{
							finish();
							
							Intent intent=new Intent(Splash.this,Login.class);
							startActivity(intent);
						}
					
					
				}
			}
			return null;
		}
		
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sqdb.close();
		System.out.println("pause event");
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		System.out.println("pause stop");
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	
		System.out.println("pause destroy");
	}

}
