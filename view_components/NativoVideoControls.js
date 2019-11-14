import {Image, ImageBackground, ProgressBarAndroid} from "react-native";
import React from "react";


const NativoVideoControls = () => {
    return(
        <ImageBackground style={{
            flexDirection: 'column',
            flex: 1,
            width: undefined,
            height: undefined,
            alignItems: 'center',
            justifyContent: 'center',
            alignSelf: 'stretch',
            resizeMode: 'cover'
        }} nativeID={'articleImage'}>
            <ImageBackground nativeID={'videoRestart'} style={{
                flex: 1,
                width: 40,
                height: '100%',
                alignSelf: 'center',
                alignItems: 'center',
                justifyContent: 'center'
            }}>
                <ImageBackground nativeID={'videoPlay'} style={{width: '100%', height: '100%'}}>
                    <ProgressBarAndroid style={{width: '100%', height: '100%'}}
                                        nativeID={'videoProgress'}/>
                </ImageBackground>
            </ImageBackground>
            <ImageBackground
                style={{width: '90%', height: 10, bottom: 10, justifyContent: 'flex-end'}}>
                <Image nativeID={'videoMuteIndicator'}
                       style={{width: 30, height: 30, alignSelf: 'flex-end'}}/>
            </ImageBackground>
        </ImageBackground>
    )
}

export default NativoVideoControls