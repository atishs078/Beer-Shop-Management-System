package com.example.nextin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpeningClosing extends AppCompatActivity {
AutoCompleteTextView type,quantity;
EditText noofbottel;
Button Submit;
FirebaseFirestore fstore;
ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_closing);
        type=findViewById(R.id.type);
        quantity=findViewById(R.id.quantity);
        noofbottel=findViewById(R.id.noofbottle);
        Submit=findViewById(R.id.Submit);
        fstore=FirebaseFirestore.getInstance();
        type.setEnabled(false);
        quantity.setEnabled(false);
        fstore.collection("type_dropdown").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> dataList = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String itemName = document.getString("Type"); // Replace "Name" with the field name in your document
                    dataList.add(itemName);
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(OpeningClosing.this, android.R.layout.simple_dropdown_item_1line, dataList);
                type.setAdapter(adapter);
                type.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OpeningClosing.this, "Something went wrong....", Toast.LENGTH_SHORT).show();
            }
        });

        fstore.collection("quantity_dropdown").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> myquantity = new ArrayList<>();
                for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                    String itemName = document.getString("value");
                    myquantity.add(itemName);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(OpeningClosing.this, android.R.layout.simple_dropdown_item_1line, myquantity);
                quantity.setAdapter(adapter);
                quantity.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(OpeningClosing.this, "Something went wrong....", Toast.LENGTH_SHORT).show();
            }
        });
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Type=type.getText().toString().trim();
                String Quantity=quantity.getText().toString().trim();
                String NumberOfBottel = noofbottel.getText().toString().trim();
                if (TextUtils.isEmpty(Quantity) || Quantity.equals("QUANTITY")) {
                    quantity.setError("Please select a valid Quantity");
                    return;
                }
                if (TextUtils.isEmpty(Type) || Type.equals("TYPE")) {
                    type.setError("Please select a valid Type");
                    return;
                }
                if (TextUtils.isEmpty(NumberOfBottel)) {
                    noofbottel.setError("Required");
                    return;
                }
                progressDialog = new ProgressDialog(OpeningClosing.this);
                progressDialog.setMessage("Submitting Data...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // Month starts from 0
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Format the date as a string
                String currentDate = String.format("%02d-%02d-%04d", day, month, year);
                CollectionReference collectionReference = fstore.collection("Opening");
                Map<String, Object> data = new HashMap<>();
                data.put("Date",currentDate);
                data.put("Type",Type);
                data.put("Quantity",Quantity);
                data.put("Number Of Bottel",NumberOfBottel);
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
                        Intent intent = new Intent(getApplicationContext(), OpeningClosing.class);
                        startActivity(intent);
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}