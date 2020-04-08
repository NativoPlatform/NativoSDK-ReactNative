import React from 'react';
import {View, StyleSheet} from 'react-native';
import NativoVideoControls from "./view_components/NativoVideoControls";
import Video from "react-native-video";

function NativoVideo(props) {
    return (
        <View style={styles.container}>
            <Video nativeID={'videoView'} useTextureView={true} style={styles.backgroundVideo} source={{uri: ''}}/>
            <NativoVideoControls/>
        </View>
    );
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    backgroundVideo: {
        position: 'absolute',
        top: 0,
        left: 0,
        bottom: 0,
        right: 0,
    },

});

export default NativoVideo;