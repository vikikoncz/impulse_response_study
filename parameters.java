import com.comsol.model.*;
import com.comsol.model.util.*;
import java.io.*;
import java.lang.Math;

public class parameters{
	double K=1e-14;
	double k_reak=1.3e11;
	double L=1;
	double T=298;
	double D_h=9.31e-9;
	double D_oh=5.28e-9;
	double D_k=1.96e-9;
	double D_cl=2.04e-9;
	double K_fix=1e-4;
	double k_fix=6e9;
	double c0_fa=4e-3;
	double magick=1;
	double R=8.3145;
	double F=96485;	

	//Stringkent a mertekegysegek tarolva
	String K_m=" [mol^2/dm^6]";
	String k_reak_m=" [dm^3/mol/s]";
	String L_m=" [mm]";
	String T_m=" [K]";
	String D_h_m=" [m^2/s]";
	String D_oh_m=" [m^2/s]";
	String D_k_m=" [m^2/s]";
	String D_cl_m=" [m^2/s]";
	String K_fix_m=" [mol/dm^3]";
	String k_fix_m=" [dm^3/mol/s]";
	String c0_fa_m=" [mol/dm^3]";
	String magick_m="";
	String R_m=" [J/mol/K]";
	String F_m=" [s*A/mol]";

	//Konstruktor
	parameters(double L, double c_fix){
		this.L=L;
		this.c0_fa=c_fix;
	}
	

	}
