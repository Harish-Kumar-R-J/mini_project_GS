package com.example.mini_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import java.util.Map;

public class prod_edit extends AppCompatActivity {
    ImageButton img;
    Button edit, delete;
    EditText name, price, desc;
    Uri imageUri;
    StorageReference storageReference;
    String generatedFilePath, the_name, file_path;
    int img_change = 0;
    FirebaseAuth fauth = FirebaseAuth.getInstance();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod_edit);

        img = findViewById(R.id.image_edit);
        name = findViewById(R.id.p_name_edit);
        price = findViewById(R.id.p_price_edit);
        desc = findViewById(R.id.p_desc_edit);
        edit = findViewById(R.id.prod_edit_btn);
        delete = findViewById(R.id.prod_delete_btn);
        img.setEnabled(false);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {Toast.makeText(prod_edit.this, edit.getText().toString(), Toast.LENGTH_SHORT).show();
                if(edit.getText().toString().equals("Edit"))
                {img.setEnabled(true);
                name.setFocusableInTouchMode(true);
                price.setFocusableInTouchMode(true);
                desc.setFocusableInTouchMode(true);
                edit.setText("save");}
                else
                    uploadImage();
            }
        });

        Intent intent = getIntent();
        Bundle b = intent.getExtras();
        file_path = b.getString("url");
        Glide.with(prod_edit.this).load(file_path).into(img);
        name.setText(b.getString("name"));
        price.setText(b.getString("price"));
        desc.setText(b.getString("desc"));
        the_name = b.getString("docx");
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,100);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.collection(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .document(the_name).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        StorageReference photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(file_path);
                        photoRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                SharedPreferences sharedPreferences = getSharedPreferences("changes", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("changed_home", 1);
                                editor.putInt("changed_user", 1);
                                editor.apply();
                                Toast.makeText(prod_edit.this, "Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                            }
                        });
                        System.out.println(the_name + " System.out.println(the_name);");

                    }
                });



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            img.setImageURI(imageUri);
            img_change = 1;
        }
    }

    private void uploadImage() {
        if(img_change == 0)
        {generatedFilePath = file_path;
            update();}
        else
        {storageReference = FirebaseStorage.getInstance().getReference("/" + the_name);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        img_btn.setImageURI(null);
                        Task<Uri> downloadUri = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                generatedFilePath = uri.toString();
                                update();

                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(prod_edit.this,"Failed to Upload",Toast.LENGTH_SHORT).show();
            }
        });
    }}
    void update()
    {Toast.makeText(prod_edit.this,generatedFilePath,Toast.LENGTH_SHORT).show();

        Map<String, Object> user = new HashMap<>();
        user.put("name", name.getText().toString());
        user.put("price", price.getText().toString());
        user.put("desc", desc.getText().toString());
        user.put("url", generatedFilePath);

//        Toast.makeText(prod_edit.this, currentDateAndTime, Toast.LENGTH_SHORT);
//        db.collection(fauth.getCurrentUser().getEmail())
//                .document(the_name)
//                .set(user)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
//                        Toast.makeText(prod_edit.this, "Success", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(prod_edit.this, "Failure", Toast.LENGTH_SHORT).show();
//            }
//        });
//        user.clear();

        user.put("email", fauth.getCurrentUser().getEmail());
        db.collection(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .document(the_name).update(user);
        SharedPreferences sharedPreferences = getSharedPreferences("changes", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("changed_home", 1);
        editor.putInt("changed_user", 1);
        editor.apply();
    }

}