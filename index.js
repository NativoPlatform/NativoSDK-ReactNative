
import { NativeModules } from 'react-native';
import NativoAd from './NativoAd';
import NativoWebContent from './NativoWebContent';

const NativoSDK = NativeModules.NativoSDK;
module.exports = { NativoSDK, NativoAd, NativoWebContent }