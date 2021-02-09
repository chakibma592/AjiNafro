package com.example.ajinafro.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ajinafro.R;
import com.example.ajinafro.models.Posts;
import com.example.ajinafro.models.UserAccountDetails;
import com.example.ajinafro.search.PostsSearchActivity;
import com.example.ajinafro.utils.BottomNavigationViewHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {
    FirebaseUser currentUser;
    private static final int ACTIVITY_NUM = 0;
    FirebaseFirestore db;
    String UserUid;
    SharedPreferences ref;
    UserAccountDetails userAccountDetails;
    Context context;
    int userId;
    @BindView(R.id.editprofil)
    LinearLayout editprofil;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.gsm)
    TextView gsm;
    @BindView(R.id.followers)
    LinearLayout followers;
    @BindView(R.id.following)
    LinearLayout following;
    @BindView(R.id.mail)
    TextView mail;
    @BindView(R.id.comment)
    TextView comment;
    @BindView(R.id.location)
    TextView location;
    @BindView(R.id.photo)
    CircleImageView photo;
    @BindView(R.id.retour)
    RelativeLayout back;

    @Override
    public void onStart() {
        super.onStart();
        db=FirebaseFirestore.getInstance();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        ButterKnife.bind(this);
        /// les données sur l utilisateur doivent etre recuperer ici est affecter à l objet utilisateur
        context=this.context;
        /////////////////////////////////////////////////////////////////////////////////:
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////
        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        ref=getSharedPreferences("ajinsafro",MODE_PRIVATE);
        UserUid=ref.getString("userUid"," ");
        db=FirebaseFirestore.getInstance();
        db.collection("users_details").document(UserUid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                onSuccessFill(documentSnapshot);
            }
        });

        /////////////////////////////////////////////////////////////////////////////////////////////////


        /*if(user.getPicturesList()!=null)
            pics.setText(user.getPicturesList().size()+"");
        else
            pics.setText("0");*/

        //photo.setImageResource(R.drawable.applogo);
        setupBottomNavigationView();

    }
    //    BottomNavigationView setup
    private void setupBottomNavigationView(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(this, this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    @OnClick (R.id.editprofil)
    void onClickEditProfil(){
        Intent nextpage= new Intent(this.getApplicationContext(),EditProfile.class);
        this.startActivity(nextpage);
        this.overridePendingTransition(R.anim.fade, R.anim.fade);
//        Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
    }
    @OnClick (R.id.followers)
    void onClicklisFollowers(){
        Intent i = new Intent(getApplicationContext(), FollowersActivity.class);
        i.putExtra("userid", userId);
        startActivity(i);
    }
    @OnClick (R.id.following)
    void onClicklisFollowing(){
        db=FirebaseFirestore.getInstance();
        db.collection("posts").whereEqualTo("publisher",UserUid)
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
                    Intent nextpage= new Intent(getApplicationContext(), PostsSearchActivity.class);
                    ArrayList<String> myPostString=new ArrayList<>();
                    for (Posts post: myPosts){
                        Gson jsonpost=new Gson();
                        String item=jsonpost.toJson(post);
                        myPostString.add(item);
                    }
                    nextpage.putExtra("myPosts",myPostString);
                    startActivity(nextpage);
                }
            }
        });
        this.overridePendingTransition(R.anim.fade, R.anim.fade);
      //  Toast.makeText(context, "Mes posts", Toast.LENGTH_SHORT).show();
    }


public void onSuccessFill(DocumentSnapshot documentSnapshot){
    userAccountDetails = documentSnapshot.toObject(UserAccountDetails.class);
    Gson gson = new Gson();
    String userJson = gson.toJson(userAccountDetails);
    name.setText(userAccountDetails.getFullname());

   /* if(userAccountDetails.getPosts()!=null){
        followers.setText(userAccountDetails.getPosts().indexOf(0));
    }else{
        followers.setText(0);
    }*/
    gsm.setText(userAccountDetails.getPhone());
    mail.setText(userAccountDetails.getEmail());
    comment.setText(userAccountDetails.getBio());
    location.setText(userAccountDetails.getCity());
   // pics.setText(userAccountDetails.getSaved_posts().size());
    Glide.with(this)
            .load(Uri.parse(userAccountDetails.getProfile_image()))
            .centerCrop()
            .placeholder(R.drawable.default_profile)
            .into(photo);
}
}
