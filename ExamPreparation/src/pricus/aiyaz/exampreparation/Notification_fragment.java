package pricus.aiyaz.exampreparation;

import java.util.ArrayList;
import java.util.zip.Inflater;

import pricus.aiyaz.exampreparation.database_helper.Database_helper;
import android.app.Fragment;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnGenericMotionListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class Notification_fragment extends Fragment {

	View rootview;
	Database_helper db_helper;
	SQLiteDatabase sqdb;
	ArrayList<String> notification_msg;
	ListView  list;
	ArrayList<String> notification_id;
	int item_id=0;
	ImageView img_back;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootview=inflater.inflate(R.layout.notification_list, container,false);
		notification_msg=new ArrayList<String>();
		notification_id=new ArrayList<String>();
	
		db_helper=new Database_helper(getActivity());
		sqdb=db_helper.getWritableDatabase();
		System.out.println("helloooo");
		select_data();
	

		list.setOnTouchListener(new OnSwipeTouchListener(getActivity()) {
		    @Override
		    public void onSwipeLeft() {
		        // Whatever
		    	System.out.println("swipiiing leftttttttt");
		    	System.out.println();	
		    }
		    
			@Override
			public void onSwipeRight(int x,int y) {
				// TODO Auto-generated method stub
				super.onSwipeRight(x,y);
				int i= (int) list.pointToRowId(x, y);
				System.out.println("swipiiing righttt");
				System.out.println("item id:------"+i);
				System.out.println(notification_msg.get(i));
				System.out.println(notification_id.get(i));
				
				sqdb.execSQL("delete from notification_table where notification_id="+notification_id.get(i));
				notification_id.clear();
				notification_msg.clear();
				select_data();
			}	    
		});
	
		list.setOnGenericMotionListener(new OnGenericMotionListener() {
			
			@Override
			public boolean onGenericMotion(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				System.out.println(list.getSelectedItemPosition());
				return false;
			}
		});
		
		return rootview;
	}
	void select_data()
	{
		Cursor c= db_helper.select_data(sqdb, "notification_table");
		System.out.println("count:-----"+c.getCount());
		if(c.getCount()>0)
		{	int count=c.getCount();
			c.moveToLast();
			Double temp=0.0;
			Double ratio;
			while(count>0)
			{ 
				System.out.println(c.getString(0)+" "+c.getString(1));
				notification_id.add(c.getString(0));
				notification_msg.add(c.getString(1));
				//Toast.makeText(getActivity(), c.getString(1), Toast.LENGTH_LONG).show();
	 			c.moveToPrevious();
				count--;
			}
		}
		else
		{
			System.out.println("You Don't have any new notification pending.");
		}
		
		Custom_notification_list abc=new Custom_notification_list(getActivity(), notification_id, notification_msg);
		
		  list=(ListView)rootview.findViewById(R.id.notification_list);
		list.setAdapter(abc);
	}
}