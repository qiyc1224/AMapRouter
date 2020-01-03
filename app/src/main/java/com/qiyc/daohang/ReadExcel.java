package com.qiyc.daohang;
/**
 * 读数据文件
 * @author cifengwang
 *
 */

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.io.InputStream;  
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import jxl.Sheet;  
import jxl.Workbook;  
import jxl.read.biff.BiffException;


public class ReadExcel {
	
	
	private double[] LValue;
	private double[] BValue;
	private double[] flux;
	private double[][] cellinfo;
	private int[] code;
	private Map<Integer, List> values=new HashMap<Integer,List>();
	
	public ReadExcel(File file) {  
        try {  
            // 打开excel  
            InputStream is = new FileInputStream(file.getAbsolutePath());  
            // 得到工作表  
            Workbook wb = Workbook.getWorkbook(is);  
            // 得到页数
            int sheet_size = wb.getNumberOfSheets();  
            for (int index = 0; index < sheet_size; index++) {  
                // 页标签  
                Sheet sheet = wb.getSheet(index);  
                cellinfo=new double[sheet.getColumns()][sheet.getRows()];
                /**
                 * 先行再列地读取
                // sheet.getRows()行数
                for (int i = 0; i < sheet.getRows(); i++) {  
                    // sheet.getColumns()列数
                    for (int j = 0; j < sheet.getColumns(); j++) {  
                        String cellinfo = sheet.getCell(j, i).getContents();  
                        System.out.println(cellinfo);  
                    }  
                }  */
                for(int i=0;i<sheet.getColumns();i++) {
                	    for(int j=0;j<sheet.getRows();j++) {
                		    double Cellinfo=Double.parseDouble(sheet.getCell(i,j).getContents());
                		    //System.out.println(Cellinfo);
                		    cellinfo[i][j]=Cellinfo;
                    }
                }
       
                this.LValue=cellinfo[0];
                this.BValue=cellinfo[1];
                this.flux=cellinfo[2];
                
                /**
                for(int i=0;i<LValue.length;i++) {
                	System.out.println(flux[i]);
                }*/
                //System.out.println(flux.length);
                //System.out.println(BValue.length);
            }  
        } catch (FileNotFoundException e) {  
            e.printStackTrace();  
        } catch (BiffException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
    }  
	
	public double[] getLvalue() {
		return this.LValue;
	}
	
	public double[] getBvalue() {
		return this.BValue;
	}
	
	/**
	 * 放到key-value的map中去
	 * key是code
	 * value一个List
	 */
	public void keyValue() {
		int[] Code=new int[LValue.length];
		for(int i=0;i<LValue.length;i++) {
			Encoding e=new Encoding(LValue[i],BValue[i]);
			//this.code[i]=e.getCode();
			Code[i]=e.getCode();
			//System.out.println(Code[i]);
		}
		this.code=Code;
		/**
		for(int i=0;i<code.length;i++) {
			System.out.println(code[i]);
		}*/
		//TODO 值放在二维数组中处理
		Map<Integer,List> valuesMap=new HashMap<Integer,List>();
		for(int i=0;i<LValue.length;i++) {
			List a=new ArrayList();
			a.add(LValue[i]);
			a.add(BValue[i]);
			a.add(flux[i]);
			valuesMap.put(code[i], a);
		}
		
			//valuesMap.put(code[i], a);
		
		this.values=valuesMap;
	}
	
	public Map<Integer,List> getValue(){
		this.keyValue();
		return this.values;
	}
	
	
	/**
	public static void main(String[] args) {
		File file=new File("/Users/cifengwang/Desktop/ap8ae8.xls");
		ReadExcel e=new ReadExcel(file);
		//e.keyValue();
		Map<Integer,List> map=new HashMap<Integer,List>();
		map=e.getValue();
		for (Entry<Integer, List> s : map.entrySet()) {
			System.out.println(s);}
		System.out.println(map.size());
	}*/


}
