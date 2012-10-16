package com.checkinlibrary.offers;



import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import android.util.Log;

import com.checkinlibrary.offers.BusinessResult.LocationResult.BusinessResultResource.AddressResult;
import com.checkinlibrary.offers.BusinessResult.LocationResult.BusinessResultResource.BizOrgSupportsResult;
import com.checkinlibrary.offers.BusinessResult.LocationResult.BusinessResultResource.DonationSettingResult;

public class BusinessResult implements Serializable{
    private LocationResult location;

    public BusinessResult(HashMap<String, String> businessParams, List<CheckinTimeResult> checkins, 
            List<BizOrgSupportsResult> orgs) {
        location = new LocationResult(businessParams, checkins, orgs);
    }

    public String toString() {
        String me = new String("Business Result: ");

        if (location != null) {
            me = me + " " + location.toString();
        }

        return me;
    }


    public Integer getId() {
        return location.id;
    }

    public String getPromotionalOffer() {
        return (location.resource.donation_setting.promotional_offer == null) ? "" : location.resource.donation_setting.promotional_offer;
    }

    public BigDecimal getPrivateCheckinAmount() {
        
        if ( location.resource.donation_setting == null) {
            Log.v("CHECKINFORGOOD", "Null id for " + this.getName());
        }
        return location.resource.donation_setting.private_checkin_amount;
    }

    public BigDecimal getPublicCheckinAmount() {
        return location.resource.donation_setting.public_checkin_amount;
    } 
    
    public String getDistance() {
        return Double.toString(location.distance);
    }

    public String getName() {
        return location.name;
    }
    
    public String getAddress() {
        return location.pretty_address;
    }
    
    public Double[] getLatLng() {
        AddressResult address = location.resource.locations.get(0);
        return new Double [] {address.latitude, address.longitude};
    }
    
    public Boolean needsSnap() {
    	Boolean has_qr =false;
    	if(location.resource.qrcode != null){
           if(location.resource.qrcode.id>0){
        	   has_qr=true;
           }
    	} 
    	return has_qr;
    }
      
    public List<HashMap<String, String>> getOrganizations() {
        List<HashMap<String, String>> orgs = new ArrayList<HashMap<String, String>>();

        for ( BizOrgSupportsResult bizOrg: location.resource.biz_org_supports ) {
            orgs.add(bizOrg.organization);
        }
        
        return orgs;
    }
    
    public List<CheckinTimeResult> getCheckinTimes() {
        return location.resource.donation_setting.check_in_times;
    }
    
    public HashMap<String, BigDecimal> getCheckinAmounts() {
        HashMap<String, BigDecimal> ret = new HashMap<String, BigDecimal>();
        ret.put("private_amount", location.resource.donation_setting.private_checkin_amount);
        ret.put("public_amount", location.resource.donation_setting.public_checkin_amount);
        
        return ret;
    }
    
    public static class LocationResult implements Serializable{
        Integer id;
        Double distance;
        String name;
        String pretty_address;
        BusinessResultResource resource;
        
        public LocationResult(HashMap<String, String> businessParams, List<CheckinTimeResult> checkins, 
                List<BizOrgSupportsResult> orgs) {
            this.pretty_address = businessParams.get("address");
            this.distance = new Double(businessParams.get("distance"));
            this.name = businessParams.get("name");
            this.id = new Integer(businessParams.get("remote_id"));
            Integer code = null;
            if ( businessParams.get("is_snap")  != null ) code = new Integer(businessParams.get("is_snap")); 
            
            DonationSettingResult ds = new DonationSettingResult(null, businessParams.get("private_checkin_amount"), 
                    businessParams.get("public_checkin_amount"), checkins, businessParams.get("promotional_offer"));
            Double[] latLng = new Double [] {new Double(businessParams.get("latitude")), new Double(businessParams.get("longitude"))};
            this.resource = new BusinessResultResource(orgs, ds, latLng, code);
        }
        
        public static class BusinessResultResource implements Serializable{
            List<BizOrgSupportsResult> biz_org_supports;
            DonationSettingResult donation_setting;
            List<AddressResult> locations;
            qrcode qrcode;
            Boolean isSnap;
            
            public BusinessResultResource(List<BizOrgSupportsResult> bizOrgs, DonationSettingResult donationSettings, Double [] latLng, Integer qrcode) {
                this.qrcode = new qrcode(qrcode);
                biz_org_supports = bizOrgs;
                donation_setting = donationSettings;
                AddressResult location = new AddressResult(latLng[0], latLng[1]);
                locations = Arrays.asList(location);
            }

            public static class qrcode implements Serializable{
                public qrcode(Integer id) {
                    this.id = id;
                }
                
                public Integer id;
            }

            public static class AddressResult implements Serializable{
                public AddressResult(Double latitude, Double longitude) {
                    this.latitude = latitude;
                    this.longitude = longitude;
                }
                
                public double latitude;
                public double longitude;
            }
            
            public static class BizOrgSupportsResult implements Serializable{
                public BizOrgSupportsResult(Integer id, String name) {
                    organization_id = id;
                    organization = new HashMap<String, String>();
                    organization.put("id", id.toString());
                    organization.put("name", name);
                }
                
                public Integer organization_id;
                //Contains key id, name
                public HashMap<String, String> organization;
            }
            
            public static class DonationSettingResult implements Serializable{
                public DonationSettingResult(Integer orgId, String privateAmount, 
                        String publicAmount, List<CheckinTimeResult> checkinTimes, String promotionalOffer) {
                    organization_id = orgId;
                    check_in_times = checkinTimes;
                    private_checkin_amount = new BigDecimal(privateAmount);
                    public_checkin_amount = new BigDecimal(publicAmount);
                    promotional_offer = promotionalOffer;
                }
                
                Integer organization_id;
                List<CheckinTimeResult> check_in_times;
                BigDecimal private_checkin_amount;
                BigDecimal public_checkin_amount;               
                String promotional_offer;
            }             
        }
    }
    
    public static class CheckinTimeResult implements Serializable{
        public CheckinTimeResult(String day, String startDate, String endDate) {
           this.day = day;
           this.start_time = startDate;
           this.end_time = endDate;
        }
        
        public String day;
        public String start_time;
        public String end_time;
    }
}