import PropTypes from 'prop-types';
import React from 'react';
import {requireNativeComponent, AppRegistry } from 'react-native';

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
    
    const allTemplates = {...nativeAdTemplate, ...videoAdTemplate, ...standardDisplayAdTemplate};
    for (const templateName in allTemplates) {
        AppRegistry.registerComponent(templateName, () => allTemplates[templateName]);
    }
    const nativeTemplateName = getFirstKey(nativeAdTemplate);
    const videoTemplateName = getFirstKey(videoAdTemplate);
    const stdDisplayTemplateName = getFirstKey(standardDisplayAdTemplate);

    return (
        <NativoAd {...other} 
            onNativeAdClick={_onNativeAdClick} 
            onDisplayAdClick={_onDisplayAdClick} 
            onNeedsRemoveAd={_onNeedsRemoveAd} 
            nativeAdTemplate={nativeTemplateName} 
            videoAdTemplate={videoTemplateName} 
            stdDisplayAdTemplate={stdDisplayTemplateName} />
    );
}

function getFirstKey(obj) {
    const keys = Object.keys(obj);
    if (keys && keys.length > 0) {
        return keys[0];
    }
    return null;
}

NativoAdComponent.propTypes = {
    sectionUrl: PropTypes.string,
    index: PropTypes.number,
    nativeAdTemplate: PropTypes.object,
    videoAdTemplate: PropTypes.object,
    standardDisplayAdTemplate: PropTypes.object,
    onNativeAdClick: PropTypes.func,
    onDisplayAdClick: PropTypes.func,
    onNeedsRemoveAd: PropTypes.func
};
const NativoAd = requireNativeComponent('NativoAd', NativoAdComponent);

export default NativoAdComponent;