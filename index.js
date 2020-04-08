
import { NativeModules } from 'react-native';
import NativoAd from './NativoAd';
import NativoWebContent from './NativoWebContent';
import NativoVideo from './NativoVideo';

const NativoSDK = NativeModules.NativoSDK;
module.exports = { NativoSDK, NativoAd, NativoWebContent, NativoVideo }