package com.example.ajinafro.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajinafro.R;
import com.example.ajinafro.models.Category;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HomeCategoriesAdapter extends RecyclerView.Adapter<HomeCategoriesAdapter.ItemHolder>{
    private List<Category> categories = new ArrayList<>();
    private Context context;
    private final OnCategoryClickListener mListener;

    public HomeCategoriesAdapter(Context context){
        this.context = context;

        try {
            this.mListener = ((OnCategoryClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnPlaceClickListener.");
        }

        String[] categoryNames = {"All","Cafe", "Sushi",
                "Restaurant","Hotel", "Monument"};

        int images_array[] = {
                R.drawable.applogo,
                R.drawable.coffe_categorie_image,
                R.drawable.sushi_categorie_image,
                R.drawable.restaurant_categorie_image,
                R.drawable.hotel_categorie_image,
                R.drawable.monument_categorie_image,

        };

        for (int i = 0; i < 5; i++){
            Category category = new Category(categoryNames[i], images_array[i]);
            categories.add(category);
        }
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_card, viewGroup, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final Category category =  categories.get(position);

        holder.mCategoryName.setText(category.getCategoryName());

        holder.mCategoryImage.setImageResource(category.getCategoryDrawable());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onCategoryClickListener(category);
            }
        });
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mCategoryName;
        public ImageView mCategoryImage;
        public View mView;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mCategoryName = itemView.findViewById(R.id.category_name);
            mCategoryImage = itemView.findViewById(R.id.category_photo);
        }

        @Override
        public void onClick(View v) {}
    }

    public interface OnCategoryClickListener {
        void onCategoryClickListener(Category category);
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }
}
