package com.example.asterik.presensi.ui.signin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.asterik.presensi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

import static android.content.ContentValues.TAG;

public class SignInFragment extends Fragment implements
        View.OnClickListener{

    private FirebaseAuth mAuth;
    TextView teksPass;
    TextView teksEmail;
    TextView info1;
    TextView info2;
    EditText email;
    EditText password;
    Button signIn;
    Button signOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_sign_in, container, false);
        teksPass = (TextView) root.findViewById(R.id.teksPass);
        teksEmail = (TextView) root.findViewById(R.id.teksEmail);
        info1 = (TextView) root.findViewById(R.id.info);
        info2 = (TextView) root.findViewById(R.id.info2);
        email = (EditText) root.findViewById(R.id.email);
        password = (EditText) root.findViewById(R.id.password);
        signIn = (Button) root.findViewById(R.id.signin);
        signIn.setOnClickListener(this);
        signOut = (Button) root.findViewById(R.id.signout);
        signOut.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            teksPass.setVisibility(View.INVISIBLE);
            teksEmail.setVisibility(View.INVISIBLE);
            email.setVisibility(View.INVISIBLE);
            password.setVisibility(View.INVISIBLE);
            signIn.setVisibility(View.INVISIBLE);
            signOut.setVisibility(View.VISIBLE);
            info1.setText("Anda Telah Sign In");
            info2.setText("Silahkan Sign Out Bila Akses Telah Digunakan");
        }
    }

    private void signIn(String emailTeks, String passwordTeks) {
        Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        mAuth.signInWithEmailAndPassword(emailTeks, passwordTeks)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            teksPass.setVisibility(View.INVISIBLE);
                            teksEmail.setVisibility(View.INVISIBLE);
                            email.setVisibility(View.INVISIBLE);
                            password.setVisibility(View.INVISIBLE);
                            signIn.setVisibility(View.INVISIBLE);
                            signOut.setVisibility(View.VISIBLE);
                            info1.setText("Anda Telah Sign In");
                            info2.setText("Silahkan Sign Out Bila Akses Telah Digunakan");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();
        teksPass.setVisibility(View.VISIBLE);
        teksEmail.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        password.setVisibility(View.VISIBLE);
        signIn.setVisibility(View.VISIBLE);
        signOut.setVisibility(View.INVISIBLE);
        info1.setText("Sign In");
        info2.setText("untuk Akses Admin");
    }

    private boolean validateForm() {
        boolean valid = true;

        String emailTeks = email.getText().toString();
        if (TextUtils.isEmpty(emailTeks)) {
            email.setError("Required.");
            valid = false;
        } else if(emailTeks.equals("asterikrafael@gmail.com")==false) {
            email.setError("Isi Dengan Email Admin");
        }else{
            email.setError(null);
        }

        String passwordTeks = password.getText().toString();
        if (TextUtils.isEmpty(passwordTeks)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }
    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signin) {
            signIn(email.getText().toString(), password.getText().toString());
        } else if (i == R.id.signout) {
            signOut();

        }
    }
}