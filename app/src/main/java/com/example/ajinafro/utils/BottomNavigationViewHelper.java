package com.example.ajinafro.utils;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.ajinafro.R;
import com.example.ajinafro.models.Posts;
import com.example.ajinafro.profile.ProfilActivity;
import com.example.ajinafro.search.PostsSearchActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class BottomNavigationViewHelper {

    private static final String TAG = "BottomNavigationViewHel";
    FirebaseFirestore db;

    public static void setupBottomNavigationView(BottomNavigationView bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
       // bottomNavigationViewEx.enableAnimation(false);
      //  bottomNavigationViewEx.enableItemShiftingMode(true);
      //  bottomNavigationViewEx.enableShiftingMode(false);
      //  bottomNavigationViewEx.setTextSize(10);
    }

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationView view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.home:
                     //   Intent intent1 = new Intent(context, HomeActivity.class); //ACTIVITY_NUM = 0
                     //   context.startActivity(intent1);
                      //  callingActivity.overridePendingTransition(R.anim.fade, R.anim.fade);
                      //  break;

                    case R.id.search:

                        FirebaseFirestore db= FirebaseFirestore.getInstance();
                        db.collection("posts")
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if( task.isSuccessful() ){
                                    ArrayList<Posts> myPosts=new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        //Log.d(TAG, document.getId() + " => " + document.getData());
                                        myPosts.add(document.toObject(Posts.class));
                                    }
                                    //open MyPostActivity()
                                    Intent nextpage= new Intent(context,PostsSearchActivity.class);
                                    ArrayList<String> myPostString=new ArrayList<>();
                                    for (Posts post: myPosts){
                                        Gson jsonpost=new Gson();
                                        String item=jsonpost.toJson(post);
                                        myPostString.add(item);
                                    }
                                    nextpage.putExtra("myPosts",myPostString);
                                    context.startActivity(nextpage);
                                }
                            }
                        });
                       callingActivity.overridePendingTransition(R.anim.fade, R.anim.fade);
                       Toast.makeText(context, "Recherche", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.newpost:
//                        Intent intent3 = new Intent(context, PlaceActivity.class); //ACTIVITY_NUM = 2
//                        context.startActivity(intent3);
//                        callingActivity.overridePendingTransition(R.anim.fade, R.anim.fade);
                        break;

                    case R.id.covoiturage:
                      //  Intent profileIntent = new Intent(context, ProfileActivity.class); //ACTIVITY_NUM = 3
                      //  context.startActivity(profileIntent);
                      //  callingActivity.overridePendingTransition(R.anim.fade, R.anim.fade);
                        break;
                    case R.id.profile:
                        Intent profileIntent = new Intent(context, ProfilActivity.class); //ACTIVITY_NUM = 3
                        context.startActivity(profileIntent);
                        callingActivity.overridePendingTransition(R.anim.fade, R.anim.fade);
                        Toast.makeText(context, "Profil", Toast.LENGTH_SHORT).show();
                        break;
                }

                return false;
            }

        });
    }
}

