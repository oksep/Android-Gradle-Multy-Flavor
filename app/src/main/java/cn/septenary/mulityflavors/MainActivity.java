package cn.septenary.mulityflavors;

import android.app.Activity;
import android.os.Bundle;
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
    }
}
