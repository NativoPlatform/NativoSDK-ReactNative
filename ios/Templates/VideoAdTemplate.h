//
//  VideoAdTemplate.h
//  react-native-nativo-ads
//
//  Created by Matthew Murray on 11/11/19.
//

#import "RCTRootView.h"
#import <NativoSDK/NativoSDK.h>

NS_ASSUME_NONNULL_BEGIN

@interface VideoAdTemplate : RCTRootView <NtvVideoAdInterface>

@property (nonatomic, readonly) UIView *videoView;
@property (nonatomic, readonly) UIImageView *authorImageView;

@end

NS_ASSUME_NONNULL_END
