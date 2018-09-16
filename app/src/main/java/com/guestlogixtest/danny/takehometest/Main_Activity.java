package com.guestlogixtest.danny.takehometest;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ScrollView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static com.guestlogixtest.danny.takehometest.R.id.activity_main;
import static com.guestlogixtest.danny.takehometest.R.id.button3;
import static com.guestlogixtest.danny.takehometest.R.id.center;
import static com.guestlogixtest.danny.takehometest.R.id.end;
import static com.guestlogixtest.danny.takehometest.R.id.endInput;
import static com.guestlogixtest.danny.takehometest.R.id.right_icon;
import static com.guestlogixtest.danny.takehometest.R.id.right_side;
import static com.guestlogixtest.danny.takehometest.R.id.strict_sandbox;

import  android.os.Message;
import android.widget.Toast;

public class Main_Activity extends AppCompatActivity{

    String startLoc;
    private GoogleMap mMap;
    EditText startInput;

    Button submitButton;
    public int numberOfLines = 1;

    ArrayList<Integer> inputID = new ArrayList<>();
    ArrayList<EditText> endInput = new ArrayList<>();
    ArrayList<String> endLoc = new ArrayList<>();
    ArrayList<String> airlineID = new ArrayList();
    List origin = new ArrayList();
    List destination = new ArrayList();

    LinearLayout ll;
    ArrayList<EditText> et = new ArrayList<>();
    LinearLayout.LayoutParams p;

    int currEt = 0;
    ArrayList<Integer> storeIndex = new ArrayList<>();
    ArrayList<String> routes = new ArrayList<>();
    int a = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ll = (LinearLayout) findViewById(R.id.linearLayoutFormat);
        et.add(new EditText(this));
        p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        loadRoutes();
        onStart();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //Initialize buttons
        final Button add_button = (Button) findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("add line");
                //Add lines dynamically adds and removes lines but didn't have time to change up the code for multiple routes
                //add_Line();
            }
        });
        final Button remove_button = (Button) findViewById(R.id.button3);
        remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(inputID.size() >= 1) {
                    System.out.println("Remove line");
                    remove_Line(v);
                }
            }
        });

        startInput = (EditText) findViewById(R.id.startInput);
        submitButton = (Button) findViewById(R.id.button);

        //On click listener for buttons
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                routes.clear();
                startLoc = startInput.getText().toString();
                System.out.println(startLoc);

                endInput = new ArrayList<>();
                endLoc = new ArrayList<>();
                endInput.add((EditText) findViewById(R.id.endInput));

                if(inputID.size()>0) {
                    for (int i = 0; i < inputID.size(); i++) {
                        endLoc.add(endInput.get(i).getText().toString());
                        endInput.add((EditText) findViewById(inputID.get(i)));
                        System.out.println(endLoc.get(i));
                    }
                    endLoc.add(endInput.get(inputID.size()).getText().toString());
                    System.out.println(endLoc.get(inputID.size()));
                }
                else{
                    endLoc.add(endInput.get(0).getText().toString());
                    System.out.println(endLoc.get(0));
                }

                //If routes are valid, launch map activity
                if(validateRoutes()){
                    //Set map locations and launch map activity
                    System.out.println("valid");
                    Intent myIntent = new Intent(Main_Activity.this, MapsActivity.class);
                    myIntent.putExtra("startID", routes);
                    myIntent.putExtra("endID", endLoc);
                    myIntent.putExtra("airlineID", airlineID);
                    startActivity(myIntent);
                    //setContentView(R.layout.activity_maps);
                    a = 0;

                    System.out.println(storeIndex);
                    System.out.println(routes);
                }
                else{
                    //send toast message
                    Toast.makeText(getApplicationContext(), "Route does not exist",
                            Toast.LENGTH_SHORT).show();
                    a = 0;
                }

            }
        });
    }

    public void add_Line() {
        currEt++;
        ll = (LinearLayout) findViewById(R.id.linearLayoutFormat);
        et.add(new EditText(this));
        p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //et.setLayoutParams(p);
        et.get(currEt).setText("");
        et.get(currEt).setHint("Enter next IATA");
        et.get(currEt).setEms(10);
        et.get(currEt).setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        TextViewCompat.setTextAppearance(et.get(currEt), android.R.style.TextAppearance_Small);
        et.get(currEt).setId(numberOfLines+1);
        inputID.add(et.get(currEt).getId());
        ll.addView(et.get(currEt),p);
        System.out.println(inputID);

        numberOfLines++;

    }

    public void remove_Line(View v){
        System.out.println(et);
        System.out.println("rev");
        ll.removeView(et.get(currEt));
        if(currEt > 0) {
            inputID.remove(currEt-1);
            currEt--;
            numberOfLines--;
        }
        System.out.println(currEt);
        System.out.println("inputId" +inputID);

    }

    public void loadRoutes(){
        InputStream is = getResources().openRawResource(R.raw.routes);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
        try {
            String csvLine;
            while ((csvLine = reader.readLine()) != null) {
                String[] column = csvLine.split(",");
                airlineID.add(column[0]);
                origin.add(column[1]);
                destination.add(column[2]);
            }
        }
        catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: "+ex);
        }
    }


    boolean validateRoutes(){

        int count = 0;

        //check for empty
        for(int j = 0; j < endLoc.size(); j++){
            if(endLoc.get(j).isEmpty()){
                System.out.println("Enter a value");
                Toast.makeText(getApplicationContext(), "Destination cannot be empty",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        //check if empty
        if(startLoc.isEmpty()){
            Toast.makeText(getApplicationContext(), "Start cannot be empty",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if(startLoc.equalsIgnoreCase(endLoc.get(0))){
            Toast.makeText(getApplicationContext(), "Beginning cannot be the same as ending location",
                    Toast.LENGTH_SHORT).show();
            return false;//not found
        }

        //find routes
        if(endLoc.size() == 1) {
            //routes.add(endLoc.get(0).toUpperCase());
            if (binarySearch(startLoc.toUpperCase(), endLoc.get(0).toUpperCase(), startLoc.toUpperCase())) {
                System.out.println("One destination valid");
                return true;
            }
        }
        else{
            //routes.add(endLoc.get(0).toUpperCase());
            //binarySearch(startLoc.toUpperCase(), endLoc.get(0).toUpperCase(), startLoc.toUpperCase());
            //storeIndex.clear();
            //a = 0;
            System.out.println("Start: " + endLoc.get(0).toUpperCase() + " end " + endLoc.get(1).toUpperCase());
            if (binarySearch(endLoc.get(0).toUpperCase(), endLoc.get(1).toUpperCase(), endLoc.get(0).toUpperCase()) && binarySearch(startLoc.toUpperCase(), endLoc.get(0).toUpperCase(), startLoc.toUpperCase())) {
                System.out.println("two destination valid");
                return true;
            }
//            for(int i = 0; i < endLoc.size() - 1; i++) {
//                if (binarySearch(endLoc.get(i).toUpperCase(),endLoc.get(i+1).toUpperCase(), endLoc.get(i).toUpperCase())){
//                    count++;
//                }
//                System.out.println("multi destinations");
//            }
//            if(count == endLoc.size() - 1){
//                System.out.println("Multiple destination valid");
//                return true;
//            }
        }


        //no destination
        return false;
    }

    boolean binarySearch(String start, String end, String OGStart){
        int index = Collections.binarySearch(origin, start);

        //Validate starting loc
        if (index<0){
            if(storeIndex.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Not a valid start",
                        Toast.LENGTH_SHORT).show();
                return false;//not found
            }
            else{
                a++;
                System.out.println("Search next destination");
                start = (String)destination.get(storeIndex.get(a));
                index = Collections.binarySearch(origin, start);
            }
        }
        int endIndex = index;

        //Validate end loc
        int endValidate = Collections.binarySearch(origin,end);

        if (endValidate<0){
            Toast.makeText(getApplicationContext(), end + " is not valid",
                    Toast.LENGTH_SHORT).show();
            return false;//not found
        }

        //decrement down to find beginning index of destination airports, breadth width search using start loc as root
        while(start.equalsIgnoreCase(origin.get(index).toString())){
            index--;
            if(index < 0){
                System.out.println("breakUwn");
                break;
            }
        }

        //increment up to find end index of destination airports
        while(start.equalsIgnoreCase(origin.get(endIndex).toString())){
            endIndex++;
            if(endIndex > origin.size()){
                System.out.println("breakUp");
                break;
            }
        }

        //Shift index since while loop will ends it one before/after
        index++;
        endIndex--;

        for(int i = index; i <= endIndex; i++){
            storeIndex.add(i);
        }

//        System.out.println("Start index: " + index);
//        System.out.println("End index: " + endIndex);
//        System.out.println("Start loc: " + start);
//        System.out.println("End loc: " + end);

        //if the start equals end return true
        if(start.equals(origin.get(index)) && end.equals(destination.get(index))){
            System.out.println("found");
            System.out.println("Start loc: " + start);
            System.out.println("End loc: " + end);
            System.out.println("OG start " + OGStart);
            routes.add(start);
            if(!start.equalsIgnoreCase(OGStart)) {
                System.out.println("keep searching");
                a = 0;
                binarySearch(OGStart, start, OGStart);
            }
            return true;
        }

        //while start !equals end, keep indexing until it is found. If not found, exists while loop
        // and recursively calls itself until found
        while(start.equals(origin.get(index)) && !end.equals(destination.get(index))){
            index++;
            if(index >= origin.size()){
                System.out.println("not found");
                break;
            }
            if(start.equals(origin.get(index)) && end.equals(destination.get(index))){
                System.out.println("found");
                System.out.println("Start loc: " + start);
                System.out.println("End loc: " + end);
                System.out.println("OG start " + OGStart);
                routes.add(start);
                if(!start.equalsIgnoreCase(OGStart)) {
                    System.out.println("keep searching");
                    a = 0;
                    binarySearch(OGStart, start, OGStart);
                }
                return true;
            }
        }

        a++;
        System.out.println(destination.get(storeIndex.get(a-1)));
        System.out.println("test");
        //Recursively call until found, since we know these locations are valid
        binarySearch((String)destination.get(storeIndex.get(a-1)), end, OGStart);
        return true;
    }

}
