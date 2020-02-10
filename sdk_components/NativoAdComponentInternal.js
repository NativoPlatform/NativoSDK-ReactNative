import React, {Component} from 'react';
import {findNodeHandle, NativeEventEmitter, requireNativeComponent, StyleSheet, UIManager, View} from 'react-native';

const NativoAdContainer = requireNativeComponent("NativoContainer");

class NativoAdComponentInternal extends Component<Props> {

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
            adLoaded: false
        };
        this.handleAdLoaded = this.handleAdLoaded.bind(this);
        this.handleAdLoadFailed = this.handleAdLoadFailed.bind(this);
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


    componentDidMount(): void {
        try {
            const eventEmitter = new NativeEventEmitter(NativoAdContainer);
            eventEmitter.addListener('needsDisplayLandingPage', (event) => {
                event.adDescription = this.state.adDescription;
                event.adTitle = this.state.adTitle
                event.adAuthorName = this.state.adAuthorName
                event.adDate = this.state.adDate
                this.props.onNativeAdClick(event)
            });
            eventEmitter.addListener('needsDisplayClickOutURL', (event) => {
                this.props.onDisplayAdClick(event.url);
            });
        } catch (e) {
            this.setDefaultState()
        }
        this.prefetchAd();
    }

    componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>, snapshot: SS): void {
        try {
            UIManager.dispatchViewManagerCommand(
                findNodeHandle(this._adContainer),
                UIManager.getViewManagerConfig('NativoContainer').Commands.placeAdInView, [this.props.index, this.props.sectionUrl]);
        } catch (e) {
            this.setDefaultState()
        }
    }

    handleAdLoadFailed(event) {
        this.setDefaultState()
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
            adLoaded: false
        });
    }

    handleAdLoaded(event) {
        try {
            if (event.nativeEvent.adType === 'NtvStandardDisplayInterface') {
                this.setState({
                    videoFlag: false,
                    nativeFlag: false,
                    standardDisplayFlag: true,
                    adDescription: '',
                    adTitle: '',
                    adAuthorName: '',
                    adDate: event.nativeEvent.adDate,
                    displayWidth: event.nativeEvent.adDisplayWidth,
                    displayHeight: event.nativeEvent.adDisplayHeight,
                    adLoaded: true

                });
            } else if (event.nativeEvent.adType === 'NtvAdtypeClickout' || event.nativeEvent.adType === 'NtvAdTypeNative') {
                this.setState({
                    videoFlag: false,
                    nativeFlag: true,
                    standardDisplayFlag: false,
                    adDescription: event.nativeEvent.adDescription,
                    adTitle: event.nativeEvent.adTitle,
                    adAuthorName: event.nativeEvent.adAuthorName,
                    adDate: event.nativeEvent.adDate,
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
                    adDate: event.nativeEvent.adDate,
                    adLoaded: true
                });
            }
        } catch (e) {
            this.setDefaultState()
        }
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
                                   style={{alignItems: 'center'}}>
                    {this.state.nativeFlag &&
                    <NativeAdTemplate adDate={this.state.adDate} adTitle={this.state.adTitle}
                                      adDescription={this.state.adDescription}
                                      adAuthorName={this.state.adAuthorName}
                                      adLoaded={this.state.adLoaded}/>}
                    {this.state.videoFlag &&
                    <NativeVideoAdTemplate adDate={this.state.adDate} adTitle={this.state.adTitle}
                                           adDescription={this.state.adDescription}
                                           adAuthorName={this.state.adAuthorName}
                                           adLoaded={this.state.adLoaded}/>}
                    {this.state.standardDisplayFlag && <StandardDisplayAdTemplate adLoaded={this.state.adLoaded} webViewForSD={true} displayWidth={this.state.displayWidth} displayHeight={this.state.displayHeight}/>}
                    {!this.state.adLoaded && <View style={{width: 1, height: 1}}/>}
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