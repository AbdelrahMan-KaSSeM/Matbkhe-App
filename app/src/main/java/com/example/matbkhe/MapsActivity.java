package com.example.matbkhe;

import android.app.AlertDialog;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    GPSTracker g=new GPSTracker(MapsActivity.this);

    public static double lat,lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
         g=new GPSTracker(MapsActivity.this);

        LatLng koko = new LatLng(g.getLatitude(), g.getLongitude());
        mMap.addMarker(new MarkerOptions().position(koko).title("Current Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(koko,8));



        int strokeColor = 0xffff0000;
        int shadeColor = 0x44ff0000;
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(g.getLatitude(), g.getLongitude()))
                .radius(200)
                .strokeColor(strokeColor)
                .fillColor(shadeColor));


      /*  Database db=new Database();
        db.ConnectDB();
        ResultSet rs=db.RunSearch("Select * From Branches");
        try{
            while (rs.next())
            {
                   koko = new LatLng(rs.getDouble(5), rs.getDouble(6));
                mMap.addMarker(new MarkerOptions().position(koko).title(rs.getString(1)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(koko,8));

            }
        }catch (SQLException ex)
        {
            ;
        }*/



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {


                Database db=new Database();
                db.ConnectDB();
                ResultSet rs=db.RunSearch("Select * from Branches where BrancheNo='"+marker.getTitle()+"'");
                try
                {
                    if(rs.next())
                    {
                        AlertDialog.Builder ms=new AlertDialog.Builder(MapsActivity.this);
                        ms.setTitle(rs.getString(2))
                                .setMessage("Address is "+rs.getString(3)+"\n"+
                                        "Phone is "+rs.getString(4))
                                .setPositiveButton("Thanks",null);
                        ms.create().show();

                    }
                }catch (SQLException ex)
                {
                    ;
                }


                return false;
            }
        });



        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng h) {

              //  mMap.clear();
              LatLng  koko = new LatLng(h.latitude, h.longitude);
                mMap.addMarker(new MarkerOptions().position(koko).title("Set Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(koko,8));


                lat=h.latitude;
                lang=h.longitude;

                g=new GPSTracker(MapsActivity.this);

                Location loc1 = new Location("");
                loc1.setLatitude(g.latitude);
                loc1.setLongitude(g.longitude);

                Location loc2 = new Location("");
                loc2.setLatitude(lat);
                loc2.setLongitude(lang);

                float distanceInMeters = loc1.distanceTo(loc2);
                Toast.makeText(MapsActivity.this, "Distance Is "+(distanceInMeters/1000)+" KM", Toast.LENGTH_SHORT).show();

            }
        });



    }





}
