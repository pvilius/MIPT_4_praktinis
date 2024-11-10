package com.example.mipt_4_praktinis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DeleteNoteActivity extends AppCompatActivity {

    Spinner spnNotes;
    Button btnDeleteSelected;
    ArrayList<String> noteTitles;
    ArrayAdapter<String> spinnerAdapter;
    TextView tvNoNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_note);

        Log.logMessage("DeleteNoteActivity: onCreate called");

        spnNotes = findViewById(R.id.spinnerNotes);
        btnDeleteSelected = findViewById(R.id.btnDelete);
        tvNoNotes = findViewById(R.id.tvNoNotes);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> savedTitles = sharedPref.getStringSet(Constants.NOTES_TITLES_KEY, null);

        if (savedTitles != null) {
            noteTitles = new ArrayList<>(savedTitles);
            Log.logMessage("DeleteNoteActivity: Notes found, displaying " + noteTitles.size() + " titles.");
        } else {
            noteTitles = new ArrayList<>();
            Log.logMessage("DeleteNoteActivity: No notes found.");
        }

        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, noteTitles);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnNotes.setAdapter(spinnerAdapter);

        updateEmptyViewVisibility();

        btnDeleteSelected.setOnClickListener(v -> {
            Log.logMessage("DeleteNoteActivity: Delete button clicked.");
            deleteSelectedNoteFromSpinner(sharedPref);
        });
    }

    private void deleteSelectedNoteFromSpinner(SharedPreferences sharedPref) {
        SharedPreferences.Editor editor = sharedPref.edit();

        String selectedNote = (String) spnNotes.getSelectedItem();
        if (selectedNote != null) {
            Log.logMessage("DeleteNoteActivity: Selected note to delete: " + selectedNote);
            noteTitles.remove(selectedNote);
            editor.putStringSet(Constants.NOTES_TITLES_KEY, new HashSet<>(noteTitles));
            editor.remove(selectedNote);
            editor.apply();
            Toast.makeText(DeleteNoteActivity.this, "Note Deleted Successfully!", Toast.LENGTH_SHORT).show();
            Log.logMessage("DeleteNoteActivity: Note deleted and changes saved.");
        } else {
            Log.logMessage("DeleteNoteActivity: No note selected for deletion.");
        }

        spinnerAdapter.notifyDataSetChanged();
        updateEmptyViewVisibility();
    }

    private void updateEmptyViewVisibility() {
        if (noteTitles.isEmpty()) {
            Log.logMessage("DeleteNoteActivity: No notes left, updating UI.");
            spnNotes.setVisibility(View.GONE);
            btnDeleteSelected.setVisibility(View.GONE);
            tvNoNotes.setVisibility(View.VISIBLE);
        } else {
            Log.logMessage("DeleteNoteActivity: Notes available, updating UI.");
            spnNotes.setVisibility(View.VISIBLE);
            btnDeleteSelected.setVisibility(View.VISIBLE);
            tvNoNotes.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.logMessage("DeleteNoteActivity: onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.notes_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.logMessage("DeleteNoteActivity: onOptionsItemSelected called");
        switch (item.getItemId()) {
            case R.id.action_home:
                Log.logMessage("DeleteNoteActivity: Home action selected, navigating to MainActivity");
                Intent intent = new Intent(DeleteNoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
