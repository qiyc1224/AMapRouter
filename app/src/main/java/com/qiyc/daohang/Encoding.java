package com.qiyc.daohang;
/**
 * Z型编码
 * @author xcc
 *
 */

public class Encoding {
	private double X;
	private double Y;
	
	public Encoding(double x,double y) {
		this.X=x;
		this.Y=y;
	}
	
	/**
	 * 
	 * @return L的二进制编码
	 */
	public String XEncode() {
		int x;
		String xCode;
		x=(int)(this.X/0.5)-1;
		
		xCode=Ten2Two(x);
		return xCode;
	}
	/**
	 * 
	 * @return B的二进制编码
	 */
	
	public String YEncode() {
		int y;
		String yCode;
		y=(int)(this.Y/5)-1;
		
		yCode=Ten2Two(y);
		return yCode;
	}
	
	/**
	 * 十进制转二进制
	 * @param n 十进制整型
	 * @return 二进制String
	 */
	public String Ten2Two(int n) {
		int[] ntwo=new int[20];
		String nTwo="";
		
		int r;
		int i=0;
		int Length=0;
		do{
			if(n%2==1) r=1;
			else r=0;
			ntwo[i]=r;
			n/=2;
			i++;
			Length++;
		}while(n>0);
		
		for(int j=Length-1;j>=0;j--){
			nTwo+=String.valueOf(ntwo[j]);
		}
		return nTwo;
	}
	
	/**
	 * 
	 * @param xcode L的二进制编码
	 * @param ycode B的二进制编码
	 * 输入的编码长度一样
	 * @return LB耦合的十进制编码
	 */
	public int EncodingBasic(String xcode, String ycode) {
		//String xcode=this.XEncode();
		//String ycode=this.YEncode();
		
		char[] xCode=xcode.toCharArray();
		char[] yCode=ycode.toCharArray();
		
		int[] CodeFour=new int[xCode.length];
		for(int i=0;i<xCode.length;i++) {
			String s1=xCode[i]+"";
			String s2=yCode[i]+"";
			int d1=Integer.parseInt(s1);
			int d2=Integer.parseInt(s2);
			
			CodeFour[i]=d1+2*d2;
		}
		
		int CodeTen=0;
		StringBuilder sb=new StringBuilder();
		for(int i=CodeFour.length-1;i>=0;i--) {
			sb.append(CodeFour[i]);
		}
		String tmp=sb.toString();
		int[] CodeFourReverse=new int[CodeFour.length];
		for(int i=0;i<CodeFour.length;i++) {
			CodeFourReverse[i]=Character.getNumericValue(tmp.charAt(i));
			CodeTen+=CodeFourReverse[i]*Math.pow(4, i);
		}
		//System.out.println(sb.toString());
		return CodeTen;
	}
	
	/**
	 * Z型编码，十进制
	 * @return 十进制的Z型编码
	 */
	public int encodingTen(){
		
		String xcode=this.XEncode();
		String ycode=this.YEncode();
		
		//System.out.println(xcode);
		//System.out.println(ycode);
		
		if(xcode.length()==ycode.length()){
			int code=EncodingBasic(xcode,ycode);
			return code;
		}else if(xcode.length()>ycode.length()){
			
			int comp=xcode.length()-ycode.length();
			int[] tmp=new int[comp];
			for(int i=0;i<comp;i++){
				tmp[i]=0;
			}
			String Tmp="";
			for(int j=0;j<comp;j++){
				Tmp+=String.valueOf(tmp[j]);
			}
			
			ycode=Tmp+ycode;
			int code=EncodingBasic(xcode,ycode);
			return code;
		}else{
			int comp=ycode.length()-xcode.length();
			int[] tmp=new int[comp];
			for(int i=0;i<comp;i++){
				tmp[i]=0;
			}
			String Tmp="";
			for(int j=0;j<comp;j++){
				Tmp+=String.valueOf(tmp[j]);
			}
			xcode=Tmp+xcode;
			int code=EncodingBasic(xcode,ycode);
			return code;
		}	
	}

	
	/**
	public String getCode() {
		int CodeTen=encodingTen();
		String code=Ten2Two(CodeTen);
		return code;
	}*/
	
	public int getCode() {
		int Code=encodingTen();
		return Code;
	}
	
	/**
	public static void main(String[] args){
		Encoding d=new Encoding(4.5,20.0);
		//int e=d.encodingTen();
		int code=d.getCode();
		//System.out.println(d.XEncode());
		//System.out.println(d.YEncode());
		System.out.println(code);
	}*/

}
