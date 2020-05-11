package com.example.nischay.notesapp.DB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by Nischay on 5/10/2020.
 */

@Dao
public interface NoteDao {

    @Insert
    void insert(Note note);

    @Delete
    void delete(Note note);

    @Update
    void updata(Note note);

    @Query("SELECT * from note")
    LiveData<List<Note>> getAllNotes();

    @Query("DELETE from note")
    void deleteAllNotes();

}
