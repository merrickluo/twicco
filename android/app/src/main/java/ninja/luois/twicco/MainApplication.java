package ninja.luois.twicco;

import android.app.Application;

import com.facebook.react.ReactApplication;
import ga.piroro.rnt.RNTPackage;
import com.oblador.vectoricons.VectorIconsPackage;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.soloader.SoLoader;

import java.util.Arrays;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.tkporter.fabrictwitterkit.FabricTwitterKitPackage;
import com.BV.LinearGradient.LinearGradientPackage;

import ninja.luois.twicco.R;


public class MainApplication extends Application implements ReactApplication {

  private final ReactNativeHost mReactNativeHost = new ReactNativeHost(this) {
    @Override
    public boolean getUseDeveloperSupport() {
      return BuildConfig.DEBUG;
    }

    @Override
    protected List<ReactPackage> getPackages() {
      return Arrays.<ReactPackage>asList(
          FabricTwitterKitPackage.getInstance(),
          new MainReactPackage(),
            new RNTPackage(),
            new VectorIconsPackage(),
          new LinearGradientPackage()
      );
    }
  };

  @Override
  public ReactNativeHost getReactNativeHost() {
    return mReactNativeHost;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    SoLoader.init(this, /* native exopackage */ false);
    TwitterAuthConfig authConfig =
        new TwitterAuthConfig(getString(R.string.twitter_key),
                              getString(R.string.twitter_secret));
    Fabric.with(this, new TwitterCore(authConfig));
  }
}
