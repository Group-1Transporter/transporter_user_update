package com.transporteruser;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.transporteruser.api.LeadService;
import com.transporteruser.api.StateService;
import com.transporteruser.api.UserService;
import com.transporteruser.bean.Bid;
import com.transporteruser.bean.Lead;
import com.transporteruser.bean.State;
import com.transporteruser.bean.Transporter;
import com.transporteruser.databinding.AddLoadBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddLoadActivity extends AppCompatActivity {
    AddLoadBinding addLoadBinding;
    String currentUserId;
    Lead leads;
    Lead lead;
    Transporter transporter;
    String name;
    static SharedPreferences sp = null;
    String states;
    ArrayList arrayList = new ArrayList();
    DatePickerDialog datePickerDialog;
    private int year, month, day;
    LeadService.LeadApi leadApi;
    UserService.UserApi userApi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addLoadBinding = AddLoadBinding.inflate(LayoutInflater.from(this));
        setContentView(addLoadBinding.getRoot());
        final State state = new State();
        SharedPreferences sp = getSharedPreferences("user",MODE_PRIVATE);
        name = sp.getString("name","");
        leadApi = LeadService.getLeadApiInstance();
        userApi = UserService.getUserApiInstance();
        StateService.StateApi stateapi = StateService.getStateApiInstance();
        Call<List<State>> call = stateapi.getstateList();
        call.enqueue(new Callback<List<State>>() {
            @Override
            public void onResponse(Call<List<State>> call, Response<List<State>> response) {
                List<State> list = response.body();
                arrayList.add(0, "State");

                for (State s : list) {
                    arrayList.add(s.getStateName());
                }
                ArrayAdapter<State> arrayAdapter = new ArrayAdapter<State>(AddLoadActivity.this, android.R.layout.simple_spinner_item, arrayList);

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
                Toast.makeText(AddLoadActivity.this, t + "", Toast.LENGTH_SHORT).show();
            }
        });
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        initComponent();
        Intent i = getIntent();
        lead = (Lead) i.getSerializableExtra("lead");
        if (lead != null) {
            addLoadBinding.toolbar.setTitle("Update Load");
            String[] pickupAddress = lead.getPickUpAddress().split(",");
            addLoadBinding.street.setText(pickupAddress[0]);
            addLoadBinding.city.setText(pickupAddress[1]);

            if(!lead.getBidCount().equalsIgnoreCase("0")){
                addLoadBinding.materialType.setVisibility(View.GONE);
                addLoadBinding.state.setVisibility(View.GONE);
                addLoadBinding.state2.setVisibility(View.GONE);
                addLoadBinding.city.setVisibility(View.GONE);
                addLoadBinding.city2.setVisibility(View.GONE);
                addLoadBinding.weight.setVisibility(View.GONE);
            }else{
                addLoadBinding.state.setVisibility(View.GONE);
                addLoadBinding.state2.setVisibility(View.GONE);
            }
            addLoadBinding.pickupContact.setText(lead.getContactForPickup());
            addLoadBinding.materialType.setText(lead.getTypeOfMaterial());
            if (!lead.getWeight().equals("")){
                addLoadBinding.weight.setText("Weight : "+lead.getWeight()+" Ton");}

            addLoadBinding.km.setText(lead.getKm()+" km");
            addLoadBinding.lastDate.setText(lead.getDateOfCompletion());
            String[] deliveryAddress = lead.getDeliveryAddress().split(",");
            addLoadBinding.street2.setText(deliveryAddress[0]);
            addLoadBinding.city2.setText(deliveryAddress[1]);


            addLoadBinding.deliveryContact.setText(lead.getContactForDelivery());
            addLoadBinding.btncreateLoad.setText("update load");

        }

        addLoadBinding.lastDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(AddLoadActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        addLoadBinding.lastDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
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
                    String state1 = addLoadBinding.state2.getSelectedItem().toString();
                    String weight = addLoadBinding.weight.getText().toString();

                    String pickupContact = addLoadBinding.pickupContact.getText().toString();
                    String deliveryContact = addLoadBinding.deliveryContact.getText().toString();
                    String lastDate = addLoadBinding.lastDate.getText().toString();
                    String km=addLoadBinding.km.getText().toString();
                    if (TextUtils.isEmpty(materialType)) {
                        addLoadBinding.materialType.setError("choose material type");
                        return;
                    }
                    if (TextUtils.isEmpty(street)) {
                        addLoadBinding.street.setError("choose your street");
                        return;
                    }
                    if (TextUtils.isEmpty(city)) {
                        addLoadBinding.city.setError("choose city");
                        return;
                    }

                    if (TextUtils.isEmpty(lastDate)) {
                        addLoadBinding.lastDate.setError("choose last date");
                        return;
                    }
                    if (TextUtils.isEmpty(street1)) {
                        addLoadBinding.street2.setError("choose street");
                        return;
                    }
                    if (TextUtils.isEmpty(city1)) {
                        addLoadBinding.city2.setError("choose city");
                        return;
                    }

                    if (TextUtils.isEmpty(pickupContact)) {
                        addLoadBinding.pickupContact.setError("choose pickup contact");
                        return;
                    }
                    if (TextUtils.isEmpty(deliveryContact)) {
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
                    leads.setDeliveryAddress(street1 + "," + city1 + "," + state1);
                    leads.setPickUpAddress(street + "," + city + "," + state);
                    leads.setUserId(currentUserId);
                    leads.setStatus("");
                    leads.setDealLockedWith("");
                    leads.setTimestamp(String.valueOf(System.currentTimeMillis()));
                    leads.setVehicleNumber("");
                    leads.setKm(km);
                    leads.setAmount("");
                    leads.setTransporterName("");
                    String button = addLoadBinding.btncreateLoad.getText().toString();
                    Toast.makeText(AddLoadActivity.this, button, Toast.LENGTH_SHORT).show();
                    if (button.equalsIgnoreCase("update load")) {
                        leads.setLeadId(lead.getLeadId());
                        leadApi.updateLeads(leads).enqueue(new Callback<Lead>() {
                            @Override
                            public void onResponse(Call<Lead> call, Response<Lead> response) {
                                if (response.code() == 200) {
                                    Intent in = new Intent(AddLoadActivity.this, MainActivity.class);
                                    startActivity(in);
                                    getUpdateNotificationUsers(response.body().getLeadId());
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Lead> call, Throwable t) {
                                Toast.makeText(AddLoadActivity.this, "Something Want wrong", Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else if (button.equalsIgnoreCase("add load")) {
                        Call<Lead> call = leadApi.createLeads(leads);
                        call.enqueue(new Callback<Lead>() {
                            @Override
                            public void onResponse(Call<Lead> call, Response<Lead> response) {
                                if(response.code() == 200) {
                                    leads = response.body();
                                    getUpdateNotificationUsers();
                                    Toast.makeText(AddLoadActivity.this, "Load added", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(AddLoadActivity.this, MainActivity.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(in);
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Lead> call, Throwable t) {
                                Toast.makeText(AddLoadActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } else
                    Toast.makeText(AddLoadActivity.this, "Check Internet connection", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(AddLoadActivity.this,NoInternetActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }

        });


    }

    private void getUpdateNotificationUsers(){
        userApi.getTransporters().enqueue(new Callback<ArrayList<Transporter>>() {
            @Override
            public void onResponse(Call<ArrayList<Transporter>> call, Response<ArrayList<Transporter>> response) {
                if(response.code() == 200){
                    for (Transporter t : response.body()){
                        notificationupdate(t.getToken());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Transporter>> call, Throwable t) {

            }
        });
    }
    private void getUpdateNotificationUsers(String leadId) {
        userApi.getAllBidsByLeadId(leadId).enqueue(new Callback<ArrayList<Bid>>() {
            @Override
            public void onResponse(Call<ArrayList<Bid>> call, Response<ArrayList<Bid>> response) {

                if(response.code() == 200){
                    ArrayList<Bid> bidList = response.body();
                    for (int i =0 ; i<bidList.size();i++) {
                        userApi.getCurrentTransporter(bidList.get(i).getTransporterId()).enqueue(new Callback<Transporter>() {
                            @Override
                            public void onResponse(Call<Transporter> call, Response<Transporter> response) {
                                if(response.code() == 200){
                                    notificationupdate(response.body().getToken());
                                }
                            }

                            @Override
                            public void onFailure(Call<Transporter> call, Throwable t) {

                            }
                        });

                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Bid>> call, Throwable t) {

            }
        });
    }

    private void initComponent() {
        addLoadBinding.toolbar.setTitle("Add Load");
        setSupportActionBar(addLoadBinding.toolbar);
        addLoadBinding.toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
    private void notification(String token){
        try{
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://fcm.googleapis.com/fcm/send";

            JSONObject data = new JSONObject();
            data.put("title","Accept Bid ");
            data.put("body", "From "+name);

            JSONObject notification_data = new JSONObject();
            notification_data.put("data", data);
            notification_data.put("to",token);

            JsonObjectRequest request = new JsonObjectRequest(url, notification_data, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String api_key_header_value = "Key=AAAAWv788Wk:APA91bFW0Z_ISKSzu2ZD97ouIZde3jHsaKSvxLG2_adRdmaUCeQ5Jv88XpcNa2o06RruMbRIWF0gYgh6VPYknq-ELrXgIEmp3SVeu3YTH_2cVmEDUT3Jbg1u6N5OxsacPVIFKqkkBhyp";
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", api_key_header_value);
                    return headers;
                }
            };
            queue.add(request);
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private void notificationupdate(String token){
        try{
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://fcm.googleapis.com/fcm/send";

            JSONObject data = new JSONObject();
            data.put("title","Bid Updated ");
            data.put("body", "From "+name);

            JSONObject notification_data = new JSONObject();
            notification_data.put("data", data);
            notification_data.put("to",token);

            JsonObjectRequest request = new JsonObjectRequest(url, notification_data, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    String api_key_header_value = "Key=AAAAWv788Wk:APA91bFW0Z_ISKSzu2ZD97ouIZde3jHsaKSvxLG2_adRdmaUCeQ5Jv88XpcNa2o06RruMbRIWF0gYgh6VPYknq-ELrXgIEmp3SVeu3YTH_2cVmEDUT3Jbg1u6N5OxsacPVIFKqkkBhyp";
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", api_key_header_value);
                    return headers;
                }
            };
            queue.add(request);
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage().toString(), Toast.LENGTH_SHORT).show();
        }
    }

}
