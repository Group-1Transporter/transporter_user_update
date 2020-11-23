package com.transporteruser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.transporteruser.adapters.MessageAdapter;
import com.transporteruser.adapters.MessageShowAdapter;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Message;
import com.transporteruser.bean.Transporter;
import com.transporteruser.bean.User;
import com.transporteruser.databinding.ChatActivityBinding;

import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {
    ChatActivityBinding binding;
    String transporterId;
    String currentUserId;
    DatabaseReference firebaseDatabase;
    MessageAdapter adapter;
    ArrayList<Message>al;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChatActivityBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        Intent in = getIntent();
        currentUserId = FirebaseAuth.getInstance().getUid();
        transporterId = (String) in.getCharSequenceExtra("transporterId");
        UserService.UserApi userApi = UserService.getUserApiInstance();
        Call<Transporter> call = userApi.getCurrentTransporter(transporterId);
        if (NetworkUtility.checkInternetConnection(this)) {
            firebaseDatabase = FirebaseDatabase.getInstance().getReference();
            call.enqueue(new Callback<Transporter>() {
                @Override
                public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                    if(response.code() == 200){
                        Transporter transporter = response.body();
                        getSupportActionBar().setTitle(transporter.getName());
                        Picasso.get().load(transporter.getImageUrl()).into(binding.civUser);
                    }
                }

                @Override
                public void onFailure(Call<Transporter> call, Throwable t) {

                }
            });
        } else {
            Toast.makeText(this, "please enable internet connection.", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        binding.tvSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String message = binding.etMessage.getText().toString();
                if (message.isEmpty()) {
                    return;
                }
                final Calendar c = Calendar.getInstance();
                long timeStamp = c.getTimeInMillis();
                if (NetworkUtility.checkInternetConnection(ChatActivity.this)) {
                    final String messageId = FirebaseDatabase.getInstance().getReference().push().getKey();
                    binding.etMessage.setText("");
                    final Message msg = new Message(messageId,currentUserId,transporterId,message,timeStamp);
                    firebaseDatabase.child("Messages").child(currentUserId).child(transporterId).child(messageId).setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                firebaseDatabase.child("Messages").child(transporterId).child(currentUserId).child(messageId).setValue(msg).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ChatActivity.this, "Message sent.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });
                } else
                    Toast.makeText(ChatActivity.this, "please enable internet connection.", Toast.LENGTH_SHORT).show();

            }
        });
        al=new ArrayList<>();

        firebaseDatabase.child("Messages").child(currentUserId).child(transporterId).orderByChild("timeStamp").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if(dataSnapshot.exists()){
                    Message msg = dataSnapshot.getValue(Message.class);
                    al.add(msg);
                    adapter = new MessageAdapter(ChatActivity.this,al);
                    binding.rv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {


            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}