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
import {requireNativeComponent} from 'react-native';

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