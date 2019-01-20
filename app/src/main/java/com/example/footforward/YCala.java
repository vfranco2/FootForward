package com.example.footforward;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.*;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;

public class YCala extends AppCompatActivity {

    private TextView shoeMsrp;
    private TextView shoeName;
    private TextView shoeRelease;
    private TextView shoeHi;
    private TextView shoeLo;
    private ImageView shoePic;

    LineChartView lineChartView;
    String[] axisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
    int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 90, 18};

    DocumentReference adidasYeezy = FirebaseFirestore.getInstance().document("adidas/yeezy_0");

    private DocumentReference firebaseDocs = adidasYeezy;

    public static final String TAG = "FOOTFORWARD";
    public static final String NAME_KEY = "name";
    public static final String PRICE_KEY = "retailPrice";
    public static final String HI_KEY = "high";
    public static final String LO_KEY = "low";
    public static final String RELEASE_KEY = "releaseDate";
    public static final String URL_KEY = "imageUrl";

    static String name = "Missing Name!";
    static String price = "Missing Price!";
    static String release = "Missing Date!";
    static String hi = "Missing Price!";
    static String lo = "Missing Price!";
    static String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shoe_layout);

        shoeMsrp = (TextView)findViewById(R.id.shoe_msrp);
        shoeName = (TextView)findViewById(R.id.shoe_name);
        shoeRelease = (TextView)findViewById(R.id.shoe_release);
        shoeHi = (TextView)findViewById(R.id.shoe_high);
        shoeLo = (TextView)findViewById(R.id.shoe_low);
        shoePic = (ImageView)findViewById(R.id.shoe_pic);

        shoeMsrp.setText(price);
        shoeName.setText(name);
        shoeRelease.setText(release);
        shoeHi.setText(hi);
        shoeLo.setText(lo);

        //Linechart stuff
        lineChartView = findViewById(R.id.chart);
        List yAxisValues = new ArrayList();
        List axisValues = new ArrayList();
        Line line = new Line(yAxisValues).setColor(Color.parseColor("#73BD76"));
        for(int i = 0; i < axisData.length; i++){
            axisValues.add(i, new AxisValue(i).setLabel(axisData[i]));
        }
        for (int i = 0; i < yAxisData.length; i++){
            yAxisValues.add(new PointValue(i, yAxisData[i]));
        }
        List lines = new ArrayList();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        lineChartView.setLineChartData(data);
        //Axis labels
        Axis axis = new Axis();
        axis.setValues(axisValues);
        axis.setTextSize(16);
        axis.setTextColor(Color.parseColor("#03A9F4"));
        data.setAxisXBottom(axis);
        Axis yAxis = new Axis();
        yAxis.setName("Price");
        yAxis.setTextColor(Color.parseColor("#03A9F4"));
        yAxis.setTextSize(16);
        data.setAxisYLeft(yAxis);
        lineChartView.setLineChartData(data);
        Viewport viewport = new Viewport(lineChartView.getMaximumViewport());
        viewport.top = 110;
        lineChartView.setMaximumViewport(viewport);
        lineChartView.setCurrentViewport(viewport);
        lineChartView.setZoomEnabled(false);
        lineChartView.setValueSelectionEnabled(true);
        line.setFilled(true);
        line.setHasLabelsOnlyForSelected(true);

        final String pricePref = "Initial Price: ";
        final String relPref = "Release Date: ";
        final String highPref = "52-Week High: ";
        final String lowPref = "52-Week Low: ";

            firebaseDocs.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()){
                        name = documentSnapshot.getString(NAME_KEY);
                        price = documentSnapshot.getString(PRICE_KEY);
                        release = documentSnapshot.getString(RELEASE_KEY);
                        hi = documentSnapshot.getString(HI_KEY);
                        lo = documentSnapshot.getString(LO_KEY);
                        url = documentSnapshot.getString(URL_KEY);

                        shoeName.setText(name);
                        shoeRelease.setText(relPref + release);
                        shoeMsrp.setText(pricePref + price);
                        shoeHi.setText(highPref + hi);
                        shoeLo.setText(lowPref + lo);
                        Picasso.with(getApplicationContext()).load(url).into(shoePic);

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