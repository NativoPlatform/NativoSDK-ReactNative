
import { NativeModules } from 'react-native';
import NativoAd from './NativoAd';
import NativoWebContent from './NativoWebContent';

const { NativoSDK } = NativeModules;

export default { NativoSDK, NativoAd, NativoWebContent }
