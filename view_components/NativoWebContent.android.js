/**
 * Copyright 2024 Nativo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import PropTypes from 'prop-types';
import React, {Component} from 'react';
import ErrorBoundary from "./ErrorBoundary";
import NativoLandingPageComponentInternal from "../android_components/NativoLandingPageComponentInternal";
import NativoStandardDisplay from "../android_components/NativoStandardDisplay";

class NativoWebContent extends Component {

    constructor(props) {
        super(props);
    }

    render() {
        return (
            <ErrorBoundary>
                {typeof(this.props.webViewForSD) == "undefined" &&
                <NativoLandingPageComponentInternal ref={(el) => (this._landingContainer = el)}
                                                    adId={this.props.index}
                                                    containerHash={this.props.containerHash}
                                                    url={this.props.sectionUrl}
                                                    onClickExternalLink={this.props.onClickExternalLink}
                                                    onFinishLoading={this.props.onFinishLoading}
                                                    {...this.props}/> }
                {this.props.webViewForSD && <NativoStandardDisplay {...this.props}/>}
            </ErrorBoundary>
        )
    }

}

NativoWebContent.propTypes = {
    sectionUrl: PropTypes.string,
    index: PropTypes.number,
    containerHash: PropTypes.number,
    shouldScroll: PropTypes.bool,
    onFinishLoading: PropTypes.func,
    onClickExternalLink: PropTypes.func
};

export default NativoWebContent;
