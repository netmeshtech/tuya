package com.example.luxtonefirst;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.centralcontrol.TuyaLightDevice;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.centralcontrol.api.ILightListener;
import com.tuya.smart.sdk.centralcontrol.api.ITuyaLightDevice;
import com.tuya.smart.sdk.centralcontrol.api.bean.LightDataPoint;
import com.tuya.smart.sdk.centralcontrol.api.constants.LightMode;
import com.tuya.smart.sdk.centralcontrol.api.constants.LightScene;

public class DeviceControl extends AppCompatActivity {

    private TextView tvDeviceName;
    private SeekBar sbBrightness;
    private Switch swStatus;
    private Spinner spWorkMode, spScene;

    String devId, devName, proId;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);

        Bundle bundle = getIntent().getExtras();

        initViews();

        String[] scenes = new String[]{"Goodnight","Casual","Work","Read"};
        String[] workModes = new String[]{"Scene","White","Color"};

        ArrayAdapter<String> sceneAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, scenes );
        ArrayAdapter<String> workModeAdapter = new ArrayAdapter<>(this,R.layout.support_simple_spinner_dropdown_item, workModes );
        spScene.setAdapter(sceneAdapter);
        spWorkMode.setAdapter(workModeAdapter);

        if(bundle != null){
            devId = bundle.getString("DeviceId");
            devName = bundle.getString("DeviceName");
            proId = bundle.getString("ProductId");
            tvDeviceName.setText(devName);

        }

        ITuyaLightDevice controlDevice = new TuyaLightDevice(devId);

        controlDevice.registerLightListener(new ILightListener() {
            @Override
            public void onDpUpdate(LightDataPoint lightDataPoint) {

            }

            @Override
            public void onRemoved() {

            }

            @Override
            public void onStatusChanged(boolean b) {

            }

            @Override
            public void onNetworkStatusChanged(boolean b) {

            }

            @Override
            public void onDevInfoUpdate() {

            }
        });
        swStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                controlDevice.powerSwitch(isChecked, new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(DeviceControl.this, "Light Change Failed", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onSuccess() {
                        Toast.makeText(DeviceControl.this, "Light Change SUCCESSFUL", Toast.LENGTH_LONG).show();

                    }
                });

            }
        });



        sbBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                controlDevice.brightness(progress, new IResultCallback() {
                    @Override
                    public void onError(String code, String error) {
                        Toast.makeText(DeviceControl.this, "Light brightness Change Failed", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onSuccess() {
                        //Toast.makeText(DeviceControl.this, "Light brightness Change successful", Toast.LENGTH_LONG).show();

                    }
                });

                spWorkMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        LightMode selectedLightMode = LightMode.MODE_WHITE;
                        String selectedWorkMode = workModeAdapter.getItem(position);

                        switch (selectedWorkMode){
                            case "Scene":
                                selectedLightMode = LightMode.MODE_SCENE;
                                break;
                            case "White":
                                selectedLightMode = LightMode.MODE_WHITE;
                                break;
                            case "Color":
                                selectedLightMode = LightMode.MODE_COLOUR;
                                break;
                        }

                        controlDevice.workMode(selectedLightMode, new IResultCallback() {
                            @Override
                            public void onError(String code, String error) {
                                Toast.makeText(DeviceControl.this, "workmode Change Failed", Toast.LENGTH_LONG).show();

                            }

                            @Override
                            public void onSuccess() {
                                Toast.makeText(DeviceControl.this, "workmode Change successful", Toast.LENGTH_LONG).show();

                            }
                        });
                        spScene.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                LightScene selectedLightScene = LightScene.SCENE_CASUAL;
                                String selectedScene = sceneAdapter.getItem(position);

                                switch(selectedScene){
                                    case "Good Night":
                                        selectedLightScene = LightScene.SCENE_GOODNIGHT;
                                        break;
                                    case "Work":
                                        selectedLightScene = LightScene.SCENE_WORK;
                                        break;
                                    case "Read":
                                        selectedLightScene = LightScene.SCENE_READ;
                                        break;
                                    case "Casual":
                                        selectedLightScene = LightScene.SCENE_CASUAL;
                                        break;
                                }

                                controlDevice.scene(selectedLightScene, new IResultCallback() {
                                    @Override
                                    public void onError(String code, String error) {

                                    }

                                    @Override
                                    public void onSuccess() {
                                        Toast.makeText(DeviceControl.this, "scene Change successful", Toast.LENGTH_LONG).show();

                                    }
                                });


                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });










    }
    private void initViews(){
        tvDeviceName = findViewById(R.id.tvControlDeviceName);
        sbBrightness = findViewById(R.id.sbBrightness);
        swStatus = findViewById(R.id.swStatus);
        spWorkMode = findViewById(R.id.spWorkMode);
        spScene = findViewById(R.id.spScene);


    }
}