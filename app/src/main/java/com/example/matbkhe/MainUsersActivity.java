package com.example.matbkhe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainUsersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    GridView gv;

    GetDepartments g=new GetDepartments();
    Departments model;
    AdapterDepartments adapterDepartments;

    ArrayList<Departments> d;
    public static String deptno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_users);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gv=findViewById(R.id.gvdepartments);
        d=new ArrayList<>(g.GetData());
        adapterDepartments=new AdapterDepartments(this,d);
        gv.setAdapter(adapterDepartments);


        TextView txtcount=findViewById(R.id.txtcount);
        SQLiteDB ds=new SQLiteDB(MainUsersActivity.this);
        txtcount.setText(String.valueOf(ds.GetCount()));


        FloatingActionButton f=findViewById(R.id.fabm);
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainUsersActivity.this,Shoppingcart.class));
            }
        });




        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                model=d.get(i);
                deptno=model.getNo();
                startActivity(new Intent(MainUsersActivity.this,ItemsOffersActivity.class));

            }
        });

        final SwipeRefreshLayout sw=findViewById(R.id.swp);
        sw.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                d=new ArrayList<>(g.GetData());
                adapterDepartments=new AdapterDepartments(MainUsersActivity.this,d);
                gv.setAdapter(adapterDepartments);
                sw.setRefreshing(false);

            }
        });


       /* SharedPreferences prefs = getSharedPreferences("X", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("lastActivity", getClass().getName());
        editor.commit();
*/



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);




        View headerView = navigationView.getHeaderView(0);
        CircleImageView imgwel=headerView.findViewById(R.id.imguser);
        TextView txtuserwel=headerView.findViewById(R.id.txtwelcome);
        TextView txtemailwel=headerView.findViewById(R.id.txtemailwelcome);

        if(LoginActivity.name!=null) {

            txtuserwel.setText("Welcome : " + LoginActivity.name);
            txtemailwel.setText(LoginActivity.email);
            PicassoClient.downloadImage(this, LoginActivity.image, imgwel);


            Menu menu = navigationView.getMenu();
            MenuItem item = menu.findItem(R.id.nav_admin);
            if (LoginActivity.usertype.equals("ENG"))
                item.setVisible(true);
            else
                item.setVisible(false);
        }
        else
        {
            getSharedPreferences("MatSH",MODE_PRIVATE)
                    .edit()
                    .clear()
                    .commit();

            startActivity(new Intent(MainUsersActivity.this,LoginActivity.class));

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
           finishAffinity();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_users, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {

            getSharedPreferences("MatSH",MODE_PRIVATE)
                    .edit()
                    .clear()
                    .commit();

            startActivity(new Intent(MainUsersActivity.this,LoginActivity.class));
            return true;
        }
        else if(id==R.id.action_web)
        {
            Uri h = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, h);
            startActivity(intent);

        }
        else if(id==R.id.unsubs)
        {
            AlertDialog.Builder ms=new AlertDialog.Builder(this);
            ms.setMessage("Are you sure delete your account")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Database db=new Database();
                            db.ConnectDB();

                            db.RunDML("Delete from users where username='"+LoginActivity.username+"'");
                            getSharedPreferences("MatSH",MODE_PRIVATE)
                                    .edit()
                                    .clear()
                                    .commit();

                            startActivity(new Intent(MainUsersActivity.this,LoginActivity.class));
                            Toast.makeText(MainUsersActivity.this, "User has been removed", Toast.LENGTH_SHORT).show();


                        }
                    }).setNegativeButton("No",null);
            ms.create().show();


        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_depart) {
            // Handle the camera action
        } else if (id == R.id.nav_offer) {

        } else if (id == R.id.nav_orders) {
            startActivity(new Intent(this,MyOrdersActivity.class));

        } else if (id == R.id.nav_cart) {

        }
        else if (id == R.id.nav_branches) {

            startActivity(new Intent(this,MapsActivity.class));

        }
        else if (id == R.id.nav_admin) {

            startActivity(new Intent(this,DepartmentsActivity.class));

        }
        else if (id == R.id.nav_share) {


        } else if (id == R.id.nav_send) {
            startActivity(new Intent(this,MainDeptLatestActivity.class));

        }
        else if (id == R.id.nav_profile) {
startActivity(new Intent(this,MyProfile.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
