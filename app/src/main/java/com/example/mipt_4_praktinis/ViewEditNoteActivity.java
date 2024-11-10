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

        noteTitle = findViewById(R.id.noteTitle);
        noteContent = findViewById(R.id.noteContent);
        saveNoteButton = findViewById(R.id.saveNoteButton);

        Intent intent = getIntent();
        originalTitle = intent.getStringExtra("NOTE_TITLE");
        String content = intent.getStringExtra("NOTE_CONTENT");

        if (originalTitle != null) {
            noteTitle.setText(originalTitle);
            noteContent.setText(content);
        }

        saveNoteButton.setOnClickListener(view -> {
            String updatedTitle = noteTitle.getText().toString().trim();
            String updatedContent = noteContent.getText().toString().trim();

            if (updatedTitle.isEmpty() || updatedContent.isEmpty()) {
                Toast.makeText(ViewEditNoteActivity.this, "Title and content cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(ViewEditNoteActivity.this);
            SharedPreferences.Editor editor = sharedPref.edit();

            if (!updatedTitle.equals(originalTitle)) {
                editor.remove(originalTitle);
            }

            editor.putString(updatedTitle, updatedContent);

            Set<String> titleSet = sharedPref.getStringSet(Constants.NOTES_TITLES_KEY, new HashSet<>());
            titleSet.remove(originalTitle);
            titleSet.add(updatedTitle);
            editor.putStringSet(Constants.NOTES_TITLES_KEY, titleSet);

            editor.apply();
            Toast.makeText(ViewEditNoteActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu with the home icon
        getMenuInflater().inflate(R.menu.notes_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home:
                Intent intent = new Intent(ViewEditNoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
