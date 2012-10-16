package com.checkinlibrary.orgs;


import java.io.Serializable;
import java.util.HashMap;

public class CauseResult implements Serializable{
    private HashMap<String, String> organization;
    
    public CauseResult() {}
    
    public CauseResult(String id, String name, String supported) {
        organization = new HashMap<String, String>();
        organization.put("name", name);
        organization.put("supported", supported);
        organization.put("id", id);
    }
 
    public Integer getId() {
        return Integer.decode(organization.get("id"));
    }
    
    public String getName() {
        return organization.get("name");
    }
    
    public Boolean getSupported() {
        String supportedStr = organization.get("supported");
        
        if ( supportedStr == "1" || supportedStr.equals("true") ) {
            return true;        
        } else {
            return false;
        }
    }
    
   /* public void setSupported(Boolean isSupported, CheckinNativeActivity context) {
        organization.put("supported", isSupported.toString());*/
        
        //Update our cache if we have it
       /* OrganizationDbAdapter adapter = new OrganizationDbAdapter(context);
        adapter.updateSupported(isSupported, getId());
    }*/
}