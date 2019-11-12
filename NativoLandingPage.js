import React, {Component} from 'react';
import ErrorBoundary from "./ErrorBoundary";
import NativoLandingPageComponentInternal from "./sdk_components/NativoLandingPageComponentInternal";

class NativoLandingPage extends Component<props> {

    constructor(props) {
        super(props);
    }

    render() {
        const {navigation} = this.props;
        const LandingPageAdTemplate = navigation.getParam('landingPageAdTemplate', 'NO-ID');
        const adId = navigation.getParam('adId');
        const containerHash = navigation.getParam('containerHash');
        const url = navigation.getParam('sectionUrl', 'NO-ID');
        return (
            <ErrorBoundary>
                <NativoLandingPageComponentInternal ref={(el) => (this._landingContainer = el)}
                                                    adId={adId}
                                                    containerHash={containerHash}
                                                    url={url}
                                                    landingPageAdTemplate={LandingPageAdTemplate}
                                                    {...this.props}/>
            </ErrorBoundary>
        )
    }
}

export default NativoLandingPage