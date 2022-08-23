package com.example.mini_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class expand extends AppCompatActivity {
    TextView name,price,desc, top_prd_name;
    EditText user_name,email,mob_no;
    ImageView img;
    FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expand);
        name = findViewById(R.id.expand_name_edit);
        price = findViewById(R.id.p_price_expand);
        desc = findViewById(R.id.p_desc_expand);
        img = findViewById(R.id.expand_img);
        user_name = findViewById(R.id.expand_name);
        email = findViewById(R.id.expand_email);
        mob_no = findViewById(R.id.expand_phno);
        top_prd_name = findViewById(R.id.top_prd_name);

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        name.setText(b.getString("name"));
        top_prd_name.setText(b.getString("name"));
        price.setText(b.getString("price"));
        desc.setText(b.getString("desc"));
        Glide.with(expand.this).load(b.getString("url")).into(img);

        FirebaseFirestore.getInstance()
                .collection("root")
                .document(b.getString("email"))
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user_name.setText(documentSnapshot.getString("name"));
                email.setText(documentSnapshot.getString("email"));
                mob_no.setText(documentSnapshot.getString("mob_num"));
            }
        });

    }
}