package pricus.aiyaz.exampreparation.database_helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class Database_helper extends SQLiteOpenHelper {
	private static String db_name="Exam_preparation_SQLitedb";
	private static String menu_table="menu_table";	
	public Database_helper(Context context) {
		super(context, db_name, null, 1);
		System.out.println("created datab");
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("On create");
		//db.execSQL("create");
	}
	
	public void create_table(SQLiteDatabase db,String table_name,String[] column_name,String[] datatype) throws SQLException
	{
		System.out.println("inside create table");
		int count=column_name.length;
		StringBuilder sb=new StringBuilder();
		String create_table_query="CREATE TABLE IF NOT EXISTS "+table_name+"(";
		for(int i=0;i<count;i++)
		{
			if(i==count-1)
			{
				sb.append(column_name[i]+" "+datatype[i]+");");
				create_table_query=create_table_query+sb.toString();
			}
			else
			{
				sb.append(column_name[i]+" "+datatype[i]+",");
			}
		}
		System.out.println(create_table_query);
		db.execSQL(create_table_query);
		System.out.println("table created......");
	}

	public void insert_data(SQLiteDatabase db,String table_name,String[] column_name,String[] values)
	{
			System.out.println("inserting values.....");
			ContentValues contentValue=new ContentValues();
			for (int i = 0; i < column_name.length; i++) {
				contentValue.put(column_name[i], values[i]);
			}
			db.insert(table_name, null, contentValue);
	}

	public Cursor select_data(SQLiteDatabase db,String table_name)
	{
		return (db.rawQuery("select * from "+table_name, null));
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public synchronized void close() {
		// TODO Auto-generated method stub
		super.close();
	}

}
