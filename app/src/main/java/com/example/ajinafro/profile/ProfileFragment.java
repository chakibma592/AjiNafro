package com.example.ajinafro.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ajinafro.R;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {


    private static final String TAG ="PROFILE Fragment : " ;
    private FirebaseFirestore db;

    public ProfileFragment() {
    }


    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        db=FirebaseFirestore.getInstance();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        Button edit_button=(Button) view.findViewById(R.id.editprofil);
        Button view_button=(Button) view.findViewById(R.id.fragment_profile_viewprofile_btn);
        view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view_profil_clicked();
            }
        });
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_profil_clicked();
            }
        });
        return view;
    }

    void edit_profil_clicked(){
        Intent nextpage= new Intent(getActivity().getApplicationContext(),EditProfile.class);
        getActivity().startActivity(nextpage);
    }
    void view_profil_clicked(){
        Intent nextpage= new Intent(getActivity().getApplicationContext(), ProfilActivity.class);
        getActivity().startActivity(nextpage);

    }




}