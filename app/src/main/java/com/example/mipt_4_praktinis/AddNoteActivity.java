package com.example.mipt_4_praktinis;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddNoteActivity extends AppCompatActivity {

    EditText noteTitleEditText;
    EditText noteContentEditText;
    Button saveNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Log.logMessage("AddNoteActivity: onCreate called");

        noteTitleEditText = findViewById(R.id.noteTitle);
        noteContentEditText = findViewById(R.id.noteContent);
        saveNoteButton = findViewById(R.id.saveNoteButton);

        Log.logMessage("AddNoteActivity: Save button setup");

        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteTitle = noteTitleEditText.getText().toString().trim();
                String noteContent = noteContentEditText.getText().toString().trim();

                Log.logMessage("AddNoteActivity: Save button clicked");

                if (noteTitle.isEmpty() || noteContent.isEmpty()) {
                    Log.logMessage("AddNoteActivity: Title or content is empty");
                    Toast.makeText(AddNoteActivity.this, "WARNING! Please enter both title and content", Toast.LENGTH_SHORT).show();
                } else {
                    Log.logMessage("AddNoteActivity: Title and content are valid, saving note");

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(Constants.NOTE_TITLE, noteTitle);
                    resultIntent.putExtra(Constants.NOTE_CONTENT, noteContent);
                    setResult(RESULT_OK, resultIntent);

                    Log.logMessage("AddNoteActivity: Note added with title: " + noteTitle);

                    Toast.makeText(AddNoteActivity.this, "Note Added Successfully!", Toast.LENGTH_SHORT).show();

                    finish();
                    Log.logMessage("AddNoteActivity: Activity finished and returning to MainActivity");
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.logMessage("AddNoteActivity: onCreateOptionsMenu called");
        getMenuInflater().inflate(R.menu.notes_home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.logMessage("AddNoteActivity: onOptionsItemSelected called");

        switch (item.getItemId()) {
            case R.id.action_home:
                Log.logMessage("AddNoteActivity: Home action selected, navigating to MainActivity");
                Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
