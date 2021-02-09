package com.example.ajinafro.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ajinafro.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PictureAdapter extends RecyclerView.Adapter <PictureAdapter.ItemHolder>{
    private List<Picture> pictures = new ArrayList<>();
    private Context context;
    private final OnPictureClickListener mListener;
    public PictureAdapter (Context context,List<Picture> pictures){
        this.context = context;
        this.pictures=pictures;

        try {
            this.mListener = ((OnPictureClickListener) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement OnUserClickListener.");
        }

    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.picturecarde, viewGroup, false);
        ItemHolder holder = new ItemHolder(view);
        return holder;
    }
    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final Picture picture =  pictures.get(position);

      // holder.mPictureId.setText(""+picture.getPictureId());
        /////////////////////// la photo de de profil de l'utilisateur////////
       // holder.mPictureImage.setImageResource(R.drawable.applogo);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null)
                    mListener.onPictureClickListener(picture);
            }
        });
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView mPictureId;
        public ImageView mPictureImage;
        public View mView;


        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            mPictureImage = itemView.findViewById(R.id.category_photo);
            mPictureId = itemView.findViewById(R.id.category_name);
        }

        @Override
        public void onClick(View v) {}
    }

    public interface OnPictureClickListener {
        void onPictureClickListener(Picture picture);
    }

    @Override
    public int getItemCount() {
        return pictures.size();
    }



}
