package com.tasmim.a7afalaty;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tasmim.a7afalaty.activities.AddService;
import com.tasmim.a7afalaty.activities.ContactUs;
import com.tasmim.a7afalaty.activities.Login;
import com.tasmim.a7afalaty.activities.MyBooking;
import com.tasmim.a7afalaty.activities.MyNotifications;
import com.tasmim.a7afalaty.activities.MyServices;
import com.tasmim.a7afalaty.activities.Policy;
import com.tasmim.a7afalaty.activities.Profile;
import com.tasmim.a7afalaty.model.LogOutModel;
import com.tasmim.a7afalaty.model.LoginModel;
import com.tasmim.a7afalaty.model.ProfileModel;
import com.tasmim.a7afalaty.network.ApiClient;
import com.tasmim.a7afalaty.network.ApiService;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static int user_id;
    public static ProfileModel.Data user_Data = null;
    ImageView headerImage;
    TextView userName, userEmail;
    NetworkTester networkTester = new NetworkTester(this);
    ApiService apiService;
    SpotsDialog dialog;

    // Shared Preferences ...
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (getIntent().getExtras() != null) {
//            user_Data = getIntent().getExtras().getParcelable("user_data");
            user_id = getIntent().getExtras().getInt("user_id");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(Home.this);
        final View hView = navigationView.getHeaderView(0);
        LinearLayout nav_user_data_layout = hView.findViewById(R.id.nav_user_data_layout);
        TextView nav_login_txt = hView.findViewById(R.id.nav_login_txt);
        headerImage = hView.findViewById(R.id.nev_header_image);

        // Navigation header Main text ...
        userName = hView.findViewById(R.id.nav_head_name);
        // Navigation header email text ...
        userEmail = hView.findViewById(R.id.nav_head_email);

        final Menu nav_menu = navigationView.getMenu();
        ////////////////////////////////
        nav_login_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Home.this, Login.class));
                finish();
            }
        });

        if (user_id == -1) {
            nav_menu.findItem(R.id.nav_logout).setVisible(false);
            nav_user_data_layout.setVisibility(View.GONE);
            nav_login_txt.setVisibility(View.VISIBLE);
        } else {
            if (networkTester.isNetworkAvailable()) {
                apiService = ApiClient.getClient().create(ApiService.class);
                Call<ProfileModel> call = apiService.getProfileData(user_id + "");
                call.enqueue(new Callback<ProfileModel>() {
                    @Override
                    public void onResponse(Call<ProfileModel> call, Response<ProfileModel> response) {
                        ProfileModel profileModel = response.body();
                        String status = profileModel.getStatus();
                        user_Data = profileModel.getData();

                        userName.setText(user_Data.getName());
                        userEmail.setText(user_Data.getEmail());
                        if (user_Data.getType() == 1) {
                            nav_menu.findItem(R.id.nav_services).setVisible(false);
                            nav_menu.findItem(R.id.nav_Add_Service).setVisible(false);
                        } else if (user_Data.getType() == 2) {
                            nav_menu.findItem(R.id.nav_booking).setVisible(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<ProfileModel> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            } else {
                Toasty.error(Home.this, getString(R.string.error_connection), 1500).show();
            }
        }

        if (user_id != -1) {

        }
//

        // set default selected fragment ....
        HomeFragment homeFragment = new HomeFragment();
        displaySelectedFragment(homeFragment);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setMessage(getString(R.string.outofApp))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //super.onBackPressed();
                            dialogInterface.dismiss();
                            //-------------------------------------------------------
                            dialog = new SpotsDialog(Home.this, getString(R.string.logging_out), R.style.Custom);
                            dialog.show();

                            String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                            apiService = ApiClient.getClient().create(ApiService.class);
                            Call<LogOutModel> call = apiService.logout(user_id + "", device_id);
                            call.enqueue(new Callback<LogOutModel>() {
                                @Override
                                public void onResponse(Call<LogOutModel> call, Response<LogOutModel> response) {
                                    LogOutModel logOutModel = response.body();
                                    String status = logOutModel.getStatus();
                                    if (status.equals("success")) {
                                        Toasty.success(Home.this, "تم تسجيل الخروج", 1500).show();
                                        pref = getSharedPreferences(Login.MY_PREFS_NAME, Context.MODE_PRIVATE);
                                        editor = pref.edit();
                                        editor.clear();
                                        editor.apply();
                                        startActivity(new Intent(Home.this, Login.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onFailure(Call<LogOutModel> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                            // -------------------------------------------------------
                            pref = getSharedPreferences(Login.MY_PREFS_NAME, Context.MODE_PRIVATE);
                            editor = pref.edit();
                            editor.clear();
                            editor.apply();
                            startActivity(new Intent(Home.this, Login.class));
                            finish();
                        }
                    })
                    .setNegativeButton(getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();
        }
    }

    private void displaySelectedFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.home_frame, fragment);
        fragmentTransaction.commit();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            recreate();
        } else if (id == R.id.nav_profile) {
            if (user_id == -1) {
                Toast.makeText(this, "يجب تسجيل الدخول اولا!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Home.this, Login.class));
            } else {
                startActivity(new Intent(Home.this, Profile.class));
            }
        } else if (id == R.id.nav_booking) {
            if (Home.user_id == -1) {
                Toast.makeText(this, "يجب تسجيل الدخول اولا!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Home.this, Login.class));
            } else {
                startActivity(new Intent(Home.this, MyBooking.class));
            }
        } else if (id == R.id.nav_services) {
            if (user_id == -1) {
                Toast.makeText(this, "يجب تسجيل الدخول اولا!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Home.this, Login.class));
            } else {
                startActivity(new Intent(Home.this, MyServices.class));
            }
        } else if (id == R.id.nav_Add_Service) {
            if (user_id == -1) {
                Toast.makeText(this, "يجب تسجيل الدخول اولا!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Home.this, Login.class));
            } else {
                startActivity(new Intent(Home.this, AddService.class));
            }
        } else if (id == R.id.nav_app_policy) {
            startActivity(new Intent(Home.this, Policy.class));
        } else if (id == R.id.nav_Notifications) {
            if (user_id == -1) {
                Toast.makeText(this, "يجب تسجيل الدخول اولا!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Home.this, Login.class));
            } else {
                startActivity(new Intent(Home.this, MyNotifications.class));
            }
        } else if (id == R.id.nav_contact_us) {
//            if (user_id == -1) {
//                Toast.makeText(this, "يجب تسجيل الدخول اولا!", Toast.LENGTH_SHORT).show();
////                startActivity(new Intent(Home.this, Login.class));
//            } else {
            startActivity(new Intent(Home.this, ContactUs.class));
//            }
        } else if (id == R.id.nav_logout) {
            if (user_id == -1) {
                Toast.makeText(this, "يجب تسجيل الدخول اولا!", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(Home.this, Login.class));
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setMessage(getString(R.string.outofApp))
                        .setCancelable(false)
                        .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //super.onBackPressed();
                                dialogInterface.dismiss();
                                //-------------------------------------------------------
                                dialog = new SpotsDialog(Home.this, getString(R.string.logging_out), R.style.Custom);
                                dialog.show();

                                String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                                apiService = ApiClient.getClient().create(ApiService.class);
                                Call<LogOutModel> call = apiService.logout(user_id + "", device_id);
                                call.enqueue(new Callback<LogOutModel>() {
                                    @Override
                                    public void onResponse(Call<LogOutModel> call, Response<LogOutModel> response) {
                                        LogOutModel logOutModel = response.body();
                                        String status = logOutModel.getStatus();
                                        if (status.equals("success")) {
                                            Toasty.success(Home.this, "تم تسجيل الخروج", 1500).show();
                                            pref = getSharedPreferences(Login.MY_PREFS_NAME, Context.MODE_PRIVATE);
                                            editor = pref.edit();
                                            editor.clear();
                                            editor.apply();
                                            startActivity(new Intent(Home.this, Login.class));
                                            finish();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<LogOutModel> call, Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                                // -------------------------------------------------------
                            }
                        })
                        .setNegativeButton(getString(R.string.dismiss), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            }

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}