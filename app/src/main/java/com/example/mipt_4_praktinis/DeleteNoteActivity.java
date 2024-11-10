package com.example.mipt_4_praktinis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DeleteNoteActivity extends AppCompatActivity {

    ListView lvDeleteNotes;
    Button btnDeleteSelected;
    ArrayList<String> noteTitles;
    ArrayAdapter<String> adapter;
    TextView tvNoNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        lvDeleteNotes = findViewById(R.id.lvDeleteNotes);
        btnDeleteSelected = findViewById(R.id.btnDeleteSelected);
        tvNoNotes = findViewById(R.id.tvNoNotes);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> savedTitles = sharedPref.getStringSet(Constants.NOTES_TITLES_KEY, null);

        if (savedTitles != null) {
            noteTitles = new ArrayList<>(savedTitles);
        } else {
            noteTitles = new ArrayList<>();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, noteTitles);
        lvDeleteNotes.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        lvDeleteNotes.setAdapter(adapter);

        updateEmptyViewVisibility();

        btnDeleteSelected.setOnClickListener(v -> deleteSelectedNotes(sharedPref));
    }

    private void deleteSelectedNotes(SharedPreferences sharedPref) {
        SharedPreferences.Editor editor = sharedPref.edit();

        ArrayList<String> selectedNotes = new ArrayList<>();
        for (int i = 0; i < lvDeleteNotes.getCount(); i++) {
            if (lvDeleteNotes.isItemChecked(i)) {
                selectedNotes.add(noteTitles.get(i));
            }
        }

        noteTitles.removeAll(selectedNotes);
        editor.putStringSet(Constants.NOTES_TITLES_KEY, new HashSet<>(noteTitles));

        for (String title : selectedNotes) {
            editor.remove(title);
        }

        editor.apply();

        adapter.notifyDataSetChanged();
        updateEmptyViewVisibility();
    }

    private void updateEmptyViewVisibility() {
        if (noteTitles.isEmpty()) {
            lvDeleteNotes.setVisibility(View.GONE);
            tvNoNotes.setVisibility(View.VISIBLE);
        } else {
            lvDeleteNotes.setVisibility(View.VISIBLE);
            tvNoNotes.setVisibility(View.GONE);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.notes_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(DeleteNoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
