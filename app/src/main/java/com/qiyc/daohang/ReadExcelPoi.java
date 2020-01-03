package com.qiyc.daohang;

import android.os.Handler;
import android.util.Log;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @Description: java类作用描述
 * @Author: rand
 * @Date: 2020-01-02 17:32
 */

public class ReadExcelPoi implements Runnable {

    private String inputFile;


    public ReadExcelPoi(String inputFile) {
        this.inputFile = inputFile;
    }

    @Override
    public void run() {
        File file = new File(inputFile);
        if (!file.exists()) {
            backInfo(false, null);
            return;
        }
        readExcel2(file);
    }


    public void readExcel2(File excelFile) {
        try {
            Workbook workbook = WorkbookFactory.create(excelFile);
            Sheet sheet = workbook.getSheetAt(0);//或者根据表名称读取：sheet=workbook.getSheet("表1");
            Iterator<Row> iterator = sheet.iterator();
            List<ExcelEntity> excelEntityList = new ArrayList<>();
            while (iterator.hasNext()) {
                //遍历每一行的数据
                Row row = iterator.next();
                Iterator<Cell> cellIterator = row.cellIterator();
                List<ExcelItemEntity> excelItemEntityList = new ArrayList<>();
                while (cellIterator.hasNext()) {
                    //遍历某行的所有单元格
                    Cell cell = cellIterator.next();
                    int cellType = cell.getCellType();
                    ExcelItemEntity excelItemEntity = null;
                    switch (cellType) {
                        case Cell.CELL_TYPE_STRING:  //String
                            excelItemEntity = new ExcelItemEntity(cell.getStringCellValue());
                            Log.d("=====String===", cell.getStringCellValue());
                            break;
                        case Cell.CELL_TYPE_NUMERIC:  //int
                            String result = getExcelEntity(cell);
                            excelItemEntity = new ExcelItemEntity(result);
                            Log.d("=====int===" ,cell.getNumericCellValue()+"");
                            break;
                        case Cell.CELL_TYPE_FORMULA:  //公式
                            excelItemEntity = new ExcelItemEntity(cell.getCellFormula());
                            Log.d("=====公式===" , cell.getCellFormula());
                            break;
                        case Cell.CELL_TYPE_BLANK:  //空格
                            Log.d("=====空格===","");
                            excelItemEntity = new ExcelItemEntity("");
                            break;
                        case Cell.CELL_TYPE_BOOLEAN:  //boolean
                            excelItemEntity = new ExcelItemEntity(String.valueOf(cell.getBooleanCellValue()));
                            Log.d("======boolean===",cell.getBooleanCellValue()+"");
                            break;
                        case Cell.CELL_TYPE_ERROR:  //错误单元格
                            excelItemEntity = new ExcelItemEntity("");
                            Log.d("===错误单元格===" ,cell.getErrorCellValue()+"");
                            break;
                        default:
                            Log.d("=====default===" , cell.getErrorCellValue()+"");
                            excelItemEntity = new ExcelItemEntity(cell.getStringCellValue());
                            break;
                    }
                    excelItemEntityList.add(excelItemEntity);
                }
                excelEntityList.add(new ExcelEntity(0, excelItemEntityList));
            }
            backInfo(true, excelEntityList);
        } catch (Exception e) {
            backInfo(false, null);
            Log.d("=====e===" , e.toString());
            e.printStackTrace();
        }
    }

    /**
     * 获取时间格式得
     *
     * @param cell
     */
    private String getExcelEntity(Cell cell) {
        String result = "";
        if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
            SimpleDateFormat sdf = null;
            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                    .getBuiltinFormat("h:mm")) {
                sdf = new SimpleDateFormat("HH:mm");
            } else {// 日期
                sdf = new SimpleDateFormat("yyyy-MM-dd");
            }
            Date date = cell.getDateCellValue();
            result = sdf.format(date);
        } else if (cell.getCellStyle().getDataFormat() == 58) {
            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            double value = cell.getNumericCellValue();
            Date date = org.apache.poi.ss.usermodel.DateUtil
                    .getJavaDate(value);
            result = sdf.format(date);
        } else {
            double value = cell.getNumericCellValue();
            CellStyle style = cell.getCellStyle();
            DecimalFormat format = new DecimalFormat();
            String temp = style.getDataFormatString();
            // 单元格设置成常规
            if (temp.equals("General")) {
                format.applyPattern("#");
            }
            result = format.format(value);
        }
        Log.d("======data===" , result);
        return result;
    }

    private void backInfo(final boolean isTrue, final List<ExcelEntity> lists) {

        handler.post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private Handler handler = new Handler();

}