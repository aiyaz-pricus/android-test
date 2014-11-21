package pricus.aiyaz.exampreparation;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.InputSource;

import pricus.aiyaz.exampreparation.database_helper.Database_helper;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends Activity {

	Button login;
	Database_helper db_helper;
	SQLiteDatabase sqdb;
	String[] login_db_columns={"username","email","phone_no"};
	Thread t;
	ProgressDialog pdialog;
	EditText u_name,email,phone_no;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);
		db_helper=new Database_helper(getApplicationContext());
		u_name=(EditText)findViewById(R.id.user_name);
		email=(EditText)findViewById(R.id.email_add);
		phone_no=(EditText)findViewById(R.id.phone_number);
		pdialog=new ProgressDialog(Login.this);
		pdialog.setMessage("Processing");
		pdialog.setCancelable(false);
		sqdb=db_helper.getWritableDatabase();
		
		login=(Button)findViewById(R.id.login_btn);
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				pdialog.show();
				new insert_login().execute("hello");
			} 
		});
		t=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("inside inserting data...");
				
			}
		});
	}
	public class insert_login extends AsyncTask<String, Integer, String>
	{

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			String name=u_name.getText().toString();
			String email1=email.getText().toString();
			String phone=phone_no.getText().toString();
			String[] values={name,email1,phone};
			db_helper.insert_data(sqdb, "login", login_db_columns, values);
			finish();
			Intent main=new Intent(Login.this,MainActivity.class);
			startActivity(main);
			pdialog.cancel();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			insert_data();
			return null;
		}
		
	}
	void insert_data()
	{
		String url="http://192.168.1.51/henal/webservices-demo/webservices-logindatainsert.php?method=insertrecord&username="+u_name.getText().toString()+"&email="+email.getText().toString()+"&phoneno="+phone_no.getText().toString();
		System.out.println(url);
		HttpClient htpclient=new DefaultHttpClient();
		HttpGet htpget=new HttpGet(url);
		try {
			HttpResponse httpresp=htpclient.execute(htpget);
			HttpEntity htpent=httpresp.getEntity();
			StatusLine statusLine = httpresp.getStatusLine();
			if(statusLine.getStatusCode() == HttpStatus.SC_OK){
				System.out.println("Data Inserted successfully");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sqdb.close();
		db_helper.close();
		
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		sqdb.close();
		db_helper.close();
	}
	
}