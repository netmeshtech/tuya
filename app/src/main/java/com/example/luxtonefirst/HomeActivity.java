package com.example.luxtonefirst;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.tuya.smart.home.sdk.TuyaHomeSdk;
import com.tuya.smart.home.sdk.bean.HomeBean;
import com.tuya.smart.home.sdk.builder.ActivatorBuilder;
import com.tuya.smart.home.sdk.callback.ITuyaHomeResultCallback;
import com.tuya.smart.sdk.api.ITuyaActivator;
import com.tuya.smart.sdk.api.ITuyaActivatorGetToken;
import com.tuya.smart.sdk.api.ITuyaSmartActivatorListener;
import com.tuya.smart.sdk.bean.DeviceBean;
import com.tuya.smart.sdk.enums.ActivatorEZStepCode;
import com.tuya.smart.sdk.enums.ActivatorModelEnum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private CardView cvDevice;
    private Button btnSearch;
    private TextView tvDeviceName, tvDeviceId, tvProductId;
    String homeName = "MyHome";
    String[] rooms = {"Kitchen", "Bedroom", "Study"};


    ArrayList<String> roomList;

    private String ssid = "Galaxy M510F8D";
    private String password = "acdf55188";

    private HomeBean currentHomeBean;
    private DeviceBean currentDeviceBean;

    ITuyaActivator tuyaActivator;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initViews();

        cvDevice.setClickable(false);
        cvDevice.setBackgroundColor(Color.BLUE);

        roomList = new ArrayList<>();
        roomList.addAll(Arrays.asList(rooms));


        createHome(homeName, roomList);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentText = btnSearch.getText().toString();

                if(tuyaActivator == null){
                    Toast.makeText(HomeActivity.this, "wifi config in progess:", Toast.LENGTH_LONG).show();

                }else{
                    if(currentText.equalsIgnoreCase("search devices")){
                        tuyaActivator.start();
                        btnSearch.setText("stop search");
                    }else{
                        btnSearch.setText("search devices");
                        tuyaActivator.stop();
                    }
                }



            }
        });
        cvDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("DeviceId", currentDeviceBean.devId);
                bundle.putString("DeviceName", currentDeviceBean.name);
                bundle.putString("ProductId", currentDeviceBean.productId);
                Intent intent = new Intent(HomeActivity.this, DeviceControl.class);
                intent.putExtras(bundle);

                startActivity(intent);

            }
        });





    }

    private void createHome(String homeName, List<String> roomList){
        TuyaHomeSdk.getHomeManagerInstance().createHome(homeName,
                0, 0, "", roomList, new ITuyaHomeResultCallback() {
                    @Override
                    public void onSuccess(HomeBean bean) {
                        currentHomeBean = bean;
                        Toast.makeText(HomeActivity.this, "Home Creation Successful", Toast.LENGTH_LONG).show();
                        getRegistrationToken();


                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        Toast.makeText(HomeActivity.this, "Home Creation failed", Toast.LENGTH_LONG).show();

                    }
                });

    }

    private void searchDevices(String token){

        tuyaActivator = TuyaHomeSdk.getActivatorInstance().newMultiActivator(new ActivatorBuilder()
                .setSsid(ssid)
                .setPassword(password)
                .setContext(this)
                .setActivatorModel(ActivatorModelEnum.TY_EZ)
                .setTimeOut(1000)
                .setToken(token)
                .setListener(new ITuyaSmartActivatorListener() {
                    @Override
                    public void onError(String errorCode, String errorMsg) {

                        Toast.makeText(HomeActivity.this, "Device Detection Failed", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onActiveSuccess(DeviceBean devResp) {

                        Toast.makeText(HomeActivity.this, "Device Detection Successful", Toast.LENGTH_LONG).show();
                        currentDeviceBean = devResp;
                        cvDevice.setClickable(true);
                        cvDevice.setBackgroundColor(Color.WHITE);
                        tvDeviceId.setText("Device ID" + currentDeviceBean.devId);
                        tvDeviceName.setText("Device Name" + currentDeviceBean.name);
                        tvProductId.setText("Product ID" + currentDeviceBean.productId);
                        btnSearch.setText("Search Devices");
                        tuyaActivator.stop();




                    }

                    @Override
                    public void onStep(String step, Object data) {
                        switch(step){
                            case ActivatorEZStepCode
                                    .DEVICE_BIND_SUCCESS:
                                Toast.makeText(HomeActivity.this, "Device Bind Successful", Toast.LENGTH_LONG).show();
                                break;
                            case ActivatorEZStepCode.DEVICE_FIND:
                                Toast.makeText(HomeActivity.this, "New Device found", Toast.LENGTH_LONG).show();
                                break;
                        }

                    }
                })

        );

    }

    private void getRegistrationToken(){

        long homeId = currentHomeBean.getHomeId();
        TuyaHomeSdk.getActivatorInstance().getActivatorToken(homeId, new ITuyaActivatorGetToken() {
            @Override
            public void onSuccess(String token) {
                searchDevices(token);

            }

            @Override
            public void onFailure(String errorCode, String errorMsg) {

            }
        });

    }

    private void initViews(){
        cvDevice = findViewById(R.id.cvDevice);
        btnSearch = findViewById(R.id.btnSearch);
        tvDeviceName = findViewById(R.id.tvDeviceName);
        tvDeviceId = findViewById(R.id.tvDeviceId);
        tvProductId = findViewById(R.id.tvProductId);

    }
}