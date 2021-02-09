package com.example.ajinafro.search;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ajinafro.R;
import com.example.ajinafro.models.Posts;
import com.example.ajinafro.profile.EditProfile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    FirebaseFirestore db;

    public SearchFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance() {
        SearchFragment fragment = new SearchFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search, container, false);
        Button search_button = (Button) view.findViewById(R.id.button2);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_clicked();
            }
        });
        return view;
    }
    void search_clicked(){
       /* Intent nextpage= new Intent(getActivity().getApplicationContext(), PostsSearchActivity.class);
        getActivity().startActivity(nextpage);*/
        db=FirebaseFirestore.getInstance();
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
                    Intent nextpage= new Intent(getActivity().getApplicationContext(),PostsSearchActivity.class);
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
    }
}