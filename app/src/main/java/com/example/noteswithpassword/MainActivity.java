/*package com.example.noteswithpassword;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteListener {

    private List<String> notesList;
    private NotesAdapter notesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize list and adapter
        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList, this);

        RecyclerView recyclerView = findViewById(R.id.recycler_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notesAdapter);

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(view -> showAddNoteDialog());
    }

    // Method to show dialog to add a new note
    private void showAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note");

        final EditText input = new EditText(this);
        input.setHint("Type your note here...");
        builder.setView(input);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String note = input.getText().toString().trim();
            if (!note.isEmpty()) {
                notesList.add(note);
                notesAdapter.notifyItemInserted(notesList.size() - 1);
            } else {
                Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onEditClick(int position) {
        String currentNote = notesList.get(position);
        showEditNoteDialog(position, currentNote);
    }

    private void showEditNoteDialog(int position, String currentNote) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Note");

        final EditText input = new EditText(this);
        input.setText(currentNote);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String editedNote = input.getText().toString().trim();
            if (!editedNote.isEmpty()) {
                notesList.set(position, editedNote);
                notesAdapter.notifyItemChanged(position);
            } else {
                Toast.makeText(this, "Note cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onDeleteClick(int position) {
        notesList.remove(position);
        notesAdapter.notifyItemRemoved(position);
        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
    }
}*/



package com.example.noteswithpassword;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NotesAdapter.OnNoteListener {

    private List<Note> notesList;
    private NotesAdapter notesAdapter;
    private TextView noNotesMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        notesList = new ArrayList<>();
        notesAdapter = new NotesAdapter(notesList, this);

        RecyclerView recyclerView = findViewById(R.id.recycler_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(notesAdapter);

        // Add spacing between cards
        int spacing = getResources().getDimensionPixelSize(R.dimen.card_spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacing));

        noNotesMessage = findViewById(R.id.no_notes_message);

        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(view -> showAddNoteDialog());

        updateNoNotesMessage();
    }

    private void showAddNoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Note");

        View customLayout = getLayoutInflater().inflate(R.layout.dialog_add_note, null);
        builder.setView(customLayout);

        EditText titleInput = customLayout.findViewById(R.id.input_title);
        EditText descriptionInput = customLayout.findViewById(R.id.input_description);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String title = titleInput.getText().toString().trim();
            String description = descriptionInput.getText().toString().trim();
            String timestamp = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault()).format(new Date());

            notesList.add(new Note(title, description, timestamp));
            notesAdapter.notifyItemInserted(notesList.size() - 1);
            updateNoNotesMessage();
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void updateNoNotesMessage() {
        noNotesMessage.setVisibility(notesList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onEditClick(Note note, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Note");

        View customLayout = getLayoutInflater().inflate(R.layout.dialog_add_note, null);
        builder.setView(customLayout);

        EditText titleInput = customLayout.findViewById(R.id.input_title);
        EditText descriptionInput = customLayout.findViewById(R.id.input_description);

        // Set current values of the note to be edited
        titleInput.setText(note.getTitle());
        descriptionInput.setText(note.getDescription());

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newTitle = titleInput.getText().toString().trim();
            String newDescription = descriptionInput.getText().toString().trim();

            note.setTitle(newTitle);
            note.setDescription(newDescription);

            notesAdapter.notifyItemChanged(position);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onDeleteClick(Note note, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Note")
                .setMessage("Are you sure you want to delete this note?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    notesList.remove(position);
                    notesAdapter.notifyItemRemoved(position);
                    updateNoNotesMessage();
                })
                .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onNoteClick(Note note, int position) {
        // Show note description in a dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(note.getTitle());
        builder.setMessage(note.getDescription());
        builder.setPositiveButton("Close", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
