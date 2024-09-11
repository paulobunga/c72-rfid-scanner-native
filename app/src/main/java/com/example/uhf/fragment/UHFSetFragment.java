package com.example.uhf.fragment;


import com.example.uhf.R;
import com.example.uhf.activity.UHFMainActivity;
import com.example.uhf.tools.UIHelper;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.rscja.deviceapi.entity.Gen2Entity;
import com.rscja.deviceapi.entity.InventoryModeEntity;
import com.rscja.utility.StringUtility;

import android.app.AlertDialog;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class UHFSetFragment extends KeyDwonFragment implements OnClickListener {
    private UHFMainActivity mContext;

    private Button btnSetFre;
    private Button btnGetFre;
    private Spinner spMode;
    @ViewInject(R.id.ll_freHop)
    private LinearLayout ll_freHop;

    @ViewInject(R.id.spPower)
    private Spinner spPower;
    //    @ViewInject(R.id.et_worktime)
//    private EditText et_worktime;
//    @ViewInject(R.id.et_waittime)
//    private EditText et_waittime;
//    @ViewInject(R.id.btnWorkWait)
//    private Button btnWorkWait;
    @ViewInject(R.id.spFreHop)
    private Spinner spFreHop; //频点列表
    @ViewInject(R.id.btnSetFreHop)
    private Button btnSetFreHop; //设置频点设置
    @ViewInject(R.id.tv_normal_set)
    private TextView tv_normal_set; //普通设置(点击5次设置频点设置)
    //    @ViewInject(R.id.btnGetWait)
//    private Button btnGetWait; //获取空占比
    @ViewInject(R.id.btnSetAgreement)
    private Button btnSetAgreement; //设置协议
    @ViewInject(R.id.SpinnerAgreement)
    private Spinner SpinnerAgreement; //协议列表
    @ViewInject(R.id.btnSetLinkParams)
    private Button btnSetLinkParams; //设置链路参数
    @ViewInject(R.id.btnGetLinkParams)
    private Button btnGetLinkParams; //获取链路参数
    @ViewInject(R.id.splinkParams)
    private Spinner splinkParams; //链路参数列表
    //    @ViewInject(R.id.btnSetQTParams)
//    private Button btnSetQTParams; //设置QT参数
//    @ViewInject(R.id.btnGetQTParams)
//    private Button btnGetQTParams; //获取QT参数
//    @ViewInject(R.id.cbQT)
//    private CheckBox cbQt; //打开QT
    @ViewInject(R.id.cbTagFocus)
    private CheckBox cbTagFocus; //打开tagFocus
    @ViewInject(R.id.cbFastID)
    private CheckBox cbFastID; //打开FastID
    @ViewInject(R.id.cbEPC_TID)
    private CheckBox cbEPC_TID; //打开EPC+TID

    @ViewInject(R.id.rb_America)
    private RadioButton rb_America; //美国频点
    @ViewInject(R.id.rb_Others)
    private RadioButton rb_Others; //其他频点
    private ArrayAdapter adapter; //频点列表适配器

    private DisplayMetrics metrics;
    private AlertDialog dialog;
    private long[] timeArr;

    private Handler mHandler = new Handler();
    private int arrPow; //输出功率

    private String[] arrayMode;

    LinearLayout llUserPtr, llUserLen;
    EditText etUserPtr, etUserLen;
    RadioButton rbEPCTIDUSER, rbEPCTID, rbEPC, rbUnknown;
    Button btnSetInventory, btnGetInventory;
    Spinner spSessionID, spInventoried;
    Button btnSetSession, btnGetSession;
    String TAG = "UHFSetFragment";
    Button btnGetPower, btnSetPower;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_uhfset, container, false);
        ViewUtils.inject(this, root);
        llUserPtr = root.findViewById(R.id.llUserPtr);
        llUserLen = root.findViewById(R.id.llUserLen);
        etUserPtr = root.findViewById(R.id.etUserPtr);
        etUserLen = root.findViewById(R.id.etUserLen);
        rbEPCTIDUSER = root.findViewById(R.id.rbEPCTIDUSER);
        rbEPCTID = root.findViewById(R.id.rbEPCTID);
        rbEPC = root.findViewById(R.id.rbEPC);
        rbUnknown = root.findViewById(R.id.rbUnknown);

        btnSetInventory = root.findViewById(R.id.btnSetInventory);
        btnGetInventory = root.findViewById(R.id.btnGetInventory);

        spSessionID = root.findViewById(R.id.spSessionID);
        spInventoried = root.findViewById(R.id.spInventoried);
        btnGetSession = root.findViewById(R.id.btnGetSession);
        btnSetSession = root.findViewById(R.id.btnSetSession);

        btnGetPower = root.findViewById(R.id.btnGetPower1);
        btnSetPower = root.findViewById(R.id.btnSetPower1);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = (UHFMainActivity) getActivity();
        arrayMode = getResources().getStringArray(R.array.arrayMode);

        btnSetFre = (Button) getView().findViewById(R.id.BtSetFre);
        btnGetFre = (Button) getView().findViewById(R.id.BtGetFre);

        spMode = (Spinner) getView().findViewById(R.id.SpinnerMode);
        spMode.setOnItemSelectedListener(new MyOnTouchListener());

        btnSetFre.setOnClickListener(new SetFreOnclickListener());
        btnGetFre.setOnClickListener(new GetFreOnclickListener());
//        btnWorkWait.setOnClickListener(new SetPWMOnclickListener());
//        btnGetWait.setOnClickListener(this);

        btnSetFreHop.setOnClickListener(this);
        tv_normal_set.setOnClickListener(this);
        btnSetAgreement.setOnClickListener(this);
//        btnSetQTParams.setOnClickListener(this);
//        btnGetQTParams.setOnClickListener(this);
        btnSetLinkParams.setOnClickListener(this);
        btnGetLinkParams.setOnClickListener(this);

        rbEPCTIDUSER.setOnClickListener(this);
        rbEPCTID.setOnClickListener(this);
        rbEPC.setOnClickListener(this);

        btnSetInventory.setOnClickListener(this);
        btnGetInventory.setOnClickListener(this);

        btnGetSession.setOnClickListener(this);
        btnSetSession.setOnClickListener(this);

        btnGetPower.setOnClickListener(this);
        btnSetPower.setOnClickListener(this);

        cbTagFocus.setOnCheckedChangeListener(new OnMyCheckedChangedListener());
        cbFastID.setOnCheckedChangeListener(new OnMyCheckedChangedListener());
        cbEPC_TID.setOnCheckedChangeListener(new OnMyCheckedChangedListener());
        cbEPC_TID.setVisibility(View.GONE);
        String ver = mContext.mReader.getVersion();
        arrPow = R.array.arrayPower;
        ArrayAdapter adapter = ArrayAdapter.createFromResource(mContext, arrPow, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPower.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            /*
            开启子线程获取参数，Handler更新UI,防止fragment打开卡顿
             */
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    getFre();
//                    getPwm();
                    getLinkParams();
                    OnClick_GetPower(null);
                    getEpcTidUserMode(false);
                    getSession();
                }
            });
        }
    }

    /**
     * 工作模式下拉列表点击选中item监听
     */
    public class MyOnTouchListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (spMode.getSelectedItem().toString().equals(getString(R.string.United_States_Standard))) {
                ll_freHop.setVisibility(View.VISIBLE);
                rb_America.setChecked(true); //默认美国频点
            } else if (position != 3) {
                ll_freHop.setVisibility(View.GONE);
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public class SetFreOnclickListener implements OnClickListener {

        @Override
        public void onClick(View v) {

            // byte[] bBaseFre = new byte[2];
            //
            // if (mContext.mReader.setFrequency(
            // (byte) spMode.getSelectedItemPosition(), (byte) 0,
            // bBaseFre, (byte) 0, (byte) 0, (byte) 0)) {
            // UIHelper.ToastMessage(mContext,
            // R.string.uhf_msg_set_frequency_succ);
            // } else {
            // UIHelper.ToastMessage(mContext,
            // R.string.uhf_msg_set_frequency_fail);
            // }

            String strMode = spMode.getSelectedItem().toString();
            int mode = getMode(strMode);
            Log.d(TAG, "setFrequencyMode mode=" + mode);
            if (mContext.mReader.setFrequencyMode((byte) mode)) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_frequency_succ);
            } else {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_frequency_fail);
            }
        }
    }

    public void getFre() {
        int mode = mContext.mReader.getFrequencyMode();
        Log.e(TAG, "getFrequencyMode()=" + mode);
        if (mode != -1) {
            int count = spMode.getCount();
            int idx = getModeIndex(mode);
            //Log.e("TAG", "spMode  " + getResources().getStringArray(R.array.arrayMode).length + "  " + (idx > count - 1 ? count - 1 : idx));
            spMode.setSelection(idx > count - 1 ? count - 1 : idx);
        } else {
            UIHelper.ToastMessage(mContext, R.string.uhf_msg_read_frequency_fail);
        }
    }

//    public void getPwm() {
//        int[] pwm = mContext.mReader.getPwm();
//
//        if (pwm == null || pwm.length < 2) {
//            UIHelper.ToastMessage(mContext, R.string.uhf_msg_read_pwm_fail);
//            return;
//        }
//
//        et_worktime.setText(pwm[0] + "");
//        et_waittime.setText(pwm[1] + "");
//
//        et_worktime.setSelection(et_worktime.getText().toString().length());
//        et_waittime.setSelection(et_waittime.getText().toString().length());
//    }

    /**
     * 获取链路参数
     */
    public void getLinkParams() {
        int idx = mContext.mReader.getRFLink();
        Log.e(TAG, "getLinkParams()=" + idx);
        if (idx != -1) {
            splinkParams.setSelection(idx);

//			UIHelper.ToastMessage(mContext,
//					R.string.uhf_msg_get_para_succ);
        } else {
            UIHelper.ToastMessage(mContext,
                    R.string.uhf_msg_get_para_fail);
        }
    }

    private int getMode(String modeName) {
        if (modeName.equals(getString(R.string.China_Standard_840_845MHz))) {
            return 0x01;
        } else if (modeName.equals(getString(R.string.China_Standard_920_925MHz))) {
            return 0x02;
        } else if (modeName.equals(getString(R.string.ETSI_Standard))) {
            return 0x04;
        } else if (modeName.equals(getString(R.string.United_States_Standard))) {
            return 0x08;
        } else if (modeName.equals(getString(R.string.Korea))) {
            return 0x16;
        } else if (modeName.equals(getString(R.string.Japan))) {
            return 0x32;
        } else if (modeName.equals(getString(R.string.South_Africa_915_919MHz))) {
            return 0x33;
        } else if (modeName.equals(getString(R.string.New_Zealand))) {
            return 0x34;
        } else if (modeName.equals(getString(R.string.Morocco))) {
            return 0x80;
        }
        return 0x08;
    }

    private String getModeName(int mode) {
        switch (mode) {
            case 0x01:
                return getString(R.string.China_Standard_840_845MHz);
            case 0x02:
                return getString(R.string.China_Standard_920_925MHz);
            case 0x04:
                return getString(R.string.ETSI_Standard);
            case 0x08:
                return getString(R.string.United_States_Standard);
            case 0x16:
                return getString(R.string.Korea);
            case 0x32:
                return getString(R.string.Japan);
            case 0x33:
                return getString(R.string.South_Africa_915_919MHz);
            case 0x34:
                return getString(R.string.New_Zealand);
            case 0x80:
                return getString(R.string.Morocco);
            default:
                return getString(R.string.United_States_Standard);
        }
    }


    private int getModeIndex(String modeName) {
        for (int i = 0; i < arrayMode.length; i++) {
            if (arrayMode[i].equals(modeName)) {
                return i;
            }
        }
        return 0;
    }

    private int getModeIndex(int mode) {
        return getModeIndex(getModeName(mode));
    }

//    public class SetPWMOnclickListener implements OnClickListener {
//
//        @Override
//        public void onClick(View v) {
//            if (mContext.mReader.setPwm(StringUtility.string2Int(et_worktime.getText().toString(), 0),
//                    StringUtility.string2Int(et_waittime.getText().toString(), 0))) {
//                UIHelper.ToastMessage(mContext,
//                        R.string.uhf_msg_set_pwm_succ);
//            } else {
//                UIHelper.ToastMessage(mContext,
//                        R.string.uhf_msg_set_pwm_fail);
////                mContext.playSound(2);
//            }
//        }
//    }

    public class GetFreOnclickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            getFre();
        }
    }

    public class OnMyCheckedChangedListener implements CompoundButton.OnCheckedChangeListener {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()) {
                case R.id.cbTagFocus:
                    if (mContext.mReader.setTagFocus(isChecked)) {
                        if (isChecked) {
                            cbTagFocus.setText(R.string.tagFocus_off);
                        } else {
                            cbTagFocus.setText(R.string.tagFocus);
                        }
                        UIHelper.ToastMessage(mContext,
                                R.string.uhf_msg_set_succ);
                    } else {
                        UIHelper.ToastMessage(mContext,
                                R.string.uhf_msg_set_fail);
//                        mContext.playSound(2);
                    }
                    break;
                case R.id.cbFastID:
                    if (mContext.mReader.setFastID(isChecked)) {
                        if (isChecked) {
                            cbFastID.setText(R.string.fastID_off);
                        } else {
                            cbFastID.setText(R.string.fastID);
                        }
                        UIHelper.ToastMessage(mContext,
                                R.string.uhf_msg_set_succ);
                    } else {
                        UIHelper.ToastMessage(mContext,
                                R.string.uhf_msg_set_fail);
//                        mContext.playSound(2);
                    }
                    break;
                case R.id.cbEPC_TID:
                    if (cbEPC_TID.isChecked()) {
                        cbEPC_TID.setText(R.string.EPC_TID_off);
                        if (mContext.mReader.setEPCAndTIDMode()) {
                            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
                        } else {
                            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
                        }
                    } else {
                        cbEPC_TID.setText(R.string.EPC_TID);
                        if (mContext.mReader.setEPCMode()) {
                            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
                        } else {
                            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
                        }
                    }
                    break;

            }
        }
    }

    public void OnClick_GetPower(View view) {
        int iPower = mContext.mReader.getPower();

        Log.i("UHFSetFragment", "OnClick_GetPower() iPower=" + iPower);

        if (iPower > -1) {
            int position = iPower - 1;
            int count = spPower.getCount();
            spPower.setSelection(position > count - 1 ? count - 1 : position);

            // UIHelper.ToastMessage(mContext,
            // R.string.uhf_msg_read_power_succ);

        } else {
            UIHelper.ToastMessage(mContext, R.string.uhf_msg_read_power_fail);
        }

    }

    public void OnClick_SetPower(View view) {
        int iPower = spPower.getSelectedItemPosition() + 1;

        Log.i("UHFSetFragment", "OnClick_SetPower() iPower=" + iPower);

        if (mContext.mReader.setPower(iPower)) {

            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_power_succ);
        } else {
            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_power_fail);
//            mContext.playSound(2);
        }

    }

    /**
     * 设置频点
     *
     * @param value 频点数值
     * @return 是否设置成功
     */
    private boolean setFreHop(float value) {
        boolean result = mContext.mReader.setFreHop(value);
        if (result) {

            UIHelper.ToastMessage(mContext,
                    R.string.uhf_msg_set_frehop_succ);
        } else {
            UIHelper.ToastMessage(mContext,
                    R.string.uhf_msg_set_frehop_fail);
//            mContext.playSound(2);
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnSetFreHop: //设置频点
//			showFrequencyDialog();
                View view = spFreHop.getSelectedView();
                if (view instanceof TextView) {
                    String freHop = ((TextView) view).getText().toString().trim();
                    setFreHop(Float.valueOf(freHop)); //设置频点
                }
                break;
//            case R.id.btnGetWait: //获取空占比
//                getPwm();
//                break;
            case R.id.btnSetAgreement: //设置协议
                if (mContext.mReader.setProtocol(SpinnerAgreement.getSelectedItemPosition())) {
                    UIHelper.ToastMessage(mContext, R.string.setAgreement_succ);
                } else {
                    UIHelper.ToastMessage(mContext, R.string.setAgreement_fail);
//                    mContext.playSound(2);
                }
                break;
//            case R.id.btnSetQTParams: //设置QT参数
//                if (!cbQt.isChecked()) {
//                    UIHelper.ToastMessage(mContext, R.string.please_on);
////                    mContext.playSound(2);
//                    return;
//                }
//                if (mContext.mReader.setQTPara(cbQt.isChecked())) {
//                    UIHelper.ToastMessage(mContext, R.string.setQTParams_succ);
//
//                } else {
//                    UIHelper.ToastMessage(mContext, R.string.setQTParams_fail);
////                    mContext.playSound(2);
//                }
//                break;
//            case R.id.btnGetQTParams: //获取QT参数
//                int[] QTParams = mContext.mReader.getQTPara();
//                if (QTParams[0] == 1) {
//                    cbQt.setChecked(QTParams[1] == 1);
//                    UIHelper.ToastMessage(mContext,
//                            R.string.getQTParams_succ);
//                } else {
//                    UIHelper.ToastMessage(mContext,
//                            R.string.getQTParams_fail);
//                }
//                break;
            case R.id.btnSetLinkParams: //设置链路参数
                if (mContext.mReader.setRFLink(splinkParams.getSelectedItemPosition())) {

                    UIHelper.ToastMessage(mContext,
                            R.string.uhf_msg_set_succ);

                } else {
                    UIHelper.ToastMessage(mContext,
                            R.string.uhf_msg_set_fail);
//                    mContext.playSound(2);
                }
                break;
            case R.id.btnGetLinkParams: //获取链路参数
                getLinkParams();
                break;
            case R.id.rbEPCTIDUSER:
                llUserLen.setVisibility(View.VISIBLE);
                llUserPtr.setVisibility(View.VISIBLE);
                break;
            case R.id.rbEPCTID:
                llUserLen.setVisibility(View.GONE);
                llUserPtr.setVisibility(View.GONE);
                break;
            case R.id.rbEPC:
                llUserLen.setVisibility(View.GONE);
                llUserPtr.setVisibility(View.GONE);
                break;
            case R.id.btnSetInventory:
                setEpcTidUserMode();
                break;
            case R.id.btnGetInventory:
                getEpcTidUserMode(true);
                break;
            case R.id.btnGetSession:
                Log.e("getSession", String.valueOf(getSession()));
                if (getSession()) {
                    UIHelper.ToastMessage(mContext, R.string.uhf_msg_get_para_succ);
                } else {
                    UIHelper.ToastMessage(mContext, R.string.uhf_msg_get_para_fail);
                }
                break;
            case R.id.btnSetSession:
                setSession();
                break;
            case R.id.btnGetPower1:
                OnClick_GetPower(null);
                break;
            case R.id.btnSetPower1:
                OnClick_SetPower(null);
                break;
            default:
                break;
        }
    }

    private boolean getSession() {
        Gen2Entity p = mContext.mReader.getGen2();
        if (p != null) {
            spSessionID.setSelection(p.getQuerySession());
            spInventoried.setSelection(p.getQueryTarget());
            return true;
        }
        return false;
    }

    private void setSession() {
        int seesionid = spSessionID.getSelectedItemPosition();
        int inventoried = spInventoried.getSelectedItemPosition();
        if (seesionid < 0 || inventoried < 0) {
            return;
        }
        Gen2Entity p = mContext.mReader.getGen2();
        if (p != null) {
            p.setQueryTarget(inventoried);
            p.setQuerySession(seesionid);
            if (mContext.mReader.setGen2(p)) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
            } else {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
            }
        } else {
            UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
        }
    }

    /**
     * 显示频点设置
     */
    private void showFrequencyDialog() {
        if (dialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//	        builder.setTitle(R.string.btSetFrequency);
            View view = getActivity().getLayoutInflater().inflate(R.layout.uhf_dialog_frequency, null);
            ListView listView = (ListView) view.findViewById(R.id.listView_frequency);
            ImageView iv = (ImageView) view.findViewById(R.id.iv_dismissDialog);
            iv.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });

            String[] strArr = getResources().getStringArray(R.array.arrayFreHop);
            listView.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.item_text1, strArr));
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    if (view instanceof TextView) {
                        TextView tv = (TextView) view;
                        float value = Float.valueOf(tv.getText().toString().trim());
                        setFreHop(value); //设置频点
                        dialog.dismiss();
                    }
                }

            });

            builder.setView(view);
            dialog = builder.create();
            dialog.show();
            dialog.setCanceledOnTouchOutside(false);

            WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
            params.width = getWindowWidth() - 100;
            params.height = getWindowHeight() - 200;
            dialog.getWindow().setAttributes(params);
        } else {
            dialog.show();
        }
    }

    /**
     * 判断是否为累计点击5次且时间少于1600毫秒（调用一次即点击一次）
     *
     * @return
     */
    private boolean isFiveClick() {
        if (timeArr == null) {
            timeArr = new long[5];
        }
        System.arraycopy(timeArr, 1, timeArr, 0, timeArr.length - 1);
        timeArr[timeArr.length - 1] = System.currentTimeMillis();
        return System.currentTimeMillis() - timeArr[0] < 1600;
    }


    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getWindowWidth() {
        if (metrics == null) {
            metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
        return metrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @return
     */
    public int getWindowHeight() {
        if (metrics == null) {
            metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        }
        return metrics.heightPixels;
    }

    @OnClick(R.id.rb_America)
    public void onClick_rbAmerica(View view) {

        adapter = ArrayAdapter.createFromResource(mContext, R.array.arrayFreHop_us, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFreHop.setAdapter(adapter);
    }

    @OnClick(R.id.rb_Others)
    public void onClick_rbOthers(View view) {

        adapter = ArrayAdapter.createFromResource(mContext, R.array.arrayFreHop, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spFreHop.setAdapter(adapter);

    }

    private void getEpcTidUserMode(boolean isToast) {
        llUserPtr.setVisibility(View.GONE);
        llUserLen.setVisibility(View.GONE);
        InventoryModeEntity inventoryModeEntity = mContext.mReader.getEPCAndTIDUserMode();
        if (inventoryModeEntity != null) {
            int mode = inventoryModeEntity.getMode();
            if (mode == 0) {
                //epc
                rbEPC.setChecked(true);
                Log.i("getEpcTidUserMode", "getEpcTidUserMode = 0");
            } else if (mode == 1) {
                //epc+TID
                rbEPCTID.setChecked(true);
                Log.i("getEpcTidUserMode", "getEpcTidUserMode = 1");
            } else if (mode == 2) {
                //epc+TID+user
                rbEPCTIDUSER.setChecked(true);
                llUserPtr.setVisibility(View.VISIBLE);
                llUserLen.setVisibility(View.VISIBLE);
                etUserPtr.setText(inventoryModeEntity.getUserOffset() + "");
                etUserLen.setText(inventoryModeEntity.getUserLength() + "");
                Log.i("getEpcTidUserMode", "getEpcTidUserMode = 2");
            }
        } else {
            if (isToast) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
            }
        }
    }

    private void setEpcTidUserMode() {

        if (rbEPC.isChecked()) {
            if (mContext.mReader.setEPCMode()) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
            } else {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
            }
        } else if (rbEPCTID.isChecked()) {
            if (mContext.mReader.setEPCAndTIDMode()) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
            } else {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
            }
        } else if (rbEPCTIDUSER.isChecked()) {
            String strUserPtr = etUserPtr.getText().toString();
            String strUserLen = etUserLen.getText().toString();
            int userPtr = 0;
            int userLen = 6;
            if (!TextUtils.isEmpty(strUserPtr)) {
                userPtr = Integer.parseInt(strUserPtr);
            }
            if (!TextUtils.isEmpty(strUserLen)) {
                userLen = Integer.parseInt(strUserLen);
            }

            if (mContext.mReader.setEPCAndTIDUserMode(userPtr, userLen)) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_succ);
            } else {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_set_fail);
            }
        }

    }
}
