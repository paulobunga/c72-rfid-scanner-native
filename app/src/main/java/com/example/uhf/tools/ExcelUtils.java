package com.example.uhf.tools;


import android.util.Log;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;


/**
 * Created by Administrator on 2019-1-23.
 */

public class ExcelUtils {
    private XSSFWorkbook workbook = null;
    private File excelFile = null;
    public static String TAG = "FileUtils";

    public ExcelUtils() {
    }

    // 创建excel表.
    public void createExcel(File file, String[] head) {
//        WritableSheet ws = null;
        excelFile = file;
        if (file.exists()) {
            file.delete();
        }
        workbook = new XSSFWorkbook();  //创建工作簿
        XSSFSheet sheet = workbook.createSheet("sheet1");   //创建工作表

        XSSFRow xssfRow = sheet.createRow(0);   //创建第一行作表头
        XSSFCell xssfCell;

        // 在指定单元格插入数据
        for (int i = 0; i < head.length; i++) {
            xssfCell = xssfRow.createCell(i);   //创建单元格
            xssfCell.setCellValue(head[i]);     //设置单元格内容
        }

        //用输出流写到excel
        try {
            OutputStream outputStream = new FileOutputStream(excelFile);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将数据存入到Excel表中
    public void writeToExcel(String[] args) {
        try {
            XSSFSheet sheet = this.workbook.getSheetAt(0);
            int lastRowNum = sheet.getLastRowNum();
            XSSFRow row = sheet.createRow(lastRowNum + 1);
            for (int j = 0; j < args.length; j++) {
                row.createCell(j).setCellValue(args[j]);
            }
            OutputStream outputStream = new FileOutputStream(excelFile);
            this.workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //将数据存入到Excel表中
    public void writeToExcel(List<String[]> listData) {
        if (listData == null || listData.size() == 0) return;

        Log.i(TAG, "writeToExcel:" + listData.size());
        try {
            XSSFCellStyle cs = this.workbook.createCellStyle();
            cs.setWrapText(true);   //设置\r\n可换行
            XSSFSheet sheet = this.workbook.getSheetAt(0);
            sheet.setColumnWidth(0, 256 * 40 + 184);    // 设置列宽40
            for (int i = 0; i < listData.size(); i++) {
                XSSFRow row = sheet.createRow(i + 1);   //在现有行号后添加数据，目前只有标题行，故+1
                String[] args = listData.get(i);
                for (int j = 0; j < args.length; j++) {
                    XSSFCell cell = row.createCell(j);
                    cell.setCellStyle(cs);
                    cell.setCellValue(args[j]);
                }
            }
            OutputStream outputStream = new FileOutputStream(excelFile);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();

//            Workbook oldWwb = Workbook.getWorkbook(excelFile);
//            this.workbook = Workbook.createWorkbook(excelFile, oldWwb);
//            WritableSheet ws = this.workbook.getSheet(0);
//            // 当前行数
//            int row = ws.getRows();
//            for (int s = 0; s < listData.size(); s++) {
//                String[] args = listData.get(s);
//                for (int k = 0; k < args.length; k++) {
//                    Label lab1 = new Label(k, row, args[k]);
//                    ws.addCell(lab1);
//                }
//                ++row;
//            }
//            // 从内存中写入文件中,只能刷一次.
//            this.workbook.write();
//            this.workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 导出excel数据
//     *
//     * @param excelFile file
//     * @return data
//     */
//    public List<UPCItem> readExcel(File excelFile) {
//        List<UPCItem> upcImportList = new ArrayList<>();
//        try {
//            Workbook oldWwb = Workbook.getWorkbook(excelFile);
//            Sheet ws = oldWwb.getSheet(0);
//            // 总行数
//            int row = ws.getRows();
//            for (int k = 1; k < row; k++) {
//                UPCItem upcItem = new UPCItem(ws.getCell(0, k).getContents(), ws.getCell(1, k).getContents(), ws.getCell(2, k).getContents());
//                upcImportList.add(upcItem);
//                Log.e(TAG, upcImportList.toString());
//            }
//            oldWwb.close();
//        } catch (Exception e) {
//            Log.e(TAG, e.toString());
//        }
//        return upcImportList;
//    }


}
