package pricus.aiyaz.exampreparation.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import pricus.aiyaz.exampreparation.MainActivity;
import pricus.aiyaz.exampreparation.database_helper.Database_helper;

public class Json_data  {

	private String http_link;
	private String table_name;
	InputStream is;
	Database_helper db_helper;
	Context context;
	SQLiteDatabase sqlite_db;
	
	String[] column_name_qa_table={"question_id","question","answer1","answer2","answer3","answer4","right_answer","question_type"};
	String[] Data_type={"varchar","varchar","varchar","varchar","varchar","varchar","varchar","varchar"};
	
	public Json_data(String http_link,String table_name,Context context) {
		// TODO Auto-generated constructor stub
		this.http_link=http_link;
		this.table_name=table_name;
		this.context=context;
		db_helper=new Database_helper(context);
		sqlite_db=db_helper.getWritableDatabase();
		
	}
	
	public String http_req_function()
	{
		HttpClient http_client=new DefaultHttpClient();
		HttpGet http_get=new HttpGet(http_link);
		
		try {
			HttpResponse htp_resp=http_client.execute(http_get);
			HttpEntity htp_entity=htp_resp.getEntity();
			is = htp_entity.getContent();
			JSONObject jobj=new JSONObject();
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
			StringBuilder sb=new StringBuilder();
			String line;
			 while ((line = br.readLine()) != null) {
		            sb.append(line + "\n");
		        }
			is.close();
			try {
				JSONArray jarray=new JSONArray(sb.toString());
				JSONObject json_data=new JSONObject();
				String[] column_name={"id","title","parentid"};
				for (int i = 0; i < jarray.length(); i++) {
					json_data=jarray.getJSONObject(i);
					String id=json_data.getString("id");
					String title=json_data.getString("title");
					String parentid=json_data.getString("parentid");
					String[] temp_value_insert={id,title,parentid};
					db_helper.insert_data(sqlite_db, table_name, column_name, temp_value_insert);
				}
				System.out.println(json_data);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "hello";
	}
	
	public String http_req_crete_table()
	{
		HttpClient http_client=new DefaultHttpClient();
		HttpGet http_get=new HttpGet(http_link);
		db_helper.create_table(sqlite_db, table_name, column_name_qa_table, Data_type);
		try {
			HttpResponse htp_resp=http_client.execute(http_get);
			HttpEntity htp_entity=htp_resp.getEntity();
			is = htp_entity.getContent();
			JSONObject jobj=new JSONObject();
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
			StringBuilder sb=new StringBuilder();
			String line;
			 while ((line = br.readLine()) != null) {
		            sb.append(line + "\n");
		        }
			is.close();
			try {
				JSONArray jarray=new JSONArray(sb.toString());
				JSONObject json_data=new JSONObject();
				//String[] column_name={"id","title","parentid"};
				for (int i = 0; i < jarray.length(); i++) {
					json_data=jarray.getJSONObject(i);
					String question_id=json_data.getString("question_id");
					String question=json_data.getString("question");
					String answer1=json_data.getString("answer1");
					String answer2=json_data.getString("answer2");
					String answer3=json_data.getString("answer3");
					String answer4=json_data.getString("answer4");
					String right_answer=json_data.getString("right_answer");
					String question_type=json_data.getString("question_type");
					String[] temp_value_insert={question_id,question,answer1,answer2,answer3,answer4,right_answer,question_type};
					db_helper.insert_data(sqlite_db, table_name, column_name_qa_table, temp_value_insert);
					System.out.println(question_id+" "+question_type);
				}
				System.out.println(json_data);
				return "inserted-"+table_name;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String http_notification_function()
	{
		int notifications=0;
		HttpClient http_client=new DefaultHttpClient();
		HttpGet http_get=new HttpGet(http_link);
		
		try {
			HttpResponse htp_resp=http_client.execute(http_get);
			HttpEntity htp_entity=htp_resp.getEntity();
			is = htp_entity.getContent();
			JSONObject jobj=new JSONObject();
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
			StringBuilder sb=new StringBuilder();
			String line;
			 while ((line = br.readLine()) != null) {
		            sb.append(line + "\n");
		        }
			is.close();
			try {
			
				JSONArray jarray=new JSONArray(sb.toString());
				JSONObject json_data=new JSONObject();
				String[] column_name={"notification_id","notification_msg"};
			
				for (int i = 0; i < jarray.length(); i++) {
					json_data=jarray.getJSONObject(i);
					String id=json_data.getString("notification_id");
					String message=json_data.getString("notification_msg");
					System.out.println("id:----"+id+" message:-----"+message);
					String[] temp_value_insert={id,message};
					notifications=i+1;
					db_helper.insert_data(sqlite_db, table_name, column_name, temp_value_insert);
				}
				System.out.println(json_data);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return "hello-"+notifications;
	}
	


}
