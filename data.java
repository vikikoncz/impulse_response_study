import com.comsol.model.*;
import com.comsol.model.util.*;
import java.io.*;
import java.lang.Math;

public class data{
	double current_stat;
	double [][] current_time1;
	double [][] current_time2;
	double [][][] result_stationary;
	double [][][] result_time1;
	double [][][] result_time2;
	double [] x_gel;
	double terulet;
	
	//Konstruktor
	data(double current_stat,double [][] current_time1,double [][] current_time2,double [][][] result_stationary,double [][][] result_time1,double [][][] result_time2,double [] x_gel){
			this.current_stat=current_stat;
			this.current_time1=current_time1;
			this.current_time2=current_time2;
			this.result_stationary=result_stationary;
			this.result_time1=result_time1;
			this.result_time2=result_time2;
			this.x_gel=x_gel;
			
			
}




}
