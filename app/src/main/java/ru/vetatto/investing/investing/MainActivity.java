package ru.vetatto.investing.investing;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.preference.PreferenceManager;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import ru.vetatto.investing.investing.Add.Add;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sp;
    String api_token="";
    Context context;
    FloatingActionButton fab;
    boolean back = false;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
      /*  MobileAds.initialize(this, "ca-app-pub-3909765981983100~5463344129");
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3909765981983100/6253573812");*/
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        api_token = sp.getString("API_TOKEN", "");
        String email = sp.getString("email", "");
        String GCM_id =sp.getString("GCM_TOKEN","");
                if(api_token.isEmpty()){
            /*android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, new FirstFragment()).commit();*/
                    Intent intent = new Intent(context, LoginActivity.class);
                    context.startActivity(intent);
                    finish();
        }
        else{

                        send_gcm(GCM_id, api_token);

            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container, new SecondFragment()).commit();
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,Add.class);
                context.startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView mNameTextView = (TextView) header.findViewById(R.id.investor_name);
        mNameTextView.setText(email);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //onActivityResult(requestCode, resultCode, data);
        PageFragment.OnResult(requestCode, resultCode, data);

    }
    public void onResume() {
        super.onResume();
    }
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(!back) {
               /* if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }*/
                back=true;
            }
            else{
                finish();
            }
        }
    }
    public void showFloatingActionButton() {
        fab.show();
    }

    public void hideFloatingActionButton() {
        fab.hide();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fragmentTransaction.replace(R.id.container, new SecondFragment()).commit();
        }
        else if(id == R.id.nav_logout){
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("email", "");
            editor.putString("password", "");
            editor.putString("API_TOKEN", " ");
            editor.commit();
            Intent intent = new Intent(context,LoginActivity.class);
            context.startActivity(intent);
            finish();
        }
        // Выделяем выбранный пункт меню в шторке
        item.setChecked(true);
        // Выводим выбранный пункт в заголовке
        setTitle(item.getTitle());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void send_gcm(String token_gcm, String api_token){
        Get send_gcm=new Get();
        Log.d("TESTE","/update_GCM/"+token_gcm);
        send_gcm.Get("/update_GCM/"+token_gcm, api_token, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("TESTE", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.d("TESTE", response.toString());
            }
        });
    }
}
