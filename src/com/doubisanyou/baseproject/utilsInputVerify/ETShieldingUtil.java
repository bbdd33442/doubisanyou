package com.doubisanyou.baseproject.utilsInputVerify;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/** 
 * <p>监听输入是否合法</p> 
 */
public class ETShieldingUtil implements TextWatcher{

	private static final String[] regExs  = 
		new String[]{
		"^\\s",//屏蔽空格
		"[^a-zA-Z0-9]"// 只允许字母和数字       
		}; 
	public static final byte REG_CHECK_NOSPACE = 0;

	//监听改变的文本框  
	private EditText editText;  

	private String regEx ;
	/** 
	 * 构造函数 
	 */  
	public ETShieldingUtil(EditText editText,byte reg_check){  
		if(reg_check<0||reg_check>=regExs.length){
			reg_check = 0;
		}
		regEx = regExs[reg_check];
		this.editText = editText;  
	}  

	@Override  
	public void onTextChanged(CharSequence ss, int start, int before, int count) {  
		String editable = editText.getText().toString();  
		String str = stringFilter(editable.toString(),regEx);
		if(!editable.equals(str)){
			editText.setText(str);
			//设置新的光标所在位置  
			editText.setSelection(str.length());
		}
	}  

	@Override  
	public void afterTextChanged(Editable s) {  

	}  
	@Override  
	public void beforeTextChanged(CharSequence s, int start, int count,int after) {  

	}  



	public static String stringFilter(String str,String regEx)throws PatternSyntaxException{     
		Pattern   p   =   Pattern.compile(regEx);     
		Matcher   m   =   p.matcher(str);     
		return   m.replaceAll("").trim();     
	}  
}