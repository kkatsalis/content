/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package FlowPusher;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


/**
 *
 * @author kostas
 */
public class FlowCreator {
    
    public static String Object2Json(String flowName,Hashtable matchings,Hashtable actions){
    
         Flow flow = new Flow();

         flow.Switch=EMacPortTable.SwitchB.getMacAddress();
         flow.flow_name=flowName; 
         flow.cookie="0"; 
         flow.priority="32768"; 
         flow.ingress_port=String.valueOf(matchings.get("ingress_port"));
         
         if(matchings.get("dst_mac") != null)
            flow.destMAC=String.valueOf(matchings.get("dst_mac"));
         
         flow.active="true"; 
         
        String actionSet="";
        Set keys=actions.keySet();
        
        for (int i = 0; i < actions.size(); i++) {
            if(i<actions.size()-1)
                actionSet = keys.remove(0)+"="+String.valueOf(actions.get(i))+",";
            else
                actionSet = keys.remove(0)+"="+String.valueOf(actions.get(i));
        }
       
        flow.actions=actionSet;

        GsonBuilder builder = new GsonBuilder();

        Gson gson= builder.create();

        String json=gson.toJson(flow);

        json=json.replace("Switch", "switch");
        json=json.replace("ingress_port", "ingress-port");
        json=json.replace("dst_mac", "dst-mac");

        System.out.println(json);

        return json;
    }
    
}
