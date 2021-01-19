import React, {Component} from 'react';
import {findNodeHandle, NativeEventEmitter, requireNativeComponent, StyleSheet, UIManager, View} from 'react-native';
import WebView from "react-native-webview";

const NativoLandingPageContainer = requireNativeComponent("NativoLandingPageContainer")
const eventEmitter = new NativeEventEmitter(NativoLandingPageContainer);

export class NativoLandingPageComponentInternal extends Component<props> {

    constructor(props) {
        super(props);
    }

    componentDidMount() {
        try {
            UIManager.dispatchViewManagerCommand(
                findNodeHandle(this._landingContainer),
                UIManager.getViewManagerConfig('NativoLandingPageContainer').Commands.injectAd, [this.props.adId, this.props.url, this.props.containerHash]);
        } catch (e) {
        }

        try {
            this.subscriptionHolderOnFinished = eventEmitter.addListener('onPageFinished', (event) => {
                this.props.onFinishLoading(event);
            });

            this.subscriptionHolderOnClick = eventEmitter.addListener('onClickExternalLink', (event) => {
                this.props.onClickExternalLink(url)
            });
        } catch (e) {
        }
    }


    componentWillUnmount() {
        this.subscriptionHolderOnFinished.remove();
        this.subscriptionHolderOnClick.remove();
    }

    render() {
        return (
            <View style={styles.container}>
                <NativoLandingPageContainer ref={(el) => (this._landingContainer = el)} injectLandingPage={{
                    'url': this.props.url,
                    'containerHash': this.props.containerHash,
                    'adId': this.props.index
                }} style={{width: '100%', height: '100%'}} onClickExternalLink={this.needsDisplayClickOutURL} onFinishLoading={this.onFinishLoading}>
                    <View nativeID={'nativoAdWebViewContainer'} style={this.props.style}>
                        <WebView nativeID={'nativoAdWebView'} javaScriptEnabled={true} automaticallyAdjustContentInsets={false}
                                 domStorageEnabled={true} scalesPageToFit={false}/>
                    </View>
                </NativoLandingPageContainer>
            </View>
        )
    }

};

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
});

export default NativoLandingPageComponentInternal;
