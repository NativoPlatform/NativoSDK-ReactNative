//
//  NativoAdTemplate.h
//  DoubleConversion
//
//  Created by Matthew Murray on 11/11/19.
//

#import "RCTRootView.h"
#import <NativoSDK/NativoSDK.h>

NS_ASSUME_NONNULL_BEGIN

@interface NativeAdTemplate : RCTRootView <NtvAdInterface>

@property (nonatomic, readonly) UIImageView *adImageView;
@property (nonatomic, readonly) UIImageView *authorImageView;

@end

NS_ASSUME_NONNULL_END
