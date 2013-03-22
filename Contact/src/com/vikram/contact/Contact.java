package com.vikram.contact;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Contact extends Activity {
	private long rowID;
	private EditText nameEt;
	private EditText addEt;
	private EditText mobEt;
	Button saveButton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		
		nameEt = (EditText)findViewById(R.id.nameEdit);
		addEt = (EditText)findViewById(R.id.capEdit);
		mobEt = (EditText)findViewById(R.id.codeEdit);
		
		Bundle extras = getIntent().getExtras();
		if(extras != null)
		{
			rowID = extras.getLong("row_id");
			nameEt.setText(extras.getString("name"));
			addEt.setText(extras.getString("add"));
			mobEt.setText(extras.getString("mob"));
		}
		
		saveButton = (Button)findViewById(R.id.saveBtn);
		saveButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v)
			{
				if(nameEt.getText().length() != 0)
				{
					AsyncTask<Object, Object, Object> saveContactTask = new AsyncTask<Object, Object, Object>()
					{
						@Override
						protected Object doInBackground(Object... params)
						{
							saveContact();
							return null;
						}
						
						@Override
						protected void onPostExecute(Object result)
						{
							finish();
						}
					};
					saveContactTask.execute((Object[]) null);
				}
				
				else
				{
					AlertDialog.Builder alert = new AlertDialog.Builder(Contact.this);
					alert.setTitle(R.string.errorTitle);
					alert.setTitle(R.string.errorMessage);
					alert.setPositiveButton(R.string.errorButton, null);
					alert.show();
					
				}
			}
		});
	}
	private void saveContact()
	{
		DatabaseConnector dbConnector = new DatabaseConnector(this);
		if(getIntent().getExtras() == null)
		{
			dbConnector.insertContact(nameEt.getText().toString(),
					addEt.getText().toString(), 
					mobEt.getText().toString());
		}
		else
		{
			dbConnector.updateContact(rowID, nameEt.getText().toString(), addEt.getText().toString(), mobEt.getText().toString());
		}
	}
}