package com.transporteruser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.User;
import com.transporteruser.databinding.ActivityMainBinding;
import com.transporteruser.fragement.HistoryFragement;
import com.transporteruser.fragement.HomeFragement;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    CreateProfileActivity createProfileActivity;
    ActionBarDrawerToggle toggle;
    SharedPreferences sp = null;
    String currentUserId;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =ActivityMainBinding.inflate(LayoutInflater.from(this));
        currentUserId = FirebaseAuth.getInstance().getUid();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(binding.getRoot());
        sp = getSharedPreferences("user",MODE_PRIVATE);
        binding.navDrawer.setItemIconTintList(null);
        binding.bottomNav.setItemIconTintList(null);
        toggle = new ActionBarDrawerToggle(this,binding.drawer,binding.toolbar,R.string.open,R.string.close);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        View view=binding.navDrawer.inflateHeaderView(R.layout.navigation_header);
        ImageView iv=view.findViewById(R.id.back);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.drawer.closeDrawer(Gravity.LEFT);
            }
        });
        view.setVisibility(View.VISIBLE);
        getFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new HomeFragement()).commit();
        binding.navDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                binding.drawer.closeDrawer(GravityCompat.START);
                Fragment selected = null;
                int id=item.getItemId();
                if(id == R.id.User){
                    Toast.makeText(MainActivity.this,"User",Toast.LENGTH_SHORT).show();
                }
                else if(id == R.id.home){
                    selected = new HomeFragement();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();
                }
                else if (id == R.id.history) {
                   selected = new HistoryFragement();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();
                }
                else if(id == R.id.TermAndCondition){
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("text/plain");
                    i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                    i.putExtra(Intent.EXTRA_TEXT, "https://transpoter-uer.flycricket.io/privacy.html");
                    startActivity(Intent.createChooser(i, "Share URL"));

                }
                else if(id == R.id.PrivacyPoalcy){
                    Intent i = new Intent(MainActivity.this, PrivacyPolicy.class);
                    startActivity(i);
                }
                else if(id == R.id.ContectUs){
                    Intent i = new Intent(MainActivity.this,ContactUsActivity.class);
                    startActivity(i);

                }
                else if(id == R.id.AboutUs){
                    Intent i = new Intent(MainActivity.this, AboutUs.class);
                    startActivity(i);

                }
                else if(id == R.id.Logout){
                    AuthUI.getInstance()
                            .signOut(MainActivity.this)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(MainActivity.this,LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            });
                }
                return false;
            }
        });

    }
    private void getFragment(){
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;
                switch (item.getItemId()){
                    case R.id.home:
                        selected = new HomeFragement();
                        break;
                    case R.id.history:
                        selected = new HistoryFragement();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();
                return true;
            }

        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if(currentUser == null){
            Intent i = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
        }
        else
           checkUserProfileCreatedOrNot();
    }
    private void checkUserProfileCreatedOrNot(){
       String status = sp.getString("userId","not_created");
       if(status.equals("not_created"))
           sendUserToCreateProfile();

    }
    private void sendUserToCreateProfile(){
        Intent in = new Intent(MainActivity.this,CreateProfileActivity.class);
        startActivity(in);
    }
}