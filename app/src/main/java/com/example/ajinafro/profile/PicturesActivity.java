package com.example.ajinafro.profile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.ajinafro.R;

import java.util.ArrayList;
import java.util.List;

public class PicturesActivity extends AppCompatActivity implements PictureAdapter.OnPictureClickListener {
    private RelativeLayout mBack;
    private RecyclerView mRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pictures);
        initialize();
        setupWidgets();
    }
    private void initialize() {
        mBack = findViewById(R.id.retour);
        mRecyclerView = findViewById(R.id.recycler_view);
    }

    private void setupWidgets() {
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        GridLayoutManager lnlGrid = new GridLayoutManager(this, 1);
        mRecyclerView.setLayoutManager(lnlGrid);
        PictureAdapter picsAdapter = new PictureAdapter(this,testData());
        mRecyclerView.setAdapter(picsAdapter);
    }
    @Override
    public void onPictureClickListener(Picture picture) {
        // Intent i = new Intent(getApplicationContext(), Profil_Activity.class);
        // i.putExtra("userid", user.getUserId());
        // startActivity(i);
    }
    public List<Picture> testData(){
        Picture u1= new Picture(1,"");
        Picture u2= new Picture(2,"");

        List<Picture> picslist=new ArrayList<>();
        picslist.add(u1);picslist.add(u2);//userslist.add(u3);
        return picslist;
    }
}