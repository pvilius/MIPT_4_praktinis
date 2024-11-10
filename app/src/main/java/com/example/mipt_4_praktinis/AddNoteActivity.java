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

        noteTitleEditText = findViewById(R.id.noteTitle);
        noteContentEditText = findViewById(R.id.noteContent);
        saveNoteButton = findViewById(R.id.saveNoteButton);

        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteTitle = noteTitleEditText.getText().toString().trim();
                String noteContent = noteContentEditText.getText().toString().trim();

                if (noteTitle.isEmpty() || noteContent.isEmpty()) {
                    Toast.makeText(AddNoteActivity.this, "WARNING! Please enter both title and content", Toast.LENGTH_SHORT).show();
                } else {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("NOTE_TITLE", noteTitle);
                    resultIntent.putExtra("NOTE_CONTENT", noteContent);
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }
            }
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
                Intent intent = new Intent(AddNoteActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

