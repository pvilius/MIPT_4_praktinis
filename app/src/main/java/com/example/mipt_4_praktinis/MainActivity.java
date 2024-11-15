package com.example.mipt_4_praktinis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> listNoteTitles = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView lvNotes;
    TextView tvNoNotesMessage;
    Map<String, String> notesMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.logMessage("onCreate called");

        this.lvNotes = findViewById(R.id.lvNotes);
        this.tvNoNotesMessage = findViewById(R.id.tvNoNotes);
        this.adapter = new ArrayAdapter<>(this, R.layout.list_item, this.listNoteTitles);
        this.lvNotes.setAdapter(adapter);

        lvNotes.setOnItemClickListener((adapterView, view, position, id) -> {
            String selectedTitle = listNoteTitles.get(position);
            String fullContent = notesMap.get(selectedTitle);
            Intent intent = new Intent(MainActivity.this, ViewEditNoteActivity.class);
            intent.putExtra(Constants.NOTE_TITLE, selectedTitle);
            intent.putExtra(Constants.NOTE_CONTENT, fullContent);
            startActivity(intent);
            Log.logMessage("Item clicked: " + selectedTitle);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.logMessage("onCreateOptionsMenu called");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_options_menu, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.logMessage("onStart called");

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> savedTitles = sharedPref.getStringSet(Constants.NOTES_TITLES_KEY, null);
        Map<String, ?> allNotes = sharedPref.getAll();

        if (savedTitles != null) {
            this.listNoteTitles.clear();
            this.notesMap.clear();
            this.listNoteTitles.addAll(savedTitles);
            for (String title : savedTitles) {
                if (allNotes.containsKey(title)) {
                    notesMap.put(title, sharedPref.getString(title, ""));
                }
            }
            this.adapter.notifyDataSetChanged();
            checkIfListIsEmpty();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.logMessage("onOptionsItemSelected called");

        if (item.getItemId() == R.id.add_note) {
            Log.logMessage("Add note selected");
            Intent i = new Intent(this, AddNoteActivity.class);
            startActivityForResult(i, 1);
            return true;
        } else if (item.getItemId() == R.id.delete_note) {
            Log.logMessage("Delete note selected");
            Intent i = new Intent(this, DeleteNoteActivity.class);
            startActivity(i);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.logMessage("onActivityResult called");

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String noteTitle = data.getStringExtra(Constants.NOTE_TITLE);
            String noteContent = data.getStringExtra(Constants.NOTE_CONTENT);

            if (noteTitle != null && noteContent != null) {
                Log.logMessage("New note added: " + noteTitle);
                listNoteTitles.add(noteTitle);
                notesMap.put(noteTitle, noteContent);
                adapter.notifyDataSetChanged();

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = sharedPref.edit();
                Set<String> updatedTitlesSet = new HashSet<>(listNoteTitles);
                editor.putStringSet(Constants.NOTES_TITLES_KEY, updatedTitlesSet);
                editor.putString(noteTitle, noteContent);
                editor.apply();

                checkIfListIsEmpty();
            }
        }
    }

    private void checkIfListIsEmpty() {
        Log.logMessage("checkIfListIsEmpty called");

        if (listNoteTitles.isEmpty()) {
            tvNoNotesMessage.setVisibility(View.VISIBLE);
            lvNotes.setVisibility(View.GONE);
        } else {
            tvNoNotesMessage.setVisibility(View.GONE);
            lvNotes.setVisibility(View.VISIBLE);
        }
    }
}
