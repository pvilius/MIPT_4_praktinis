package com.example.mipt_4_praktinis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class ViewEditNoteActivity extends AppCompatActivity {

    EditText noteTitle;
    EditText noteContent;
    Button saveNoteButton;
    String originalTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        Log.logMessage("ViewEditNoteActivity: onCreate called");

        noteTitle = findViewById(R.id.noteTitle);
        noteContent = findViewById(R.id.noteContent);
        saveNoteButton = findViewById(R.id.saveNoteButton);

        Intent intent = getIntent();
        originalTitle = intent.getStringExtra(Constants.NOTE_TITLE);
        String content = intent.getStringExtra(Constants.NOTE_CONTENT);

        // Log the received note data
        Log.logMessage("ViewEditNoteActivity: Received originalTitle: " + originalTitle);
        Log.logMessage("ViewEditNoteActivity: Received content: " + content);

        if (originalTitle != null) {
            noteTitle.setText(originalTitle);
            noteContent.setText(content);
            Log.logMessage("ViewEditNoteActivity: Note data set in EditText fields");
        }

        saveNoteButton.setOnClickListener(view -> {
            String updatedTitle = noteTitle.getText().toString().trim();
            String updatedContent = noteContent.getText().toString().trim();

            Log.logMessage("ViewEditNoteActivity: Save button clicked");

            if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
                Log.logMessage("ViewEditNoteActivity: Title or content is empty");
                Toast.makeText(ViewEditNoteActivity.this, "WARNING! Please enter both title and content", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ViewEditNoteActivity.this);
            SharedPreferences.Editor editor = sharedPref.edit();

            if (!updatedTitle.equals(originalTitle)) {
                Log.logMessage("ViewEditNoteActivity: Title changed from " + originalTitle + " to " + updatedTitle);
                editor.remove(originalTitle);
            }

            editor.putString(updatedTitle, updatedContent);
            Log.logMessage("ViewEditNoteActivity: New note saved with title: " + updatedTitle);

            Set<String> titleSet = sharedPref.getStringSet(Constants.NOTES_TITLES_KEY, new HashSet<>());
            titleSet.remove(originalTitle);
            titleSet.add(updatedTitle);
            editor.putStringSet(Constants.NOTES_TITLES_KEY, titleSet);

            editor.apply();
            Toast.makeText(ViewEditNoteActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
            Log.logMessage("ViewEditNoteActivity: Changes applied and saved");

            finish();
            Log.logMessage("ViewEditNoteActivity: Activity finished and returning to MainActivity");
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.logMessage("ViewEditNoteActivity: onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.notes_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.logMessage("ViewEditNoteActivity: onOptionsItemSelected called");

        switch (item.getItemId()) {
            case R.id.action_home:
                Log.logMessage("ViewEditNoteActivity: Home action selected, navigating to MainActivity");
                Intent intent = new Intent(ViewEditNoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
