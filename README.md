
# react-native-nativo-ads

Monetize your app with Nativo for React Native. Requires React Native `0.60.5` or greater. 

## Getting started
```bash
npm install react-native-nativo-ads --save
cd ios
pod install
```

## Usage
```javascript
import { NativoSDK, NativoAd } from 'react-native-nativo-ads';

NativoSDK.enableDevLogs();
NativoSDK.enableTestAdvertisementsWithType(NativoSDK.AdTypes.NATIVE);

needsDisplayClickOutURL = (event) => {
    this.props.navigation.navigate('ClickOutScreen', {
        url: event.url,
    });
};

displayLandingPage = (event) => {
    this.props.navigation.navigate('NativoLandingScreen', event);
};

const NativeAdTemplate = (props) => {
    return (
        <View nativeID={'nativoAdView'} style={styles.nativeCard}>
            <Text style={{color: '#1A1AFF', fontWeight: 'bold'}}>Sponsored Content</Text>
            <Image style={styles.cardImage} nativeID={'articleImage'}/>
            <View>
                <Text nativeID={'articleDate'} style={{textAlign: 'right', height: 30}}>{props.adDate} </Text>
                <Text editable={false} nativeID={'articleTitle'}
                      style={{textAlign: 'center', fontWeight: 'bold', height: 35}}>{props.adTitle}</Text>
                <Text numberOfLines={2} multiline={true} editable={false} nativeID={'articleDescription'}
                      style={{textAlign: 'center', height: 50}}>{props.adDescription} </Text>
            </View>
            <View style={styles.textRow}>
                <Image nativeID={'authorImage'} style={{height: 30, width: 30}}/>
                <Text nativeID={'authorName'}>{props.adAuthorName}</Text>
            </View>
        </View>
    );
};

<NativoAd 
    sectionUrl={ "www.mypub.com" }
    index={ 8 }
    nativeAdTemplate={ NativeAdTemplate }
    onNativeAdClick={ this.displayLandingPage }
    onDisplayAdClick={ this.needsDisplayClickOutURL }
     />
```

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE)