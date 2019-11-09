// 159336
//Assignment 3: Image To Text Editor
//Student ID:18032679
//Name:Jijin Johney Mundappallil

package com.example.imagetotexteditor;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;





public class EditorActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_format);
        final ImageButton mSave= findViewById(R.id.save);
        final ImageButton mButtonShare;
        final EditText mName = findViewById(R.id.name);
        final EditText message = findViewById(R.id.text_formatting);
        final DBHandler dbHandler = new DBHandler(getApplicationContext());
        message.setTypeface(Typeface.SERIF);
        getSupportActionBar().hide();
        final Intent mIntent = getIntent();
        final String option = mIntent.getStringExtra("option");
        // Now set this value to EditText
        if (option.equals("update")) {
            //Set the values
            mName.setText(mIntent.getStringExtra("name"));
            message.setText(mIntent.getStringExtra("message"));
        }else{
            //mName.setText("Sample");
            message.setText(mIntent.getStringExtra("message"));
        }
        mButtonShare = findViewById(R.id.btnShare);
        mButtonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, message.getText().toString());
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Check if either name or message has value
                if (mName.getText().toString().isEmpty()) {
                    //Toast.makeText(EditorActivity.this, "Please add a title to your note.", Toast.LENGTH_SHORT).show();
                    add_fileName(EditorActivity.this);
                    return;
                }

                //Check if you create a new Note or Update an existing note and go back to main activity
                if (option.equals("add")) {
                    dbHandler.addNote(mName.getText().toString(), message.getText().toString());
                    Toast.makeText(EditorActivity.this, mName.getText().toString() +" saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditorActivity.this, ListActivity.class));
                } else {
                    long receivedId = mIntent.getLongExtra("id", -1);
                    dbHandler.updateNote(receivedId, mName.getText().toString(), message.getText().toString());
                    Toast.makeText(EditorActivity.this, "Changes made "+mName.getText().toString() +" saved", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(EditorActivity.this, ListActivity.class));

                }
            }
        });
    }
    private void add_fileName(Context context) {
        final CharSequence[] options = { "Ok" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Please add a title to your note.");

        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Ok")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

}







