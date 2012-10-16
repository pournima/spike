package com.checkinlibrary.org;

import java.util.concurrent.BlockingQueue;

import com.checkinlibrary.ws.services.OrganizationWebService;


public class SupportQueueReader implements Runnable {
    private BlockingQueue<SupportParams> queue;

    public SupportQueueReader(BlockingQueue<SupportParams> queue) {
        this.queue = queue;
    }

    public void run() {
        try {
            long orgId;
            Boolean[] succeeded;
            
            while ( true ) {
                SupportParams params = queue.take();
                orgId = params.causeProfileFragment.mOrganizationResult.getOrganization().getId();
                succeeded =  OrganizationWebService.supportOrg((int)orgId, params.isSupported);
                
                final Boolean[] closureSucceeded = succeeded;
                final SupportParams closureParams = params;
                
                params.context.runOnUiThread(new Runnable() {
                    public void run() {
                        closureParams.causeProfileFragment.updateSupport(closureSucceeded);
                    }
                  });
            }   
        }
        catch(InterruptedException e) {
        }           
    }
}
