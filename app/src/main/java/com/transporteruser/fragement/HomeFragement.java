package com.transporteruser.fragement;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.transporteruser.adapters.ConfirmLoadAdapter;
import com.transporteruser.adapters.CurrentLoadAdapter;
import com.transporteruser.databinding.HomeFragementBinding;

import java.util.ArrayList;

public class HomeFragement extends Fragment {
CurrentLoadAdapter adapter;
ConfirmLoadAdapter adapter1;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//
//            HomeFragementBinding binding = HomeFragementBinding.inflate(getLayoutInflater());
//            View v = binding.getRoot();
//            ArrayList<CurrentLoadBean> list = new ArrayList<>();
//            list.add(new CurrentLoadBean("Indore To Ujjain", "Iron", "123", "456"));
//            list.add(new CurrentLoadBean("Indore To Bhopal", "Laptop", "123", "111"));
//            list.add(new CurrentLoadBean("Indore To jhasi", "Tyre", "123 adhfkjdkjkfdkjasfkjhdsjkhf", "456dfdsafdsafdsfdsfdsafds"));
//            list.add(new CurrentLoadBean("Indore To shajapur", "Pc", "123", "888"));
//            list.add(new CurrentLoadBean("Indore To Pune", "Mouse", "123", "8877"));
//            list.add(new CurrentLoadBean("Indore To Ahamdabad", "steel", "123", "998855 dhjkf asdhfjkh djhsf"));
//            list.add(new CurrentLoadBean("Indore To AliRajpur", "Copper", "123", "8899 dfjk djkfk jdshf"));
//            list.add(new CurrentLoadBean("Indore To Sawer", "Wood", "123", "4885 dsfkj jdksbfka df"));
//            list.add(new CurrentLoadBean("Indore To Nakpur", "Plastic", "123", "4dsfsd sdfsda  dsf"));
//            list.add(new CurrentLoadBean("Indore To Ratlam", "Laminate", "123", "225 asdfdsf sdfsdaf"));
//
//            adapter = new CurrentLoadAdapter(list, container.getContext());
//            binding.rv.setAdapter(adapter);
//            binding.rv.setLayoutManager(new LinearLayoutManager(container.getContext()));
//            return v;
//

        {
            HomeFragementBinding binding1 = HomeFragementBinding.inflate(getLayoutInflater());
            View view = binding1.getRoot();
            ArrayList<ConfirmdLoadBean> list1 = new ArrayList<>();
            list1.add(new ConfirmdLoadBean("Road Runner", "Indore To Ujjain", "Iron", "198 sgsits B Blocak Near Railway Station Indore", "456", "10000", "10/11/2020", "Loaded"));
            list1.add(new ConfirmdLoadBean("Speed Post", "Ratlam To maksi", "Laptop", "", "59 ssd ", "10000", "10/11/2020", "Intransist"));
            list1.add(new ConfirmdLoadBean("Eagle Shippers", "Nasik To Ujjain", "Wood", "86 Rafel Tower Indore", "456", "10000", "10/11/2020", "Reached"));
            list1.add(new ConfirmdLoadBean("Fedex", "Indore To Nasik", "Laminate", "111 tass", "456", "10000", "10/11/2020", "Loaded"));



            adapter1 = new ConfirmLoadAdapter(list1, container.getContext());
            binding1.rv.setAdapter(adapter1);
            binding1.rv.setLayoutManager(new LinearLayoutManager(container.getContext()));

            return view;
        }

    }
}
