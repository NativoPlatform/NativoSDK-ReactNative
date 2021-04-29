import React, {Component} from 'react';
import ErrorBoundary from "./ErrorBoundary";
import NativoAdComponentInternal from "./sdk_components/NativoAdComponentInternal";
import PropTypes from 'prop-types';

class NativoAd extends Component<props> {

    constructor(props) {
        super(props);
        this._nodes = new Map();
    }

    render() {
        return (
            <ErrorBoundary>
                <NativoAdComponentInternal ref={c => this._nodes.set(this.props.index, c)} {...this.props}
                                           sectionUrl={this.props.sectionUrl}
                                           index={this.props.index}
                                           nativeAdTemplate={this.props.nativeAdTemplate}
                                           nativeVideoAdTemplate={this.props.videoAdTemplate}
                                           standardDisplayAdTemplate={this.props.standardDisplayAdTemplate}
                                           enableGAMVersion={this.props.enableGAMVersion}/>
            </ErrorBoundary>
        )
    }
}

NativoAd.propTypes = {
    sectionUrl: PropTypes.string,
    index: PropTypes.number,
    nativeAdTemplate: PropTypes.func,
    videoAdTemplate: PropTypes.func,
    standardDisplayAdTemplate: PropTypes.func,
    onNativeAdClick: PropTypes.func,
    onDisplayAdClick: PropTypes.func,
    onAdRemoved: PropTypes.func,
    onAdRendered: PropTypes.func,
    extraTemplateProps: PropTypes.object,
    enableGAMVersion: PropTypes.string
};

export default NativoAd;
