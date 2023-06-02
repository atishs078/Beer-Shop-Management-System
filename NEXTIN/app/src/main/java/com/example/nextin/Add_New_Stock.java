package com.example.nextin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Add_New_Stock extends AppCompatActivity {
    AutoCompleteTextView name, quantity, type;
    EditText number;
    Button Submit;
    FirebaseFirestore fstore;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_stock);
        name = findViewById(R.id.name);
        quantity = findViewById(R.id.quantity);
        type = findViewById(R.id.type);
        number = findViewById(R.id.name1);
        Submit = findViewById(R.id.Submit);
        fstore = FirebaseFirestore.getInstance();
        name.setEnabled(false);
        quantity.setEnabled(false);
        type.setEnabled(false);
        fstore.collection("dropdown_list").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<String> dataList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            String itemName = document.getString("Name"); // Replace "Name" with the field name in your document
                            dataList.add(itemName);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(Add_New_Stock.this, android.R.layout.simple_dropdown_item_1line, dataList);
                        name.setAdapter(adapter);
                        name.setEnabled(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Add_New_Stock.this, "Something went wrong....", Toast.LENGTH_SHORT).show();
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
                ArrayAdapter<String> adapter = new ArrayAdapter<>(Add_New_Stock.this, android.R.layout.simple_dropdown_item_1line, myquantity);
                quantity.setAdapter(adapter);
                quantity.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Add_New_Stock.this, "Something Went Wrong....", Toast.LENGTH_SHORT).show();
            }
        });
        fstore.collection("type_dropdown").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                List<String> Mytype=new ArrayList<>();
                for(QueryDocumentSnapshot document:queryDocumentSnapshots){
                    String itemType=document.getString("Type");
                    Mytype.add(itemType);
                }
                ArrayAdapter<String> adapter=new ArrayAdapter<>(Add_New_Stock.this, android.R.layout.simple_dropdown_item_1line,Mytype);
                type.setAdapter(adapter);
                type.setEnabled(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Add_New_Stock.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
            }
        });
        // ...

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = name.getText().toString().trim();
                String Quantity = quantity.getText().toString().trim();
                String Type = type.getText().toString().trim();
                String NumberOfBottel = number.getText().toString().trim();

                if (TextUtils.isEmpty(Name) || Name.equals("NAME")) {
                    name.setError("Please select a valid Name");
                    return;
                }
                if (TextUtils.isEmpty(Quantity) || Quantity.equals("QUANTITY")) {
                    quantity.setError("Please select a valid Quantity");
                    return;
                }
                if (TextUtils.isEmpty(Type) || Type.equals("TYPE")) {
                    type.setError("Please select a valid Type");
                    return;
                }
                if (TextUtils.isEmpty(NumberOfBottel)) {
                    number.setError("Required");
                    return;
                }

                progressDialog = new ProgressDialog(Add_New_Stock.this);
                progressDialog.setMessage("Submitting Data...");
                progressDialog.setCancelable(false);
                progressDialog.show();

                CollectionReference collectionReference = fstore.collection("Stock");
                collectionReference.whereEqualTo("Name", Name)
                        .whereEqualTo("Quantity", Quantity)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        // Data not present, create a new document
                                        Map<String, Object> myStock = new HashMap<>();
                                        myStock.put("Name", Name);
                                        myStock.put("Quantity", Quantity);
                                        myStock.put("Type", Type);
                                        myStock.put("NoOfBottel", NumberOfBottel);

                                        collectionReference.add(myStock)
                                                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                                        progressDialog.dismiss();
                                                        showCompletionDialog();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Add_New_Stock.this, "Something Went Wrong.....", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        // Data already present, update the existing document
                                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                        String documentId = documentSnapshot.getId();
                                        String currentBottel = documentSnapshot.getString("NoOfBottel");
                                        int currentNoOfBottles = Integer.parseInt(currentBottel);
                                        int updatedNoOfBottles = currentNoOfBottles + Integer.parseInt(NumberOfBottel);
                                        String newNumber = Integer.toString(updatedNoOfBottles);

                                        collectionReference.document(documentId)
                                                .update("NoOfBottel", newNumber)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressDialog.dismiss();
                                                        showCompletionDialog();
                                                    }
                                                })
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(Add_New_Stock.this, "Something Went Wrong.....", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    Toast.makeText(Add_New_Stock.this, "Something Went Wrong.....", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // Month starts from 0
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Format the date as a string
                String currentDate = String.format("%02d-%02d-%04d", day, month, year);
                CollectionReference collection=fstore.collection("New Stock");
                
                collection.whereEqualTo("Date", currentDate).whereEqualTo("Quantity", Quantity).whereEqualTo("Type",Type).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().isEmpty()){
                                Map<String, Object> data = new HashMap<>();
                                data.put("Date",currentDate);
                                data.put("Quantity",Quantity);
                                data.put("Type",Type);
                                data.put("Number of Bottel",NumberOfBottel);
                                collection.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentReference> task) {
                                        Toast.makeText(Add_New_Stock.this, "Ok", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Add_New_Stock.this, "Something went wrong...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                String documentId = documentSnapshot.getId();
                                String currentBottel = documentSnapshot.getString("Number of Bottel");
                                int currentNoOfBottles = Integer.parseInt(currentBottel);
                                int updatedNoOfBottles = currentNoOfBottles + Integer.parseInt(NumberOfBottel);
                                String newNumber = Integer.toString(updatedNoOfBottles);
                                collection.document(documentId).update("Number of Bottel",newNumber).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(Add_New_Stock.this, "Ok", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(Add_New_Stock.this, "Something Went Wrong...", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                
                                
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        
                    }
                });
            }
        });


// ...


    }
    private void showCompletionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Data Submitted")
                .setMessage("The data has been submitted successfully.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
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