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

import React, {Component} from 'react';
import ErrorBoundary from "./ErrorBoundary";
import NativoAdComponentInternal from "../android_components/NativoAdComponentInternal";
import PropTypes from 'prop-types';

class NativoAd extends Component {

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
