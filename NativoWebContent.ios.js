import PropTypes from 'prop-types';
import React from 'react';
import {requireNativeComponent } from 'react-native';

function NativoWebComponent(props) {
    const [sectionUrl, setSectionUrl] = React.useState();
    const [index, setIndex] = React.useState();
    const [shouldScroll, setShouldScroll] = React.useState(false);

    _onClickExternalLink = (event) => {
        if (!props.onClickExternalLink) {
          return;
        }
        props.onClickExternalLink(event.nativeEvent);
    }
    _onFinishLoading = (event) => {
        if (!props.onFinishLoading) {
          return;
        }
        props.onFinishLoading(event.nativeEvent);
    }
    return (<NativoWebContent {...props} onClickExternalLink={this._onClickExternalLink} onFinishLoading={this._onFinishLoading} />);
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