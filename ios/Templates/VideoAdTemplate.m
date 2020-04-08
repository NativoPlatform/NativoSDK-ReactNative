//
//  VideoAdTemplate.m
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import "VideoAdTemplate.h"
#import <React/UIView+React.h>
#import <React/RCTUIManager.h>
@import AVFoundation;

@implementation VideoAdTemplate

- (UIView *)videoView {
    UIView *videoView = [self.bridge.uiManager viewForNativeID:@"videoView" withRootTag:self.reactTag];
    return videoView;
}

- (UIImageView *)authorImageView {
    UIView *authorImageView = [self.bridge.uiManager viewForNativeID:@"adAuthorImage" withRootTag:self.reactTag];
    if (authorImageView) {
        return (UIImageView *)authorImageView;
    }
    return nil;
}

- (UIView *)adChoicesIconView {
    UIView *adChoicesIconView = [self.bridge.uiManager viewForNativeID:@"adChoicesImage" withRootTag:self.reactTag];
    if (adChoicesIconView) {
        return (UIView *)adChoicesIconView;
    }
    return nil;
}

- (UILabel *)titleLabel {
    return nil;
}

- (UILabel *)authorNameLabel {
    return nil;
}

- (nonnull NSString *)videoFillMode {
    return AVLayerVideoGravityResizeAspectFill;
}


@end
