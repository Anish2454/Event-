package com.shenoy.anish.ribbit;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by ANISH on 8/6/2015.
 */
public class RibbitApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "ZuaKUrGFF7nqQNBxjACITRLHDw526nQxNyXw0l1g", "fVrcnuW0tMRgoZyU9yE9YEy04Tgg6xwpizNhd1Pu");
    }
}
