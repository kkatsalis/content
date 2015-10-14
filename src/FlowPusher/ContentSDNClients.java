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
            String ingress_port="21";
            String output="19";
            String vlan="692";
            int vlan_priority=0;
            
            addConnection(19,21,"692",0);
          
        }
        
        private static int deleteConnection(int portA,int portB,String vlan){
        
            String controllerIP="10.64.45.8";
            ApacheHttpClient floodlightClient=new ApacheHttpClient(controllerIP);
            String flowName;
            
            try{
                  //give the name of the flow you want to delete
                flowName=portA+"_"+portB+"_"+"uplink";
                floodlightClient.deleteFloodlightClient(flowName); 
                
                flowName=portA+"_"+portB+"_"+"udownlink";
                floodlightClient.deleteFloodlightClient(flowName); 
            
          
        } catch (IOException ex) {
            Logger.getLogger(ContentSDNClients.class.getName()).log(Level.SEVERE, null, ex);
        }
            return 0;
            
        }
            
    
        private static int addConnection(int portA,int portB,String vlan,int vlan_priority){
         
        String controllerIP="10.64.45.8";
          
            int trunk_port;
            int node_port;
            String node_mac="";
            
           //find the trunk port
           
           if(portA>30){
               node_port=portB;
               trunk_port=EMacPortTable.SwitchB.getPort();
           }
           else{
               node_port=portA;
               trunk_port=EMacPortTable.SwitchB.getPort(); 
           }
           
           
           for (EMacPortTable p : EMacPortTable.values())
              if(p.getPort()==node_port)
                node_mac=p.getMacAddress();
                 //System.out.println("port:"+p.getSwitchPort()+" - mac:"+p.getHostMac());   
               
           try {
               String flowName;
               Hashtable matchings=new Hashtable();
               Hashtable actions=new Hashtable();
               ApacheHttpClient floodlightClient=new ApacheHttpClient(controllerIP);

               // Flow A: uplink
               matchings.put("switch",EMacPortTable.SwitchB);
               matchings.put("ingress_port",node_port);
              
               actions.put("output",trunk_port);
               actions.put("set-vlan-id",vlan);
               actions.put("set-vlan-priority",vlan_priority); 

               flowName=portA+"_"+portB+"_"+"uplink";
               String jsonBody=FlowCreator.Object2Json(flowName,matchings,actions);
               
               floodlightClient.postFloodlightClient(jsonBody);
       
        
               // Flow B: downlink
               flowName=portA+"_"+portB+"_"+"downlink";
               
               matchings.clear();
               matchings.put("switch",EMacPortTable.SwitchB);
               matchings.put("ingress_port",trunk_port);
               matchings.put("destMAC",node_mac);

                       
               actions.clear();
               actions.put("output",node_port);
               actions.put("set-vlan-id",vlan);
               actions.put("set-vlan-priority",vlan_priority); 
               
               jsonBody=FlowCreator.Object2Json(flowName,matchings,actions);
               floodlightClient.postFloodlightClient(jsonBody);
          
        } catch (IOException ex) {
            Logger.getLogger(ContentSDNClients.class.getName()).log(Level.SEVERE, null, ex);
    }
        
        return 1;
    
    
    
    }
    
}
