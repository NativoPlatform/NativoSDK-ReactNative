import PropTypes from 'prop-types';
import React from 'react';
import {requireNativeComponent, AppRegistry } from 'react-native';

const _registeredTemplates = [];

function NativoAdComponent(props) {
    const { nativeAdTemplate, videoAdTemplate, standardDisplayAdTemplate, ...other } = props;

    _onNativeAdClick = (event) => {
        if (!props.onNativeAdClick) {
            console.log("Nativo ad at index "+ props.index +" was clicked but 'onNativeAdClick' not implemented");
            return;
        }
        props.onNativeAdClick(event.nativeEvent);
    }
    _onDisplayAdClick = (event) => {
        if (!props.onDisplayAdClick) {
            console.log("Nativo ad at index "+ props.index +" was clicked but 'onDisplayAdClick' not implemented");
            return;
        }
        props.onDisplayAdClick(event.nativeEvent);
    }
    _onNeedsRemoveAd = (event) => {
        if (!props.onNeedsRemoveAd) {
            console.log("Nativo ad at index "+ props.index +" should be removed but 'onNeedsRemoveAd' not implemented");
            return;
        }
        props.onNeedsRemoveAd(event.nativeEvent);
    }
    
    // Register templates with app registry root views
    const allTemplates = [nativeAdTemplate, videoAdTemplate, standardDisplayAdTemplate];
    allTemplates.forEach((template) => {
        if (template != null) {
            if ( !_registeredTemplates.includes(template.name) ) {
                AppRegistry.registerComponent(template.name, () => template);
                _registeredTemplates.push(template.name);
            }
        }
    });

    return (
        <NativoAd {...other} 
            onNativeAdClick={_onNativeAdClick} 
            onDisplayAdClick={_onDisplayAdClick} 
            onNeedsRemoveAd={_onNeedsRemoveAd} 
            nativeAdTemplate={nativeAdTemplate.name} 
            videoAdTemplate={videoAdTemplate.name} 
            stdDisplayAdTemplate={standardDisplayAdTemplate.name}>
        </NativoAd>
    );
}

NativoAdComponent.propTypes = {
    sectionUrl: PropTypes.string,
    index: PropTypes.number,
    nativeAdTemplate: PropTypes.func,
    videoAdTemplate: PropTypes.func,
    standardDisplayAdTemplate: PropTypes.func,
    onNativeAdClick: PropTypes.func,
    onDisplayAdClick: PropTypes.func,
    onNeedsRemoveAd: PropTypes.func
};
const NativoAd = requireNativeComponent('NativoAd', NativoAdComponent);

export default NativoAdComponent;