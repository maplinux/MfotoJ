package com.example.mfotoj.adapters;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfotoj.EntryActivity;
import com.example.mfotoj.R;
import com.example.mfotoj.models.Drink;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private ArrayList<Drink> mDrinks;
    private Context mContext;




    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCommentTextView;
        public TextView mDateTimeTextView;
        public ImageView mImageView;
        public Drink mDrink;

        public ViewHolder(View v) {
            super(v);
        }
    }

    public MainAdapter(Context context,
                       ArrayList<Drink> drinks) {
        mDrinks = drinks;
        mContext = context;
    }


    private int REQUEST_EDIT_ENTRY = 2;

    @NonNull
    @Override
    public MainAdapter.ViewHolder
    onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate( R.layout.adapter_main_card_view, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.mDateTimeTextView =  (TextView) v.findViewById(  R.id.main_date_time_textview);
        viewHolder.mCommentTextView =   (TextView) v.findViewById( R.id.main_comment_textview);
        viewHolder.mImageView =         (ImageView) v.findViewById( R.id.main_image_view);

        //pasar abajo return viewHolder; --- ok

        v.setTag(viewHolder);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewHolder holder = (ViewHolder) view.getTag();
                if (view.getId() == holder.itemView.getId())
                {
                    Intent intent = new Intent(mContext, EntryActivity.class);
                    intent.putExtra("edit_drink", holder.mDrink);
                    ((Activity)mContext).startActivityForResult(intent, REQUEST_EDIT_ENTRY);
                }
            }
        });


        return viewHolder;
       }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Drink currentDrink = mDrinks.get(position);

        holder.mDrink = currentDrink;

        holder.mCommentTextView.setText(  currentDrink.comments);
        holder.mDateTimeTextView.setText(  currentDrink.dateAndTime.toString());
        if (currentDrink.imageUri != null){
            holder.mImageView.setImageURI(
                    Uri.parse(currentDrink.imageUri));


            ////////////
            if (currentDrink.imageUri != null){
                Bitmap bitmap =
                        getBitmapFromUri(Uri.parse(currentDrink.imageUri));
                holder.mImageView.setImageBitmap(bitmap);
             /////////////////////
            }
        }

    }

    public Bitmap getBitmapFromUri(Uri uri) {
        mContext.getContentResolver().notifyChange(uri, null);
        ContentResolver cr = mContext.getContentResolver();
        try {
            Bitmap bitmap =
                    android.provider.MediaStore.Images.Media.getBitmap(cr, uri);
            return bitmap;
        }
        catch (Exception e) {
            Toast.makeText(mContext, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            return null;
        }
    }


    @Override
    public int getItemCount() {
        return mDrinks.size();
    }
}