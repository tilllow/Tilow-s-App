package com.hfad.exploreshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    ArrayList<SuggestedItem> itemList = new ArrayList<>();
    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES){
            setTheme(R.style.Theme_ExploreShopping);
        } else{
            setTheme(R.style.Theme_ExploreShopping);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
                        break;
                    case R.id.action_cart:
                        fragment = new CartItemsFragment();
                        break;
                    case R.id.action_profile:
                        fragment = new ProfileFragment();
                        break;
                    case R.id.action_purchases:
                        fragment = new PurchasedItemsFragment();
                        break;
                    default:
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer,fragment).commit();
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.action_home);
    }

}