package com.example.luxtonefirst;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.tuya.smart.centralcontrol.TuyaLightDevice;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.centralcontrol.api.ILightListener;
import com.tuya.smart.sdk.centralcontrol.api.ITuyaLightDevice;
import com.tuya.smart.sdk.centralcontrol.api.bean.LightDataPoint;
import com.tuya.smart.sdk.centralcontrol.api.constants.LightMode;
import com.tuya.smart.sdk.centralcontrol.api.constants.LightScene;

import java.util.ArrayList;
import java.util.List;

public class LightDetailActivity extends AppCompatActivity {
    private ITuyaLightDevice tuyaLightDevice;
    private EditText mLightDeviceID;
    private Spinner mModeSwitch;
    private Spinner mSceneSwitch;
    private List<String> mModeSwitchData = new ArrayList<>();
    private ArrayAdapter<String> mModeSwitchAdapter;
    private ArrayAdapter<String> mSceneSwitchAdapter;
    private List<String> msceneSwitchData = new ArrayList<>();
    private Context mContext;
    private int mHData;
    private int mSData;
    private int mVData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_detail);
        mContext = this;
        mLightDeviceID = findViewById(R.id.id_et_device_id);
        findViewById(R.id.id_bt_init).setOnClickListener(v -> {
            String text = mLightDeviceID.getText().toString();
            if (!TextUtils.isEmpty(text)) {
                tuyaLightDevice = new TuyaLightDevice(text);
                tuyaLightDevice.registerLightListener(new LightListnener());
            }
        });

        initSwith();
        //initLightSwitch();
        //initSceneSwitch();
        initBrightness();
        initTemperature();
        initH();
        initS();
        initV();
    }

    /**
     * 初始化V值
     */
    private void initV() {
        SeekBar seekBarV = findViewById(R.id.id_skb_v);
        TextView v = findViewById(R.id.id_tv_v);
        seekBarV.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mVData = progress;
                v.setText(mVData + "");
                if (tuyaLightDevice != null) {
                    tuyaLightDevice.colorHSV(mHData, mSData, mVData, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {

                        }

                        @Override
                        public void onSuccess() {

                        }
                    });
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 初始化S值
     */
    private void initS() {
        SeekBar seekBarS = findViewById(R.id.id_skb_s);
        TextView s = findViewById(R.id.id_tv_s);
        seekBarS.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSData = progress;
                s.setText(mSData + "");
                if (tuyaLightDevice != null) {
                    tuyaLightDevice.colorHSV(mHData, mSData, mVData, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {

                        }

                        @Override
                        public void onSuccess() {

                        }
                    });
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 初始化H值
     */
    private void initH() {
        SeekBar seekBarH = findViewById(R.id.id_skb_h);
        TextView h = findViewById(R.id.id_tv_h);
        seekBarH.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mHData = (int) (progress * 3.6);
                h.setText(mHData + "");
                if (tuyaLightDevice != null) {
                    tuyaLightDevice.colorHSV(mHData, mSData, mVData, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {

                        }

                        @Override
                        public void onSuccess() {

                        }
                    });
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 初始化色温
     */
    private void initTemperature() {
        SeekBar seekBarTemperature = findViewById(R.id.id_skb_temperature);
        TextView temperature = findViewById(R.id.id_tv_temperature);
        seekBarTemperature.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                temperature.setText(progress + "");
                if (tuyaLightDevice != null) {
                    tuyaLightDevice.colorTemperature(progress, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {

                        }

                        @Override
                        public void onSuccess() {

                        }
                    });
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 初始化亮度
     */
    private void initBrightness() {
        TextView brighteness = findViewById(R.id.id_tv_brightness);
        SeekBar seekBarBrightness = findViewById(R.id.id_skb_brightness);
        seekBarBrightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                brighteness.setText(progress + "");
                if (tuyaLightDevice != null) {
                    tuyaLightDevice.brightness(progress, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {

                        }

                        @Override
                        public void onSuccess() {

                        }
                    });
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    /**
     * 初始化情景切换
     */


    /**
     * 初始化模式切换
     */


    /**
     * 初始化开关
     */
    private void initSwith() {
        Switch lightswitch = findViewById(R.id.id_sw_switch);
        lightswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override

            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                //控制开关字体颜色
                if (tuyaLightDevice != null) {
                    tuyaLightDevice.powerSwitch(b, new IResultCallback() {
                        @Override
                        public void onError(String code, String error) {

                        }

                        @Override
                        public void onSuccess() {

                        }
                    });
                }
            }
        });
    }

    public class LightListnener implements ILightListener {

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
    }
}
