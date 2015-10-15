/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlowPusher;

import java.util.Hashtable;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kostas
 */
public class ContentSDNClients {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

           
            String controllerIP="10.64.45.8";
            int portA=15;
            int portB=100;
            String vlan="692";
            int vlan_priority=0;
            
           if(false)
                addConnection(controllerIP,portA,portB,vlan,vlan_priority);
           if(true)
                deleteConnection(controllerIP,portA,portB,vlan);
          
        }
        
        private static int deleteConnection(String controllerIP,int portA,int portB,String vlan){
        
            ApacheHttpClient floodlightClient=new ApacheHttpClient(controllerIP);
            String flowName;
            
            try{
                  //give the name of the flow you want to delete
                flowName=portA+"_"+portB+"_"+"uplink";
                floodlightClient.deleteFloodlightClient(flowName); 
                
                flowName=portB+"_"+portA+"_"+"uplink";
                floodlightClient.deleteFloodlightClient(flowName); 
                
                flowName=portB+"_"+portA+"_"+"downlink";
                floodlightClient.deleteFloodlightClient(flowName); 
            
                flowName=portA+"_"+portB+"_"+"downlink";
                floodlightClient.deleteFloodlightClient(flowName); 
            
          
        } catch (IOException ex) {
            Logger.getLogger(ContentSDNClients.class.getName()).log(Level.SEVERE, null, ex);
        }
            return 0;
            
        }
            
    
    private static int addConnection(String controllerIP,int portA,int portB,String vlan,int vlan_priority){
         
        Boolean externalTraffic=false;
                      
        int trunk_port=EMacPortTable.SwitchB.getPort();
        int internall_port=-1;
        String internall_port_mac="";
        
        // Case A: Internal Traffic we use portA and portB as is in the method call
        
        // Case B: Extrenal Traffic: one port is trunk on is the internal port
        if(portA>30||portB>30){
        
          externalTraffic=true;
           
            if(portA>30){
                internall_port=portB;
            }
            else{
                internall_port=portA;
            }
           
            for (EMacPortTable p : EMacPortTable.values())
              if(p.getPort()==internall_port)
                internall_port_mac=p.getMacAddress();
           
        }
        
        try {
            
            String flowName;
            String jsonBody;
            Hashtable matchings=new Hashtable();
            Hashtable actions=new Hashtable();
            ApacheHttpClient floodlightClient=new ApacheHttpClient(controllerIP);

               
                   if (externalTraffic){
                      // Add Flow A: uplink
                       flowName=portA+"_"+portB+"_"+"uplink";
                       
                       matchings.put("switch",EMacPortTable.SwitchB);
                       matchings.put("ingress_port",internall_port);

                       actions.put("output",trunk_port);
                       actions.put("set-vlan-id",vlan);
                       actions.put("set-vlan-priority",vlan_priority); 

                       jsonBody=FlowCreator.Object2Json(flowName,matchings,actions);
                       floodlightClient.postFloodlightClient(jsonBody);

                       // Add Flow B: downlink
                       flowName=portA+"_"+portB+"_"+"downlink";

                       matchings.clear();
                       matchings.put("switch",EMacPortTable.SwitchB);
                       matchings.put("ingress_port",trunk_port);
                       matchings.put("dst_mac",internall_port_mac);

                       actions.clear();
                       actions.put("output",internall_port);
                       actions.put("set-vlan-id",vlan);
                       actions.put("set-vlan-priority",vlan_priority);  
                       
                       jsonBody=FlowCreator.Object2Json(flowName,matchings,actions);
                       floodlightClient.postFloodlightClient(jsonBody);
                   } 
                   else{
                       // Add Flow A: uplink
                       matchings.put("switch",EMacPortTable.SwitchB);
                       matchings.put("ingress_port",portA);

                       actions.put("output",portB);
                       actions.put("set-vlan-id",vlan);
                      actions.put("set-vlan-priority",vlan_priority); 

                       flowName=portA+"_"+portB+"_"+"uplink";
                       jsonBody=FlowCreator.Object2Json(flowName,matchings,actions);

                       floodlightClient.postFloodlightClient(jsonBody);

                       // Add Flow B: downlink
                       flowName=portA+"_"+portB+"_"+"downlink";

                       matchings.clear();
                       matchings.put("switch",EMacPortTable.SwitchB);
                       matchings.put("ingress_port",portB);
                      
                       actions.clear();
                       actions.put("output",portA);
                       actions.put("set-vlan-id",vlan);
                       actions.put("set-vlan-priority",vlan_priority);  
                       
                       jsonBody=FlowCreator.Object2Json(flowName,matchings,actions);
                       floodlightClient.postFloodlightClient(jsonBody);
                   }
              
          
        } catch (IOException ex) {
            Logger.getLogger(ContentSDNClients.class.getName()).log(Level.SEVERE, null, ex);
    }
        
        return 1;
    
    
    
    }
    
}
