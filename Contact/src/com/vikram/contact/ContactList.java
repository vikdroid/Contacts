package com.vikram.contact;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ContactList extends ListActivity {
	public static final String ROW_ID = "row_id";
	private ListView contLv;
	private CursorAdapter contA;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contLv=getListView();
		contLv.setOnItemClickListener(viewContListener);
		String[] from = new String[] {"name"};
		int[] to = new int[] {R.id.contactTextView };
		contA = new SimpleCursorAdapter(ContactList.this, R.layout.contact_list, null, from, to);
		setListAdapter(contA);
		
	}
	@Override
	protected void onResume()
	{
		super.onResume();
		new GetContacts().execute((Object[]) null);
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onStop()
	{
		Cursor cursor = contA.getCursor();
		if(cursor != null)
			cursor.deactivate();
		contA.changeCursor(null);
		super.onStop();
	}
	
	private class GetContacts extends AsyncTask<Object, Object, Cursor>
	{
		DatabaseConnector dbConnector = new DatabaseConnector(ContactList.this);
		@Override
		protected Cursor doInBackground(Object... params)
		{
			dbConnector.open();
			return dbConnector.getAllContacts();
		}
		
		protected void onPostExecute(Cursor result)
		{
			contA.changeCursor(result);
			dbConnector.close();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.contact_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		Intent addContact = new Intent(ContactList.this, Contact.class);
		startActivity(addContact);
		return super.onOptionsItemSelected(item);
	}
	
	OnItemClickListener viewContListener = new OnItemClickListener()
	{
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
		{
			Intent viewCont = new Intent(ContactList.this, ViewContact.class);
			viewCont.putExtra(ROW_ID, arg3);
			startActivity(viewCont);
		}
	};
}
