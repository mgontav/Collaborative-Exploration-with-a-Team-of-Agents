package sim.app.exploration.utils;

import sim.app.exploration.agents.ExplorerAgent;
import sim.app.exploration.objects.SimObject;

public class Utils {

	
	public static double getDistance(ExplorerAgent agent, SimObject obj){
		double d;
		
		if(obj == null){
			System.out.println(obj);
		}
		
		d = agent.getLoc().distance(obj.getLoc());
		
		return d;
	}
}
