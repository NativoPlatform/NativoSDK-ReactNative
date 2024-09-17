
import { NativeModules } from 'react-native';
import NativoAd from './view_components/NativoAd';
import NativoWebContent from './view_components/NativoWebContent';
import NativoVideo from './view_components/NativoVideo';

const NativoSDK = NativeModules.NativoSDK;
module.exports = { NativoSDK, NativoAd, NativoWebContent, NativoVideo }