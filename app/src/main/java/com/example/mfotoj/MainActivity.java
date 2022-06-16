package com.example.mfotoj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfotoj.adapters.MainAdapter;
import com.example.mfotoj.models.Lugar;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private MainAdapter mAdapter;
    private ArrayList<Lugar> mLugars;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView)
                findViewById(R.id.main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ////Dumi

        mLugars = new ArrayList<Lugar>();
        Lugar firstLugar = new Lugar();
        firstLugar.comments = "I like water with bubbles most of the time...";
        firstLugar.dateAndTime = new Date();
        //firstDrink.imageUri = "/sdcard/1603945076458selfie.jpg";
        mLugars.add(firstLugar);
        Lugar secondLugar = new Lugar();
        secondLugar.comments = "I also like water without bubbles. It depends on my mood I guess ;-)";
        secondLugar.dateAndTime = new Date();
        mLugars.add(secondLugar);

        ////fin Dumi


        mAdapter = new MainAdapter(this, mLugars);
        recyclerView.setAdapter(mAdapter);

        findViewById(R.id.main_button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEntry();
            }
        });
    }

    //EntryActivity to return data later:
    private int REQUEST_NEW_ENTRY = 1;
    private void showEntry(){
        Intent intent = new Intent(this, EntryActivity.class);
        startActivityForResult(intent, REQUEST_NEW_ENTRY);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_NEW_ENTRY && resultCode == RESULT_OK)
        {
            Bundle bundle = data.getExtras();
            Lugar newLugar = new Lugar();
            newLugar.comments = bundle.getString("comments");
            newLugar.imageUri = bundle.getString("uri");
            newLugar.dateAndTime = new Date();
            mLugars.add(newLugar);
            mAdapter.notifyDataSetChanged();
        }

}
}
