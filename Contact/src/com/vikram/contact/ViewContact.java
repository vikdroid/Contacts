package com.vikram.contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewContact extends Activity {
	private long rowID;
	private TextView nameTv;
	private TextView addTv;
	private TextView mobTv;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_contact);
		setUpViews();
		Bundle extras = getIntent().getExtras();
		rowID = extras.getLong(ContactList.ROW_ID);
	}

	private void setUpViews() {
		nameTv = (TextView)findViewById(R.id.nameText);
		addTv = (TextView)findViewById(R.id.capText);
		mobTv = (TextView)findViewById(R.id.codeText);
		
	}
	
	@Override
	protected void onResume()
	{
		super.onResume();
		new LoadContacts().execute(rowID);
	}
	
	private class LoadContacts extends AsyncTask<Long, Object, Cursor>
	{
		DatabaseConnector dbConnector = new DatabaseConnector(ViewContact.this);
		@Override
		protected Cursor doInBackground(Long... params)
		{
			dbConnector.open();
			return dbConnector.getOneContact(params[0]);
		}
		@Override
		protected void onPostExecute(Cursor result)
		{
			super.onPostExecute(result);
			result.moveToFirst();
			
			int nameIndex = result.getColumnIndex("name");
			int addIndex = result.getColumnIndex("add");
			int mobIndex = result.getColumnIndex("mob");
			nameTv.setText(result.getString(nameIndex));
			addTv.setText(result.getString(addIndex));
			mobTv.setText(result.getString(mobIndex));
			result.close();
			dbConnector.close();
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.veiw_contact_menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		switch(item.getItemId())
		{
		case R.id.editItem:
			Intent addEditContact = 
			new Intent(this, Contact.class);
			addEditContact.putExtra(ContactList.ROW_ID, rowID);
			addEditContact.putExtra("name", nameTv.getText());
			addEditContact.putExtra("add", addTv.getText());
			addEditContact.putExtra("mob", mobTv.getText());
			startActivity(addEditContact);
			return true;
		
		case R.id.deleteItem:
			deleteContact();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		
		}
	}

	private void deleteContact() {
		AlertDialog.Builder alert = new AlertDialog.Builder(ViewContact.this);
		alert.setTitle(R.string.confirmTitle);
		alert.setMessage(R.string.confirmMessage);
		alert.setPositiveButton(R.string.delete_btn,
				new DialogInterface.OnClickListener()
		{
		 public void onClick(DialogInterface dialog, int button)
		 {
			 final DatabaseConnector dbConnector =
					 new DatabaseConnector(ViewContact.this);
			 AsyncTask<Long, Object, Object> deleteTask = new AsyncTask<Long, Object, Object>()
			 {
				 protected Object doInBackground(Long... params){
					 dbConnector.deleteContact(params[0]);
					 return null;
				 }
				 
				 @Override
				 protected void onPostExecute(Object result)
				 {
					 finish();
				 }
			 };
			deleteTask.execute(new Long[] {rowID });
		 }
					
				});
		alert.setNegativeButton(R.string.cancel_btn, null).show();
	}
}
