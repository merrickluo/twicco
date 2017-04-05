package ninja.luois.twicco;

import android.content.Intent;

import com.facebook.react.ReactActivity;
import com.tkporter.fabrictwitterkit.FabricTwitterKitPackage;


public class MainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "twicco";
    }

    	@Override
      public void onActivityResult(int requestCode, int resultCode, Intent data) {
          FabricTwitterKitPackage.getInstance().onActivityResult(this, requestCode, resultCode, data);
      }
}
