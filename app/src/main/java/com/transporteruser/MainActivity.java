package com.transporteruser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import java.util.Calendar;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import com.transporteruser.adapters.HomeAdapter;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;
import com.transporteruser.bean.Rating;
import com.transporteruser.bean.Transporter;
import com.transporteruser.bean.User;
import com.transporteruser.databinding.ActivityMainBinding;
import com.transporteruser.databinding.NointernentBinding;
import com.transporteruser.databinding.RatingDialogBinding;
import com.transporteruser.fragement.HistoryFragement;
import com.transporteruser.fragement.HomeFragement;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    CreateProfileActivity createProfileActivity;
    ActionBarDrawerToggle toggle;
    SharedPreferences sp = null;
    String currentUserId;
    UserService.UserApi userApi;
    FirebaseUser currentUser;
    float ra =2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean isInternetConnected = NetworkUtility.checkInternetConnection(MainActivity.this);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        binding.toolbar.setTitle("Home");
        currentUserId = FirebaseAuth.getInstance().getUid();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        setContentView(binding.getRoot());
        sp = getSharedPreferences("user", MODE_PRIVATE);
        if (isInternetConnected) {
            binding.navDrawer.setItemIconTintList(null);
            binding.bottomNav.setItemIconTintList(null);

            toggle = new ActionBarDrawerToggle(this, binding.drawer, binding.toolbar, R.string.open, R.string.close);
            toggle.syncState();
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
            View view = binding.navDrawer.inflateHeaderView(R.layout.navigation_header);
            ImageView iv = view.findViewById(R.id.back);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    binding.drawer.closeDrawer(Gravity.LEFT);
                }
            });
            view.setVisibility(View.VISIBLE);
            getFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame, new HomeFragement()).commit();
            String image = sp.getString("imageUrl","not_found");
            if(!image.equalsIgnoreCase("not_found"))
                Picasso.get().load(image).into(binding.profileShow);

            binding.profileShow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,UpdateProfileActivity.class);
                    startActivity(intent);
                }
            });
            binding.navDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    binding.drawer.closeDrawer(GravityCompat.START);
                    Fragment selected = null;
                    int id = item.getItemId();
                    if (id == R.id.User) {
                        Intent intent = new Intent(MainActivity.this,UpdateProfileActivity.class);
                        startActivity(intent);
                    } else if (id == R.id.home) {
                        selected = new HomeFragement();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
                    } else if (id == R.id.history) {
                        selected = new HistoryFragement();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
                    }
                     else if (id==R.id.currentLoad){
                         Intent i=new Intent(MainActivity.this,CurrentLoad.class);
                         startActivity(i);

                    }
                     else if (id==R.id.confirmedLoad){
                         Intent i=new Intent(MainActivity.this,ConfirmedLoad.class);
                         startActivity(i);

                    }
                    else if (id == R.id.TermAndCondition) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_SUBJECT, "Sharing URL");
                        i.putExtra(Intent.EXTRA_TEXT, "https://transpoter-uer.flycricket.io/privacy.html");
                        startActivity(Intent.createChooser(i, "Share URL"));

                    } else if (id == R.id.PrivacyPoalcy) {
                        Intent i = new Intent(MainActivity.this, PrivacyPolicy.class);
                        startActivity(i);
                    } else if (id == R.id.ContectUs) {
                        Intent i = new Intent(MainActivity.this, ContactUsActivity.class);
                        startActivity(i);

                    } else if (id == R.id.AboutUs) {
                        Intent i = new Intent(MainActivity.this, AboutUs.class);
                        startActivity(i);

                    } else if (id == R.id.Logout) {
                        AuthUI.getInstance()
                                .signOut(MainActivity.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(i);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.clear();
                                        editor.commit();
                                        finish();

                                    }
                                });
                    }
                    return false;
                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Please enable internet connection", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(MainActivity.this,NoInternetActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

    }
    public void getRatingDialog(final String transporterId, final String leadId){
        final AlertDialog ab = new AlertDialog.Builder(this).create();
        final RatingDialogBinding ratingDialogBinding =  RatingDialogBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        ab.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        ratingDialogBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ab.dismiss();
            }
        });
        ratingDialogBinding.ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ra = rating;
            }
        });
        ratingDialogBinding.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {

                userApi.checkProfile(currentUserId).enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        if (response.code() == 200){
                            final Rating  r = new Rating();
                            String feedback = ratingDialogBinding.etOverview.getText().toString();
                            r.setRating(""+ra);
                            long timestamp = Calendar.getInstance().getTimeInMillis();
                            r.setReview(feedback);
                            r.setTimestamp(timestamp);
                            r.setUserName(response.body().getName());
                            r.setImageUrl(response.body().getImageUrl());
                            userApi.getNumberOfRating(transporterId).enqueue(new Callback<ArrayList<Float>>() {
                                @Override
                                public void onResponse(Call<ArrayList<Float>> call, Response<ArrayList<Float>> response) {
                                    if (response.code() ==200){
                                        ArrayList<Float>al = response.body();
                                        final float numberOfPersons = al.get(0) + 1;
                                        final float totalNumberOfRating = al.get(1)+ra;
                                        userApi.getTransporter(transporterId).enqueue(new Callback<Transporter>() {
                                            @Override
                                            public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                                                if(response.code() == 200){
                                                    Transporter transporter = response.body();
                                                    transporter.setRating(""+totalNumberOfRating/numberOfPersons);
                                                    userApi.updateTransporter(transporter).enqueue(new Callback<Transporter>() {
                                                        @Override
                                                        public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                                                            if(response.code() == 200){
                                                                userApi.createRating(transporterId,leadId,r).enqueue(new Callback<Rating>() {
                                                                    @Override
                                                                    public void onResponse(Call<Rating> call, Response<Rating> response) {
                                                                        if(response.code() == 200){
                                                                            Toast.makeText(MainActivity.this, ""+ra+" stars give ", Toast.LENGTH_SHORT).show();
                                                                            ab.dismiss();
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Call<Rating> call, Throwable t) {
                                                                        Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                                                    }
                                                                });
                                                            }
                                                        }

                                                        @Override
                                                        public void onFailure(Call<Transporter> call, Throwable t) {
                                                            Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Transporter> call, Throwable t) {
                                                Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                }

                                @Override
                                public void onFailure(Call<ArrayList<Float>> call, Throwable t) {
                                    Toast.makeText(MainActivity.this, ""+t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {

                    }
                });
            }
        });
        ab.show();
    }
    private void getFragment() {
        binding.bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;
                switch (item.getItemId()) {
                    case R.id.home:
                        selected = new HomeFragement();
                        binding.toolbar.setTitle("Home");
                        break;
                    case R.id.history:
                        selected = new HistoryFragement();
                        binding.toolbar.setTitle("History");
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame, selected).commit();
                return true;
            }

        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        if (currentUser == null) {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        } else {
            checkUserProfileCreatedOrNot();
        }
    }

    private void checkUserProfileCreatedOrNot() {
        String status = sp.getString("userId", "not_created");
        if (status.equals("not_created")) {
            final String token = FirebaseInstanceId.getInstance().getToken();
            final UserService.UserApi userApi = UserService.getUserApiInstance();
            Call<User> call = userApi.checkProfile(currentUserId);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if(response.code() == 200){
                        User user = response.body();
                        user.setToken(token);
                            userApi.updateProfile(user).enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    if(response.code() == 200){
                                        saveDataLocally(response.body());
                                    }
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {

                                }
                            });
                    }else if(response.code() == 404){
                        Toast.makeText(MainActivity.this, "Create Profile", Toast.LENGTH_SHORT).show();
                        sendUserToCreateProfile();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(MainActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void saveDataLocally(User user){
        SharedPreferences.Editor editor =sp.edit();
        editor.putString("name",user.getName());
        editor.putString("address",user.getAddress());
        editor.putString("contactNumber",user.getContactNumber());
        editor.putString("token",user.getToken());
        editor.putString("imageUrl",user.getImageUrl());
        editor.putString("userId",user.getUserId());
        editor.commit();
    }
    private void sendUserToCreateProfile() {
        Intent in = new Intent(MainActivity.this, CreateProfileActivity.class);
        startActivity(in);
    }

}