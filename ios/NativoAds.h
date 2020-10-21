//
//  NativoAds.h
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import <React/RCTViewManager.h>
#import <NativoSDK/NativoSDK.h>


@interface NativoAdManager : RCTViewManager
@end


@interface NativoAd: UIView

@property (nonatomic) RCTBridge *bridge;
@property (nonatomic) RCTBubblingEventBlock onDisplayAdClick;
@property (nonatomic) RCTBubblingEventBlock onNativeAdClick;
@property (nonatomic) RCTBubblingEventBlock onAdRendered;
@property (nonatomic) RCTBubblingEventBlock onAdRemoved;
- (void)injectWithAdData:(NtvAdData *)adData;
- (void)collapseView;

@end

@interface NtvAdData (NtvUUID)
@property (nonatomic) NSUUID *adID;
@end
