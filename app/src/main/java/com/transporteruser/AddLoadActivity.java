package com.transporteruser;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.transporteruser.api.LeadService;
import com.transporteruser.api.StateService;
import com.transporteruser.bean.Lead;
import com.transporteruser.bean.State;
import com.transporteruser.databinding.AddLoadBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLoadActivity extends AppCompatActivity {
    AddLoadBinding addLoadBinding;
    String currentUserId;
    Lead leads;
    static SharedPreferences sp=null;
    String states;
    ArrayList arrayList =new ArrayList();
    DatePickerDialog datePickerDialog;
    private int year,month,day;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLoadBinding= AddLoadBinding.inflate(LayoutInflater.from(this));
        setContentView(addLoadBinding.getRoot());
        currentUserId= FirebaseAuth.getInstance().getCurrentUser().getUid();
        Intent i = getIntent();
        leads =(Lead) i.getSerializableExtra("lead");
        if(leads.getLeadId().length()!=1){
            String[] pickupAddress = leads.getPickUpAddress().split(",");
            addLoadBinding.street.setText(pickupAddress[0]);
            addLoadBinding.city.setText(pickupAddress[1]);

            addLoadBinding.pickupContact.setText(leads.getContactForPickup());
            addLoadBinding.materialType.setText(leads.getTypeOfMaterial());
            addLoadBinding.weight.setText(leads.getWeight());
            addLoadBinding.lastDate.setText(leads.getDateOfCompletion());
            String[] deliveryAddress = leads.getDeliveryAddress().split(",");
            addLoadBinding.street2.setText(deliveryAddress[0]);
            addLoadBinding.city2.setText(deliveryAddress[1]);

            addLoadBinding.deliveryContact.setText(leads.getContactForDelivery());
            addLoadBinding.btncreateLoad.setText("update load");
        }


        initComponent();
         addLoadBinding.lastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               final Calendar calendar=Calendar.getInstance();
                 year=calendar.get(Calendar.YEAR);
                 month=calendar.get(Calendar.MONTH);
                 day=calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog =new DatePickerDialog(AddLoadActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        addLoadBinding.lastDate.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                },year,month,day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        final State state=new State();
        StateService.StateApi stateapi=StateService.getStateApiInstance();
        Call<List<State>> call=stateapi.getstateList();
        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                List<State> list =response.body();
                arrayList.add(0,"State");

                for (State s: list){
                    arrayList.add(s.getStateName());
                }
                ArrayAdapter<State> arrayAdapter=new ArrayAdapter<State>(AddLoadActivity.this, android.R.layout.simple_spinner_item,arrayList);

                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


                addLoadBinding.state2.setAdapter(arrayAdapter);
                addLoadBinding.state2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                            addLoadBinding.state2.setSelection(position);

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                addLoadBinding.state.setAdapter(arrayAdapter);
                addLoadBinding.state.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        addLoadBinding.state.setSelection(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });


            }

            @Override
            public void onFailure(Call<List<State>> call, Throwable t) {
                Toast.makeText(AddLoadActivity.this, t+"", Toast.LENGTH_SHORT).show();
            }
        });

           addLoadBinding.btncreateLoad.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               if (NetworkUtility.checkInternetConnection(AddLoadActivity.this)) {
                   String materialType = addLoadBinding.materialType.getText().toString();
                   String street = addLoadBinding.street.getText().toString();
                   String city = addLoadBinding.city.getText().toString();
                   String state = addLoadBinding.state.getSelectedItem().toString();
                   String street1 = addLoadBinding.street2.getText().toString();
                   String city1 = addLoadBinding.city2.getText().toString();
                   String state1=addLoadBinding.state2.getSelectedItem().toString();
                   String weight = addLoadBinding.weight.getText().toString();
                   String pickupContact = addLoadBinding.pickupContact.getText().toString();
                   String deliveryContact = addLoadBinding.deliveryContact.getText().toString();
                   String lastDate = addLoadBinding.lastDate.getText().toString();
                   String km=addLoadBinding.km.getText().toString();

                   if (TextUtils.isEmpty(materialType)){
                       addLoadBinding.materialType.setError("choose material type");
                       return;
                   }
                   if (TextUtils.isEmpty(street)){
                       addLoadBinding.street.setError("choose your street");
                       return;
                   }
                   if (TextUtils.isEmpty(city)){
                       addLoadBinding.city.setError("choose city");
                       return;
                   }

                   if (TextUtils.isEmpty(lastDate)){
                       addLoadBinding.lastDate.setError("choose last date");
                       return;
                   }
                   if (TextUtils.isEmpty(street1)){
                       addLoadBinding.street2.setError("choose street");
                       return;
                   }
                   if (TextUtils.isEmpty(city1)){
                       addLoadBinding.city2.setError("choose city");
                       return;
                   }

                   if (TextUtils.isEmpty(pickupContact)){
                       addLoadBinding.pickupContact.setError("choose pickup contact");
                       return;
                   }
                   if (TextUtils.isEmpty(deliveryContact)){
                       addLoadBinding.deliveryContact.setError("choose delivery contact");
                       return;
                   }
                   if (TextUtils.isEmpty(km)){
                       addLoadBinding.km.setError("choose distance");
                       return;
                   }


                   leads = new Lead();
                   leads.setTypeOfMaterial(materialType);
                   leads.setWeight(weight);
                   leads.setContactForPickup(pickupContact);
                   leads.setContactForDelivery(deliveryContact);
                   leads.setDateOfCompletion(lastDate);
                   leads.setDeliveryAddress(street + "," + city + "," + state);
                   leads.setPickUpAddress(street1 + "," + city1 + "," + state1 );
                   leads.setUserId(currentUserId);
                   leads.setStatus("");
                   leads.setDealLockedWith("");
                   leads.setTimestamp(String.valueOf(System.currentTimeMillis()));
                   leads.setVehicleNumber("");
                   leads.setKm(km);


                   LeadService.LeadApi leadApi = LeadService.getLeadApiInstance();
                   Call<Lead> call = leadApi.createLeads(leads);
                   call.enqueue(new Callback<Lead>() {
                       @Override
                       public void onResponse(Call<Lead> call, Response<Lead> response) {
                           leads = response.body();
                           Toast.makeText(AddLoadActivity.this, "Load added", Toast.LENGTH_SHORT).show();
                           Intent in = new Intent(AddLoadActivity.this, MainActivity.class);
                           in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                           startActivity(in);
                           finish();
                       }

                       @Override
                       public void onFailure(Call<Lead> call, Throwable t) {
                           Toast.makeText(AddLoadActivity.this, ""+t, Toast.LENGTH_SHORT).show();
                       }
                   });
               }
               else
                   Toast.makeText(AddLoadActivity.this,"Check Internet connection",Toast.LENGTH_SHORT).show();
           }

       });


    }
    private void initComponent() {
        addLoadBinding.toolbar.setTitle("Add Load");
        setSupportActionBar(addLoadBinding.toolbar);
        addLoadBinding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }





}
