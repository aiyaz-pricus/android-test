package pricus.aiyaz.exampreparation;

import java.util.ArrayList;
import java.util.Random;

import pricus.aiyaz.exampreparation.data_adapter.Temp_ques_answer;
import pricus.aiyaz.exampreparation.database_helper.Database_helper;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.gesture.Gesture;
import android.gesture.GestureOverlayView;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnGenericMotionListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeFragment extends Fragment {
	TextView question,ans1,ans2,ans3,ans4,counter,right_ans,wrong_ans,title_test;
	Temp_ques_answer get_data;
	View rootView;
	
	TextView start_exam,back_home;
	Chronometer time_count;
	String[] record;
	int pos=0,attempted=0,right_answer=0,wrong_answer=0,time_attempting=0;   
	float avg_performance=0.0f;
	int count,total_time=0;
	Thread t;
	boolean isRight;
	int size_question_list;
	SQLiteDatabase sqdb;
	String count_q_b,count_q,test_name;
	ImageView skip_ques;
	MediaPlayer mp;
	Random random_ques;
	int low_random=0;
	int high_random;
	Typeface typeFace;
	Button statistics;
	Database_helper db_helper;
	ArrayList<Integer> question_attempted_list=new ArrayList<Integer>();


	public HomeFragment(){


	}
	public HomeFragment(Temp_ques_answer tmp,int count,String name_of_test)
	{
		test_name=name_of_test;
		get_data=tmp;
		this.count=count;
		count_q_b="/"+count;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		rootView= inflater.inflate(R.layout.fragment_home, container, false);
		String[] test_name1=test_name.split("-");
	typeFace=Typeface.createFromAsset(getActivity().getAssets(), "fonts/saumil_guj2.ttf");
		
		title_test=(TextView)rootView.findViewById(R.id.title_text);
		title_test.setText(test_name1[0]+"-"+test_name1[1]);
		start_exam=(TextView)rootView.findViewById(R.id.start_exam);
		back_home=(TextView)rootView.findViewById(R.id.back_home);
		
		start_exam.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				start_exam.setVisibility(start_exam.GONE);
				back_home.setVisibility(back_home.GONE);
				initialize();
				click_methods();
			}
		});
		
		back_home.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			remove_me();
			}
		});
		time_count=(Chronometer)rootView.findViewById(R.id.chronometer1);
		mp= MediaPlayer.create(getActivity(), R.raw.button_sound);
		random_ques=new Random();

	
		
		return rootView;

	}
	
	void click_methods()
	{

		
		ans1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isRight= isRightAnswer(1);
				if(isRight==true)
				{
					ans1.setBackgroundResource(R.drawable.ans_pressed);
					ans1.setTextColor(getResources().getColor(R.color.list_item_title));
					attempted++;
					right_answer++;
					right_ans.setText(right_answer+"");
					ans1.setSoundEffectsEnabled(false);
					mp.start();
					ans2.setEnabled(false);
					ans3.setEnabled(false);
					ans1.setEnabled(false);
					ans4.setEnabled(false);

					new abc().execute("abc");

				}
				else
				{
					ans1.setBackgroundResource(R.drawable.ans_bg_pressed);
					ans1.setTextColor(getResources().getColor(R.color.list_item_title));
					attempted++;
					wrong_answer++;
					mp.start();
					wrong_ans.setText(wrong_answer+"");
					ans2.setEnabled(false);
					ans3.setEnabled(false);
					ans1.setEnabled(false);
					ans4.setEnabled(false);

					String abc="ans"+right_answer;
					set_right_ans();
					new abc().execute("abc");
				}
			}
		});
		ans2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isRight= isRightAnswer(2);
				if(isRight==true)
				{
					ans2.setBackgroundResource(R.drawable.ans_pressed);
					ans2.setTextColor(getResources().getColor(R.color.list_item_title));
					attempted++;
					right_answer++;
					right_ans.setText(right_answer+"");
					mp.start();
					ans2.setEnabled(false);
					ans3.setEnabled(false);
					ans1.setEnabled(false);
					ans4.setEnabled(false);
					new abc().execute("abc");
				}
				else
				{
					ans2.setBackgroundResource(R.drawable.ans_bg_pressed);
					ans2.setTextColor(getResources().getColor(R.color.list_item_title));
					attempted++;
					wrong_answer++;
					wrong_ans.setText(wrong_answer+"");
					ans2.setEnabled(false);
					ans3.setEnabled(false);
					ans1.setEnabled(false);
					ans4.setEnabled(false);
					new abc().execute("abc");
					mp.start();
					set_right_ans();
				}
			}
		});

		ans3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isRight= isRightAnswer(3);
				if(isRight==true)
				{
					ans3.setBackgroundResource(R.drawable.ans_pressed);
					ans3.setTextColor(getResources().getColor(R.color.list_item_title));
					attempted++;
					right_answer++;
					right_ans.setText(right_answer+"");
					mp.start();
					ans2.setEnabled(false);
					ans3.setEnabled(false);
					ans1.setEnabled(false);
					ans4.setEnabled(false);

					new abc().execute("abc");
				}
				else
				{
					ans3.setBackgroundResource(R.drawable.ans_bg_pressed);
					ans3.setTextColor(getResources().getColor(R.color.list_item_title));
					attempted++;
					wrong_answer++;
					wrong_ans.setText(wrong_answer+"");
					ans2.setEnabled(false);
					ans3.setEnabled(false);
					ans1.setEnabled(false);
					ans4.setEnabled(false);

					mp.start();
					new abc().execute("abc");
					set_right_ans();

				}
			}
		});
		ans4.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean isRight= isRightAnswer(4);
				if(isRight==true)
				{
					ans4.setBackgroundResource(R.drawable.ans_pressed);
					ans4.setTextColor(getResources().getColor(R.color.list_item_title));
					
					attempted++;
					right_answer++;
					right_ans.setText(right_answer+"");
					ans2.setEnabled(false);
					ans3.setEnabled(false);
					ans1.setEnabled(false);
					ans4.setEnabled(false);
					new abc().execute("abc");
					mp.start();

				}
				else
				{
					ans4.setBackgroundResource(R.drawable.ans_bg_pressed);
					ans4.setTextColor(getResources().getColor(R.color.list_item_title));
					attempted++;
					wrong_answer++;
					wrong_ans.setText(wrong_answer+"");
					ans2.setEnabled(false);
					ans3.setEnabled(false);
					ans1.setEnabled(false);
					ans4.setEnabled(false);

					new abc().execute("abc");
					mp.start();
					set_right_ans();
				}

			}
		});
		statistics.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Statistics_fragment stat=new Statistics_fragment();
				if (stat != null) {
					remove_me();
				FragmentManager fm=getFragmentManager();
				FragmentTransaction fragmentTransaction = fm.beginTransaction();
			    fragmentTransaction.replace(R.id.frame_container, stat);
			    fragmentTransaction.commit();
			    

				}
				}
		});
		skip_ques.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				ans2.setEnabled(false);
				ans3.setEnabled(false);
				ans1.setEnabled(false);
				ans4.setEnabled(false);
				new abc().execute("abc");
				  // animation duration
			     //   animation.setRepeatCount(5);  // animation repeat count
			    //    animation.setRepeatMode(2);   // repeat animation (left to right, right to left )
//			      animation.setFillAfter(true);     
			 
			        
				mp.start();
			}
		});
	}
	
	void remove_me()
	{
		getActivity().getFragmentManager().beginTransaction().remove(this).commit();
	}
	boolean isRightAnswer(int ans)
	{
		if(ans==Integer.parseInt(record[6]))
		{ 
			return true;
		}
		return false;
	}
	public class abc extends AsyncTask<String, Integer, String>
	{

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			next_question();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

	}
	void set_right_ans()
	{
		if(Integer.parseInt(record[6])==1)
		{
			ans1.setBackgroundResource(R.drawable.ans_pressed);
			ans1.setTextColor(getResources().getColor(R.color.list_item_title));
		}
		else if(Integer.parseInt(record[6])==2)
		{
			ans2.setBackgroundResource(R.drawable.ans_pressed);
			ans2.setTextColor(getResources().getColor(R.color.list_item_title));
		}
		else if(Integer.parseInt(record[6])==3)
		{
			ans3.setBackgroundResource(R.drawable.ans_pressed);
			ans3.setTextColor(getResources().getColor(R.color.list_item_title));
		}
		else if(Integer.parseInt(record[6])==4)
		{
			ans4.setBackgroundResource(R.drawable.ans_pressed);
			ans4.setTextColor(getResources().getColor(R.color.list_item_title));
		}
	}
	void initialize()
	{

	
		question=(TextView)rootView.findViewById(R.id.question);
		right_ans=(TextView)rootView.findViewById(R.id.rightAns);
		wrong_ans=(TextView)rootView.findViewById(R.id.wrongAns);
		ans1=(TextView)rootView.findViewById(R.id.ans1);
		ans2=(TextView)rootView.findViewById(R.id.ans2);
		ans3=(TextView)rootView.findViewById(R.id.ans3);
		ans4=(TextView)rootView.findViewById(R.id.ans4);
		counter=(TextView)rootView.findViewById(R.id.q_a_counter);
		statistics=(Button)rootView.findViewById(R.id.button1);
		db_helper=new Database_helper(getActivity());
		sqdb=db_helper.getWritableDatabase();
		skip_ques=(ImageView)rootView.findViewById(R.id.skip_ques);
		ans1.setSoundEffectsEnabled(false);
		ans2.setSoundEffectsEnabled(false);
		ans3.setSoundEffectsEnabled(false);
		ans4.setSoundEffectsEnabled(false);
		
		if(count>0)
		{high_random=count;
		int r=random_ques.nextInt(high_random-1);
		size_question_list=question_attempted_list.size();
		question_attempted_list.add(r);

			question.setVisibility(question.VISIBLE);
			ans1.setVisibility(ans1.VISIBLE);
			ans2.setVisibility(ans2.VISIBLE);
			ans3.setVisibility(ans3.VISIBLE);
			ans4.setVisibility(ans4.VISIBLE);
			skip_ques.setVisibility(skip_ques.VISIBLE);
			statistics.setVisibility(statistics.VISIBLE);
			time_count.setVisibility(time_count.VISIBLE);
			counter.setVisibility(counter.VISIBLE);
			right_ans.setVisibility(counter.VISIBLE);
			wrong_ans.setVisibility(counter.VISIBLE);
			record=get_data.get_values(r);
			question.setText(record[1]);
			ans1.setText(record[2]);
			ans2.setText(record[3]);
			ans3.setText(record[4]);
			ans4.setText(record[5]);
			counter.setText((pos+1)+count_q_b);
			pos=pos+1;
			attempted++;
			time_count.start();
		}
		else
		{
			Toast.makeText(getActivity(), "No Record Found", Toast.LENGTH_LONG).show();
			question.setVisibility(question.INVISIBLE);
			ans1.setVisibility(ans1.INVISIBLE);
			ans2.setVisibility(ans2.INVISIBLE);
			ans3.setVisibility(ans3.INVISIBLE);
			ans4.setVisibility(ans4.INVISIBLE);
			counter.setVisibility(counter.INVISIBLE);
			right_ans.setVisibility(counter.INVISIBLE);
			wrong_ans.setVisibility(counter.INVISIBLE);
			time_count.setVisibility(time_count.INVISIBLE);
			statistics.setVisibility(statistics.INVISIBLE);
			skip_ques.setVisibility(skip_ques.INVISIBLE);
		}
		//Toast.makeText(getActivity(),record[1], Toast.LENGTH_LONG).show();
	}
	void next_question()
	{ 
	


		
		if(pos<count)
		{
			int r=random_ques.nextInt(high_random);
			System.out.println("generated random number:--"+r);
			r= question_asked_before(r);
			if(r<count)
			{
				record=get_data.get_values(r);
				question.setText(record[1]);
				ans1.setText(record[2]);
				ans2.setText(record[3]);
				ans3.setText(record[4]);
				ans4.setText(record[5]);
				counter.setText((pos+1)+count_q_b);
				pos=pos+1;
				ans4.setEnabled(true);
				ans2.setEnabled(true);
				ans3.setEnabled(true);
				ans1.setEnabled(true);
				ans1.setBackgroundResource(R.drawable.ans_normal);
				//	ans1.setBackground(R.drawable.ans_normal);
				ans2.setBackgroundResource(R.drawable.ans_normal);
				ans3.setBackgroundResource(R.drawable.ans_normal);
				ans4.setBackgroundResource(R.drawable.ans_normal);
				ans1.setTextColor(getResources().getColor(R.color.text_color));
				ans2.setTextColor(getResources().getColor(R.color.text_color));
				ans3.setTextColor(getResources().getColor(R.color.text_color));
				ans4.setTextColor(getResources().getColor(R.color.text_color));
			}
		}
		else
		{
			System.out.println("attempted-=-=-=-=-=-=-=-="+attempted);
			String a=time_count.getText().toString();
			time_count.stop();
			String[] min=a.split(":");
			String minutes=min[0];
			int total_seconds=Integer.parseInt(minutes)*60;
			total_seconds=total_seconds+Integer.parseInt(min[1]);
			total_seconds=total_seconds-((attempted-1)*2);
			attempted=attempted-1;
			float succ_ratio=(right_answer*100)/attempted;
			float succ_ratio_ans=(75*succ_ratio)/100;
			Toast.makeText(getActivity(),"succ ratio:--- "+succ_ratio_ans, Toast.LENGTH_LONG).show();

			float time_ratio=(total_seconds*100)/(attempted*10);
			time_ratio=100-time_ratio;
			System.out.println(time_ratio);
			System.out.println(succ_ratio);
			System.out.println(succ_ratio_ans);
			float time_ratio_succ=(25*time_ratio)/100;
			float final_ratio=succ_ratio_ans+time_ratio_succ;
			question.setVisibility(question.INVISIBLE);
		
			ans1.setVisibility(ans1.INVISIBLE);
			ans2.setVisibility(ans2.INVISIBLE);
			ans3.setVisibility(ans3.INVISIBLE);
			ans4.setVisibility(ans4.INVISIBLE);
			counter.setVisibility(counter.INVISIBLE);
			right_ans.setVisibility(counter.INVISIBLE);
			wrong_ans.setVisibility(counter.INVISIBLE);
			time_count.setVisibility(time_count.INVISIBLE);
		
			String[] insert_column={"name_test","success_ratio","time_test","succ_time","succ_answer","total_question","attempted","right_ans","wrong_ans"};
			String[] values={test_name,final_ratio+"",total_seconds+"",time_ratio+"",succ_ratio_ans+"",count+"",attempted+"",right_answer+"",wrong_answer+""};
			db_helper.insert_data(sqdb, "user_statistics", insert_column, values);
			System.out.println("time:-"+time_ratio_succ+" ans:-"+succ_ratio_ans+ " succ ratio:--"+final_ratio);
			Toast.makeText(getActivity(),"time:-"+time_ratio_succ+" ans:-"+succ_ratio_ans+ " succ ratio:--"+final_ratio, Toast.LENGTH_LONG).show();
			Toast.makeText(getActivity(),attempted + total_seconds +"  No more Questions", Toast.LENGTH_LONG).show();
		}
	}
	int question_asked_before(int r)
	{
		for(int i=0;i<i+1;i++)
		{
			r=random_ques.nextInt(high_random);
			if(!question_attempted_list.contains(r))
			{
				question_attempted_list.add(r);
				System.out.println("selected random number:---  "+r);
				return r;
			}
		}
		return r;
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		
		try{
		db_helper.close();
		}
		catch(Exception e)
		{
			
		}
		System.out.println("onPause");
		super.onPause();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try{
			db_helper.close();
			}
			catch(Exception e)
			{
				
			}
		System.out.println("onDestroy");
	}
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		try{
			db_helper.close();
			}
			catch(Exception e)
			{
				
			}
		System.out.println("onDetach");
	}
	
}
