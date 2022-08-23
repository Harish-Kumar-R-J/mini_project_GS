package com.example.mini_project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    EditText u_name, pass, mob_num, email, conf_pwd;
    TextInputLayout register_uname, email_id, register_password, mob_num2;
    Button login;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    FirebaseAuth f_auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        register_uname = findViewById(R.id.register_uname);
        register_password = findViewById(R.id.register_password);
        email_id = findViewById(R.id.email_id);
        mob_num2 = findViewById(R.id.mob_num);
        constraintLayout = findViewById(R.id.register_const_2);
        email = findViewById(R.id.email_id_content);
        mob_num = findViewById(R.id.mob_num_content);
        u_name = findViewById(R.id.register_uname_content);
        pass = findViewById(R.id.register_password_content);
        login = findViewById(R.id.register_button);
        conf_pwd = findViewById(R.id.confirm_pwd_content);
        progressBar = findViewById(R.id.register_progressBar);

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                View v = getCurrentFocus();
                {email.clearFocus();
                    mob_num.clearFocus();
                    register_uname.clearFocus();
                    pass.clearFocus();
                    conf_pwd.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);}
                return false;

            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate_name();
            }
        });

    }
    void validate_name()
    {int temp = 0;
    if(u_name.getText().toString().equals(""))
    {register_uname.setHelperText("Invalid Name");
    temp+=1;}
    if(email.getText().toString().equals(""))
    {email_id.setHelperText("Invalid Email");
    temp+=1;}
    if(!pass.getText().toString().equals(conf_pwd.getText().toString()))
    {register_password.setHelperText("Invalid password");
    temp+=1;}
    if(pass.getText().toString().length()<7)
    {register_password.setHelperText("Invalid Password");
    temp+=1;}
    if(!(mob_num.getText().toString().length() ==10))
    {mob_num2.setHelperText("Invalid mobile number");
    temp+=1;}
    if(temp==0)
    {validator();}}

//    class load extends AsyncTask<Void, Void, Void>
//    {
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//
////        authenticate();
//
//        }
//
//        @Override
//        protected void onPostExecute(Void unused) {
//            super.onPostExecute(unused);
//
//        }
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            authenticate();
//            return null;
//        }
//    }

    void validator()
    {            progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        authenticate();
    }

    private void authenticate()
    {   Map<String, Object> user = new HashMap<>();
        user.put("name", u_name.getText().toString());
        user.put("email", email.getText().toString());
        user.put("mob_num", mob_num.getText().toString());

        f_auth = FirebaseAuth.getInstance();
        f_auth.createUserWithEmailAndPassword(email.getText().toString(), pass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                FirebaseFirestore.getInstance().collection("root").document(email.getText().toString()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(register.this, "success", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        startActivity(new Intent(register.this, home.class));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(register.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }
}