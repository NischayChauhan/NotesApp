package com.example.nischay.notesapp.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.widget.Switch;

import java.util.List;

/**
 * Created by Nischay on 5/10/2020.
 */

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;

    public NoteRepository(Application application) {
        NoteRoom noteRoom = NoteRoom.getInstance(application);

        noteDao = noteRoom.noteDao();
        allNotes = noteDao.getAllNotes();
    }

    public void insert(Note note){
        // calls noteDao's insert
        new NoteAsyncTask(noteDao,1).execute(note);
    }
    public void update(Note note){
        new NoteAsyncTask(noteDao,2).execute(note);
    }
    public void delete(Note note){
        new NoteAsyncTask(noteDao,3).execute(note);
    }
    public void deleteAllNotes(){
        new NoteAsyncTask(noteDao,4).execute();
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    private class NoteAsyncTask extends AsyncTask<Note,Void,Void> {
        NoteDao noteDao;
        int taskId;
            // 1 = Insert
            // 2 = Update
            // 3 = Delete
            // 4 = DeleteAll
        public NoteAsyncTask(NoteDao noteDao,int taskId) {
            this.noteDao = noteDao;
            this.taskId = taskId;
        }
        @Override
        protected Void doInBackground(Note... notes) {
            switch (taskId){
                case 1:
                    noteDao.insert(notes[0]);
                    return null;
                case 2:
                    noteDao.updata(notes[0]);
                    return null;
                case 3:
                    noteDao.delete(notes[0]);
                    return null;
                case 4:
                    noteDao.deleteAllNotes();
                    return null;
                default:
                    return null;
            }
        }
    }
}
