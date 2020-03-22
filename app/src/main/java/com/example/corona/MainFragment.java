package com.example.corona;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {

    Button trackButton;
    TextView tv1;
    TextView riskTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getActivity().getString(R.string.main_header_text));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        Constants.CurrentFragment = this;
        Constants.MainFragment = this;

        tv1 = (TextView)getActivity().findViewById(R.id.textView);
        trackButton = (Button)getActivity().findViewById(R.id.trackButton);
        riskTv = (TextView)getActivity().findViewById(R.id.riskStatusTv);
        riskTv.setText(getString(R.string.risk_low));
        updateUI();

        trackButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!Constants.tracking) {
                    Constants.startingToTrack = true;
                    try {
                        Log.e("logme","start service");

                        if (!Utils.hasPermissions(getActivity())) {
                            ActivityCompat.requestPermissions(getActivity(), Constants.permissions, 1);
                        }
                        else {
                            Utils.createNotificationChannel(getActivity());
                            getActivity().startService(new Intent(getActivity(), LocationService.class));
                            Constants.tracking = true;
                            Constants.startingToTrack = false;
                        }

                    } catch (SecurityException e) {
                        Log.e("log", e.getMessage());
                    }
                }
                else {
                    Log.e("logme","stop service");
                    getActivity().stopService(new Intent(getActivity(), LocationService.class));
                    Constants.tracking = false;
                }
                updateUI();
            }
        });
    }

    public void updateUI() {
        if (Constants.tracking) {
            trackButton.setText("Stop tracking");
            trackButton.setBackgroundResource(R.drawable.stopbutton);
        }
        else {
            trackButton.setText("Start tracking");
            trackButton.setBackgroundResource(R.drawable.startbutton);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Constants.MainFragment = this;
        Constants.CurrentFragment = this;
    }
}