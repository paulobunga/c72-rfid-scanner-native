package com.example.uhf.fragment;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.uhf.R;
import com.example.uhf.activity.UHFMainActivity;
import com.example.uhf.tools.UIHelper;
import com.example.uhf.view.CircleSeekBar;
import com.example.uhf.view.RadarView;
import com.lidroid.xutils.util.LogUtils;
import com.rscja.deviceapi.entity.RadarLocationEntity;
import com.rscja.deviceapi.interfaces.IUHF;
import com.rscja.deviceapi.interfaces.IUHFRadarLocationCallback;

import java.util.List;

public class UHFRadarLocationFragment extends KeyDwonFragment {

    public final String TAG = "UHFRadarLocationFrag";
    private UHFMainActivity mContext;

    private RadarView radarView;
    private EditText etEPC;
    private Button btStart;
    private Button btStop;
    private CircleSeekBar seekBarPower;

    private boolean inventoryFlag = false;
    private String targetEpc; // 定位标签号
    //    private final RadarBackgroundView.StartAngle radarAngle = new RadarBackgroundView.StartAngle(0);
//    private final List<RadarLocationEntity> radarTagList = new LinkedList<>();
    int progress = 5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_uhf_radar_location, container, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (UHFMainActivity) getActivity();
        mContext.currentFragment = this;

        radarView = getView().findViewById(R.id.radarView);
        etEPC = getView().findViewById(R.id.etRadarEPC);
        btStart = getView().findViewById(R.id.btRadarStart);
        btStop = getView().findViewById(R.id.btRadarStop);
        seekBarPower = getView().findViewById(R.id.seekBarPower);
        seekBarPower.setEnabled(false);
        seekBarPower.setProgress(5);
        btStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocated();
            }
        });
        btStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopLocated();
            }
        });

        getView().post(new Runnable() {
            @Override
            public void run() {
                String selectItem = mContext.uhfInfo.getSelectItem();
                if (selectItem != null && !selectItem.equals("")) {
                    etEPC.setText(selectItem);
                    targetEpc = selectItem;
                } else {
                    etEPC.setText("");
                }
            }
        });

//        mContext.mReader.setKeyEventCallback(new KeyEventCallback() {
//            @SuppressLint("LongLogTag")
//            @Override
//            public void onKeyDown(int keycode) {
//                Log.i(TAG, "keycode=" + keycode);
//                if (!mContext.isSupportRssi) {
//                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.uhf_not_support_rssi));
//                    return;
//                }
//                if (mContext.uhf.getConnectStatus() == ConnectionStatus.CONNECTED) {
//                    if (keycode == 3) {
//                        mContext.isKeyDownUP = true;
//                        startLocated();
//                    } else {
//                        if (!mContext.isKeyDownUP) {
//                            if (keycode == 1) {
//                                if (mContext.isScanning) {
//                                    stopLocated();
//                                } else {
//                                    startLocated();
//                                }
//                            }
//                        }
////                        if (keycode == 2) {
////                            if (mContext.isScanning) {
////                                stop();
////                                SystemClock.sleep(100);
////                            }
////                            //MR20
////                            inventory();
////                        }
//                    }
//
//                }
//            }
//
//            @SuppressLint("LongLogTag")
//            @Override
//            public void onKeyUp(int keycode) {
//                Log.d(TAG, "  keycode =" + keycode);
////                if (!mContext.isSupportRssi) {
////                    UIHelper.ToastMessage(mContext, getResources().getString(R.string.uhf_not_support_rssi));
////                    return;
////                }
//                if (keycode == 4) {
//                    stopLocated();
//                }
//            }
//        });

    }

    Toast toast = null;

    private void showMSG(int power) {
        if (toast == null) {
            toast = new Toast(mContext);
        }
        toast.cancel();
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText("功率:" + power);
        toast.show();
    }

    private Handler handler = new Handler(Looper.getMainLooper());

    @SuppressLint("LongLogTag")
    private void startLocated() {
        if (inventoryFlag) return;

        radarView.clearPanel();
        targetEpc = etEPC.getText().toString();
        boolean result = mContext.mReader.startRadarLocation(mContext, targetEpc, IUHF.Bank_EPC, 32, new IUHFRadarLocationCallback() {
            @Override
            public void getLocationValue(final List<RadarLocationEntity> list) {
//                Log.i(TAG, " list.size=" + list.size());
                radarView.bindingData(list, targetEpc);
                mContext.playSound(1);
            }

            @Override
            public void getAngleValue(int angle) {
                //Log.i(TAG, "angle=" + angle);
                radarView.setRotation(-angle);
            }
        });
        if (!result) {
            UIHelper.ToastMessage(mContext, "启动失败");
            return;
        }

        seekBarPower.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress2, boolean fromUser) {
                Log.d(TAG, "  progress =" + progress2);
                progress = progress2;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "  onStartTrackingTouch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int p = 35 - progress;
                mContext.mReader.setPowerOnDynamic(p);
                Log.d(TAG, "  onStopTrackingTouch  p=" + p + "  progress=" + progress);
                //  Toast.makeText(getContext(),"功率："+progress,Toast.LENGTH_SHORT).show();
            }
        });
        seekBarPower.setEnabled(true);
        inventoryFlag = true;
        btStart.setEnabled(false);
        etEPC.setEnabled(false);

        radarView.startRadar(); // 启动雷达扫描动画
        Log.i(TAG, "startLocated success");
    }

    @SuppressLint("LongLogTag")
    private void stopLocated() {
        if (!inventoryFlag) return;
        radarView.stopRadar();  // 停止雷达扫描动画

        boolean result = mContext.mReader.stopRadarLocation();
        if (!result) {
            //停止失败
            Log.e(TAG, "stopLocated failure");
            mContext.playSound(2);
            Toast.makeText(mContext, R.string.uhf_msg_inventory_stop_fail, Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "stopLocated success");
            inventoryFlag = false;
            btStart.setEnabled(true);
            etEPC.setEnabled(true);
        }
        seekBarPower.setOnSeekBarChangeListener(null);
        seekBarPower.setProgress(5);
        seekBarPower.setEnabled(false);
    }

    @Override
    public void myOnKeyDwon() {
        if (inventoryFlag) {
            stopLocated();
        } else {
            startLocated();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//         targetEpc = etEPC.getText().toString();
//        radarView.bindingData(radarTagList, targetEpc);  // 绑定标签数据
//         radarView.setRotation(-angle);
//         radarView.setStartAngle(radarAngle);
    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocated();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}

