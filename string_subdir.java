import com.comsol.model.*;
import com.comsol.model.util.*;
import java.io.*;
import java.lang.Math;
import java.util.*;
import java.text.SimpleDateFormat;

public class string_subdir{

	String get_subdir_name(sol sol, sol sol_t, double [] tlist1, parameters par) throws IOException{
		System.out.println("subdir");
		int x=tlist1.length;
		double t=tlist1[x-1];
		int U=(int)sol.U0;
		double c_kcl_b=sol.c_kcl_base;
		double L=par.L;
		double c_fix=par.c0_fa;
		
		String L_s="L_"+Double.toString(L)+"mm";
		String c_fix_s="c_fix_"+Double.toString(c_fix);	
		String U_s=Integer.toString(U)+"_V";
		String t_s="t_"+Double.toString(t);
		String c_kcl_b_s="c_kcl_b_"+Double.toString(c_kcl_b);
		
		System.out.println("U_s="+U_s);


		machine_settings ms=new machine_settings();
		
		/*1st check the dictionaries in the current_time subfolder*/
		String main_dir=ms.getDIR_for_current();
		
		String path=main_dir+L_s;
		createFolder(path);
		path=path+"/"+c_fix_s;
		createFolder(path);
		path=path+"/"+U_s;	
		createFolder(path);
		path=path+"/"+t_s;	
		createFolder(path);
		path=path+"/"+c_kcl_b_s;
		createFolder(path);

		/*2nd check the dictionaries in the profile subfolder*/
		main_dir=ms.getDIR_for_profile();
		
		path=main_dir+L_s;
		createFolder(path);
		path=path+"/"+c_fix_s;
		createFolder(path);
		path=path+"/"+U_s;	
		createFolder(path);
		path=path+"/"+t_s;	
		createFolder(path);
		path=path+"/"+c_kcl_b_s;
		createFolder(path);

		
		String s=L_s+"/"+c_fix_s+"/"+U_s+"/"+t_s+"/"+c_kcl_b_s+"/";
		
		return s;
	}


private boolean createFolder(String theFilePath)
{
    boolean result = false;

    File directory = new File(theFilePath);

    if (directory.exists()) {
        System.out.println("Folder already exists");
    } else {
        result = directory.mkdirs();
	System.out.println("Folder is created");
    }

    return result;
}


}
