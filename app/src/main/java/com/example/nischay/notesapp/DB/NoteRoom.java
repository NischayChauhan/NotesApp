package com.example.nischay.notesapp.DB;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by Nischay on 5/10/2020.
 */

@Database(entities = {Note.class},version = 1)
public abstract class NoteRoom extends RoomDatabase {

    private static NoteRoom instance;
    public abstract NoteDao noteDao();

    public static synchronized NoteRoom getInstance(Context context){
        if(instance==null){
            Log.e(TAG, "getInstance: Created");
            instance = Room.databaseBuilder(context.getApplicationContext(),NoteRoom.class,"note_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();

        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            // This is fine because it will called after instance is assigned
            Log.e(TAG, "onCreate: added dummy data");
            new AddDefaultValue(instance).execute();
        }
    };


    private static class AddDefaultValue extends AsyncTask<Void,Void,Void>{
        private NoteDao noteDao;
        private AddDefaultValue(NoteRoom noteRoom){
            noteDao = noteRoom.noteDao();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            noteDao.insert(new Note("title 1","desciption 1",1));
            noteDao.insert(new Note("title 2","desciption 2",2));
            noteDao.insert(new Note("title 3","desciption 3",3));
            return null;
        }
    }
}
