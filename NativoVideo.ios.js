import React from 'react';
import { View } from 'react-native';

function NativoVideo(props) {
    return (<View {...props} nativeID={'videoView'} />);
}

export default NativoVideo;