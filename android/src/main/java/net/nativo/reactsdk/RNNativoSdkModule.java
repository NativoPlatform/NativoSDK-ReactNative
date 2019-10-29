
package net.nativo.reactsdk;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

public class RNNativoSdkModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  public static Activity currentactivity;

  public RNNativoSdkModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
    currentactivity = getCurrentActivity();
  }

  @Override
  public String getName() {
    return "RNNativoSdkReactNative";
  }

  @ReactMethod
  public void show(String text) {
    Context context = getReactApplicationContext();
    Toast.makeText(context, text, Toast.LENGTH_LONG).show();

  }

}