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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class product_add extends AppCompatActivity {
Button submit;
ImageButton img_btn;
EditText name, price, desc;
Uri imageUri;
StorageReference storageReference;
String the_name;
String generatedFilePath;
FirebaseFirestore db = FirebaseFirestore.getInstance();
String currentDateAndTime;
FirebaseAuth fauth = FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_add);
        submit = findViewById(R.id.prod_submit);
        name = findViewById(R.id.add_p_name_content);
        price = findViewById(R.id.add_p_price_content);
        desc = findViewById(R.id.add_p_desc_content);
        img_btn = findViewById(R.id.select_image);

        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,100);
            }
        });
        currentDateAndTime = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss-SSS").format(Calendar.getInstance().getTimeInMillis());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                uploadImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            img_btn.setImageURI(imageUri);
        }
    }
    private void uploadImage() {
        the_name = name.getText().toString() + currentDateAndTime;
        storageReference = FirebaseStorage.getInstance().getReference("/" + the_name);
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
                Toast.makeText(product_add.this,e.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    void update()
    {
//        Toast.makeText(product_add.this,generatedFilePath,Toast.LENGTH_SHORT).show();

        Map<String, Object> user = new HashMap<>();
        user.put("name", name.getText().toString());
        user.put("price", price.getText().toString());
        user.put("desc", desc.getText().toString());
        user.put("email", fauth.getCurrentUser().getEmail());
        user.put("url", generatedFilePath);

//        Toast.makeText(product_add.this, currentDateAndTime, Toast.LENGTH_SHORT);
        db.collection(fauth.getCurrentUser().getEmail())
                .document(the_name)
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(product_add.this, "Success", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences = getSharedPreferences("changes", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("changed_home", 1);
                        editor.putInt("changed_user", 1);
                        editor.apply();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(product_add.this, "Failure", Toast.LENGTH_SHORT).show();
            }
        });

//        user.clear();
//        user.put("email", fauth.getCurrentUser().getEmail());
//        db.collection("root")
//                .document(fauth.getCurrentUser().getEmail())
//                .set(user)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void unused) {
////                                Toast.makeText(product_add.this, "Success", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(product_add.this, "Failure", Toast.LENGTH_SHORT).show();
//            }
//        });


    }
}