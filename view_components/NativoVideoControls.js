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