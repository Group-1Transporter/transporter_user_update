package com.transporteruser;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.transporteruser.api.LeadService;
import com.transporteruser.api.StateService;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Lead;
import com.transporteruser.bean.State;
import com.transporteruser.bean.User;
import com.transporteruser.databinding.AddLoadBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditLoadActivity extends AppCompatActivity {
    AddLoadBinding binding;
    ArrayList arrayList =new ArrayList();
    String currentUserId, leadId;
    SharedPreferences sp;
    LeadService.LeadApi leadApi;
    User user;
    DatePickerDialog datePickerDialog;
    private int year,month,day;
    Lead leads;
    String selectState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        leadApi = LeadService.getLeadApiInstance();
        binding = AddLoadBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        initComponent();


         final Intent in = getIntent();
         final Lead lead = (Lead) in.getSerializableExtra("lead");

        binding.lastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                year=calendar.get(Calendar.YEAR);
                month=calendar.get(Calendar.MONTH);
                day=calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog =new DatePickerDialog(EditLoadActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        binding.lastDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        final State state=new State();
        StateService.StateApi stateapi=StateService.getStateApiInstance();
        Call<List<State>> call=stateapi.getstateList();
        String[] pickupAddress = lead.getPickUpAddress().split(",");
        binding.street.setText(pickupAddress[0]);
        binding.city.setText(pickupAddress[1]);
        final String picupState=pickupAddress[2];


        String[] deliveryAddress = lead.getDeliveryAddress().split(",");
        binding.street2.setText(deliveryAddress[0]);
        binding.city2.setText(deliveryAddress[1]);
        final String deliveryState=deliveryAddress[2];
        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                final List<State> list =response.body();
                arrayList.add(0,"State");



                ArrayAdapter<State> arrayAdapter=new ArrayAdapter<State>(EditLoadActivity.this, android.R.layout.simple_spinner_item,list);
                int i=0;
                for (State s:list){
                    if (s.getStateName().equals(picupState)){
                        list.set(0,s);
                        arrayAdapter.notifyDataSetChanged();
                        break;
                    }
                    if (s.getStateName().equals(deliveryState)){
                        list.set(0,s);
                        arrayAdapter.notifyDataSetChanged();
                        break;
                    }

                }
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.state2.setAdapter(arrayAdapter);
                binding.state2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        binding.state2.setSelection(position);
                        State s= list.get(position);
                        selectState=s.getStateName();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                binding.state.setAdapter(arrayAdapter);
                binding.state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        binding.state.setSelection(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                Toast.makeText(EditLoadActivity.this, t+"", Toast.LENGTH_SHORT).show();
            }
        });


        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.materialType.setText(lead.getTypeOfMaterial());
        binding.weight.setText(lead.getWeight());
        binding.lastDate.setText(lead.getDateOfCompletion());
        binding.pickupContact.setText(lead.getContactForPickup());
        binding.deliveryContact.setText(lead.getContactForDelivery());
        binding.km.setText(lead.getKm());




        binding.btncreateLoad.setText("Update Load");

        binding.btncreateLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkUtility.checkInternetConnection(EditLoadActivity.this)) {

                        String materialType = binding.materialType.getText().toString();
                        String street = binding.street.getText().toString();
                        String city = binding.city.getText().toString();
                        String state = binding.state.getSelectedItem().toString();
                        String street1 = binding.street2.getText().toString();
                        String city1 = binding.city2.getText().toString();
                        String state1 = binding.state2.getSelectedItem().toString();
                        String weight = binding.weight.getText().toString();
                        String pickupContact = binding.pickupContact.getText().toString();
                        String deliveryContact = binding.deliveryContact.getText().toString();
                        String lastDate = binding.lastDate.getText().toString();
                        String km = binding.km.getText().toString();
                        if (TextUtils.isEmpty(materialType)) {
                            binding.materialType.setError("choose material type");
                            return;
                        }

                        if (lead.getBidCount().equals(0)){
                              binding.materialType.setClickable(false);
                              }
                        if (TextUtils.isEmpty(street)) {
                            binding.street.setError("choose your street");
                            return;
                        }
                        if (TextUtils.isEmpty(city)) {
                            binding.city.setError("choose city");
                            return;
                        }

                        if (TextUtils.isEmpty(lastDate)) {
                            binding.lastDate.setError("choose last date");
                            return;
                        }
                        if (TextUtils.isEmpty(street1)) {
                            binding.street2.setError("choose street");
                            return;
                        }
                        if (TextUtils.isEmpty(city1)) {
                            binding.city2.setError("choose city");
                            return;
                        }

                        if (TextUtils.isEmpty(pickupContact)) {
                            binding.pickupContact.setError("choose pickup contact");
                            return;
                        }
                        if (TextUtils.isEmpty(deliveryContact)) {
                            binding.deliveryContact.setError("choose delivery contact");
                            return;
                        }
                        if (TextUtils.isEmpty(km)) {
                            binding.km.setError("choose distance");
                            return;
                        }

                        Lead lead1 = new Lead();
                        lead1.setTypeOfMaterial(materialType);
                        lead1.setWeight(weight);
                        lead1.setContactForPickup(pickupContact);
                        lead1.setContactForDelivery(deliveryContact);
                        lead1.setDateOfCompletion(lastDate);
                        lead1.setDeliveryAddress(street + "," + city + "," + state);
                        lead1.setPickUpAddress(street1 + "," + city1 + "," + state1);
                        lead1.setUserId(currentUserId);
                        lead1.setStatus("");
                        lead1.setDealLockedWith("");
                        lead1.setTimestamp(String.valueOf(System.currentTimeMillis()));
                        lead1.setVehicleNumber("");
                        lead1.setKm(km);
                        lead1.setLeadId(lead.getLeadId());
                        LeadService.LeadApi leadApi = LeadService.getLeadApiInstance();
                        Call<Lead> call = leadApi.updateLeads(lead1);
                        call.enqueue(new Callback<Lead>() {
                            @Override
                            public void onResponse(Call<Lead> call, Response<Lead> response) {
                                leads = response.body();
                                Toast.makeText(EditLoadActivity.this, "Load edited", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(EditLoadActivity.this, MainActivity.class);
                                in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(in);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<Lead> call, Throwable t) {
                                Toast.makeText(EditLoadActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                else {
                    Toast.makeText(EditLoadActivity.this, "Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void initComponent() {
        binding.toolbar.setTitle("Edit Load");
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}





