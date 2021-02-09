package com.example.ajinafro.search;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ajinafro.R;
import com.example.ajinafro.models.Posts;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ItemHolder> implements Filterable {
    private static String TAG= "postAdapter: ";
    ArrayList<Posts> arrayListFiltered;
    private List<Posts> mPosts ;
    private Context context;
    private final OnPlaceClickListener mListener;
    FirebaseFirestore db;
    private static DecimalFormat df2 = new DecimalFormat("#.##");
    private Location location;
    public PostsAdapter(Context context,ArrayList<Posts> mPosts,Location location){
        this.context = context;
        this.mPosts = mPosts;
        this.arrayListFiltered = mPosts;
        this.location=location;

        try {
            this.mListener = ((OnPlaceClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnPlaceClickListener.");
        }


      /*  readData(new MyCallback() {
            @Override
            public void onCallback(List<Posts> postList) {
                // Log.d("TAG", eventList.toString);
               // this.mPosts=postList;
               // Log.d("TAG", " callback "+postList.size());

            }
        });*/
       // Log.d("TAG", "aprés le callback "+mPosts.size());

    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_card, viewGroup, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
        final Posts place =  mPosts.get(position);
        holder.mItem = place;
       // holder.postimage.
        Glide.with(context)
                .load(Uri.parse(place.getPhoto().get(0)))
                .centerCrop()
                .placeholder(R.drawable.default_profile)
                .into(holder.postimage);
        holder.placeName.setText(place.getName());
        if(location!=null){
        double d= SphericalUtil.computeDistanceBetween(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(place.getAdresse().getLatitude(), place.getAdresse().getLongitude()));
        String  mesureUnit="m";
        if(d>=1000.0){
            d=d/1000;
            mesureUnit="km";}
        holder.placeLocation.setText(place.getAdresse().getVille()+"-"+df2.format(d) + " "+mesureUnit);}
        else{
            holder.placeLocation.setText(place.getAdresse().getVille());
        }
       // holder.placeLocation.setText(place.getAdresse().toString());

        if(place.getLikes()!=null)
       holder.placeRating.setText(place.getLikes().size());
        else
        holder.placeRating.setText("0");
        holder.icFavorite.setImageResource(R.drawable.star2);
        holder.lnlFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener  != null)
                    mListener.onPlaceFavoriteClick(place);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onPlaceClickListener(place);
            }
        });
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView placeName, placeLocation, placeRating, placeDelivery;
        public RelativeLayout lnlFavorite;
        public ImageView icFavorite,postimage;
        public final View mView;
        public Posts mItem;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            placeName = itemView.findViewById(R.id.place_name);
            placeLocation = itemView.findViewById(R.id.place_location);
            placeRating = itemView.findViewById(R.id.place_rating);
            placeDelivery = itemView.findViewById(R.id.place_delivery);
            lnlFavorite = itemView.findViewById(R.id.lnl_favorite);
            icFavorite = itemView.findViewById(R.id.ic_favorite);
            postimage = itemView.findViewById(R.id.postimage);

        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "Clicked Item", Toast.LENGTH_SHORT).show();
        }
    }

    public interface OnPlaceClickListener {
        void onPlaceClickListener(Posts place);
        void onPlaceFavoriteClick(Posts place);
    }
    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                ArrayList<Posts> arrayListFilter = new ArrayList<Posts>();

                if(constraint == null|| constraint.length() == 0) {
                    results.count = mPosts.size();
                    results.values = mPosts;
                } else {
                    for (Posts itemModel : mPosts) {


                        if(itemModel.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                            arrayListFilter.add(itemModel);

                        }
                    }
                    results.count = arrayListFilter.size();
                    results.values = arrayListFilter;

                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                arrayListFiltered = (ArrayList<Posts>) results.values;
                notifyDataSetChanged();

                if(arrayListFiltered.size() == 0) {
                    Toast.makeText(context, "Non trouvé", Toast.LENGTH_LONG).show();
                }

            }
        };
        return filter;
    }
    @Override
    public int getItemCount() {
        return arrayListFiltered.size();
    }


   /* public void readData(MyCallback myCallback) {
        db=FirebaseFirestore.getInstance();
        db.collection("posts")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if( task.isSuccessful() ){
                    ArrayList<Posts> myPosts=new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                      //  Log.d(TAG, " first try "+myPosts.size());
                        myPosts.add(document.toObject(Posts.class));
                    }

                            myCallback.onCallback(myPosts);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });
    }
    public interface MyCallback {
        void onCallback(List<Posts> postList);
    }*/
}
