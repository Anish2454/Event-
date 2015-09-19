package com.shenoy.anish.ribbit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.FacebookSdk;
import com.parse.Parse;
import com.parse.ParseFacebookUtils;

public class Application extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Parse.initialize(this, "ZuaKUrGFF7nqQNBxjACITRLHDw526nQxNyXw0l1g", "fVrcnuW0tMRgoZyU9yE9YEy04Tgg6xwpizNhd1Pu");
        ParseFacebookUtils.initialize(this);

    }

}
