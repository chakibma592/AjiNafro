package com.example.ajinafro.search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.icu.text.Edits;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ajinafro.PermissionUtils;
import com.example.ajinafro.R;
import com.example.ajinafro.models.Category;
import com.example.ajinafro.models.Posts;
import com.example.ajinafro.utils.BottomNavigationViewHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PostsSearchActivity extends AppCompatActivity implements PostsAdapter.OnPlaceClickListener,
    HomeCategoriesAdapter.OnCategoryClickListener , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{
    private RecyclerView mCategoryRecycler, mPlaceRecycler;
    private HomeCategoriesAdapter mCategoriesAdapter;
    private PostsAdapter mPlaceAdapter;
    private TextView mPlaceList, mCategoriesList,mTitle;
    private ArrayList<Posts> mPosts= new ArrayList<>();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private GoogleMap googleMap;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    PendingResult<LocationSettingsResult> result;
    final static int REQUEST_LOCATION = 199;
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = PostsSearchActivity.this;
    private static final String TAG = "PostsSearchActivity";
    private SearchView mSearch;
    private RelativeLayout mBack;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_search);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).build();
        mGoogleApiClient.connect();
        mPosts= new ArrayList<>();
        ArrayList <String> listGson = getIntent().getStringArrayListExtra("myPosts");
        Iterator it= listGson.iterator();
        while(it.hasNext()){
            String postGson= (String) it.next();
            if(postGson != null){
                Gson gson = new Gson();
                Posts post = gson.fromJson(postGson , Posts.class);

                mPosts.add(post);
            }
        }
        //Log.d(TAG, "size: "+mPosts.size());
       // mPosts = (ArrayList<Posts>) getIntent().getSerializableExtra("myPosts");
        initComponents();
        setupWidgets();
        setupBottomNavigationView();
    }
    //    BottomNavigationView setup
    private void setupBottomNavigationView(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationView);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
    private void initComponents() {
        mCategoryRecycler = findViewById(R.id.trending_recycler_view);
        mPlaceRecycler = findViewById(R.id.place_recycler_view);
        mPlaceList = findViewById(R.id.place_list);
        mCategoriesList = findViewById(R.id.categories_list);
        mSearch = findViewById(R.id.search_text);
        mTitle = findViewById(R.id.toolbar_title);
        mBack=findViewById(R.id.retour);

    }

    private void setupWidgets() {

        LinearLayoutManager llmTrending = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mCategoryRecycler.setLayoutManager(llmTrending);
        mCategoriesAdapter = new HomeCategoriesAdapter(this);
        mCategoryRecycler.setAdapter(mCategoriesAdapter);
        /////////////////
        mTitle.setText("Recherche");
        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPlaceRecycler.setLayoutManager(llmPlace);
       // mPosts= getPostLlist();


        mPlaceAdapter = new PostsAdapter(this,mPosts,getMyLocation());

        mPlaceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(PostsSearchActivity.this, PlacesListActivity.class));
            }

        });
        ////////////////
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mSearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                mPlaceAdapter.getFilter().filter(newText);
                return false;
            }
        });
        mPlaceRecycler.setAdapter(mPlaceAdapter);
////////////
        mCategoriesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              // startActivity(new Intent(HomeActivity.this, CategoriesListActivity.class));
            }
        });
    }
    @Override
    public void onCategoryClickListener(Category category) {
      /*  Intent i = new Intent(HomeActivity.this, PlacesListActivity.class);
        i.putExtra(ARG_CATEGORY_NAME, category.getCategoryName());
        startActivity(i);*/
        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPlaceRecycler.setLayoutManager(llmPlace);
        // mPosts= getPostLlist();
        if(category.getCategoryName().equals("All")) {
            mPlaceAdapter = new PostsAdapter(this, mPosts, getMyLocation());
        }else if(category.getCategoryName().equals("Cafe")) {
            ArrayList<Posts> postList= new ArrayList<>();
            Iterator it= mPosts.iterator();
            while(it.hasNext()){
                Posts post= (Posts)it.next();
                if(post.getCategory().equals("Cafe")) {
                    postList.add(post);
                }
            }
            mPlaceAdapter = new PostsAdapter(this, postList, getMyLocation());
        }else if(category.getCategoryName().equals("Hotel")) {
            ArrayList<Posts> postList= new ArrayList<>();
            Iterator it= mPosts.iterator();
            while(it.hasNext()){
                Posts post= (Posts)it.next();
                if(post.getCategory().equals("Hotel")) {
                    postList.add(post);
                }
            }
            mPlaceAdapter = new PostsAdapter(this, postList, getMyLocation());
        }else if(category.getCategoryName().equals("Sushi")) {
            ArrayList<Posts> postList= new ArrayList<>();
            Iterator it= mPosts.iterator();
            while(it.hasNext()){
                Posts post= (Posts)it.next();
                if(post.getCategory().equals("Sushi")) {
                    postList.add(post);
                }
            }
            mPlaceAdapter = new PostsAdapter(this, postList, getMyLocation());
        }else if(category.getCategoryName().equals("Restaurant")) {
            ArrayList<Posts> postList= new ArrayList<>();
            Iterator it= mPosts.iterator();
            while(it.hasNext()){
                Posts post= (Posts)it.next();
                if(post.getCategory().equals("Restaurant")) {
                    postList.add(post);
                }
            }
            mPlaceAdapter = new PostsAdapter(this, postList, getMyLocation());
        }else if(category.getCategoryName().equals("Monument Historique")) {
            ArrayList<Posts> postList= new ArrayList<>();
            Iterator it= mPosts.iterator();
            while(it.hasNext()){
                Posts post= (Posts)it.next();
                if(post.getCategory().equals("Cafe")) {
                    postList.add(post);
                }
            }
            mPlaceAdapter = new PostsAdapter(this, postList, getMyLocation());
        }

        mPlaceRecycler.setAdapter(mPlaceAdapter);
        mPlaceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // startActivity(new Intent(PostsSearchActivity.this, PlacesListActivity.class));
            }

        });

    }


    @Override
    public void onPlaceClickListener(Posts post) {
        //  startActivity(new Intent(HomeActivity.this, PlaceActivity.class));
    }

    @Override
    public void onPlaceFavoriteClick(Posts place) {
       // mPlaceAdapter.setFavorite(place.getPlaceId());
      //  mPlaceAdapter.notifyDataSetChanged();
    }

    private Location getMyLocation()
    {
        Location location=null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            enableMyLocation();
        } else
        {
//            googleMap.setMyLocationEnabled(true);
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));

        }
        return location;
    }
    // need for android 6 and above
    private void enableMyLocation()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE, Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (googleMap != null)
        {
            googleMap.setMyLocationEnabled(true);
        }
    }

    // need for android 6 and above
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE)
        {
            return;
        }
        if (PermissionUtils.isPermissionGranted(permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION))
        {
            enableMyLocation();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(30 * 1000);
        mLocationRequest.setFastestInterval(5 * 1000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);

        result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(
                                    PostsSearchActivity.this,
                                    REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:

                        break;
                }
            }
        });

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult()", Integer.toString(resultCode));
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK: {
                        Toast.makeText(PostsSearchActivity.this,"Localisation Activée", Toast.LENGTH_LONG).show();
                        break;
                    }
                    case Activity.RESULT_CANCELED: {
                        Toast.makeText(PostsSearchActivity.this, "Localisation Annulée", Toast.LENGTH_LONG).show();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                break;
        }
    }
    public interface MyCallback {

        void onCallback(List<Posts> postList);
    }

}