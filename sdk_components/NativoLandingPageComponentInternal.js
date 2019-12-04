import React, {Component} from 'react';
import {findNodeHandle, requireNativeComponent, StyleSheet, UIManager, View} from 'react-native';

const NativoLandingPageContainer = requireNativeComponent("NativoLandingPageContainer")

export class NativoLandingPageComponentInternal extends Component {

    componentDidMount() {
        try {
            UIManager.dispatchViewManagerCommand(
                findNodeHandle(this._landingContainer),
                UIManager.getViewManagerConfig('NativoLandingPageContainer').Commands.injectAd, []);
        } catch (e) {
        }
    }

    render() {
        const LandingPageAdTemplate = this.props.landingPageAdTemplate;
        return (
            <View style={styles.container}>
                <NativoLandingPageContainer ref={(el) => (this._landingContainer = el)} injectLandingPage={{
                    'url': this.props.url,
                    'containerHash': this.props.containerHash,
                    'adId': this.props.adId
                }} style={{width: '100%', height: '100%'}}>
                    <LandingPageAdTemplate {...this.props}/>
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