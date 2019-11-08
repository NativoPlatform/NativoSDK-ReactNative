import React, {Component} from 'react';
import ErrorBoundary from "./ErrorBoundary";
import NativoAdComponentInternal from "./sdk_components/NativoAdComponentInternal";

class NativoAd extends Component<props> {

    constructor(props) {
        super(props);
        this._nodes = new Map();
    }

    prefetchAd() {
        Array.from(this._nodes.values())
            .filter(node => node != null)
            .forEach(node => {
                node.prefetchAd();
            });
    }

    render() {
        return (
            <ErrorBoundary>
                <NativoAdComponentInternal ref={c => this._nodes.set(this.props.index, c)} {...this.props}
                                   sectionUrl={this.props.sectionUrl}
                                   index={this.props.index} nativeAdTemplate={this.props.nativeAdTemplate}
                                   nativeVideoAdTemplate={this.props.nativeVideoAdTemplate}
                                   standardDisplayAdTemplate={this.props.standardDisplayAdTemplate}
                                   landingPageAdTemplate={this.props.landingPageAdTemplate}/>
            </ErrorBoundary>
        )
    }
}

export default NativoAd