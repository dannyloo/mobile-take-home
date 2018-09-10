package com.guestlogixtest.danny.takehometest;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.DoubleBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.R.attr.label;
import static com.guestlogixtest.danny.takehometest.R.id.map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String startLoc;
    ArrayList<String> endLoc = new ArrayList<>();
    ArrayList<String> airlineId = new ArrayList<>();

    List name = new ArrayList();
    List city = new ArrayList();
    List country = new ArrayList();
    ArrayList<String > IATA = new ArrayList();
    ArrayList<String> latitude = new ArrayList();
    ArrayList<String > longitude = new ArrayList();
    ArrayList<Double> airportLat = new ArrayList<>();
    ArrayList<Double> airportLong = new ArrayList<>();
    ArrayList<Integer> endIndex = new ArrayList<>();
    int startIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        loadAirports();

        Bundle extras = getIntent().getExtras();
        if (extras != null){
            endLoc = (ArrayList<String>) getIntent().getSerializableExtra("endID");
            startLoc = extras.getString("startID");
            airlineId = (ArrayList<String>) getIntent().getSerializableExtra("airlineID");
        }

        System.out.println("oncreate done");
        onStart();

    }

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println(startLoc);
        System.out.println(endLoc);
        System.out.println("test complete");

        //Start marker
        startIndex = Collections.binarySearch(IATA,startLoc.toUpperCase());
        if(startIndex < 0){
            //should never enter but throws error if does
            startIndex=0;
        }
        System.out.println(startIndex);
        System.out.println(name.get(startIndex));

        endIndex = new ArrayList<>();
        for(int i = 0; i < endLoc.size(); i++){
            endIndex.add(Collections.binarySearch(IATA,endLoc.get(i).toString().toUpperCase()));
            if(endIndex.get(i) < 0){
                //should never enter but throws error if does
                endIndex.add(0);
            }
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        ArrayList<LatLng> mEnd = new ArrayList<>();

        // Add a marker
        LatLng start = new LatLng(Double.parseDouble(latitude.get(startIndex)), Double.parseDouble(longitude.get(startIndex)));
        mMap.addMarker(new MarkerOptions().position(start).title("Starting Airport").snippet
                ("Airport Name: "+ name.get(startIndex) + "\n"+
                        "City: " + city.get(startIndex) + "\n"+
                        "Country: " + country.get(startIndex) + "\n"+
                        "Airline ID: " + airlineId.get(startIndex) + "\n"+
                        "IATA: " + IATA.get(startIndex)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));


        for(int j = 0; j < endLoc.size(); j++) {
            mEnd.add(new LatLng(Double.parseDouble(latitude.get(endIndex.get(j))), Double.parseDouble(longitude.get(endIndex.get(j)))));
            mMap.addMarker(new MarkerOptions().position(mEnd.get(j)).title("Destination #: " + (j+1)).snippet
                    ("Airport Name: "+ name.get(endIndex.get(j)) + "\n"+
                            "City: " + city.get(endIndex.get(j)) + "\n"+
                            "Country: " + country.get(endIndex.get(j)) + "\n"+
                            "Airline ID: " + airlineId.get(endIndex.get(j)) + "\n"+
                            "IATA: " + IATA.get(endIndex.get(j))).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }

        connectMarkers(Double.parseDouble(latitude.get(startIndex)), Double.parseDouble(longitude.get(startIndex)),
                Double.parseDouble(latitude.get(endIndex.get(0))), Double.parseDouble(longitude.get(endIndex.get(0))));

        for(int p = 0; p < endLoc.size()-1; p++){
            connectMarkers(Double.parseDouble(latitude.get(endIndex.get(p))), Double.parseDouble(longitude.get(endIndex.get(p))),
                    Double.parseDouble(latitude.get(endIndex.get(p+1))), Double.parseDouble(longitude.get(endIndex.get(p+1))));
        }



        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                Context mContext = getApplicationContext();

                LinearLayout info = new LinearLayout(mContext);
                info.setOrientation(LinearLayout.VERTICAL);

                TextView title = new TextView(mContext);
                title.setTextColor(Color.BLACK);
                title.setGravity(Gravity.CENTER);
                title.setTypeface(null, Typeface.BOLD);
                title.setText(marker.getTitle());

                TextView snippet = new TextView(mContext);
                snippet.setTextColor(Color.GRAY);
                snippet.setText(marker.getSnippet());

                info.addView(title);
                info.addView(snippet);

                return info;
            }
        });

    }

    public void loadAirports(){
        InputStream is = getResources().openRawResource(R.raw.airports);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] column = csvLine.split(",");
                name.add(column[0]);
                city.add(column[1]);
                country.add(column[2]);
                IATA.add(column[3]);
                latitude.add(column[4]);
                longitude.add(column[5]);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
    }

    public void connectMarkers(double startLat, double startLng, double endLat, double endLng ){
        Polyline line = mMap.addPolyline(new PolylineOptions()
                .add(new LatLng(startLat, startLng), new LatLng(endLat, endLng))
                .width(5)
                .color(Color.RED));
        line.setVisible(true);
    }


}
