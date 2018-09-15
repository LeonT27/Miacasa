package com.ltolentino.miacasa.Util;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ltolentino.miacasa.R;
import com.ltolentino.miacasa.activity.MainUserActivity;

import java.util.ArrayList;
import java.util.Map;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostItemViewHolder> {

    final ArrayList<Map<String, Object>> datos;
    final Context context;

    public PostAdapter(Context context, ArrayList<Map<String, Object>> datos) {
        this.context = context;
        this.datos = datos;
    }

    @NonNull
    @Override
    public PostItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_post, viewGroup, false);
        PostItemViewHolder postItemViewHolder = new PostItemViewHolder(view);
        return postItemViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostItemViewHolder holder, final int i) {
        Glide.with(holder.itemView)
                .load(this.datos.get(i).get("image").toString())
                .into(holder.image_post);
        holder.desc.setText(this.datos.get(i).get("description").toString());
        holder.location.setText(this.datos.get(i).get("location").toString());
        holder.email.setText("Contacto: " + this.datos.get(i).get("email").toString());

        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = datos.get(i).get("description").toString() + " - " +datos.get(i).get("location").toString() + " Contacto: " + datos.get(i).get("email").toString();
                String shareSub = datos.get(i).get("description").toString();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Comparte con:"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return datos.size();
    }

    public static class PostItemViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_post;
        public TextView desc;
        public TextView location;
        public Button share;
        public TextView email;

        public PostItemViewHolder(View itemView) {
            super(itemView);
            image_post = itemView.findViewById(R.id.image_post);
            desc = itemView.findViewById(R.id.description);
            location = itemView.findViewById(R.id.location);
            share = itemView.findViewById(R.id.share);
            email = itemView.findViewById(R.id.email);
        }
    }
}
