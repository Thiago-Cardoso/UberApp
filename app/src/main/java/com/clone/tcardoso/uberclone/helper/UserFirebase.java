package com.clone.tcardoso.uberclone.helper;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.clone.tcardoso.uberclone.activity.MapsActivity;
import com.clone.tcardoso.uberclone.activity.RequisitionActivity;
import com.clone.tcardoso.uberclone.config.ConfigurationFirebase;
import com.clone.tcardoso.uberclone.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class UserFirebase {

    public static FirebaseUser getUserCurrenty(){
            FirebaseAuth user = ConfigurationFirebase.getFirebaseAuth();

            return user.getCurrentUser();
    }

    public static boolean updateNameUser(String name){

        try{

            FirebaseUser user = getUserCurrenty();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfiç", "Erro ao atualizar o nome de perfil");
                    }
                }
            });

            return true;

        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static void redirectUserLogged(final Activity activity){

        FirebaseUser user = getUserCurrenty();
        if(user != null) {

            final DatabaseReference userRef = ConfigurationFirebase.getFirebaseDatabase()
                    .child("users")
                    .child(getIdentificationUser());

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.getValue(User.class);

                    String typeUser = user.getType();
                    if (typeUser.equals("M")) {
                        Intent i = new Intent(activity, RequisitionActivity.class);
                        activity.startActivity(i);
                    } else {
                        Intent i = new Intent(activity, MapsActivity.class);
                        activity.startActivity(i);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public static String getIdentificationUser(){
        return getUserCurrenty().getUid();
    }
}
