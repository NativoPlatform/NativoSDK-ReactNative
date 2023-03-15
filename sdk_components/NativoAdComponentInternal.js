import React, {Component} from 'react';
import {
    findNodeHandle,
    NativeEventEmitter,
    requireNativeComponent,
    StyleSheet,
    UIManager,
    View,
    NativeModules
} from 'react-native';

const NativoAdContainer = requireNativeComponent("NativoContainer");

class NativoAdComponentInternal extends Component {

    constructor(props) {
        super(props);
        this.state = {
            nativeFlag: false,
            videoFlag: false,
            standardDisplayFlag: false,
            adDescription: '',
            adTitle: '',
            adAuthorName: '',
            adDate: '',
            adLoaded: false,
            adAuthorUrl: '',
            adImgUrl: '',
            adUUID: '',
            adShareUrl: '',
            adAdID: 0
        };
        this.handleAdLoaded = this.handleAdLoaded.bind(this);
        this.handleAdLoadFailed = this.handleAdLoadFailed.bind(this);
        this.displayLandingPage = this.displayLandingPage.bind(this);
        NativeModules.NativoSDK.registerTemplates();
    }

    prefetchAd() {
        try {
            UIManager.dispatchViewManagerCommand(
                findNodeHandle(this._adContainer),
                UIManager.getViewManagerConfig('NativoContainer').Commands.prefetchAd, [this.props.index, this.props.sectionUrl]);
        } catch (e) {
            this.setDefaultState()
        }
    }


    componentDidMount() {
        try {
            const eventEmitter = new NativeEventEmitter(NativoAdContainer);
            this.clickoutListener = eventEmitter.addListener('needsDisplayClickOutURL', (event) => {
                this.props.onDisplayAdClick(event);
            });
            
        } catch (e) {
            this.setDefaultState()
        }
        if (!this.props.enableGAMVersion) {
            this.prefetchAd();
        }
    }

    componentWillUnmount() {
        try {
            this.clickoutListener.remove();
        } catch (e) {}
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        try {
            UIManager.dispatchViewManagerCommand(
                findNodeHandle(this._adContainer),
                UIManager.getViewManagerConfig('NativoContainer').Commands.placeAdInView, [this.props.index, this.props.sectionUrl, this.state.adAdID]);
        } catch (e) {
            this.setDefaultState()
        }
    }

    handleAdLoadFailed(event) {
        event.sectionUrl = this.props.sectionUrl;
        event.index = this.props.index;
        this.setDefaultState()
        this.props.onAdRemoved(event)
    }

    setDefaultState() {
        this.setState({
            videoFlag: false,
            nativeFlag: false,
            standardDisplayFlag: false,
            adDescription: '',
            adTitle: '',
            adAuthorName: '',
            adDate: '',
            adLoaded: false,
            adAuthorUrl:'',
            adImgUrl: '',
            adUUID: '',
            adAdID: 0

        });
    }

    handleAdLoaded(event) {
        try {
            const date = new Date(event.nativeEvent.adDate);
            if (event.nativeEvent.adType === 'NtvStandardDisplayInterface') {
                this.setState({
                    videoFlag: false,
                    nativeFlag: false,
                    standardDisplayFlag: true,
                    adDescription: '',
                    adTitle: '',
                    adAuthorName: '',
                    adAuthorUrl: event.nativeEvent.adAuthorUrl,
                    adImgUrl: event.nativeEvent.adImgUrl,
                    adAdID: event.nativeEvent.adAdID,
                    adDate: date.getTime(),
                    adUUID: event.nativeEvent.adUUID,
                    displayWidth: event.nativeEvent.adDisplayWidth,
                    displayHeight: event.nativeEvent.adDisplayHeight,
                    adLoaded: true

                });
            } else if (event.nativeEvent.adType === 'NtvAdtypeClickout'
                || event.nativeEvent.adType === 'NtvAdTypeNative' || event.nativeEvent.adType === 'NtvAdTypeStory') {
                this.setState({
                    videoFlag: false,
                    nativeFlag: true,
                    standardDisplayFlag: false,
                    adDescription: event.nativeEvent.adDescription,
                    adTitle: event.nativeEvent.adTitle,
                    adAuthorName: event.nativeEvent.adAuthorName,
                    adAuthorUrl: event.nativeEvent.adAuthorUrl,
                    adImgUrl: event.nativeEvent.adImgUrl,
                    adAdID: event.nativeEvent.adAdID,
                    adUUID: event.nativeEvent.adUUID,
                    adShareUrl: event.nativeEvent.adShareUrl,
                    adDate: date.getTime(),
                    adLoaded: true

                });
            } else {
                this.setState({
                    videoFlag: true,
                    nativeFlag: false,
                    standardDisplayFlag: false,
                    adDescription: event.nativeEvent.adDescription,
                    adTitle: event.nativeEvent.adTitle,
                    adAuthorName: event.nativeEvent.adAuthorName,
                    adAuthorUrl: event.nativeEvent.adAuthorUrl,
                    adImgUrl: event.nativeEvent.adImgUrl,
                    adAdID: event.nativeEvent.adAdID,
                    adUUID: event.nativeEvent.adUUID,
                    adDate: date.getTime(),
                    adLoaded: true
                });
            }
            let index= this.props.index,
                sectionUrl= this.props.sectionUrl;
            this.props.onAdRendered({index, sectionUrl});
        } catch (e) {
            this.setDefaultState()
        }
    }

    displayLandingPage(event) {
        event.adDescription = this.state.adDescription;
        event.adTitle = this.state.adTitle
        event.adAuthorName = this.state.adAuthorName
        event.adDate = this.state.adDate
        event.adAuthorImgUrl = this.state.adAuthorUrl
        event.adImgUrl = this.state.adImgUrl
        event.adShareUrl = this.state.adShareUrl
        // this mapping is necessary for compatibility with iOS, which uses index for landing page
        event.adID = this.state.adUUID
        event.index = event.nativeEvent.adId
        event.sectionUrl = event.nativeEvent.sectionUrl
        event.containerHash = event.nativeEvent.containerHash
        this.props.onNativeAdClick(event)
    }

    render() {
        const NativeAdTemplate = this.props.nativeAdTemplate;
        const NativeVideoAdTemplate = this.props.videoAdTemplate;
        const StandardDisplayAdTemplate = this.props.standardDisplayAdTemplate;
        return (
            <View style={styles.container}>
                <NativoAdContainer ref={(el) => (this._adContainer = el)}
                                   sectionUrl={{'url': this.props.sectionUrl, 'index': this.props.index}}
                                   onAdLoaded={this.handleAdLoaded} onAdFailed={this.handleAdLoadFailed}
                                   onDisplayLandingPage={this.displayLandingPage}
                                   style={{alignItems: 'center'}}>
                    <View nativeID={'nativoAdView'}>
                        {this.state.nativeFlag &&
                        <NativeAdTemplate {...this.props.extraTemplateProps} adDate={this.state.adDate}
                                          adTitle={this.state.adTitle}
                                          adDescription={this.state.adDescription}
                                          adAuthorName={this.state.adAuthorName}
                                          adLoaded={this.state.adLoaded}
                                          extraTemplateProps={this.props.extraTemplateProps}/>}
                        {this.state.videoFlag &&
                        <NativeVideoAdTemplate {...this.props.extraTemplateProps} adDate={this.state.adDate}
                                               adTitle={this.state.adTitle}
                                               adDescription={this.state.adDescription}
                                               adAuthorName={this.state.adAuthorName}
                                               adLoaded={this.state.adLoaded}
                                               extraTemplateProps={this.props.extraTemplateProps}/>}
                        {this.state.standardDisplayFlag &&
                        <StandardDisplayAdTemplate adLoaded={this.state.adLoaded} webViewForSD={true}
                                                   displayWidth={this.state.displayWidth}
                                                   displayHeight={this.state.displayHeight}
                                                   extraTemplateProps={this.props.extraTemplateProps}/>}
                        {!this.state.adLoaded && <View style={{width: 1, height: 1}}/>}
                    </View>
                </NativoAdContainer>
            </View>
        )
    }
}


const styles = StyleSheet.create({
    container: {
        flex: 1,
        alignItems: 'center'
    },
});

export default NativoAdComponentInternal;
