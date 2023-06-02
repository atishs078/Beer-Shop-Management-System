package com.example.nextin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;


import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TodaysDataInsertion extends AppCompatActivity {
    EditText name, noofbottel, priceofbottel;
    AutoCompleteTextView quantity;
    Button Submit;
    FirebaseAuth fAuth;
    FirebaseFirestore fb;
    ProgressDialog progressDialog;
    public static final String TAG = "TAG";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_data_insertion);
        name = findViewById(R.id.editTextProduct1);
        Submit = findViewById(R.id.buttonSubmit);
        noofbottel = findViewById(R.id.editTextProduct2);
        priceofbottel = findViewById(R.id.editTextProduct3);
        quantity = findViewById(R.id.autoCompleteTextView);
        Intent intent = getIntent();
        String nam = intent.getStringExtra("name");
        name.setText(nam);
        String[] quantityy = getResources().getStringArray(R.array.Quantity);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.dropdown_item, quantityy);
        AutoCompleteTextView autocompleteTV = findViewById(R.id.autoCompleteTextView);
        autocompleteTV.setAdapter(arrayAdapter);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString().trim();
                String noOfBottels = noofbottel.getText().toString().trim();
                String Price = priceofbottel.getText().toString().trim();
                String qua = quantity.getText().toString().trim();

                fAuth = FirebaseAuth.getInstance();
                fb = FirebaseFirestore.getInstance();
                String user = fAuth.getCurrentUser().getUid();
                if (TextUtils.isEmpty(Name)) {
                    name.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(noOfBottels)) {
                    noofbottel.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(Price)) {
                    priceofbottel.setError("Required");
                    return;
                }
                if (TextUtils.isEmpty(qua) || qua.equals("Choose Quantity")) {
                    quantity.setError("Please select a valid quantity");
                    return;
                }
                progressDialog = new ProgressDialog(TodaysDataInsertion.this);
                progressDialog.setMessage("Submitting Data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Date date=new Date();
                LocalDate localDate=LocalDate.now();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // Month starts from 0
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Format the date as a string
                String currentDate = String.format("%02d-%02d-%04d", day, month, year);

                CollectionReference collectionReference = fb.collection("Sale");
                Map<String, Object> data = new HashMap<>();

                data.put("Date",currentDate);
                data.put("User",user);
                data.put("Name", Name);
                data.put("Price", Price);
                data.put("Total Sale", noOfBottels);
                data.put("Quantity", qua);


                collectionReference.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        progressDialog.dismiss();
                        showCompletionDialog();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Error!", Toast.LENGTH_SHORT).show();
                        Log.w(TAG, "Error!", e);
                    }
                });
              CollectionReference collect=fb.collection("Stock");
              collect.whereEqualTo("Name",Name).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                  @Override
                  public void onComplete(@NonNull Task<QuerySnapshot> task) {
                      if(task.isSuccessful()){
                          for (DocumentSnapshot documentSnapshot:task.getResult()) {
                              String numberofbottel = documentSnapshot.getString("NoOfBottel");
                              String quantity = documentSnapshot.getString("Quantity");
                              String type = documentSnapshot.getString("Type");
                              String docId = documentSnapshot.getId();
                              int currentBottel = Integer.parseInt(noOfBottels);
                              int updatedBottels = Integer.parseInt(numberofbottel) - currentBottel;
                              String UpdatedBottel = Integer.toString(updatedBottels);
                              if (quantity.equals(qua)) {
                                  collect.document(docId).update("NoOfBottel", UpdatedBottel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                      @Override
                                      public void onSuccess(Void unused) {
                                          Toast.makeText(TodaysDataInsertion.this, "Previous: " + numberofbottel, Toast.LENGTH_SHORT).show();

                                      }
                                  }).addOnFailureListener(new OnFailureListener() {
                                      @Override
                                      public void onFailure(@NonNull Exception e) {
                                          Toast.makeText(TodaysDataInsertion.this, "Failed....", Toast.LENGTH_SHORT).show();
                                      }
                                  });


                              }
                          }
                      }
                      else{
                          Toast.makeText(TodaysDataInsertion.this, "Something went wrong....", Toast.LENGTH_SHORT).show();
                      }
                  }
              }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(TodaysDataInsertion.this, "Failed to Retrive", Toast.LENGTH_SHORT).show();
                  }
              });
            }
        });
    }

    private void showCompletionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Data Submitted")
                .setMessage("The data has been submitted successfully.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), TodaySale.class);
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
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
}