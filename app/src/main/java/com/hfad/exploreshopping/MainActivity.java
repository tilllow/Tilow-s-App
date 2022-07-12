package com.hfad.exploreshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    ArrayList<SuggestedItem> itemList = new ArrayList<>();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;
    MenuItem menuItem;
    public int savedInteger = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings_menu_bar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menuItem = menu.findItem(R.id.miChangeTheme);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.miChangeTheme) {
            Intent intent = new Intent(MainActivity.this,EditThemeTest.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.Theme_ExploreShopping);
        } else {
            setTheme(R.style.Theme_ExploreShopping);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuItem = findViewById(R.id.miChangeTheme);
        //getUserPurchasedItems();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
//
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuitem) {

                Fragment fragment = new HomeFragment();

                switch (menuitem.getItemId()) {
                    case R.id.action_home:
                        // TODO: update fragment
                        fragment = new HomeFragment();
                        if (menuItem != null){
                            menuItem.setVisible(true);
                        }
                        break;
                    case R.id.action_cart:
                        fragment = new CartItemsFragment();
                        menuItem.setVisible(false);

                        break;
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        menuItem.setVisible(false);

                        break;
                    case R.id.action_purchases:
                        fragment = new PurchasedItemsFragment();
                        menuItem.setVisible(false);

                        break;
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }
}