package com.clone.tcardoso.uberclone.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.Toast;

import com.clone.tcardoso.uberclone.R;
import com.clone.tcardoso.uberclone.config.ConfigurationFirebase;
import com.clone.tcardoso.uberclone.helper.UserFirebase;
import com.clone.tcardoso.uberclone.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText fieldName, fieldEmail, fieldPassword;
    private Switch switchTypeUser;
    private FirebaseAuth autentication;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initialize fields
        fieldName = findViewById(R.id.editRegisterName);
        fieldEmail = findViewById(R.id.editRegisterEmail);
        fieldPassword = findViewById(R.id.editRegisterPassword);
        switchTypeUser = findViewById(R.id.switchTypeUser);
    }

    public void validateRegisterUser(View view){

        //recovered test of fields
        String textName = fieldName.getText().toString();
        String textEmail = fieldEmail.getText().toString();
        String textPassword = fieldPassword.getText().toString();

        if( !textName.isEmpty() ) {//verificate name
            if( !textEmail.isEmpty() ) {//verificate e-mail
                if( !textPassword.isEmpty() ) {//verificate password

                    User user = new User();
                    user.setName( textName );
                    user.setEmail( textEmail );
                    user.setPassword( textPassword );
                    user.setType( verificateTypeUser() );
                    
                    registerUser(user);

                }else {
                    Toast.makeText(RegisterActivity.this,
                            "Preencha a senha!",
                            Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(RegisterActivity.this,
                        "Preencha o email!",
                        Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(RegisterActivity.this,
                    "Preencha o nome!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void registerUser(final User user) {

        autentication = ConfigurationFirebase.getFirebaseAuth();
        autentication.createUserWithEmailAndPassword(
                user.getEmail(),
                user.getPassword()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {


                if(task.isSuccessful()){

                    try{


                    String idUser = task.getResult().getUser().getUid();
                    user.setId(idUser);
                    user.register();

                    //update user in firebase
                    UserFirebase.updateNameUser(user.getName());

                    //redirect user according type
                    //if user passenger call maps
                    //else call activity requisition

                    if(verificateTypeUser() == "P")
                    {
                        startActivity(new Intent(RegisterActivity.this, MapsActivity.class));
                        finish();

                        Toast.makeText(RegisterActivity.this,
                                "Sucesso ao cadastrar Passageiro!",
                                Toast.LENGTH_SHORT).show();

                    }else{

                        startActivity(new Intent(RegisterActivity.this, RequisitionActivity.class));
                        finish();

                        Toast.makeText(RegisterActivity.this,
                                "Sucesso ao cadastrar Motorista!",
                                Toast.LENGTH_SHORT).show();
                    }

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{

                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao= "Por favor, digite um e-mail válido";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excecao = "Este conta já foi cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(RegisterActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public String verificateTypeUser(){
        return switchTypeUser.isChecked() ? "M" : "P" ;
    }
}
