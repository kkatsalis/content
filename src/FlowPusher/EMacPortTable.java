/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FlowPusher;

/**
 *
 * @author kostas
 */

    public enum EMacPortTable {
    Node083("00:03:1d:0d:bc:87", 19),
    Node084("00:03:1d:0d:bc:8d",21),  
    SwitchA("02:b4:f0:92:1c:21:b9:00", 562),
    SwitchB("02:b4:64:51:06:b4:35:00",562);
    

    private final int port;
    private final String mac;

    private EMacPortTable(String host_mac,int switch_port) {
        this.port = switch_port;
        this.mac = host_mac;
    }
    
    public String getMacAddress(){return mac;}
    public int getPort(){return port;}
}

