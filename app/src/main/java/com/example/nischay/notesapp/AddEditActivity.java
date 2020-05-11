package com.example.nischay.notesapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

public class AddEditActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private NumberPicker numberPickerPriority;

    public static final String EXTRA_TITLE = "com.example.nischay.notesapp.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.example.nischay.notesapp.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY = "com.example.nischay.notesapp.EXTRA_PRIORITY";
    public static final String EXTRA_ID = "com.example.nischay.notesapp.EXTRA_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        numberPickerPriority = findViewById(R.id.number_picker_priority);

        numberPickerPriority.setMinValue(1);
        numberPickerPriority.setMaxValue(10);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_launcher_foreground);

        if (getIntent().hasExtra(EXTRA_ID)) {
            editTextTitle.setText(getIntent().getStringExtra(EXTRA_TITLE));
            editTextDescription.setText(getIntent().getStringExtra(EXTRA_DESCRIPTION));
            numberPickerPriority.setValue(getIntent().getIntExtra(EXTRA_PRIORITY,1));
            setTitle("EDIT NODE");
        } else {
            setTitle("Add Node");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_edit_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                saveNotes();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveNotes() {
        String textTitle = editTextTitle.getText().toString();
        String textDescription = editTextDescription.getText().toString();
        int pickerPriority = numberPickerPriority.getValue();

        if (textTitle.trim().isEmpty() || textDescription.trim().isEmpty()) {
            Toast.makeText(getApplicationContext(), "ENTER SOMETHING", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();

        intent.putExtra(EXTRA_TITLE, textTitle);
        intent.putExtra(EXTRA_DESCRIPTION, textDescription);
        intent.putExtra(EXTRA_PRIORITY, pickerPriority);

        if(getIntent().hasExtra(EXTRA_ID)){
            intent.putExtra(EXTRA_ID,getIntent().getIntExtra(EXTRA_ID,-1));
        }

        setResult(RESULT_OK, intent);
        finish();
    }
}
