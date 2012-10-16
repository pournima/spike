package com.checkinlibrary.org;

import com.checkinlibrary.CheckinLibraryActivity;

public class SupportParams {
    public CauseProfileFragment causeProfileFragment;
    public Boolean isSupported;
    public CheckinLibraryActivity context;
    
    public SupportParams(CheckinLibraryActivity context, CauseProfileFragment causeProfileFragment, Boolean isSupported) {
        this.isSupported = isSupported;
        this.causeProfileFragment = causeProfileFragment;
        this.context=context;
    }
}
