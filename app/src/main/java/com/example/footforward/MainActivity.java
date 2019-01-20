package com.example.footforward;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.net.*;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private TextView shoeMsrp1;
    private TextView shoeName1;
    private ImageView shoePic1;

    private TextView shoeMsrp2;
    private TextView shoeName2;
    private ImageView shoePic2;

    private TextView shoeMsrp3;
    private TextView shoeName3;
    private ImageView shoePic3;



    DocumentReference adidasYeezy = FirebaseFirestore.getInstance().document("adidas/yeezy_0");
    DocumentReference nikeJordan = FirebaseFirestore.getInstance().document("nike/jordan_0");
    DocumentReference supremeNorth = FirebaseFirestore.getInstance().document("supreme/jacket_0");

    private DocumentReference firebaseDocsYeezy = adidasYeezy;
    private DocumentReference firebaseDocsJordan = nikeJordan;
    private DocumentReference firebaseDocsSupreme = supremeNorth;

    public static final String TAG = "FOOTFORWARD";
    public static final String NAME_KEY = "name";
    public static final String PRICE_KEY = "retailPrice";
    public static final String URL_KEY = "imageUrl";

    static String name = "Missing Name!";
    static String price = "Missing Price!";
    static String url = "";






    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shoeMsrp1 = (TextView)findViewById(R.id.last_sale_1);
        shoeName1 = (TextView)findViewById(R.id.shoe_name_1);
        shoePic1 = (ImageView)findViewById(R.id.shoe_image_1);
        shoeMsrp1.setText(price);
        shoeName1.setText(name);

        shoeMsrp2 = (TextView)findViewById(R.id.last_sale_2);
        shoeName2 = (TextView)findViewById(R.id.shoe_name_2);
        shoePic2 = (ImageView)findViewById(R.id.shoe_image_2);
        shoeMsrp2.setText(price);
        shoeName2.setText(name);

        shoeMsrp3 = (TextView)findViewById(R.id.last_sale_3);
        shoeName3 = (TextView)findViewById(R.id.shoe_name_3);
        shoePic3 = (ImageView)findViewById(R.id.shoe_image_3);
        shoeMsrp3.setText(price);
        shoeName3.setText(name);

        final String pricePref = "MSRP: ";

        firebaseDocsYeezy.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    name = documentSnapshot.getString(NAME_KEY);
                    price = documentSnapshot.getString(PRICE_KEY);
                    url = documentSnapshot.getString(URL_KEY);

                    shoeName1.setText(name);
                    shoeMsrp1.setText(pricePref + price);
                    Picasso.with(getApplicationContext()).load(url).into(shoePic1);
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

        firebaseDocsJordan.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    name = documentSnapshot.getString(NAME_KEY);
                    price = documentSnapshot.getString(PRICE_KEY);
                    url = documentSnapshot.getString(URL_KEY);

                    shoeName2.setText(name);
                    shoeMsrp2.setText(pricePref + price);
                    Picasso.with(getApplicationContext()).load(url).into(shoePic2);
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

        firebaseDocsSupreme.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()){
                    name = documentSnapshot.getString(NAME_KEY);
                    price = documentSnapshot.getString(PRICE_KEY);
                    url = documentSnapshot.getString(URL_KEY);

                    shoeName3.setText(name);
                    shoeMsrp3.setText(pricePref + price);
                    Picasso.with(getApplicationContext()).load(url).into(shoePic3);
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


        CardView shoeYeezy = findViewById(R.id.shoe_card_1);
        shoeYeezy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, YCala.class));
            }
        });

        CardView shoeJordan = findViewById(R.id.shoe_card_2);
        shoeJordan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, JSpace.class));
            }
        });

        CardView shoeSupreme = findViewById(R.id.shoe_card_3);
        shoeSupreme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Supreme.class));
            }
        });
    }


}