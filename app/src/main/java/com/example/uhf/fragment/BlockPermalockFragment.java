package com.example.uhf.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.uhf.R;
import com.example.uhf.activity.UHFMainActivity;
import com.example.uhf.tools.UIHelper;
import com.rscja.deviceapi.RFIDWithUHFUART;
import com.rscja.utility.StringUtility;


public class BlockPermalockFragment extends KeyDwonFragment implements View.OnClickListener{

    UHFMainActivity mContext;
    EditText etPtr_filter;
    EditText EtRange;
    EditText etLen_filter;
    EditText etData_filter;
    RadioButton rbEPC_filter;
    RadioButton rbTID_filter;
    RadioButton rbUser_filter;
    CheckBox cb_filter;
    Spinner SpinnerBank;
    EditText EtPtr;
    Spinner SpinnerReadLock;
    EditText EtAccessPwd;
    Button btnOK;
    TextView maskbuf;
    CheckBox cbBlock1,cbBlock2,cbBlock3,cbBlock4,cbBlock5,cbBlock6,cbBlock7,cbBlock8;
    CheckBox cbBlock9,cbBlock10,cbBlock11,cbBlock12,cbBlock13,cbBlock14,cbBlock15,cbBlock16;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_block_permalock, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = (UHFMainActivity) getActivity();
        init();
    }

    private void init(){
          cbBlock1=(CheckBox)mContext.findViewById(R.id.cbBlock1);
          cbBlock2=(CheckBox)mContext.findViewById(R.id.cbBlock2);
          cbBlock3=(CheckBox)mContext.findViewById(R.id.cbBlock3);
          cbBlock4=(CheckBox)mContext.findViewById(R.id.cbBlock4);
         cbBlock5=(CheckBox)mContext.findViewById(R.id.cbBlock5);
         cbBlock6=(CheckBox)mContext.findViewById(R.id.cbBlock6);
         cbBlock7=(CheckBox)mContext.findViewById(R.id.cbBlock7);
         cbBlock8=(CheckBox)mContext.findViewById(R.id.cbBlock8);
         cbBlock9=(CheckBox)mContext.findViewById(R.id.cbBlock9);
         cbBlock10=(CheckBox)mContext.findViewById(R.id.cbBlock10);
         cbBlock11=(CheckBox)mContext.findViewById(R.id.cbBlock11);
         cbBlock12=(CheckBox)mContext.findViewById(R.id.cbBlock12);
         cbBlock13=(CheckBox)mContext.findViewById(R.id.cbBlock13);
         cbBlock14=(CheckBox)mContext.findViewById(R.id.cbBlock14);
         cbBlock15=(CheckBox)mContext.findViewById(R.id.cbBlock15);
         cbBlock16=(CheckBox)mContext.findViewById(R.id.cbBlock16);
          etPtr_filter=(EditText)mContext.findViewById(R.id.etPtr_filter_perm);
          etLen_filter=(EditText)mContext.findViewById(R.id.etLen_filter_perm);
          etData_filter=(EditText)mContext.findViewById(R.id.etData_filter_perm);
          rbEPC_filter=(RadioButton)mContext.findViewById(R.id.rbEPC_filter_perm);
          rbTID_filter=(RadioButton)mContext.findViewById(R.id.rbTID_filter_perm);
          rbUser_filter=(RadioButton)mContext.findViewById(R.id.rbUser_filter_perm);
          cb_filter=(CheckBox)mContext.findViewById(R.id.cb_filter_2);
          SpinnerBank=(Spinner)mContext.findViewById(R.id.SpinnerBank);
          EtPtr=(EditText)mContext.findViewById(R.id.EtPtr);
          EtRange=(EditText)mContext.findViewById(R.id.EtRange);
        //----------
        EtRange.setFocusable(false);
        EtRange.setCursorVisible(false);
        EtRange.setFocusableInTouchMode(false);
        //--------
          SpinnerReadLock=(Spinner)mContext.findViewById(R.id.SpinnerReadLock);
          EtAccessPwd=(EditText)mContext.findViewById(R.id.EtAccessPwd);
          btnOK=(Button)mContext.findViewById(R.id.btnOK);
          maskbuf=(TextView)mContext.findViewById(R.id.maskbuf);

         SpinnerBank.setSelection(3);
         rbEPC_filter.setOnClickListener(this);
         rbTID_filter.setOnClickListener(this);
         rbUser_filter.setOnClickListener(this);
         btnOK.setOnClickListener(this);

        cbBlock1.setOnClickListener(this);
        cbBlock2.setOnClickListener(this);
        cbBlock3.setOnClickListener(this);
        cbBlock4.setOnClickListener(this);
        cbBlock5.setOnClickListener(this);
        cbBlock6.setOnClickListener(this);
        cbBlock7.setOnClickListener(this);
        cbBlock8.setOnClickListener(this);
        cbBlock9.setOnClickListener(this);
        cbBlock10.setOnClickListener(this);
        cbBlock11.setOnClickListener(this);
        cbBlock12.setOnClickListener(this);
        cbBlock13.setOnClickListener(this);
        cbBlock14.setOnClickListener(this);
        cbBlock15.setOnClickListener(this);
        cbBlock16.setOnClickListener(this);

        cb_filter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    String data = etData_filter.getText().toString().trim();
                    String rex = "[\\da-fA-F]*"; //匹配正则表达式，数据为十六进制格式
                    if(data==null || data.isEmpty() || !data.matches(rex)) {
                        UIHelper.ToastMessage(mContext, getString(R.string.uhf_msg_filter_data_must_hex));
                        cb_filter.setChecked(false);
                        return;
                    }
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnOK:
                blockPermalock();
                break;
            case R.id.rbEPC_filter_perm:
                UIHelper.ToastMessage(mContext, "epc");
                etPtr_filter.setText("32");
                break;
            case R.id.rbTID_filter_perm:
                UIHelper.ToastMessage(mContext, "tid");
                etPtr_filter.setText("0");
                break;
            case R.id.rbUser_filter_perm:
                etPtr_filter.setText("0");
                break;
            case R.id.cbBlock1:
            case R.id.cbBlock2:
            case R.id.cbBlock3:
            case R.id.cbBlock4:
            case R.id.cbBlock5:
            case R.id.cbBlock6:
            case R.id.cbBlock7:
            case R.id.cbBlock8:
            case R.id.cbBlock9:
            case R.id.cbBlock10:
            case R.id.cbBlock11:
            case R.id.cbBlock12:
            case R.id.cbBlock13:
            case R.id.cbBlock14:
            case R.id.cbBlock15:
            case R.id.cbBlock16:
                getMastBuff();
                break;
        }
    }

    private void blockPermalock(){
          String pwdStr= EtAccessPwd.getText().toString();
          int filter_ptr=0;
          int filter_len=0;
          String filter_data="00";
          int filter_bank= RFIDWithUHFUART.Bank_EPC;
          int readLock= SpinnerReadLock.getSelectedItemPosition();
          int bank=RFIDWithUHFUART.Bank_EPC;

          if(cb_filter.isChecked()){
              if(etPtr_filter.getText().toString()==null || etPtr_filter.getText().toString().isEmpty()){
                  UIHelper.ToastMessage(mContext, getString(R.string.uhf_msg_filter_addr_not_null));
                  return;
              }
              if(etData_filter.getText().toString()==null || etData_filter.getText().toString().isEmpty()){
                  UIHelper.ToastMessage(mContext, getString(R.string.uhf_msg_filter_data_not_null));
                  return;
              }
              if(etLen_filter.getText().toString()==null || etLen_filter.getText().toString().isEmpty()){
                  UIHelper.ToastMessage(mContext, getString(R.string.uhf_msg_filter_len_not_null));
                  return;
              }
              if(!etData_filter.getText().toString().matches("[\\da-fA-F]*")) {
                  UIHelper.ToastMessage(mContext, getString(R.string.uhf_msg_filter_data_must_hex));
                  return;
              }
              filter_ptr=Integer.parseInt(etPtr_filter.getText().toString());
              filter_len=Integer.parseInt(etLen_filter.getText().toString());
              filter_data=etData_filter.getText().toString();
              if(rbTID_filter.isChecked())
                  filter_bank=RFIDWithUHFUART.Bank_TID;
              if(rbUser_filter.isChecked())
                  filter_bank=RFIDWithUHFUART.Bank_USER;
          }
         if (!TextUtils.isEmpty(pwdStr)) {
            if (pwdStr.length() != 8) {
                UIHelper.ToastMessage(mContext, R.string.uhf_msg_addr_must_len8);
                return;
            } else if (!mContext.vailHexInput(pwdStr)) {
                UIHelper.ToastMessage(mContext, R.string.rfid_mgs_error_nohex);
                return;
            }
         } else {
            pwdStr = "00000000";
         }

        if(SpinnerBank.getSelectedItemPosition()==1){
            bank=RFIDWithUHFUART.Bank_EPC;
        }else if(SpinnerBank.getSelectedItemPosition()==2){
            bank=RFIDWithUHFUART.Bank_TID;
        }else{
            bank=RFIDWithUHFUART.Bank_USER;
        }

        if (EtPtr.getText().toString().equals("")) {
            UIHelper.ToastMessage(mContext, R.string.uhf_msg_addr_not_null);
            return;
        }
        int prt=Integer.parseInt(EtPtr.getText().toString());
        int uRange = Integer.parseInt(EtRange.getText().toString());
        byte[] mastBuff= StringUtility.hexString2Bytes(getMastBuff());

        boolean result=mContext.mReader.uhfBlockPermalock(pwdStr,filter_bank,filter_ptr,filter_len,filter_data,readLock,bank,prt,uRange,mastBuff);
        if(!result){
            UIHelper.ToastMessage(mContext, "fail");
            mContext.playSound(2);
        }else{
            mContext.playSound(1);
            if (readLock == 0) {
                maskbuf.setText(StringUtility.bytesHexString(mastBuff));
                int temp=mastBuff[0]&0xff;
                cbBlock8.setChecked ((temp&1)==1?true:false);
                cbBlock7.setChecked ((temp&2)==2?true:false);
                cbBlock6.setChecked ((temp&4)==4?true:false);
                cbBlock5.setChecked ((temp&8)==8?true:false);
                cbBlock4.setChecked ((temp&16)==16?true:false);
                cbBlock3.setChecked ((temp&32)==32?true:false);
                cbBlock2.setChecked ((temp&64)==64?true:false);
                cbBlock1.setChecked ((temp&128)==128?true:false);
                 temp=mastBuff[1]&0xff;
                cbBlock16.setChecked ((temp&1)==1?true:false);
                cbBlock15.setChecked ((temp&2)==2?true:false);
                cbBlock14.setChecked ((temp&4)==4?true:false);
                cbBlock13.setChecked ((temp&8)==8?true:false);
                cbBlock12.setChecked ((temp&16)==16?true:false);
                cbBlock11.setChecked ((temp&32)==32?true:false);
                cbBlock10.setChecked ((temp&64)==64?true:false);
                cbBlock9.setChecked ((temp&128)==128?true:false);
            }
        }
    }
    private String getMastBuff(){
        int temp=0;
        temp=cbBlock8.isChecked()?1:temp;
        temp=cbBlock7.isChecked()?temp|2:temp;
        temp=cbBlock6.isChecked()?temp|4:temp;
        temp=cbBlock5.isChecked()?temp|8:temp;
        temp=cbBlock4.isChecked()?temp|16:temp;
        temp=cbBlock3.isChecked()?temp|32:temp;
        temp=cbBlock2.isChecked()?temp|64:temp;
        temp=cbBlock1.isChecked()?temp|128:temp;
        String hex = Integer.toHexString(temp);
        if (hex.length() == 1) {
            hex = "0" + hex;
        }
        temp=0;
        temp=cbBlock16.isChecked()?1:temp;
        temp=cbBlock15.isChecked()?temp|2:temp;
        temp=cbBlock14.isChecked()?temp|4:temp;
        temp=cbBlock13.isChecked()?temp|8:temp;
        temp=cbBlock12.isChecked()?temp|16:temp;
        temp=cbBlock11.isChecked()?temp|32:temp;
        temp=cbBlock10.isChecked()?temp|64:temp;
        temp=cbBlock9.isChecked()?temp|128:temp;
        String hex2 = Integer.toHexString(temp);
        if (hex2.length() == 1) {
            hex2 = "0" + hex2;
        }
        hex=hex+hex2;
        maskbuf.setText(hex);
        return hex;
    }
}
