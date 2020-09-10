import PropTypes from 'prop-types';
import React from 'react';
import {requireNativeComponent, AppRegistry } from 'react-native';

const _registeredTemplates = [];

function NativoAdComponent(props) {
    const { nativeAdTemplate, videoAdTemplate, standardDisplayAdTemplate, extraTemplateProps, ...other } = props;

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
    _onAdRendered = (event) => {
        if (!props.onAdRendered) { return; }
        props.onAdRendered(event.nativeEvent);
    }
    _onAdRemoved = (event) => {
        if (!props.onAdRemoved) { return; }
        props.onAdRemoved(event.nativeEvent);
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
            onAdRendered={_onAdRendered}
            onAdRemoved={_onAdRemoved} 
            nativeAdTemplate={nativeAdTemplate ? nativeAdTemplate.name : null} 
            videoAdTemplate={videoAdTemplate ? videoAdTemplate.name : null} 
            stdDisplayAdTemplate={standardDisplayAdTemplate ? standardDisplayAdTemplate.name : null}
            extraTemplateProps={extraTemplateProps}>
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
    onAdRendered: PropTypes.func, 
    onAdRemoved: PropTypes.func,
    enableDFPVersion: PropTypes.string,
    extraTemplateProps: PropTypes.object
};
const NativoAd = requireNativeComponent('NativoAd', NativoAdComponent);

export default NativoAdComponent;