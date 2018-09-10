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

        final Button add_button = (Button) findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("add line");
                add_Line();
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
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                if(validateRoutes()){
                    //Set map locations and launch map activity
                    System.out.println("valid");
                    Intent myIntent = new Intent(Main_Activity.this, MapsActivity.class);
                    myIntent.putExtra("startID", startLoc.toUpperCase());
                    myIntent.putExtra("endID", endLoc);
                    myIntent.putExtra("airlineID", airlineID);
                    startActivity(myIntent);
                    //setContentView(R.layout.activity_maps);

                }
                else{
                    //send toast message
                    System.out.println("not valid");
                    Toast.makeText(getApplicationContext(), "Route does not exist",
                            Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    public void add_Line() {
        Button myButton = new Button(this);
//        myButton.setText("–");
//        RelativeLayout r1 = (RelativeLayout) findViewById(R.id.rl);
//        RelativeLayout.LayoutParams r = new RelativeLayout.LayoutParams(135, 135);
//        r1.addView(myButton,r);

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

//        LinearLayout bl = (LinearLayout) findViewById(R.id.linearLayoutFormat);
//        LinearLayout.LayoutParams b = new LinearLayout.LayoutParams(135, 135);
//        bl.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
//        bl.addView(myButton,b);

        numberOfLines++;

    }

    public void remove_Line(View v){
//        LinearLayout ll = (LinearLayout) findViewById(R.id.linearLayoutFormat);
//        EditText et = new EditText(this);

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

        //if beginning is null
        //need at least one ending
        int count = 0;

        if(endLoc.size() <= 1){
            if(endLoc.get(0).isEmpty()){
                //System.out.println("Enter a destination");
                    Toast.makeText(getApplicationContext(), "Destination cannot be empty",
                            Toast.LENGTH_SHORT).show();
                return false;
            }
            if(startLoc.isEmpty()){
                //System.out.println("Enter a start");
                    Toast.makeText(getApplicationContext(), "Please enter a start",
                            Toast.LENGTH_SHORT).show();
                return false;
            }
            if(binarySearch(startLoc.toUpperCase(),endLoc.get(0).toUpperCase()) >= 0){
                System.out.println("One destination valid");
                return true;
            }

        }
        else{

            for(int j = 0; j < endLoc.size(); j++){
                if(endLoc.get(j).isEmpty()){
                    //System.out.println("Enter a value");
                    Toast.makeText(getApplicationContext(), "Please enter another destination",
                            Toast.LENGTH_SHORT).show();
                    return false;
                }
            }
            for(int i = 0; i < endLoc.size() - 1; i++) {
                if (binarySearch(endLoc.get(i).toUpperCase(),endLoc.get(i+1).toUpperCase()) >= 0 && binarySearch(startLoc.toUpperCase(),endLoc.get(0).toUpperCase()) >= 0){
                    count++;
                }
            }
            if(count == endLoc.size() - 1){
                System.out.println("Multiple destination valid");
                return true;
            }
        }

        //no destination
        return false;
    }

    int binarySearch(String start, String end){
        //binarySearch
        int index = Collections.binarySearch(origin, start);
        int temp = index;

        //Validate starting loc
        if (index<0){
            Toast.makeText(getApplicationContext(), "Not a valid start",
                    Toast.LENGTH_SHORT).show();
            return -1;//not found
        }

        //Validate end loc
        int endValidate = Collections.binarySearch(origin,end);
        if (endValidate<0){
            Toast.makeText(getApplicationContext(), end + " is not valid",
                    Toast.LENGTH_SHORT).show();
            return -1;//not found
        }

        System.out.println("airline ID: " + airlineID.get(index));

        if(start.equals(origin.get(index)) && end.equals(destination.get(index))) {
            //System.out.println("found");
            return index;
        }

        //increment from initial index to find destination
        while(start.equals(origin.get(index)) && !end.equals(destination.get(index))){
            System.out.println(destination.get(index));
            index++;
            if(index >= origin.size()){
                System.out.println("breakup");
                break;
            }
            if(start.equals(origin.get(index)) && end.equals(destination.get(index))){
                //System.out.println("found");
                return index;
            }

        }
        //decrement from initial index to find destination
        while(start.equalsIgnoreCase(origin.get(temp).toString()) && !end.equalsIgnoreCase(destination.get(temp).toString())){
            //System.out.println("testdwon");
            temp--;
            if(temp < 0){
                System.out.println("breakdwn");
                break;
            }
            if(start.equalsIgnoreCase(origin.get(temp).toString()) && end.equalsIgnoreCase(destination.get(temp).toString())){
                System.out.println("found");
                return index;
            }
        }
        //no destination found
        return -1;
    }

}
