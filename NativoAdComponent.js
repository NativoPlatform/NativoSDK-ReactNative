import React, {Component} from 'react';
import {findNodeHandle, NativeEventEmitter, requireNativeComponent, StyleSheet, UIManager, View} from 'react-native';

const NativoAdContainer = requireNativeComponent("NativoContainer");

export default class NativoAdComponent extends Component<Props> {

    constructor(props) {
        super(props);
        this.state = {
            nativeFlag: false,
            videoFlag: false,
            standardDisplayFlag: false,
            adDescription: '',
            adTitle: '',
            adAuthorName: '',
            adDate: ''
        };
        this.handleAdLoaded = this.handleAdLoaded.bind(this);
    }

    prefetchAd() {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this._adContainer),
            UIManager.getViewManagerConfig('NativoContainer').Commands.prefetchAd, [this.props.index]);
    }


    componentDidMount(): void {
        const eventEmitter = new NativeEventEmitter(NativoAdContainer);
        eventEmitter.addListener('EventReminder', (event) => {
            this.props.navigation.navigate('LandingScreen', {
                sectionUrl: event.sectionUrl,
                adId: event.adId,
                containerHash: event.containerHash,
                landingPageAdTemplate: this.props.landingPageAdTemplate
            })
        });
    }

    componentDidUpdate(prevProps: Readonly<P>, prevState: Readonly<S>, snapshot: SS): void {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this._adContainer),
            UIManager.getViewManagerConfig('NativoContainer').Commands.placeAdInView, [this.props.index]);
    }

    handleAdLoaded(event) {
        if (event.nativeEvent.adType === 'NtvStandardDisplayInterface') {
            this.setState({
                videoFlag: false,
                nativeFlag: false,
                standardDisplayFlag: true,
                adDescription: '',
                adTitle: '',
                adAuthorName: '',
                adDate: ''

            });
        } else if (event.nativeEvent.adType === 'NtvAdtypeClickout' || event.nativeEvent.adType === 'NtvAdTypeNative') {
            this.setState({
                videoFlag: false,
                nativeFlag: true,
                standardDisplayFlag: false,
                adDescription: event.nativeEvent.adDescription,
                adTitle: event.nativeEvent.adTitle,
                adAuthorName: event.nativeEvent.adAuthorName,
                adDate: event.nativeEvent.adDate

            });
        } else {
            this.setState({
                videoFlag: true,
                nativeFlag: false,
                standardDisplayFlag: false,
                adDescription: event.nativeEvent.adDescription,
                adTitle: event.nativeEvent.adTitle,
                adAuthorName: event.nativeEvent.adAuthorName,
                adDate: event.nativeEvent.adDate
            });
        }
    }


    render() {
        const NativeAdTemplate = this.props.nativeAdTemplate;
        const NativeVideoAdTemplate = this.props.nativeVideoAdTemplate;
        const StandardDisplayAdTemplate = this.props.standardDisplayAdTemplate;
        return (
            <View style={styles.container}>
                <NativoAdContainer ref={(el) => (this._adContainer = el)}
                                   sectionUrl={{'url': this.props.sectionUrl, 'index': this.props.index}}
                                   onAdLoaded={this.handleAdLoaded}>
                    {this.state.nativeFlag &&
                    <NativeAdTemplate adDate={this.state.adDate} adTitle={this.state.adTitle}
                                      adDescription={this.state.adDescription} adAuthorName={this.state.adAuthorName}/>}
                    {this.state.videoFlag &&
                    <NativeVideoAdTemplate adDate={this.state.adDate} adTitle={this.state.adTitle}
                                           adDescription={this.state.adDescription}
                                           adAuthorName={this.state.adAuthorName}/>}
                    {this.state.standardDisplayFlag && <StandardDisplayAdTemplate/>}
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