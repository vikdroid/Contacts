package com.vikram.contact;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseConnector extends Activity {
	private static final String DB_NAME = "Contacts";
	private SQLiteDatabase database;
	private DatabaseOpenHelper dbOpenHelper;
	public DatabaseConnector(Context context) {
		dbOpenHelper = new DatabaseOpenHelper(context, DB_NAME, null, 1);
	}
	
	public void open() throws SQLException
	{
		database = dbOpenHelper.getWritableDatabase();
	}
	
	public void close()
	{
		if(database != null)
			database.close();
	}
	
	public void insertContact(String name, String add, String mob)
	{
		ContentValues newCont = new ContentValues();
		newCont.put("name", name);
		newCont.put("add", add);
		newCont.put("mob", mob);
		open();
		database.insert("contact", null, newCont);
	}
	
	public void updateContact(long id, String name, String add, String mob)
	{
		ContentValues editCont = new ContentValues();
		editCont.put("name", name);
		editCont.put("add", add);
		editCont.put("mob", mob);
		open();
		database.update("contact", editCont, "_id=" + id, null);
		close();
	}
	
	public Cursor getAllContacts()
	{
		return database.query("contact", new String[] {"_id", "name"}, null, null, null, null, "name");
	}
	
	public Cursor getOneContact(long id)
	{
		return database.query("contact", null, "_id=" + id, null, null, null, null);
	}
	
	public void deleteContact(long id)
	{
		open();
		database.delete("contact", "_id=" + id, null);
		close();
	}
}

