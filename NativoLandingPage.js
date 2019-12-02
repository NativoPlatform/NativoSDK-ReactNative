import React, {Component} from 'react';
import ErrorBoundary from "./ErrorBoundary";
import NativoLandingPageComponentInternal from "./sdk_components/NativoLandingPageComponentInternal";

class NativoLandingPage extends Component<props> {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <ErrorBoundary>
                <NativoLandingPageComponentInternal ref={(el) => (this._landingContainer = el)}
                                                    adId={this.props.adId}
                                                    containerHash={this.props.containerHash}
                                                    url={this.props.url}
                                                    landingPageAdTemplate={this.props.LandingPageAdTemplate}
                                                    {...this.props}/>
            </ErrorBoundary>
        )
    }
}

export default NativoLandingPage