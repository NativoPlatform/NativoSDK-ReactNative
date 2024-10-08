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
import {StyleSheet, View} from "react-native";
import WebView from "react-native-webview";

class NativoStandardDisplay extends Component {

    render() {
        return (
            <View style={styles.container}>
                <View style={this.props.style}>
                    <WebView nativeID={'nativoAdWebView'} javaScriptEnabled={true}
                             automaticallyAdjustContentInsets={false}
                             domStorageEnabled={true} scalesPageToFit={false}/>
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
});

export default NativoStandardDisplay;