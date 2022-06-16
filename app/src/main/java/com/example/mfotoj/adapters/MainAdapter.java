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
import com.example.mfotoj.models.Lugar;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private ArrayList<Lugar> mLugars;
    private Context mContext;




    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mCommentTextView;
        public TextView mDateTimeTextView;
        public ImageView mImageView;
        public Lugar mLugar;

        public ViewHolder(View v) {
            super(v);
        }
    }

    public MainAdapter(Context context,
                       ArrayList<Lugar> lugars) {
        mLugars = lugars;
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
                    intent.putExtra("edit_drink", holder.mLugar);
                    ((Activity)mContext).startActivityForResult(intent, REQUEST_EDIT_ENTRY);
                }
            }
        });


        return viewHolder;
       }




    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Lugar currentLugar = mLugars.get(position);

        holder.mLugar = currentLugar;

        holder.mCommentTextView.setText(  currentLugar.comments);
        holder.mDateTimeTextView.setText(  currentLugar.dateAndTime.toString());
        if (currentLugar.imageUri != null){
            holder.mImageView.setImageURI(
                    Uri.parse(currentLugar.imageUri));


            ////////////
            if (currentLugar.imageUri != null){
                Bitmap bitmap =
                        getBitmapFromUri(Uri.parse(currentLugar.imageUri));
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
        return mLugars.size();
    }
}