import React, {Component} from 'react';
import {StyleSheet, View} from "react-native";
import WebView from "react-native-webview";

class NativoStandardDisplay extends Component<props>{


    render(){
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