/*
 * impulzus_prototipus2.java
 */

import com.comsol.model.*;
import com.comsol.model.util.*;
import java.io.*;
import java.lang.Math;
import java.lang.Exception;

/** Model exported on Jun 19 2012, 11:58 by COMSOL 4.2.1.166. */
public class impulzus_prototipus2 {

  public static void main(String[] args) throws IOException {

	//Parameters
double c_koh=0.1;
double c_hcl=0.1;
double c_kcl_base=Double.parseDouble(args[0]);
double c_kcl_acid=Double.parseDouble(args[1]);
double U0=Double.parseDouble(args[2]);

double c_koh_t=0.1;
double c_hcl_t=0.1;
double c_kcl_base_t=Double.parseDouble(args[3]);
double c_kcl_acid_t=Double.parseDouble(args[4]);
double U0_t=Double.parseDouble(args[5]);

double L=Double.parseDouble(args[6]);
double c_fix=Double.parseDouble(args[7]);

//Debug
System.out.println("c_kcl_base:"+c_kcl_base);
System.out.println("c_kcl_acid:"+c_kcl_acid);
System.out.println("U0:"+U0);
System.out.println("U0:"+Math.round(U0));
System.out.println("c_kcl_base_t:"+c_kcl_base_t);
System.out.println("c_kcl_acid_t:"+c_kcl_acid_t);
System.out.println("U0_t:"+U0_t);
System.out.println("L:"+L);
System.out.println("c_fix:"+c_fix);
  
parameters par=new parameters(L,c_fix);
	
//solba csak azer van benne az U0, h konnyen ki lehessen irni fajlba
sol sol=new sol(c_koh,c_hcl,c_kcl_base,c_kcl_acid, U0);
sol sol_t=new sol(c_koh_t,c_hcl_t,c_kcl_base_t,c_kcl_acid_t, U0_t);

double [][] gel=new proba_normalis().run(sol,par);
double [][] gel_t=new proba_normalis().run(sol_t,par);
  
//U-kat meg itt kell modositani
// U=U0-u_base+u_acid
	double U=U0-gel[0][4]+gel[1][4];
	double U_t=U0_t-gel_t[0][4]+gel_t[1][4];

run(gel,gel_t,par, sol, sol_t,U, U_t);
  
    
  }

  public static Model run(double [][] gel, double [][] gel_t,parameters par, sol sol, sol sol_t, double U, double U_t) throws IOException {
//Atiranyitas a standard kimenetrol file-be
//ez nem kell, amugysem mukodik
		


//Model
    Model model = ModelUtil.create("Model");
	
    model.modelNode().create("mod1");
    model.modelNode("mod1").sorder("quadratic");

try{

//Geometry
    model.geom().create("geom1", 1);		
    model.geom("geom1").lengthUnit("mm");
    model.geom("geom1").scaleUnitValue(true);
    model.geom("geom1").feature().create("i1", "Interval");
	model.geom("geom1").feature("i1").set("intervals", "one");
    model.geom("geom1").feature("i1").set("p1", "0");
    model.geom("geom1").feature("i1").set("p2", par.L);	
    model.geom("geom1").run();


	
//Setting parameters and variables
init_constants_and_variables cons_and_vars=new init_constants_and_variables();
cons_and_vars.run(model, gel, gel_t, par, U, U_t);



//MESH
    //MESH letrehozasa
	
    	model.mesh().create("mesh1", "geom1");   //modify

	double kezdeti_pont=0;
	double veg_pont=0.05;
	//int n_refine=100000;
	int n_refine=50000;
	int n_alap=2000;
//Mesh1 to the time-dependent solvers
	mesh mesh1=new mesh(kezdeti_pont, veg_pont, n_refine, n_alap, par);      	

//Insert vertices and edges and create mesh
	model.mesh("mesh1").data().setElem("edg", mesh1.edge);
	model.mesh("mesh1").data().setVertex(mesh1.vtx);
	model.mesh("mesh1").data().createMesh();

//Mesh2 to the stationary solver  //modify
	kezdeti_pont=0;
	veg_pont=0.02;
	n_refine=8000;
	n_alap=500;

	mesh mesh2=new mesh(kezdeti_pont, veg_pont, n_refine, n_alap, par);
 	
	model.mesh().create("mesh2", "geom1");
    	model.mesh("mesh2").data().setElem("edg", mesh2.edge);
	model.mesh("mesh2").data().setVertex(mesh2.vtx);
	model.mesh("mesh2").data().createMesh();   


//Physics
//Nernst-Planck
    
	physics_Nernst_Planck NP=new physics_Nernst_Planck();
	NP.run(model);

//Poisson
    	physics_Poisson P=new physics_Poisson();
	P.run(model);



/***********************************************************************************************************
************************************************************************************************************
************************************************************************************************************
************************************************************************************************************/

//Study - Study-steps

	model.study().create("std1");

//Step 1 : Stationary (to get the initial conditions of the time-dependent simulation)

double [] plist={1e-10, 1.01e-10, 1.1e-10, 1e-9, 1e-8, 1e-7, 2e-7, 3e-7, 1e-6, 1.01e-6, 1e-5, 1e-4, 2e-4, 4e-4, 6e-4, 8e-4 ,1e-3, 1e-2, 1e-1, 0.12, 0.14, 0.16, 0.18, 0.2, 0.22, 0.24, 0.3, 0.4, 0.5, 0.55, 0.6, 0.8, 1};

//double [] plist={1e-10, 1.01e-10, 1.1e-10, 1e-9};    


    model.study("std1").feature().create("stat1", "Stationary");
    model.study("std1").feature("stat1").set("geomselection", "geom1");
    model.study("std1").feature("stat1").set("mesh", new String[]{"geom1", "mesh2"});
    model.study("std1").feature("stat1").set("useparam", "on");
    model.study("std1").feature("stat1").set("pname", new String[]{"magick"});
    model.study("std1").feature("stat1").set("plist", plist);
    model.study("std1").feature("stat1").set("plot", "on");
    model.study("std1").feature("stat1").set("probesel", "manual");
	

//Step 2 : Time-dependent 

//double [] tlist1={0, 0.001, 0.005, 0.01, 0.02, 0.04, 0.06, 0.08, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.9, 0.95, 1};
double [] tlist1={0, 0.001, 0.005, 0.01, 0.02, 0.04, 0.06, 0.08, 0.1, 0.15, 0.2, 0.25, 0.3, 0.35, 0.4, 0.45, 0.5};
//	double [] tlist1={0, 0.001, 0.005, 0.01, 0.02, 0.04, 0.06, 0.08, 0.1, 0.12, 0.14, 0.16, 0.18, 0.2};   //modify
//double [] tlist1={0, 0.001, 0.005, 0.01, 0.02, 0.04, 0.06, 0.08, 0.1};

    model.study("std1").feature().create("time", "Transient");
    model.study("std1").feature("time").set("geomselection", "geom1");
    model.study("std1").feature("time").set("mesh", new String[]{"geom1", "mesh1"});
    model.study("std1").feature("time").set("tlist", tlist1);

//Step 3: Time-dependent 2 (boundary conditions are the same as in the stationary simulation)

double [] tlist2={0, 0.01, 0.02, 0.04, 0.06, 0.08, 0.1, 0.12, 0.14, 0.16, 0.18, 0.2, 0.22, 0.24, 0.26, 0.28, 0.3, 0.32, 0.34, 0.36, 0.38, 0.4, 0.42, 0.44, 0.46, 0.48, 0.5, 0.52, 0.54, 0.56, 0.58, 0.6, 0.62, 0.64, 0.68, 0.7, 0.72, 0.74, 0.76, 0.78, 0.8, 0.82, 0.84, 0.86, 0.88, 0.9, 0.92, 0.94, 0.96, 1, 1.1, 1.2, 1.3, 1.4, 1.5, 1.6, 1.8, 2, 2.2, 2.4, 2.6, 2.8, 3, 3.2, 3.4, 3.6, 3.8, 4, 4.2, 4.4, 4.6, 4.8, 5, 5.2, 5.4, 5.6, 5.8, 6, 6.2, 6.4, 6.8, 7, 7.2, 7.4, 7.6, 7.8, 8, 8.2, 8.4, 8.6, 8.8, 9, 9.2, 9.4, 9.6, 9.8, 10, 10.5, 11, 11.5, 12, 12.5, 13, 13.5, 14, 14.5, 15, 16, 17, 18, 19, 20, 22, 24, 26, 28, 30, 35, 40, 45, 50, 55, 60, 65, 70, 80, 90, 100};


    
    model.study("std1").feature().create("time2", "Transient");
    model.study("std1").feature("time2").set("tlist", tlist2);
    
    model.study("std1").feature("time2").set("mesh", new String[]{"geom1", "mesh1"});

//Solvers
//Solver3 : contains steps 1, 2

    model.sol().create("sol3");
    model.sol("sol3").study("std1");
    model.sol("sol3").attach("std1");

//Stationary solver
    model.sol("sol3").feature().create("st1", "StudyStep");
    model.sol("sol3").feature().create("v1", "Variables");
    model.sol("sol3").feature().create("s1", "Stationary");
    model.sol("sol3").feature("s1").feature().create("fc1", "FullyCoupled");
    model.sol("sol3").feature("s1").feature().create("d1", "Direct");
    model.sol("sol3").feature("s1").feature().create("p1", "Parametric");
    model.sol("sol3").feature("s1").feature().remove("fcDef");
    

    model.sol("sol3").feature("st1").name("Compile Equations: Stationary 1");
    model.sol("sol3").feature("st1").set("studystep", "stat1");
    model.sol("sol3").feature("v1").set("control", "stat1");
    model.sol("sol3").feature("s1").set("nonlin", "on");
    model.sol("sol3").feature("s1").feature("dDef").set("linsolver", "pardiso");
    model.sol("sol3").feature("s1").feature("dDef").set("pardreorder", "nd");
    model.sol("sol3").feature("s1").feature("fc1").set("dtech", "hnlin");
    model.sol("sol3").feature("s1").feature("fc1").set("maxiter", "100");
    model.sol("sol3").feature("s1").feature("fc1").set("ntolfact", "5");
    model.sol("sol3").feature("s1").feature("fc1").set("probesel", "manual");
    model.sol("sol3").feature("s1").feature("d1").set("linsolver", "pardiso");
    model.sol("sol3").feature("s1").feature("p1").set("control", "stat1");
    model.sol("sol3").feature("s1").feature("p1").set("pname", new String[]{"magick"});
    model.sol("sol3").feature("s1").feature("p1").set("plist", plist);
    model.sol("sol3").feature("s1").feature("p1").set("plot", "on");
    model.sol("sol3").feature("s1").feature("p1").set("probesel", "manual");
    


//Time-dependent solver
model.sol("sol3").feature().create("st2", "StudyStep");
    model.sol("sol3").feature().create("v2", "Variables");

model.sol("sol3").feature().create("t1", "Time");
    model.sol("sol3").feature("t1").feature().create("taDef", "TimeAdaption");    

model.sol("sol3").feature("st2").name("Compile Equations: Time Dependent (2)");
    model.sol("sol3").feature("st2").set("studystep", "time");
    model.sol("sol3").feature("v2").set("initmethod", "sol");
    model.sol("sol3").feature("v2").set("initsol", "sol3");
    model.sol("sol3").feature("v2").set("notsolmethod", "sol");
    model.sol("sol3").feature("v2").set("notsol", "sol3");
    model.sol("sol3").feature("t1").set("control", "time");
    model.sol("sol3").feature("t1").set("tlist", tlist1);
    model.sol("sol3").feature("t1").feature("dDef").set("linsolver", "spooles");

	model.sol("sol3").feature("t1").feature("fcDef").set("dtech", "auto");
    model.sol("sol3").feature("t1").feature().remove("taDef");
	
	
	//Here you can modify the tolerance values of the TD-solver... just try!
//with previous mesh
	model.sol("sol3").feature("t1").set("atolglobal", "0.000010");
    model.sol("sol3").feature("t1").set("fieldselection", "mod1_oh");
//Relative tolerance beallitasok
	//model.sol("sol3").feature("t1").set("control", "user");
	model.sol("sol3").feature("t1").set("rtol", "0.001");

    //model.name("fixch_strong_ez_talan_OK2_Model.mph");

//Solver 5 : contains step 3
//Time-dependent solver (inherited from default solver)
    model.sol("sol3").detach();
    model.sol().create("sol5");

	model.sol("sol5").feature().create("st3", "StudyStep");
    model.sol("sol5").feature().create("v3", "Variables");

    model.sol("sol5").feature().create("t2", "Time");
    model.sol("sol5").feature("t2").feature().create("taDef", "TimeAdaption");    

    model.sol("sol5").feature("st3").name("Compile Equations: Time Dependent (2)");
    model.sol("sol5").feature("st3").set("studystep", "time2");
    model.sol("sol5").feature("v3").set("initmethod", "sol");
    model.sol("sol5").feature("v3").set("initsol", "sol3");
    model.sol("sol5").feature("v3").set("notsolmethod", "sol");
    model.sol("sol5").feature("v3").set("notsol", "sol3");
    model.sol("sol5").feature("t2").set("control", "time");
    model.sol("sol5").feature("t2").set("tlist", tlist2);
    model.sol("sol5").feature("t2").feature("dDef").set("linsolver", "spooles");

	model.sol("sol5").feature("t2").feature("fcDef").set("dtech", "auto");
    model.sol("sol5").feature("t2").feature().remove("taDef");

	//Here you can modify the tolerance values of the TD-solver... just try!
//with previous mesh
	model.sol("sol5").feature("t2").set("atolglobal", "0.000010");
    model.sol("sol5").feature("t2").set("fieldselection", "mod1_oh");
//Relative tolerance beallitasok
	//model.sol("sol5").feature("t2").set("control", "user");
	model.sol("sol5").feature("t2").set("rtol", "0.001");
	
	
	
/*
************************************************************************************
********************************************************************************
*/
//Must solve the model to get results

//Settings about the profiles to have figuers
int p=500; //number of points
double h=par.L/500;

double [] x_gel=new double[p+1];
for(int i=0; i<=p; i++){
	x_gel[i]=i*h;
}

//Step 1
//Solve the stationary model
	
	model.sol("sol3").run("s1");

//Getting the stationary results (the initial condition of the transient analysis) 1st time-dependent solver uses the same sol3 (resrite the datas must be avoided)

	/*Arrays for the interpolation*/
	String tomb[]={"h","oh","k","cl","c_fa","phi"};	
	int [] parameters={plist.length-1};  //the last magic factor

	model.result().numerical().create("stationary","Interp");
       model.result().numerical("stationary").set("expr", tomb);
	model.result().numerical("stationary").set("coord", x_gel);
	model.result().numerical("stationary").set("solnum", parameters);
	
	
	double [][][] result_stationary = model.result().numerical("stationary").getData();

	System.out.println("result_stationary.length (expression)"+result_stationary.length);
	System.out.println("result_stationary[0].length (t_list))"+result_stationary[0].length);
	System.out.println("result_stationary[0][0].length (coordinates)"+result_stationary[0][0].length);

	

	//Currents (integration)
	model.result().numerical().create("int1", "IntLine");
    	
	model.result().numerical("int1").set("innerinput", "manual");
    	model.result().numerical("int1").set("solnum", parameters);

	model.result().numerical("int1").set("expr", "J");
    	//model.result().numerical("int1").set("unit", "A/m");
    	//model.result().numerical("int1").set("descractive", true);
    	//model.result().numerical("int1").set("descr", "postint");
    	model.result().numerical("int1").selection().all();
	model.result().numerical("int1").run();

	double [][] current=model.result().numerical("int1").getReal();

	double current_stat=current[0][0]*1000/par.L; //ez a stat aram micro_amperben

	System.out.println("current_stat"+current_stat);


//Step 2
//Solve the time-dependent model
//Reset the boundary conditions
    model.physics("chds").feature("conc2").set("c0", 1, "c_h_t_acid");
    model.physics("chds").feature("conc2").set("c0", 2, "c_oh_t_acid");
    model.physics("chds").feature("conc2").set("c0", 3, "c_k_t_acid");
    model.physics("chds").feature("conc2").set("c0", 4, "c_cl_t_acid");

    model.physics("chds").feature("conc1").set("c0", 1, "c_h_t_base");
    model.physics("chds").feature("conc1").set("c0", 2, "c_oh_t_base");
    model.physics("chds").feature("conc1").set("c0", 3, "c_k_t_base");
    model.physics("chds").feature("conc1").set("c0", 4, "c_cl_t_base");
	
	model.physics("poeq").feature("dir2").set("r", "u_acid_t");

	model.sol("sol3").runFrom("st2");



//Get the results
	model.result().numerical().create("time","Interp");
	model.result().numerical("time").set("expr", tomb);
	model.result().numerical("time").set("coord",x_gel);
	model.result().numerical("time").set("t", tlist1);
	
	double [][][] result_time1 = model.result().numerical("time").getData();

	

	System.out.println("result_time1.length (expression)"+result_time1.length);
	System.out.println("result_time1[0].length (t_list))"+result_time1[0].length);
	System.out.println("result_time1[0][0].length (coordinates)"+result_time1[0][0].length);
	

//Currents (integration) of time_dependent solver (step2)
	model.result().numerical().create("int2", "IntLine");
    	
	model.result().numerical("int2").set("innerinput", "manual");
    	

	model.result().numerical("int2").set("expr", "J");
	model.result().numerical("int2").set("t", tlist1);
    	
    	model.result().numerical("int2").selection().all();
	model.result().numerical("int2").run();

	double [][] current_time1=model.result().numerical("int2").getReal();
	
	
//Step 3
//Solve the time-dependent model
//Reset the boundary conditions
       model.physics("chds").feature("conc2").set("c0", 1, "c_h_acid");
       model.physics("chds").feature("conc2").set("c0", 2, "c_oh_acid");
       model.physics("chds").feature("conc2").set("c0", 3, "c_k_acid");
       model.physics("chds").feature("conc2").set("c0", 4, "c_cl_acid");

		model.physics("chds").feature("conc1").set("c0", 1, "c_h_base");
    	model.physics("chds").feature("conc1").set("c0", 2, "c_oh_base");
    	model.physics("chds").feature("conc1").set("c0", 3, "c_k_base");
    	model.physics("chds").feature("conc1").set("c0", 4, "c_cl_base");
		
		model.physics("poeq").feature("dir2").set("r", "u_acid");

       model.sol("sol5").run("t2");

//Get the results
	model.result().numerical().create("time2","Interp");
	model.result().numerical("time2").set("expr", tomb);
	model.result().numerical("time2").set("coord", x_gel);
	model.result().numerical("time2").set("t", tlist2);
	//Dataset must also be set
	model.result().numerical("time2").set("data", "dset2");
	
	double [][][] result_time2 = model.result().numerical("time2").getData();

	System.out.println("result_time2.length (expression)"+result_time2.length);
	System.out.println("result_time2[0].length (t_list))"+result_time2[0].length);
	System.out.println("result_time2[0][0].length (coordinates)"+result_time2[0][0].length);

//Currents (integration) of time_dependent solver2 (step3)
	model.result().numerical().create("int3", "IntLine");
    	
	model.result().numerical("int3").set("innerinput", "manual");
    	
	model.result().numerical("int3").set("expr", "J");
	model.result().numerical("int3").set("t", tlist2);
	model.result().numerical("int3").set("data", "dset2");
    	
    	model.result().numerical("int3").selection().all();
	model.result().numerical("int3").run();

	double [][] current_time2=model.result().numerical("int3").getReal();
	
//Results must be written to a file

	data data=new data(current_stat, current_time1,current_time2,result_stationary,result_time1,result_time2, x_gel);

	current_write current_write=new current_write();
	System.out.println("Vagyok");
	current_write.run(data, tlist1, tlist2, sol, sol_t, par);
	System.out.println("Vagyok");
	
	profile_write profile_write=new profile_write();
	profile_write.run(data, tlist1, tlist2, sol, sol_t, par);


}
catch(Exception e){
	System.out.println(e.getClass());
	System.out.print(e.getMessage());
	System.out.print(e.getCause());
	System.out.print(e.getLocalizedMessage());
	System.out.print(e.getStackTrace());
	//System.out.print(e.getSuppressed());  //ezt valamiert nem ismeri fel
	//innen le lehet-e meg kerni valamilyen infor a message-rol???
}

    return model;
	
  }

}
