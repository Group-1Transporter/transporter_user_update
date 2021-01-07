package com.transporteruser;

import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.transporteruser.databinding.NointernentBinding;

public class NoInternetActivity extends AppCompatActivity {
    NointernentBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=NointernentBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

    }
}
