// 159336
//Assignment 3: Image To Text Editor
//Student ID:18032679
//Name:Jijin Johney Mundappallil

package com.example.imagetotexteditor;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class NoteAdapter extends ArrayAdapter<Note> {
    private Activity mContext;
    private ArrayList<Note> notes;

    NoteAdapter(Activity context, ArrayList<Note> list) {
        super(context, R.layout.notepad_list_view, list);
        this.mContext = context;
        this.notes = list;
    }


    @NonNull
    public View getView(int position, View view, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.mContext.getLayoutInflater();

        if (view == null) {
            view = inflater.inflate(R.layout.notepad_list_view, parent, false);
        }

        //Set the name on Text view with Bold typeface
        ((TextView) view.findViewById(R.id.name)).setTypeface(null, Typeface.BOLD);
        ((TextView) view.findViewById(R.id.name)).setText(notes.get(position).getName());

        return view;
    }

}
