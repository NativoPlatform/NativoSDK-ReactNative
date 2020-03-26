//
//  NativeAdTemplate.h
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import "RCTRootView.h"
#import <NativoSDK/NativoSDK.h>

NS_ASSUME_NONNULL_BEGIN

@interface NativeAdTemplate : RCTRootView <NtvAdInterface>

@property (nonatomic, readonly) UIImageView *adImageView;
@property (nonatomic, readonly) UIImageView *authorImageView;

@end

NS_ASSUME_NONNULL_END
