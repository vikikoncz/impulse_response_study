import com.comsol.model.*;
import com.comsol.model.util.*;
import java.io.*;
import java.lang.Math;
import java.util.*;
import java.text.SimpleDateFormat;

public class current_write{

void run(data data, double [] tlist1, double [] tlist2, sol sol, sol sol_t, parameters par)throws IOException
{
	machine_settings ms=new machine_settings();
		
	String dirnev_elo=ms.getDIR_for_current();
	
	string_subdir string_subdir=new string_subdir();
	
	String subdir=string_subdir.get_subdir_name(sol, sol_t, tlist1, par);
	
	String dirnev=dirnev_elo+subdir;
	System.out.println(dirnev);
	System.out.println(dirnev_elo);
	System.out.println(subdir);
	
	string_filename string_filename=new string_filename();
	String s=string_filename.get_file_name(sol, sol_t, tlist1, par);
	
	Date dateNow = new Date ();
	String d1=dateNow.toString();
	String d2=d1.replaceAll(" ","_");
	String d3=d2.replaceAll(":",".");
	/*System.out.println(dateNow);
	System.out.println(d3);
	*/
	System.out.println("current");
	File f=new File(dirnev+s+"_"+d3+".dat");
	FileWriter ki_stream = new FileWriter(f);
	PrintWriter ki = new PrintWriter(ki_stream);
	
	ki.println("%Results of the time-dependent simulation of acid-base diode\n%Time-current");
	
	ki.println("%Stationary settings and concentrations:");
	ki.println("%c_koh="+sol.c_koh);
	ki.println("%c_hcl="+sol.c_hcl);
	ki.println("%c_kcl_base="+sol.c_kcl_base);
	ki.println("%c_kcl_acid="+sol.c_kcl_acid);
	ki.println("%U0="+sol.U0);
	ki.println("%");
	
	ki.println("%Time-dependent settings and concentrations");
	ki.println("%c_koh_t="+sol_t.c_koh);
	ki.println("%c_hcl_t="+sol_t.c_hcl);
	ki.println("%c_kcl_base_t="+sol_t.c_kcl_base);
	ki.println("%c_kcl_acid_t="+sol_t.c_kcl_acid);
	ki.println("%U0_t="+sol_t.U0);
	ki.println("%");
	
	int x=tlist1.length; 
	int y=tlist2.length;

//Compute gorbe alatti terulet!!!
	double sum=0;
	for(int i=1; i<x; i++){
		double delta_t=tlist1[i]-tlist1[i-1];
		double c_zero1=data.current_time1[0][i-1]*1000-data.current_stat;
		double c_zero2=data.current_time1[0][i]*1000-data.current_stat;

		double A=delta_t*c_zero1;
		double B=(c_zero2-c_zero1)*delta_t;
		sum=sum+A+B;
	}

	for(int i=1; i<y; i++){
		double delta_t=tlist2[i]-tlist2[i-1];
		double c_zero1=data.current_time2[0][i-1]*1000-data.current_stat;
		double c_zero2=data.current_time2[0][i]*1000-data.current_stat;

		double A=delta_t*c_zero1;
		double B=(c_zero2-c_zero1)*delta_t;
		sum=sum+A+B;
	}



	
	ki.println("%Time_settings");
	ki.println("%Duration of the modeified concentration (peak/impulse)="+tlist1[x-1]);
	ki.println("%Time lists");
	
	ki.print("%tlist1=");
	for(int i=0; i<x; i++){
		ki.print(tlist1[i]+";");
	}
	ki.print("\n");
	
	ki.print("%tlist2=");
	for(int i=0; i<y; i++){
		ki.print(tlist2[i]+";");
	}
	ki.print("\n");
	
	ki.println("%");
	ki.println("%");
	
	//ki.println("%Parameters used in the simulation (meg hianyzik)");
	ki.println("%Parameters used in the simulation");
	ki.println("%K(vizionszorzat)="+par.K);
	ki.println("%k_reak(viz disszociacio sebessegi allando)="+par.k_reak);
	ki.println("%L="+par.L);
	ki.println("%T="+par.T);
	ki.println("%D_h="+par.D_h);
	ki.println("%D_oh="+par.D_oh);
	ki.println("%D_k="+par.D_k);
	ki.println("%D_cl="+par.D_cl);
	ki.println("%K_fix="+par.K_fix);
	ki.println("%k_fix="+par.k_fix);
	ki.println("%c0_fa="+par.c0_fa);
	ki.println("%magick="+par.magick);
	ki.println("%R="+par.R);
	ki.println("%F="+par.F);

	
	ki.println("%");
	ki.println("%");
	
	ki.println("%Time(modified scale in sec) Current (microA)");
	
	ki.println("%Current_stat="+data.current_stat);
	
	for(int i=0; i<x; i++){
		ki.println(tlist1[i]+"\t"+data.current_time1[0][i]*1000/par.L);
	}
	
	int z=x-1;
	for(int i=0; i<y; i++){
		ki.println(tlist2[i]+tlist1[z]+"\t"+data.current_time2[0][i]*1000/par.L);
	}
	

	ki.println("%Gorbe alatti terulet="+sum);
		
	ki.close();





}


}
