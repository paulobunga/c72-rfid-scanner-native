package com.example.uhf.tools;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;

import com.example.uhf.fragment.UHFReadTagFragment;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ExportExcelAsyncTask extends AsyncTask<String, Integer, Boolean> {

    protected ProgressDialog mypDialog;
    protected Activity mContext;

    String pathRoot = Environment.getExternalStorageDirectory() + File.separator + "UHF_exportData";
    //String pathRoot = "sdcard/UHF_exportData/";


    String path = pathRoot + File.separator + GetTimesyyyymmddhhmmss() + ".xlsx";

    boolean isSotp = false;
    ArrayList<HashMap<String, String>> tagList;

    public ExportExcelAsyncTask(Activity act, ArrayList<HashMap<String, String>> tagList) {
        this.mContext = act;
        this.tagList = tagList;
        File file = new File(pathRoot);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        File file = new File(path);
        String[] head = new String[]{"EPC", "Count"};
        ExcelUtils excelUtils = new ExcelUtils();
        excelUtils.createExcel(file, head);

        List<String[]> list = new ArrayList<>();

        for (int i = 0; !isSotp && i < tagList.size(); i++) {
            int pro = (int) (div(i + 1, tagList.size(), 2) * 100);
            publishProgress(pro);
            //LogUtils.logDebug(TAG, "size:" + upcList.size() + " k=" + k);
            String[] data = new String[]{
                    this.tagList.get(i).get(UHFReadTagFragment.TAG_EPC_TID).replace("\n", "\r\n"),
                    this.tagList.get(i).get(UHFReadTagFragment.TAG_COUNT),
            };
            list.add(data);
//                if((i!=0) && (i%5000==0)){
//                    //每次最多执行5000行
//                    excelUtils.writeToExcel(list);
//                    list.clear();
//                }
        }

        list.add(new String[]{"", ""});
        list.add(new String[]{"", ""});
        list.add(new String[]{"标签总数", String.valueOf(this.tagList.size())});

        long begin = System.currentTimeMillis();
        publishProgress(101);
        excelUtils.writeToExcel(list);
        // notifySystemToScan(file);
        long waitTime = 6000 - (System.currentTimeMillis() - begin);
        sleepTime(waitTime);
        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        mypDialog.cancel();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
        if (values[0] == 101) {
            mypDialog.setMessage("path:" + path);
        } else {
            mypDialog.setProgress(values[0]);
        }
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
        mypDialog = new ProgressDialog(mContext);
        mypDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mypDialog.setMessage("...");
        mypDialog.setCanceledOnTouchOutside(false);
        mypDialog.setMax(100);
        mypDialog.setProgress(0);

        mypDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                isSotp = true;
            }
        });

        mypDialog.show();
    }

    private String GetTimesyyyymmddhhmmss() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
        String dt = formatter.format(curDate);
        return dt;
    }

    private void sleepTime(long time) {
        try {
            Thread.sleep(time);
        } catch (Exception ex) {
        }
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */
    private float div(float v1, float v2, int scale) {
        BigDecimal b1 = new BigDecimal(Float.toString(v1));
        BigDecimal b2 = new BigDecimal(Float.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).floatValue();
    }

    @Override
    protected void onCancelled(Boolean aBoolean) {
        super.onCancelled(aBoolean);
        mContext = null;
        tagList = null;
    }
}
