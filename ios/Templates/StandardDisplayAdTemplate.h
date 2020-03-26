//
//  StandardDisplayAdTemplate.h
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import "RCTRootView.h"
#import <React/RCTView.h>
#import <NativoSDK/NativoSDK.h>

NS_ASSUME_NONNULL_BEGIN

@interface StandardDisplayAdTemplate : RCTRootView <NtvStandardDisplayAdInterface>

@property (nonatomic) NtvContentWebView *contentWebView;
@property (nonatomic) RCTBubblingEventBlock onFinishLoading;

@end

NS_ASSUME_NONNULL_END
