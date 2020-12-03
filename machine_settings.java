import com.comsol.model.*;
import com.comsol.model.util.*;
import java.io.*;

public class machine_settings{

	//for MY_SUSE
	//private String dirnev_elo="/home/vikik/acidbase_transient_data/ugras/";

	//for phyndi
	//private String dirnev_elo="/home/matlab/acidbase_transient_data/impulzus_new/";

	//for phyndi2 (under GIBBS) 
	private String dirnev_elo="/home/comsol/acidbase_transient_data/impulzus/";

	//for kissrv
	//private String dirnev_elo="/d/inst/viki/acidbase_transient_data/ugras/"; 

	public String getDIR_for_current(){
	
		String dir=dirnev_elo+"current_time/";
		return dir;
	
	}

	public String getDIR_for_profile(){
		
		String dir=dirnev_elo+"profile/";
		return dir;
	}

}
