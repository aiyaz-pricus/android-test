package pricus.aiyaz.exampreparation;


import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Custom_list_adapter extends ArrayAdapter<String> {

	private final Activity context;
	private final ArrayList<String> name;
	private final ArrayList<String> score;
	private final ArrayList<String> performance;
	
	public Custom_list_adapter(Activity context,ArrayList<String> name, ArrayList<String> score,ArrayList<String> performance) {

		super(context,R.layout.custom_list_view, name);
		this.context = context;
		this.name = name;
		this.score = score;
		this.performance = performance;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		LayoutInflater inflater = context.getLayoutInflater();
		View rowView= inflater.inflate(R.layout.custom_list_view, null, true);
		TextView txtTitle = (TextView) rowView.findViewById(R.id.textView1);
		TextView txtview = (TextView) rowView.findViewById(R.id.textView2);
		ImageView imgview=(ImageView)rowView.findViewById(R.id.imageView1);
		txtTitle.setText(name.get(position));
		txtview.setText(score.get(position));
		if(performance.get(position).equals("up"))
		{
			imgview.setRotation(270);
		}
		else
		{
			imgview.setRotation(90);
		}
		
		return rowView;
	}


}
