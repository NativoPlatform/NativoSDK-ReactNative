//
//  VideoAdTemplate.h
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import "RCTRootView.h"
#import <NativoSDK/NativoSDK.h>

NS_ASSUME_NONNULL_BEGIN

@interface VideoAdTemplate : RCTRootView <NtvVideoAdInterface>

@property (nonatomic, readonly) UIView *videoView;
@property (nonatomic, readonly) UIImageView *authorImageView;
@property (nonatomic, readonly) UIView *adChoicesIconView;

@end

NS_ASSUME_NONNULL_END
