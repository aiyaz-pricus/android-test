package pricus.aiyaz.exampreparation;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Custom_notification_list extends ArrayAdapter<String> {
	
	private final Activity context;
	private final ArrayList<String> notification_id;
	private final ArrayList<String> notification_message;
	int pos;
	TextView txtTitle;
	
	public Custom_notification_list(Activity context,ArrayList<String> notification_id, ArrayList<String> notification_message) {
		super(context,R.layout.custom_list_view, notification_message);
		// TODO Auto-generated constructor stub
		this.context=context;
		this.notification_id=notification_id;
		this.notification_message=notification_message;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		pos=position;
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.custom_notification_list, null, true);
		txtTitle = (TextView) rowView.findViewById(R.id.notification_message);
		ImageView imgview=(ImageView)rowView.findViewById(R.id.noti_img_close);
		txtTitle.setText(notification_message.get(position));	
		
		
	
		return rowView;
	}	
}
