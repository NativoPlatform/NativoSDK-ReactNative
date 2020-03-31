//
//  NativeAdTemplate.m
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import "NativeAdTemplate.h"
#import <React/UIView+React.h>
#import <React/RCTUIManager.h>

@implementation NativeAdTemplate

- (UIImageView *)adImageView {
    UIView *adImageView = [self.bridge.uiManager viewForNativeID:@"adImage" withRootTag:self.reactTag];
    if (adImageView) {
        return (UIImageView *)adImageView;
    }
    return nil;
}

- (UIImageView *)authorImageView {
    UIView *authorImageView = [self.bridge.uiManager viewForNativeID:@"adAuthorImage" withRootTag:self.reactTag];
    if (authorImageView) {
        return (UIImageView *)authorImageView;
    }
    return nil;
}

- (UILabel *)titleLabel {
    return nil;
}

- (UILabel *)authorNameLabel {
    return nil;
}

@end
