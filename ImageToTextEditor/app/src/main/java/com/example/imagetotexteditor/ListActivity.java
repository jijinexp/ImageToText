// 159336
//Assignment 3: Image To Text Editor
//Student ID:18032679
//Name:Jijin Johney Mundappallil

package com.example.imagetotexteditor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.appcompat.widget.PopupMenu;
import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {
    private ArrayList<Note> notes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        //Retrieve the notes from the database & set the ListView adapter
        final DBHandler db = new DBHandler(getApplicationContext());
        // get all notes
        notes = db.getAllNotes();
        ImageButton newEntry = findViewById(R.id.add);
        ImageButton ToCamera = findViewById(R.id.camera_option);
        ListView mList = findViewById(R.id.notepad_listview);
        getSupportActionBar().hide();
        //Populate all notes using Adapter
        final NoteAdapter noteAdapter = new NoteAdapter(this, notes);
        mList.setAdapter(noteAdapter);
        if (isFirstTime()) {
            StartDialog(ListActivity.this);
        }
        //StartDialog(ListActivity.this);
        //Set on click listener which starts the Choose from in-built gallery activity
        newEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ImageActivity = new Intent(ListActivity.this, MainActivity.class);
                startActivity(ImageActivity);
            }
        });
        ToCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ToCameraActivity = new Intent(ListActivity.this, CameraActivity.class);
                startActivity(ToCameraActivity);
            }
        });
        // access previously saved notes
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendEditIntent(position);
            }
        });
        // long press to edit or delete note
        mList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                //Inflate view
                PopupMenu popup = new PopupMenu(ListActivity.this, view);
                popup.getMenuInflater().inflate(R.menu.list_menu, popup.getMenu());
                popup.setGravity(Gravity.END);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        //Gets selected title

                        switch (item.getItemId()) {
                            case R.id.menuEdit:
                                sendEditIntent(position);
                                break;
                            case R.id.menuDelete:
                                db.deleteNote(notes.get(position).getId());
                                notes.remove(position);
                                noteAdapter.notifyDataSetChanged();
                                break;
                            default:
                                break;
                        }

                        return false;
                    }
                });
                popup.show();
                return true;
            }
        });



    }
    private boolean isFirstTime()
    {
        final SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        final boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.apply();
        }
        return ranBefore;
    }
    // Access and edit Existing notes
    private void sendEditIntent(int position) {
        Intent updateNoteActivity = new Intent(ListActivity.this, EditorActivity.class);
        updateNoteActivity.putExtra("option", "update");
        updateNoteActivity.putExtra("id", notes.get(position).getId());
        updateNoteActivity.putExtra("name", notes.get(position).getName());
        updateNoteActivity.putExtra("message", notes.get(position).getMessage());
        startActivity(updateNoteActivity);
    }
    private void StartDialog(Context context) {
        final CharSequence[] options = { "Ok" };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Click on ImageToText icon to create new note or Edit existing notes");


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
