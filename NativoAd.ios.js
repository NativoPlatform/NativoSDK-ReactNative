import PropTypes from 'prop-types';
import {requireNativeComponent, AppRegistry } from 'react-native';
import React, { Component } from 'react';

const _registeredTemplates = [];

class NativoAdComponent extends Component {

    constructor(props) {
        super(props);

        // Register templates with app registry root views
        const allTemplates = [props.nativeAdTemplate, props.videoAdTemplate, props.standardDisplayAdTemplate];
        allTemplates.forEach((template) => {
            if (template != null) {
                if ( !_registeredTemplates.includes(template.name) ) {
                    AppRegistry.registerComponent(template.name, () => template);
                    _registeredTemplates.push(template.name);
                }
            }
        });
    }

    _onNativeAdClick = (event) => {
        if (!this.props.onNativeAdClick) {
            console.log("Nativo ad at index "+ this.props.index +" was clicked but 'onNativeAdClick' not implemented");
            return;
        }
        this.props.onNativeAdClick(event.nativeEvent);
    }
    _onDisplayAdClick = (event) => {
        if (!this.props.onDisplayAdClick) {
            console.log("Nativo ad at index "+ this.props.index +" was clicked but 'onDisplayAdClick' not implemented");
            return;
        }
        this.props.onDisplayAdClick(event.nativeEvent);
    }
    _onAdRendered = (event) => {
        if (!this.props.onAdRendered) { return; }
        this.props.onAdRendered(event.nativeEvent);
    }
    _onAdRemoved = (event) => {
        if (!this.props.onAdRemoved) { return; }
        this.props.onAdRemoved(event.nativeEvent);
    }

    render() {
        return (
            <NativoAd style={this.props.style}
                sectionUrl={this.props.sectionUrl}
                index={this.props.index}
                onNativeAdClick={this._onNativeAdClick} 
                onDisplayAdClick={this._onDisplayAdClick} 
                onAdRendered={this._onAdRendered}
                onAdRemoved={this._onAdRemoved} 
                nativeAdTemplate={this.props.nativeAdTemplate ? this.props.nativeAdTemplate.name : null} 
                videoAdTemplate={this.props.videoAdTemplate ? this.props.videoAdTemplate.name : null} 
                stdDisplayAdTemplate={this.props.standardDisplayAdTemplate ? this.props.standardDisplayAdTemplate.name : null}
                enableDFPVersion={this.props.enableDFPVersion}
                extraTemplateProps={this.props.extraTemplateProps}>
            </NativoAd>
        );
    }   
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