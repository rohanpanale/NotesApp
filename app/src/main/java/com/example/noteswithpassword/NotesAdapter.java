package com.example.noteswithpassword;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteViewHolder> {

    private final List<Note> notesList;
    private final OnNoteListener onNoteListener;

    public NotesAdapter(List<Note> notesList, OnNoteListener onNoteListener) {
        this.notesList = notesList;
        this.onNoteListener = onNoteListener;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        return new NoteViewHolder(view, onNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note note = notesList.get(position);
        holder.title.setText(note.getTitle());
        holder.timestamp.setText(note.getTimestamp());
        holder.description.setText(note.getDescription());

        // Show edit and delete buttons in the card view
        holder.editButton.setVisibility(View.VISIBLE);
        holder.deleteButton.setVisibility(View.VISIBLE);

        // Handle item click to show full description
        holder.itemView.setOnClickListener(v -> onNoteListener.onNoteClick(note, position));
    }

    @Override
    public int getItemCount() {
        return notesList.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, description, timestamp;
        ImageButton editButton, deleteButton;

        public NoteViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_note_title);
            description = itemView.findViewById(R.id.tv_note_description);
            timestamp = itemView.findViewById(R.id.tv_note_timestamp);
            editButton = itemView.findViewById(R.id.btn_edit);
            deleteButton = itemView.findViewById(R.id.btn_delete);

            // Handle item clicks for edit and delete buttons
            editButton.setOnClickListener(v -> {
                Note note = notesList.get(getAdapterPosition());
                onNoteListener.onEditClick(note, getAdapterPosition());
            });

            deleteButton.setOnClickListener(v -> {
                Note note = notesList.get(getAdapterPosition());
                onNoteListener.onDeleteClick(note, getAdapterPosition());
            });
        }
    }

    public interface OnNoteListener {
        void onNoteClick(Note note, int position);
        void onEditClick(Note note, int position);
        void onDeleteClick(Note note, int position);
    }
}
