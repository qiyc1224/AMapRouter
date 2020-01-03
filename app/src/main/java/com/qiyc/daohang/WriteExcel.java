package com.qiyc.daohang;
/**
 * 把编码和LB按照编码从小到大的顺序写到Excel文件中
 * @author cifengwang
 *
 */

import java.io.File;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;
import java.io.FileNotFoundException;  
import java.io.IOException;  
import java.io.InputStream; 
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import jxl.Sheet;  
import jxl.Workbook;  
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.biff.*;

//import readwriteExcel.Encoding;

public class WriteExcel {
	private final static String[] headers={"code","L-value","B-value","flux"};
	private int exlength;
	
	private File fileRead=new File("/Users/cifengwang/Desktop/ap8ae8.xls");
	private Map<Integer,List> values=new HashMap<Integer,List>();
	//排好序的键值对
	private Map<Integer,List> sortedValues=new TreeMap<Integer,List>();
	private String filePath;
	
	private String[] Code;
	private String[] Lvalue;
	private String[] Bvalue;
	private String[] Flux;
	
	private List valueList=new ArrayList();
	
	public WriteExcel(String filepath) {
		this.filePath=filepath;
		ReadExcel readFile=new ReadExcel(fileRead);
		//double[] Lvalues=readFile.getLvalue();
		//double[] Bvalues=readFile.getBvalue();
		
		/**
		Set LBvaluesSet=new HashSet();
		for(int i=0;i<Lvalues.length;i++){
			LBvaluesSet.add(Lvalues[i]);
			//LBvaluesSet.add(Bvalues[i]);
		}
		System.out.println(LBvaluesSet);*/
		
		
		this.values=readFile.getValue();
		this.exlength=values.size();
		
		/**
		for (Entry<Integer, List> s : values.entrySet()) {
			System.out.println(s);}
		System.out.println(values.size());*/
		
		this.sortedValues=sortedMap();
		//System.out.println(sortedValues.size());
		//System.out.println(exlength);
		
		String[] code=new String[exlength];
		String[] lvalue=new String[exlength];
		String[] bvalue=new String[exlength];
		String[] flux=new String[exlength];
		
		int countC=0;int countV=0;
		
		for(Integer key:sortedValues.keySet()){
			code[countC]=key.toString();
			countC++;
		}
		
		for(List l:sortedValues.values()) {
			lvalue[countV]=l.get(0).toString();
			bvalue[countV]=l.get(1).toString();
			flux[countV]=l.get(2).toString();
			countV++;
		}
		/**
		for(int i=0;i<exlength;i++) {
			System.out.println(code[i]+" "+lvalue[i]+" "+bvalue[i]+" "+flux[i]);
		}*/
		
		this.Code=code;
		this.Lvalue=lvalue;
		this.Bvalue=bvalue;
		this.Flux=flux;
		/**
		for(int i=0;i<exlength;i++) {
			System.out.println(Code[i]+" "+Lvalue[i]+" "+Bvalue[i]+" "+Flux[i]);}*/
		
		//System.out.println(valueList.get(0));
		
		/**
		for (Entry<Integer, List> s : sortedValues.entrySet()) {
			System.out.println(s);}*/
	}
	
	
	public Map<Integer,List> sortedMap(){
		Map<Integer,List> sortedMap=new TreeMap<Integer,List>(new Comparator<Integer>(){
			public int compare(Integer Int1,Integer Int2){
				return Int1.compareTo(Int2);
			}
		});
		sortedMap.putAll(values);
		return sortedMap;
	}
	
	
	//把sortedMap写入Excel
	public void run(){
		WritableWorkbook workbook=null;
		File file=new File(this.filePath);
		
		if(file.exists()){
			try{
				Workbook book=Workbook.getWorkbook(file);
				workbook=Workbook.createWorkbook(file,book);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		else{
			try{
				file.createNewFile();
				workbook=Workbook.createWorkbook(file);
			}catch (Exception e){
				e.printStackTrace();
			}
		}
		
		try{
			//TODO 写入Map
			//headers.setAlignment(Alignment.CENTRE);
			WritableSheet sheet=null;
			sheet=workbook.createSheet("第一页", 0);
			
			/**
			for(int i=0;i<headers.length;i++){
				Label label=new Label(i,0,headers[i]);
				sheet.addCell(label);
				sheet.setColumnView(i, exlength);
			}*///写好了表头,怎么写数据
			
			for(int i=0;i<exlength;i++) {
				Label label1=new Label(0,i,Code[i]);
				sheet.addCell(label1);
				Label label2=new Label(1,i,Lvalue[i]);
				sheet.addCell(label2);
				Label label3=new Label(2,i,Bvalue[i]);
				sheet.addCell(label3);
				Label label4=new Label(3,i,Flux[i]);
				sheet.addCell(label4);
			}
			
			/**
			int c=sheet.getRows();
			sheet.addCell(new Label((LabelCell) sortedValues.keySet()));
			sheet.addCell((WritableCell) sortedValues.values());*/
			
			workbook.write();
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			try{workbook.close();}catch (Exception e){
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	public static void main(String[] args) {
		WriteExcel w=new WriteExcel("/Users/cifengwang/Desktop/ap8ae8_sorted.xls");
		w.run();
	}


}
