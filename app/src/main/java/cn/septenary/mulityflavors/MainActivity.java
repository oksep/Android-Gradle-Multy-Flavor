package cn.septenary.mulityflavors;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.txtView);
        tv.append(BuildConfig.SotreName);
        tv.append("\n");

        tv.append(new Flavor().getFlavorName());
        tv.append("\n");

        tv.append(getResources().getString(R.string.hello_flavor));
        tv.append("\n");

        tv.append(BuildConfig.API_URL);
        tv.append("\n");

        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            String channel = appInfo.metaData.getString("CHANNEL");
            tv.append(channel);
            tv.append("\n");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
