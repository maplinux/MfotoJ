package com.example.mfotoj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mfotoj.adapters.MainAdapter;
import com.example.mfotoj.models.Drink;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private MainAdapter mAdapter;
    private ArrayList<Drink> mDrinks;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView)
                findViewById(R.id.main_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ////Dumi
        mDrinks = new ArrayList<Drink>();
        Drink firstDrink = new Drink();
        firstDrink.comments = "I like water with bubbles most of the time...";
        firstDrink.dateAndTime = new Date();
        //firstDrink.imageUri = "/sdcard/1603945076458selfie.jpg";
        mDrinks.add(firstDrink);
        Drink secondDrink = new Drink();
        secondDrink.comments = "I also like water without bubbles. It depends on my mood I guess ;-)";
        secondDrink.dateAndTime = new Date();
        mDrinks.add(secondDrink);

        ////fin Dumi

        mAdapter = new MainAdapter(this, mDrinks);
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
            Drink newDrink = new Drink();
            newDrink.comments = bundle.getString("comments");
            newDrink.imageUri = bundle.getString("uri");
            newDrink.dateAndTime = new Date();
            mDrinks.add(newDrink);
            mAdapter.notifyDataSetChanged();
        }

}
}
