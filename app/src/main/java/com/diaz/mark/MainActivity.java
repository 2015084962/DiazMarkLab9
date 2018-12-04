package com.diaz.mark;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText etFullname, etAge, etGender;
    TextView tvFullname, tvAge, tvGender;
    String fullname, gender;
    Person person;
    int age;
    FirebaseDatabase DiazMarkLab9;
    DatabaseReference db;
    ArrayList<Person> personList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etFullname = findViewById(R.id.etName);
        etAge = findViewById(R.id.etAge);
        etGender = findViewById(R.id.etGender);
        tvFullname = findViewById(R.id.tvFullname);
        tvAge = findViewById(R.id.tvAge);
        tvGender = findViewById(R.id.tvGender);

        etFullname.setText("");
        etAge.setText("");
        etGender.setText("");

        DiazMarkLab9 = FirebaseDatabase.getInstance();
        db = DiazMarkLab9.getReference("diazmarklab9");
        personList = new ArrayList<>();

    }

    @Override
    protected void onStart() {
        super.onStart();
        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                personList.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Person person = ds.getValue(Person.class);
                    personList.add(person);
                    //hehehe
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void saveRecord(View v) {
        if(!etFullname.getText().toString().equals("")) {
            fullname = etFullname.getText().toString().trim();
            if(!etAge.getText().toString().equals("")) {
                try {
                    age = Integer.parseInt(etAge.getText().toString());
                    if(!etGender.getText().toString().equals("")) {
                        gender = etGender.getText().toString().trim();
                        if(gender.equalsIgnoreCase("male") || gender.equalsIgnoreCase("female")) {
                            if(personList.isEmpty()) {
                                person = new Person(fullname, age, gender);
                                String key = db.push().getKey();
                                db.child(key).setValue(person);
                                Toast.makeText(this, "Record Stored.", Toast.LENGTH_SHORT).show();
                            } else {
                                for(int i = 0; i < personList.size(); i++) {
                                    String storedFullname = personList.get(i).getFullname();
                                    if(storedFullname.equals(fullname)) {
                                        Toast.makeText(this, "Record Already Exists.", Toast.LENGTH_SHORT).show();
                                        break;
                                    } else {
                                        person = new Person(fullname, age, gender);
                                        String key = db.push().getKey();
                                        db.child(key).setValue(person);
                                        Toast.makeText(this, "Record Stored.", Toast.LENGTH_SHORT).show();
                                        break;
                                    }
                                }
                            }
                            etFullname.setText("");
                            etAge.setText("");
                            etGender.setText("");
                            etGender.onEditorAction(EditorInfo.IME_ACTION_DONE);
                        } else {
                            Toast.makeText(this, "Please enter either Male or Female for Gender.", Toast.LENGTH_SHORT).show();
                        }

                    } else {
                        Toast.makeText(this, "Please answer all fields.", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "Please enter an integer for the Age.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please answer all fields.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please answer all fields.", Toast.LENGTH_SHORT).show();
        }
    }

    public void searchRecord(View v) {
        fullname = etFullname.getText().toString().trim();
        for(int i = 0; i < personList.size(); i++) {
            String storedFullname = personList.get(i).getFullname();
            if(storedFullname.equalsIgnoreCase(fullname)) {
                tvFullname.setText(storedFullname);
                String storedAge = personList.get(i).getAge()+"";
                tvAge.setText(storedAge);
                tvGender.setText(personList.get(i).getGender());
                Toast.makeText(this, "Record Retrieved.", Toast.LENGTH_SHORT).show();
                break;
            } else {
                Toast.makeText(this, "Record not Found.", Toast.LENGTH_SHORT).show();
                break;
            }
        }

        etFullname.onEditorAction(EditorInfo.IME_ACTION_NEXT);
    }




}
