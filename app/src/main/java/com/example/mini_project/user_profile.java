package com.example.mini_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class user_profile extends AppCompatActivity {
        Button logout, add;
        EditText email, u_name, ph_no;
        TextInputLayout user_name, user_email, user_phno;
        ImageButton edit;
    adapter adapter_obj;
    List<String> p_name = new ArrayList<>();
    List<String> p_desc = new ArrayList<>();
    List<String> p_price = new ArrayList<>();
    List<String> p_image = new ArrayList<>();
    List<String> p_email = new ArrayList<>();
    List<String> collections_2 = new ArrayList<>();
    ShimmerFrameLayout shimmerFrameLayout;
    int clicked = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        edit = findViewById(R.id.user_profile_edit);
        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        user_phno = findViewById(R.id.user_phno);
        shimmerFrameLayout = findViewById(R.id.shimmerFrameLayout2);
        u_name = findViewById(R.id.user_name_content);
        ph_no = findViewById(R.id.user_phno_content);
        email = findViewById(R.id.user_email_content);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clicked==1)
                {update();}
                user_name.setEnabled(true);
                user_email.setEnabled(true);
                user_phno.setEnabled(true);
                clicked=1;
                edit.setBackground(ContextCompat.getDrawable(user_profile.this, R.drawable.ic_baseline_save_24));

            }
        });

        add = findViewById(R.id.prod_add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(user_profile.this, product_add.class));
            }
        });

        logout = findViewById(R.id.logout_btn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(user_profile.this, MainActivity.class));
            }
        });

        email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        List<String> docx = new ArrayList<>();
    get_collection(new for_collection() {
        @Override
        public void onCallback(List<String> collection) {
        for(String s: collection)
        {get_document(new Get_documents() {
            @Override
            public void onCallback(List<String> name, List<String> price, List<String> desc, List<String> url, List<String> email, List<String> collections_2) {
                System.out.println(s + " jjjjjjjj");
                docx.add(s);
                LinearLayoutManager mainlayoutManager = new LinearLayoutManager(user_profile.this, LinearLayoutManager.VERTICAL, false);
                RecyclerView main_recycler = findViewById(R.id.recycler_user_profile);
                main_recycler.setLayoutManager(mainlayoutManager);
//    main_screen_adapter main_screen_adapter = new main_screen_adapter(MainActivity.this, districts, notes, active, confirmed, migrated, deceased, recovered, d_confirmed, d_recovered, d_deceased);
                adapter_obj = new adapter(user_profile.this,name, desc, price,url, email,collections_2);

                System.out.println("ssssssssssss "  + name + " " + collections_2);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.INVISIBLE);
                main_recycler.setAdapter(adapter_obj);
            }
        }, s, FirebaseAuth.getInstance().getCurrentUser().getEmail()); }}});

        FirebaseFirestore.getInstance()
                .collection("root")
                .document(email.getText().toString())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                u_name.setText(documentSnapshot.get("name").toString());
                ph_no.setText(documentSnapshot.get("mob_num").toString());
            }
        });



            }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("changes", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if(sharedPreferences.getInt("changed_user", 0)==1)
        {
            p_name.clear();
            p_desc.clear();
            p_price.clear();
            p_image.clear();
            collections_2.clear();
            List<String> docx = new ArrayList<>();
        get_collection(new for_collection() {
            @Override
            public void onCallback(List<String> collection) {
                for(String s: collection)
                {get_document(new Get_documents() {
                    @Override
                    public void onCallback(List<String> name, List<String> price, List<String> desc, List<String> url, List<String> email, List<String> collections_2) {
                        System.out.println(s + " jjjjjjjj");
                        docx.add(s);
                        LinearLayoutManager mainlayoutManager = new LinearLayoutManager(user_profile.this, LinearLayoutManager.VERTICAL, false);
                        RecyclerView main_recycler = findViewById(R.id.recycler_user_profile);
                        main_recycler.setLayoutManager(mainlayoutManager);
//    main_screen_adapter main_screen_adapter = new main_screen_adapter(MainActivity.this, districts, notes, active, confirmed, migrated, deceased, recovered, d_confirmed, d_recovered, d_deceased);
                        adapter_obj = new adapter(user_profile.this,name, desc, price,url, email,collections_2);

                        System.out.println("ssssssssssss "  + name + " " + collections_2);
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.INVISIBLE);
                        main_recycler.setAdapter(adapter_obj);

                    }
                }, s, FirebaseAuth.getInstance().getCurrentUser().getEmail()); }}});

   editor.putInt("changed_user", 0);
   editor.apply();}}

    private interface for_collection
    {void onCallback(List<String> collection);}

    private interface Get_documents
    {void onCallback(List<String> name, List<String> price, List<String> desc, List<String>url, List<String> email,  List<String>collections_2);}

    void get_collection(user_profile.for_collection for_collection)
    {            shimmerFrameLayout.setVisibility(View.VISIBLE);
        shimmerFrameLayout.startShimmer();
        List<String> collection = new ArrayList<>();
        System.out.println();
        FirebaseFirestore.getInstance()
                .collection(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (DocumentSnapshot document : task.  getResult())
                        {collection.add(document.getId());}
                        if(collections_2.size()==0)
                        {shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.INVISIBLE);}
                        for_collection.onCallback(collection);
                    }
                });
    }

    void get_document(user_profile.Get_documents get_documents, String collection, String doc_name) {
        System.out.println("in get_document");
        getter_setter gs = new getter_setter();
        DocumentSnapshot document;
        FirebaseFirestore.getInstance()
                .collection(doc_name)
                .document(collection)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        p_name.add(documentSnapshot.getString("name"));
                        p_desc.add(documentSnapshot.getString("desc"));
                        p_price.add(documentSnapshot.getString("price"));
                        p_image.add(documentSnapshot.getString("url"));
                        p_email.add(documentSnapshot.getString("email"));
                        collections_2.add(collection);
                        get_documents.onCallback(p_name, p_price, p_desc, p_image, p_email,collections_2);
                    }
                });
    }

    void update()
    {
        Map<String, Object> user = new HashMap<>();
        user.put("name", u_name.getText().toString());
        user.put("email", email.getText().toString());
        user.put("mob_num", ph_no.getText().toString());

        FirebaseFirestore.getInstance()
                .collection("root")
                .document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);
                SharedPreferences sharedPreferences = getSharedPreferences("changes", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("changed_home", 1);
                editor.apply();
            }
        });

    }

}