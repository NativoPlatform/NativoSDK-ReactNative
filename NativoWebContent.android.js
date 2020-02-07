import PropTypes from 'prop-types';
import React, {Component} from 'react';
import ErrorBoundary from "./ErrorBoundary";
import NativoLandingPageComponentInternal from "./sdk_components/NativoLandingPageComponentInternal";

class NativoWebContent extends Component<props> {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <ErrorBoundary>
                <NativoLandingPageComponentInternal ref={(el) => (this._landingContainer = el)}
                                                    adId={this.props.index}
                                                    containerHash={this.props.containerHash}
                                                    url={this.props.sectionUrl}
                                                    onClickExternalLink={this.props.onClickExternalLink}
                                                    onFinishLoading={this.props.onFinishLoading}
                                                    {...this.props}/>
            </ErrorBoundary>
        )
    }

}

NativoWebContent.propTypes = {
    sectionUrl: PropTypes.string,
    index: PropTypes.number,
    shouldScroll: PropTypes.bool,
    onFinishLoading: PropTypes.func,
    onClickExternalLink: PropTypes.func
};

export default NativoWebContent;
