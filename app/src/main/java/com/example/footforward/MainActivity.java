package com.example.footforward;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.net.*;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    //private DrawerLayout mDrawerLayout;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<PriceHolder> priceList;
    private PriceAdapter priceAdapter;

    String[] shoeName = {"Content not found!", "Content not found!", "Content not found!"};

    static String[] shoePrice = {"Content not found!", "Content not found!", "Content not found!"};

    String[] shoeURL = {"", "", ""};

    String[] shoeImage = {
            "https://imgplaceholder.com/420x320/ff7f7f/333333/fa-image",
            "https://imgplaceholder.com/420x320/ff7f7f/333333/fa-image",
            "https://imgplaceholder.com/420x320/ff7f7f/333333/fa-image"};


    //Firebase references
    DocumentReference shoe1 = FirebaseFirestore.getInstance().document("shoe1/card_0");
    DocumentReference shoe2 = FirebaseFirestore.getInstance().document("shoe2/card_0");
    DocumentReference shoe3 = FirebaseFirestore.getInstance().document("shoe3/card_0");

    private DocumentReference[] firebaseDocs = {shoe1, shoe2, shoe3};

    //Used for getting data fields in Firebase
    public static final String TAG = "FOOTFORWARD";
    public static final String NAME_KEY = "name";
    public static final String PRICE_KEY = "price";
    public static final String URL_KEY = "url";
    public static final String IMAGE_KEY = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);

        //Recycler view stuff, fills with default images/text
        mRecyclerView.setLayoutManager(mLayoutManager);
        priceList = new ArrayList<>();

        for (int i = 0; i < shoeName.length; i++){
            PriceHolder article = new PriceHolder(shoeName[i], shoePrice[i], shoeURL[i], shoeImage[i]);
            priceList.add(article);
        }

        priceAdapter = new PriceAdapter(priceList, getApplicationContext());
        mRecyclerView.setAdapter(priceAdapter);
        priceAdapter.notifyDataSetChanged();



        //Firebase stuff, assigns scraped data to cards
        for (int k = 0; k < firebaseDocs.length; k++){
            final int j = k;
            firebaseDocs[j].get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        shoePrice[j] = documentSnapshot.getString(PRICE_KEY);
                        shoeName[j] = documentSnapshot.getString(NAME_KEY);
                        shoeURL[j] = documentSnapshot.getString(URL_KEY);
                        shoeImage[j] = documentSnapshot.getString(IMAGE_KEY);

                        //Re-populate the view with database content at that location
                        PriceHolder article = new PriceHolder(shoePrice[j], shoeName[j], shoeURL[j], shoeImage[j]);
                        priceList.set(j, article);

                        //Click handling for each article
                        priceAdapter.setOnItemClickListener(new PriceAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(int position) {
                                Toast.makeText(getApplicationContext(), "You clicked " + position, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Log.d(TAG, "Error: Document does not exist.");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d(TAG, "Document was not retrieved.", e);
                }
            });
        }
    }
}