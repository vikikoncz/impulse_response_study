import com.comsol.model.*;
import com.comsol.model.util.*;
import java.io.*;
import java.lang.Math;

public class mesh{
	double [][] vtx;
	int [][] edge;

//kell egy olyan konstruktor is, ami nem csinal semmit
mesh(){}

//kell egy olyan konstruktor, ami ekvidisztans mesh-t keszit
mesh(int n, parameters par){

		vtx=new double [1][n+1];
		vtx[0][0]=0;
		vtx[0][n]=par.L;	//L a gel hossza!!!

		//double lepes=(double)par.L/(double)n;	
			
		for(int i=1; i<n; i++){
			vtx[0][i]=(double)par.L/n*i;				
		
		}


		this.generate_edge();		

}


//konstruktor, ami az for_mesh_current_results-nal kiszamitott vtx alapjan megcsinalja a mesh-t 

mesh(double [][] vtx){
	this.vtx=vtx;
	this.generate_edge();

}






//Konstruktor, egy helyen van surites	
mesh(double kezdeti_pont, double veg_pont, int n_refine, int n_alap, parameters par){
	
	//Itt a MESH 3 szakaszbol all!
/*Van egy zona ahol lehet suriteni*/
	
	double r_refine=veg_pont-kezdeti_pont;
	//System.out.println("r_refine="+r_refine);
	double r_veg=par.L-veg_pont;	
	//System.out.println("r_veg="+r_veg);
	//double r_veg=1-veg_pont;
	 //a gel tobbi reszen levo szakaszok szama
	int n_osszes=n_refine+n_alap;

	vtx=new double [1][n_osszes+1];
	vtx[0][0]=0;
	vtx[0][n_osszes]=par.L;	
	
	System.out.println("par.L="+par.L);
	System.out.println("vtx[0][n_osszes]="+vtx[0][n_osszes]);


//A MESH 2 szakaszbol all (pl. mar at van utve a dioda!!!)
//ez sem feltetlen jo! double-ok
//TODO! valami segedvaltozot ki kene majd talalni, hogy a szakasz melyik reszen legyen surites
if(kezdeti_pont==0)
{	
	int h=1;
	for(int i=1; i<n_refine ; i++){
	vtx[0][i]=(double)veg_pont/n_refine*h;
	h++;
	}

	h=1;
	for(int i=n_refine; i<n_osszes; i++){
	//vtx[0][i]=(double)(1-veg_pont)/n_alap*h+vtx[0][n_refine-1];
	vtx[0][i]=(double)(par.L-veg_pont)/n_alap*h+vtx[0][n_refine-1];
	h++;
	}

	
}

//TODO! double-oket amugyse lehet osszehasonlitani, valamit ki kell talalni erre 
else if(veg_pont==par.L){

}


//A MESH 3 szakaszbol all
else{

	
	
//Kiszamaitani, surites elotti es utani szakaszokra mennyi pont jut n_alapbol (aranyosan)
	int kezdeti=(int)Math.rint(n_alap*(kezdeti_pont/(par.L-(veg_pont-kezdeti_pont))));  //egeszosztas marad 
	//System.out.println("kezdeti="+kezdeti);
	
	int kozepso=kezdeti+n_refine;
	//System.out.println("kozepso="+kozepso);
	int veg=n_alap-kezdeti;
	//System.out.println("veg="+veg);

//Pontok berakasa vtx-be
	int h=1;
	
	for(int i=1;i<kezdeti;i++){
		vtx[0][i]=(double)kezdeti_pont/kezdeti*h;
		h++;		
	} 	

	h=1;	
	vtx[0][kezdeti]=kezdeti_pont;
	int kozepso_help_variable=kezdeti+1;
	for(int i=kozepso_help_variable;i<kozepso;i++){
		vtx[0][i]=(double)r_refine/n_refine*h+vtx[0][kezdeti];	
		h++;
	}	
	
	h=1; 	
	for(int i=kozepso;i<n_osszes;i++){
		vtx[0][i]=(double)r_veg/veg*h+vtx[0][kozepso-1];
		h++;	
	}
//Ez itt az ELSE vege!
}	

//Debughoz segitseg
/*
	for(int i=0; i<vtx[0].length; i++){
		System.out.println(i+".elem="+vtx[0][i]);
	}
*/
        

//Edge letrehozasa	
		this.generate_edge();		
	
	}


void generate_edge(){
	int n_pont=vtx[0].length-1;
	edge=new int [2][n_pont];
	for(int i=0;i<edge[0].length;i++){
		for(int j=0;j<edge.length;j++){
			if(j==0) edge[j][i]=i;			
			else edge[j][i]=i+1;			
		}
	}


}


}




