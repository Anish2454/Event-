package com.shenoy.anish.ribbit;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseInstallation;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Parse.initialize(this, "ZuaKUrGFF7nqQNBxjACITRLHDw526nQxNyXw0l1g", "fVrcnuW0tMRgoZyU9yE9YEy04Tgg6xwpizNhd1Pu");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParseFacebookUtils.initialize(this);

    }

}
