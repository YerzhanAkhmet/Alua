package com.example.alua.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alua.Fragments.LibraryFragment;
import com.example.alua.Fragments.NewsFragment;
import com.example.alua.Fragments.ProfileFragment;
import com.example.alua.Fragments.SettingsFragment;
import com.example.alua.R;

import java.util.ArrayList;
import java.util.List;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity implements LibraryFragment.OnFragmentInteractionListener {

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    NavigationView navigationView;
    ImageView headerIV;
    TextView headerTV;
    Toolbar toolbar;
    DrawerLayout drawer;
    View headerLayout;
    Menu menuLayout;
    TextView toolbarTV;
    List<String> label;
    Boolean exit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialization();
        Paper.init(MainActivity.this);
        setSupportActionBar(toolbar);
        int i = getIntent().getIntExtra("intent", 0);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        label = new ArrayList<>();
        setLabel();
        Glide.with(this).load("https://pp.userapi.com/c854416/v854416043/347a0/UoBNThbOXaA.jpg").into(headerIV);
        headerTV.setText("Alua's Library");
        if (i == 1){
            openSettingsFragment();
            toolbarTV.setText(label.get(3));
        } else {
            openLibraryFragment();
            toolbarTV.setText(label.get(1));
        }
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if (id == R.id.nav_profile) {
                    openProfileFragment();
                    toolbarTV.setText(label.get(0));
                } else if (id == R.id.nav_library) {
                    openLibraryFragment();
                    toolbarTV.setText(label.get(1));
                } else if (id == R.id.nav_news) {
                    openNewsFragment();
                    toolbarTV.setText(label.get(2));
                } else if (id == R.id.nav_settings) {
                    openSettingsFragment();
                    toolbarTV.setText(label.get(3));
                } else if (id == R.id.nav_exit) {
                    if (exit) {
                        Paper.book().delete("UserLoggedIn");
                        Intent intent = new Intent(MainActivity.this, SplashActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else {
                        exit = true;
                        Toast.makeText(MainActivity.this,
                                "Нажимая кнопку \"Выход\", вы перенаправитесь в окно авторизации и удалите созданного вами пользователя",
                                Toast.LENGTH_SHORT).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                exit = false;
                            }
                        }, 2000);
                    }
                }
                if (!exit) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    public void openLibraryFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        LibraryFragment libraryFragment = new LibraryFragment();
        fragmentTransaction.replace(R.id.main_activity_frame, libraryFragment, LibraryFragment.TAG);
        fragmentTransaction.commit();
    }
    public void openProfileFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        ProfileFragment profileFragment = new ProfileFragment();
        fragmentTransaction.replace(R.id.main_activity_frame, profileFragment, ProfileFragment.TAG);
        fragmentTransaction.commit();
    }
    public void openNewsFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        NewsFragment newsFragment = new NewsFragment();
        fragmentTransaction.replace(R.id.main_activity_frame, newsFragment, NewsFragment.TAG);
        fragmentTransaction.commit();
    }
    public void openSettingsFragment(){
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        SettingsFragment settingsFragment = new SettingsFragment();
        fragmentTransaction.replace(R.id.main_activity_frame, settingsFragment, SettingsFragment.TAG);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    public void initialization(){
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        headerLayout = navigationView.getHeaderView(0);
        menuLayout = navigationView.getMenu();
        headerIV = headerLayout.findViewById(R.id.header_IV);
        headerTV = headerLayout.findViewById(R.id.header_text_view);
        toolbarTV = findViewById(R.id.label_toolbar_main);
    }

    public void setLabel(){
        if (Paper.book().contains("language")) {
            if (Paper.book().read("language").equals("ru")) {
                label.add(0, getString(R.string.menu_profile_ru));
                label.add(1, getString(R.string.menu_library_ru));
                label.add(2, getString(R.string.menu_news_ru));
                label.add(3, getString(R.string.menu_settings_ru));
                label.add(4, getString(R.string.menu_exit_ru));
            } else if (Paper.book().read("language").equals("kz")) {
                label.add(0, getString(R.string.menu_profile_kz));
                label.add(1, getString(R.string.menu_library_kz));
                label.add(2, getString(R.string.menu_news_kz));
                label.add(3, getString(R.string.menu_settings_kz));
                label.add(4, getString(R.string.menu_exit_kz));
            } else {
                label.add(0, getString(R.string.menu_profile));
                label.add(1, getString(R.string.menu_library));
                label.add(2, getString(R.string.menu_news));
                label.add(3, getString(R.string.menu_settings));
                label.add(4, getString(R.string.menu_exit));
            }
        } else {
            label.add(0, getString(R.string.menu_profile));
            label.add(1, getString(R.string.menu_library));
            label.add(2, getString(R.string.menu_news));
            label.add(3, getString(R.string.menu_settings));
            label.add(4, getString(R.string.menu_exit));
        }
        menuLayout.getItem(0).setTitle(label.get(0));
        menuLayout.getItem(1).setTitle(label.get(1));
        menuLayout.getItem(2).setTitle(label.get(2));
        menuLayout.getItem(3).setTitle(label.get(3));
        menuLayout.getItem(4).setTitle(label.get(4));
    }
}
