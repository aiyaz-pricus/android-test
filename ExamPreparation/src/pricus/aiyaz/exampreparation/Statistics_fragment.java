package pricus.aiyaz.exampreparation;

import java.util.ArrayList;

import pricus.aiyaz.exampreparation.database_helper.Database_helper;
import android.app.Fragment;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class Statistics_fragment extends Fragment {
	
	View rootview;
	Database_helper db_helper;
	SQLiteDatabase sqdb;
	ArrayList<String> test_name;
	ArrayList<String> test_score;
	ArrayList<String> performance;
	ImageView img_back;
	public Statistics_fragment() {
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview=inflater.inflate(R.layout.statistics_layout_fragment, container,false);
		test_name=new ArrayList<String>();
		test_score=new ArrayList<String>();
		performance=new ArrayList<String>();
		img_back=(ImageView)rootview.findViewById(R.id.back_img);
		db_helper=new Database_helper(getActivity());
		sqdb=db_helper.getWritableDatabase();
		System.out.println("helloooo");
		getActivity().getActionBar().hide();
		
		Cursor c= db_helper.select_data(sqdb, "user_statistics");
		System.out.println("count:-----"+c.getCount());
		if(c.getCount()>0)
		{	int count=c.getCount();
			c.moveToFirst();
			Double temp=0.0;
			Double ratio;
			while(count>0)
			{ 
				ratio=Double.parseDouble(c.getString(1));
				if(c.getCount()==count)
				{
					 temp=ratio;
					 performance.add("up");
				}
				else
				{
					if(temp<ratio)
					{
						 performance.add("up");
						 temp=ratio;
					}
					else
					{
						 performance.add("down");
						 temp=ratio;
					}
				}
				System.out.println(c.getString(0)+" "+c.getString(1));
				test_name.add(c.getString(0));
				test_score.add(c.getString(1));
				//Toast.makeText(getActivity(), c.getString(1), Toast.LENGTH_LONG).show();
				c.moveToNext();
				count--;
			}
		}
		Custom_list_adapter abc=new Custom_list_adapter(getActivity(), test_name, test_score,performance);
		ListView  list=(ListView)rootview.findViewById(R.id.listView1);
		list.setAdapter(abc);
		
		img_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				frag_finish();
			}
		});
		
		return rootview;
	}
	void frag_finish()
	{
		getActivity().getActionBar().show();
		getActivity().getFragmentManager().beginTransaction().remove(this).commit();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		db_helper.close();
		System.out.println("onDestroy");
		
	}

	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		super.onDetach();
		db_helper.close();
		System.out.println("onDetach");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		db_helper.close();
		System.out.println("onPause");
	}
	
	
}
