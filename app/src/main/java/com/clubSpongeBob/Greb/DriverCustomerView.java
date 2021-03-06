package com.clubSpongeBob.Greb;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DriverCustomerView extends AppCompatActivity {
    private static String TAG = "DriverCustomerView";
    private static ArrayList<Driver> dList = CommonUtils.getDriverArrayList();
    public static customerdriverlist_RecyclerAdapter myAdapter;
    RecyclerView recyclerView;

    public DriverCustomerView() {
        //Required empty public constructor
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_customer_view);

        if (dList.isEmpty()){
            CommonUtils.setWaitingPageListening(false);
            Toast.makeText(getApplicationContext(), "Unable to find driver", Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(this, CustomerLanding.class));
        }


        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        View view = getSupportActionBar().getCustomView();
        ImageView imageView = view.findViewById(R.id.backNavigation);
        imageView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                CommonUtils.getSelf().clearCustomerOrder();
                FirebaseUtils.resetCustomer();
                CommonUtils.setWaitingPageListening(false);
                startActivity(new Intent(view.getContext(), CustomerLanding.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        TextView name = view.findViewById(R.id.name);
        name.setText("Available Drivers");

        FloatingActionButton refreshBtn = this.findViewById(R.id.refreshButton);
        refreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Pressed");
                CommonUtils.getSelf().setStatus(0);
                Driver driver = dList.get(0);
                driver.setCustomer("");
                FirebaseUtils.updateDriver(driver, "Refresh", "Unable to Refresh");
            }
        });

        recyclerView = this.findViewById(R.id.driverRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

        myAdapter = new customerdriverlist_RecyclerAdapter(this, dList, new customerdriverlist_RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Driver driver = dList.get(position);
                Customer customer = CommonUtils.getSelf();
                Log.i(TAG, "Clicked: " + driver.getName());
                driver.setCustomer(customer.getName());
                driver.setStatus(0);
                customer.setStatus(2);
                customer.setEat(driver.getEat());
                CommonUtils.setSelectedDriver(driver);
                FirebaseUtils.updateCustomer(customer, null,null);
                FirebaseUtils.updateDriver(driver,"Successfully pick driver","Unable to pick driver");
                CommonUtils.setWaitingPageListening(false);
                startActivity(new Intent(getApplicationContext(), DriverComing.class));
                finish();
            }
        });

        recyclerView.setAdapter(myAdapter);
    }

    @Override
    public void onBackPressed() {
        CommonUtils.getSelf().clearCustomerOrder();
        FirebaseUtils.resetCustomer();
        CommonUtils.setWaitingPageListening(false);
        startActivity(new Intent(this.getApplicationContext(), CustomerLanding.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
    }
}









