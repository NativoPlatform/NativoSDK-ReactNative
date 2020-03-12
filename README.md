
# react-native-nativo-ads

## Getting started

`$ npm install react-native-nativo-ads --save`

#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-nativo-ads` and add `RNNativoSdkReactNative.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libRNNativoSdkReactNative.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import RNNativoSdkReactNativePackage;` to the imports at the top of the file
  - Add `new RNNativoSdkReactNativePackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-nativo-ads'
  	project(':react-native-nativo-ads').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-nativo-ads/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-nativo-ads')
  	```

#### Windows
[Read it! :D](https://github.com/ReactWindows/react-native)

1. In Visual Studio add the `RNNativoSdkReactNative.sln` in `node_modules/react-native-nativo-ads/windows/RNNativoSdkReactNative.sln` folder to their solution, reference from their app.
2. Open up your `MainPage.cs` app
  - Add `using Nativo.Sdk.React.Native.RNNativoSdkReactNative;` to the usings at the top of the file
  - Add `new RNNativoSdkReactNativePackage()` to the `List<IReactPackage>` returned by the `Packages` method


## Usage
```javascript
import NativoSDK, NativoAd from 'react-native-nativo-ads';

