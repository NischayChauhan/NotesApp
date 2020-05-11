package com.example.nischay.notesapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nischay.notesapp.DB.Note;
import com.example.nischay.notesapp.DB.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    public static final int ADD_NOTE_REQUEST = 101;
    public static final int EDIT_NOTE_REQUEST = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter();

        recyclerView.setAdapter(noteAdapter);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                noteAdapter.submitList(notes);
//                Log.e("SIZE "," "+notes.size());
            }
        });

        noteAdapter.setOnItemClickListener(new NoteAdapter.customListener() {
            @Override
            public void onItemClick(Note note) {
                Log.e("CLICKED  ", note.getTitle() + "");
                Intent in = new Intent(getApplicationContext(), AddEditActivity.class);
                in.putExtra(AddEditActivity.EXTRA_TITLE, note.getTitle());
                in.putExtra(AddEditActivity.EXTRA_DESCRIPTION, note.getDescription());
                in.putExtra(AddEditActivity.EXTRA_PRIORITY, note.getPriority());
                in.putExtra(AddEditActivity.EXTRA_ID, note.getId());

                startActivityForResult(in, EDIT_NOTE_REQUEST);
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getApplicationContext(),"Removed",Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditActivity.EXTRA_PRIORITY, 1);

            Note note = new Note(title, description, priority);
            noteViewModel.insert(note);
            Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditActivity.EXTRA_PRIORITY,1);
            int id = data.getIntExtra(AddEditActivity.EXTRA_ID,-1);

            if(id==-1){
                Toast.makeText(getApplicationContext(), "Data Failed", Toast.LENGTH_SHORT).show();
                return;
            }

            Note note = new Note(title,description,priority);
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Data Nothing", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_new_notes:
                // add notes
                Intent in = new Intent(MainActivity.this, AddEditActivity.class);
                startActivityForResult(in, ADD_NOTE_REQUEST);
                return true;
            case R.id.delete_all_notes:
                noteViewModel.deleteAllNotes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
