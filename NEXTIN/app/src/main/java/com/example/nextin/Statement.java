package com.example.nextin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class Statement extends AppCompatActivity {
TextView toText,fromText;
Button Submit;
TableLayout tableLayout;
FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statement);
        toText=findViewById(R.id.to);
        fromText=findViewById(R.id.from);
        Submit=findViewById(R.id.Submit);

        toText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(toText);
            }
        });
        fromText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog(fromText);
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String startingDate = fromText.getText().toString();
                String endingDate = toText.getText().toString();
                Intent intent=new Intent(Statement.this,GeneratedStatement.class);
                intent.putExtra("Starting",startingDate);
                intent.putExtra("Ending",endingDate);
                startActivity(intent);

            }
        });


    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE

                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }
    private void showDatePickerDialog(final TextView textView) {
        // Get current date
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) ; // Month starts from 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Format the date as a string

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(Statement.this,new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        // Set selected date to the TextView
                        String selectedDate = String.format("%02d-%02d-%04d", dayOfMonth, monthOfYear + 1, year);
                        textView.setText(selectedDate);

                    }
                }, year, month, day);

        // Show the DatePickerDialog
        datePickerDialog.show();
    }
}