import PropTypes from 'prop-types';
import React, {Component} from 'react';
import {requireNativeComponent, UIManager, findNodeHandle } from 'react-native';

class NativoWebComponent extends Component {

    constructor(props) {
        super(props);
    }

    _onClickExternalLink = (event) => {
        if (!this.props.onClickExternalLink) {
          return;
        }
        this.props.onClickExternalLink(event.nativeEvent);
    }
    _onFinishLoading = (event) => {
        if (!this.props.onFinishLoading) {
          return;
        }
        this.props.onFinishLoading(event.nativeEvent);
    }
    trackDidShare = () => {
        UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.getViewManagerConfig('NativoWebContent').Commands.trackDidShare,
            []
        );
    };
    
    render () {
        return (
            <NativoWebContent {...this.props} 
                onClickExternalLink={this._onClickExternalLink} 
                onFinishLoading={this._onFinishLoading} />);
    } 
}

NativoWebComponent.propTypes = {
    sectionUrl: PropTypes.string,
    index: PropTypes.number,
    shouldScroll: PropTypes.bool,
    onFinishLoading: PropTypes.func,
    onClickExternalLink: PropTypes.func
};
const NativoWebContent = requireNativeComponent('NativoWebContent', NativoWebComponent);

export default NativoWebComponent;