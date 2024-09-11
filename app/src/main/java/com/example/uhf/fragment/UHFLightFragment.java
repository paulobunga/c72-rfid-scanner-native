package com.example.uhf.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.uhf.R;
import com.example.uhf.activity.UHFMainActivity;
import com.example.uhf.tools.UIHelper;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.deviceapi.interfaces.IUHF;

public class UHFLightFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "UHFLightFragment";
    private UHFMainActivity mContext;

    private boolean loopFlag = false;

    CheckBox cb_light_filter;
    EditText etPtr_light_filter;
    EditText etData_light_filter;
    EditText etLen_light_filter;
    RadioButton rbEPC_light_filter;
    RadioButton rbTID_light_filter;
    RadioButton rbUser_light_filter;

    Button btn_light_single;
    Button btn_light_continuous;


    public UHFLightFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.uhf_light_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getView().post(new Runnable() {
            @Override
            public void run() {
                EditText etData_light_filter = (EditText) getView().findViewById(R.id.etData_light_filter);
                EditText etLen_light_filter = (EditText) getView().findViewById(R.id.etLen_light_filter);
                String selectItem = mContext.uhfInfo.getSelectItem();
                if (selectItem != null && !selectItem.equals("")) {
                    etData_light_filter.setText(selectItem);
                    etLen_light_filter.setText(String.valueOf(selectItem.length() * 4));
                } else {
                    etData_light_filter.setText("");
                    etLen_light_filter.setText("0");
                }
            }
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = (UHFMainActivity) getActivity();
        cb_light_filter = (CheckBox) view.findViewById(R.id.cb_light_filter);
        etPtr_light_filter = (EditText) view.findViewById(R.id.etPtr_light_filter);
        etLen_light_filter = (EditText) view.findViewById(R.id.etLen_light_filter);
        etData_light_filter = (EditText) view.findViewById(R.id.etData_light_filter);
        rbEPC_light_filter = (RadioButton) view.findViewById(R.id.rbEPC_light_filter);
        rbTID_light_filter = (RadioButton) view.findViewById(R.id.rbTID_light_filter);
        rbUser_light_filter = (RadioButton) view.findViewById(R.id.rbUser_light_filter);
        btn_light_single = (Button) view.findViewById(R.id.btn_light_single);
        btn_light_continuous = (Button) view.findViewById(R.id.btn_light_continuous);

        rbEPC_light_filter.setOnClickListener(this);
        rbTID_light_filter.setOnClickListener(this);
        rbUser_light_filter.setOnClickListener(this);

        btn_light_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lightTag();
            }
        });

        btn_light_continuous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loopFlag) {
                    loopFlag = false;
                    btn_light_continuous.setText(R.string.btn_light_continuous);
                } else {
                    loopFlag = true;
                    btn_light_continuous.setText(R.string.btn_light_stop);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (loopFlag) {
                                lightTag();
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }).start();
                }
            }
        });

        cb_light_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String data = etData_light_filter.getText().toString().trim();
                    String rex = "[\\da-fA-F]*"; //匹配正则表达式，数据为十六进制格式
                    if (data == null || data.isEmpty() || !data.matches(rex)) {
                        UIHelper.ToastMessage(mContext, getString(R.string.uhf_msg_filter_data_must_hex));
                        cb_light_filter.setChecked(false);
                    }
                }
            }
        });
    }

    private void lightTag() {
        if (cb_light_filter.isChecked()) {  //  过滤
            if (etPtr_light_filter.getText().toString() == null || etPtr_light_filter.getText().toString().isEmpty()) {
                UIHelper.ToastMessage(mContext, getString(R.string.uhf_msg_filter_addr_not_null));
                return;
            }
            if (etLen_light_filter.getText().toString() == null || etLen_light_filter.getText().toString().isEmpty()) {
                UIHelper.ToastMessage(mContext, getString(R.string.uhf_msg_filter_len_not_null));
                return;
            }
            if (etData_light_filter.getText().toString() == null || etData_light_filter.getText().toString().isEmpty()) {
                UIHelper.ToastMessage(mContext, getString(R.string.uhf_msg_filter_data_not_null));
                return;
            }

            int filterPtr = Integer.parseInt(etPtr_light_filter.getText().toString());
            String filterData = etData_light_filter.getText().toString();
            int filterCnt = Integer.parseInt(etLen_light_filter.getText().toString());
            int filterBank = RFIDWithUHFUART.Bank_EPC;
            if (rbEPC_light_filter.isChecked()) {
                filterBank = RFIDWithUHFUART.Bank_EPC;
            } else if (rbTID_light_filter.isChecked()) {
                filterBank = RFIDWithUHFUART.Bank_TID;
            } else if (rbUser_light_filter.isChecked()) {
                filterBank = RFIDWithUHFUART.Bank_USER;
            }
            mContext.mReader.readData("00000000",
                    filterBank,
                    filterPtr,
                    filterCnt,
                    filterData,
                    IUHF.Bank_RESERVED, 4, 1
            );
        } else {
            mContext.mReader.readData("00000000", IUHF.Bank_RESERVED, 4, 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rbEPC_filter:
                if (rbEPC_light_filter.isChecked()) {
                    etPtr_light_filter.setText("32");
                }
                break;
            case R.id.rbTID_filter:
                if (rbTID_light_filter.isChecked()) {
                    etPtr_light_filter.setText("0");
                }
                break;
            case R.id.rbUser_filter:
                if (rbUser_light_filter.isChecked()) {
                    etPtr_light_filter.setText("0");
                }
                break;
        }
    }
}