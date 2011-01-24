package sim.app.exploration.utils;

import java.util.Hashtable;

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

	public static Class getHighestProb(Hashtable<Class, Float> probs) {
		float maxProb = 0;
		Class maxClass = null;
		
		for(Class c: probs.keySet()){
			if(probs.get(c) > maxProb){
				maxProb = probs.get(c);
				maxClass = c;
			}
		}
		
		return maxClass;
	}
}
