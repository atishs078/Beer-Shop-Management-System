package com.example.nextin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class GeneratedStatement extends AppCompatActivity {
    FirebaseFirestore db;
    TableLayout tableLayout;
    ProgressDialog progressDialog;
    LinearLayout linearLayout;
    int openingSumMild650 = 0;
    int newStockSumMild650 = 0;
    int totalMild650;
    int openingSumMild330 = 0;
    int newStockSumMild330 = 0;
    int totalMild330;
    int openingSumMild500 = 0;
    int newStockSumMild500 = 0;
    int totalMild500;
    int openingSumStrong650 = 0;
    int newStockSumStrong650 = 0;
    int totalStrong650;
    int openingSumStrong330 = 0;
    int newStockSumStrong330 = 0;
    int totalStrong330;
    int openingSumStrong500 = 0;
    int newStockSumStrong500 = 0;
    int totalStrong500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_statement);
        tableLayout = findViewById(R.id.mytable);
        linearLayout = findViewById(R.id.myConatiner);
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String start = intent.getStringExtra("Starting");
        String end = intent.getStringExtra("Ending");
        CollectionReference collectionReference = db.collection("Opening");
        collectionReference.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    // Clear data except for the first row
                    int childCount = tableLayout.getChildCount();
                    if (childCount > 1) {
                        tableLayout.removeViews(1, childCount - 1);
                    }
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        String Quantity = documentSnapshot.getString("Quantity");
                        String Type = documentSnapshot.getString("Type");
                        String Number = documentSnapshot.getString("Number Of Bottel");
                        TableRow tableRow = new TableRow(getApplicationContext());
                        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        tableRow.setLayoutParams(layoutParams);
                        TextView textView = createTextView(Quantity);
                        TextView textView1 = createTextView(Type);
                        TextView textView2 = createTextView(Number);
                        tableRow.addView(textView);
                        tableRow.addView(textView1);
                        tableRow.addView(textView2);
                        tableLayout.addView(tableRow);
                        addCellBorders(textView);
                        addCellBorders(textView1);
                        addCellBorders(textView2);

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        CollectionReference collectionReference1 = db.collection("New Stock");
        collectionReference1.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    TextView textView = createTextView("Recived");
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(0, 16, 0, 0);
                    textView.setLayoutParams(layoutParams);

                    // Add the additional content to the linearLayout
                    linearLayout.addView(textView);
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        String Quantity = queryDocumentSnapshot.getString("Quantity");
                        String Type = queryDocumentSnapshot.getString("Type");
                        String Number = queryDocumentSnapshot.getString("Number of Bottel");
                        TableLayout tableLayout1 = new TableLayout(GeneratedStatement.this);
                        TableRow tableRow = new TableRow(GeneratedStatement.this);
                        TableRow.LayoutParams layoutParams1 = new TableRow.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                        );
                        tableRow.setLayoutParams(layoutParams);
                        TextView textView3 = createTextView(Quantity);
                        TextView textView1 = createTextView(Type);
                        TextView textView2 = createTextView(Number);
                        tableRow.addView(textView3);
                        tableRow.addView(textView1);
                        tableRow.addView(textView2);
                        tableLayout1.addView(tableRow);
                        addCellBorders(textView3);
                        addCellBorders(textView1);
                        addCellBorders(textView2);
                        tableLayout1.setLayoutParams(layoutParams);
                        linearLayout.addView(tableLayout1);

                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


        collectionReference.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).whereEqualTo("Type", "Mild").whereEqualTo("Quantity", "650").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        int quantity = Integer.parseInt(documentSnapshot.getString("Number Of Bottel"));
                        openingSumMild650 += quantity;

                    }
                    collectionReference1.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).whereEqualTo("Type", "Mild").whereEqualTo("Quantity", "650").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    int quantity = Integer.parseInt(documentSnapshot.getString("Number of Bottel"));
                                    newStockSumMild650 += quantity;
                                }
                                totalMild650 = openingSumMild650 + newStockSumMild650;
                                Toast.makeText(GeneratedStatement.this, "" + totalMild650, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }

        });
        collectionReference.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).whereEqualTo("Type", "Mild").whereEqualTo("Quantity", "330").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        int quantity = Integer.parseInt(documentSnapshot.getString("Number Of Bottel"));
                        openingSumMild330 += quantity;

                    }
                    collectionReference1.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).whereEqualTo("Type", "Mild").whereEqualTo("Quantity", "330").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    int quantity = Integer.parseInt(documentSnapshot.getString("Number of Bottel"));
                                    newStockSumMild330 += quantity;
                                }
                                totalMild330 = openingSumMild330 + newStockSumMild330;
                                if (totalMild330 == 0) {

                                } else {

                                    Toast.makeText(GeneratedStatement.this, "" + totalMild330, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }

        });
        collectionReference.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).whereEqualTo("Type", "Mild").whereEqualTo("Quantity", "500").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        int quantity = Integer.parseInt(documentSnapshot.getString("Number Of Bottel"));
                        openingSumMild500 += quantity;

                    }
                    collectionReference1.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).whereEqualTo("Type", "Mild").whereEqualTo("Quantity", "500").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    int quantity = Integer.parseInt(documentSnapshot.getString("Number of Bottel"));
                                    newStockSumMild500 += quantity;
                                }
                                totalMild500 = openingSumMild500 + newStockSumMild500;
                                if (totalMild500 == 0) {

                                } else {

                                    Toast.makeText(GeneratedStatement.this, "" + totalMild500, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }

        });
        collectionReference.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).whereEqualTo("Type", "Strong").whereEqualTo("Quantity", "650").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        int quantity = Integer.parseInt(documentSnapshot.getString("Number Of Bottel"));
                        openingSumStrong650 += quantity;

                    }
                    collectionReference1.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).whereEqualTo("Type", "Mild").whereEqualTo("Quantity", "500").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    int quantity = Integer.parseInt(documentSnapshot.getString("Number of Bottel"));
                                    newStockSumStrong650 += quantity;
                                }
                                totalStrong650 = openingSumStrong650 + newStockSumStrong650;
                                if (totalStrong650 == 0) {

                                } else {

                                    Toast.makeText(GeneratedStatement.this, "" + totalStrong650, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }

        });
        collectionReference.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).whereEqualTo("Type", "Strong").whereEqualTo("Quantity", "330").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        int quantity = Integer.parseInt(documentSnapshot.getString("Number Of Bottel"));
                        openingSumStrong330 += quantity;

                    }
                    collectionReference1.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).whereEqualTo("Type", "Mild").whereEqualTo("Quantity", "330").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    int quantity = Integer.parseInt(documentSnapshot.getString("Number of Bottel"));
                                    newStockSumStrong330 += quantity;
                                }
                                totalStrong330 = openingSumStrong330 + newStockSumStrong330;
                                if (totalStrong330 == 0) {

                                } else {

                                    Toast.makeText(GeneratedStatement.this, "" + totalStrong650, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }

        });
        collectionReference.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).whereEqualTo("Type", "Strong").whereEqualTo("Quantity", "500").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        int quantity = Integer.parseInt(documentSnapshot.getString("Number Of Bottel"));
                        openingSumStrong500 += quantity;

                    }
                    collectionReference1.whereEqualTo("Date", end).whereLessThanOrEqualTo("Date", start).whereEqualTo("Type", "Mild").whereEqualTo("Quantity", "500").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    int quantity = Integer.parseInt(documentSnapshot.getString("Number of Bottel"));
                                    newStockSumStrong500 += quantity;
                                }
                                totalStrong500 = openingSumStrong500 + newStockSumStrong500;
                                if (totalStrong500 == 0) {

                                } else {

                                    Toast.makeText(GeneratedStatement.this, "" + totalStrong650, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }

        });
        TableLayout tableLayout2=new TableLayout(GeneratedStatement.this);
        TableRow headerRow = new TableRow(getApplicationContext());

// Create header text views
        TextView quantityHeader = new TextView(getApplicationContext());
        quantityHeader.setText("Quantity");
        quantityHeader.setTypeface(null, Typeface.BOLD);
        headerRow.addView(quantityHeader);

        TextView typeHeader = new TextView(getApplicationContext());
        typeHeader.setText("Type");
        typeHeader.setTypeface(null, Typeface.BOLD);
        headerRow.addView(typeHeader);

        TextView totalHeader = new TextView(getApplicationContext());
        totalHeader.setText("Total");
        totalHeader.setTypeface(null, Typeface.BOLD);
        headerRow.addView(totalHeader);

// Add the header row to the table layout
        tableLayout.addView(headerRow);
        linearLayout.addView(tableLayout2);
    }

    private TextView createTextView(String text) {
        TextView textView = new TextView(GeneratedStatement.this);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setPadding(16, 16, 16, 16);
        textView.setGravity(Gravity.CENTER);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        return textView;
    }


    private void addCellBorders(TextView textView) {
        textView.setBackgroundResource(R.drawable.tableborder);
    }
}
