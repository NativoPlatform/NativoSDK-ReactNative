//
//  NativoAdTemplate.m
//  DoubleConversion
//
//  Created by Matthew Murray on 11/11/19.
//

#import "NativeAdTemplate.h"
#import <React/UIView+React.h>
#import <React/RCTUIManager.h>

@implementation NativeAdTemplate

- (UIImageView *)adImageView {
    UIView *adImageView = [self.bridge.uiManager viewForNativeID:@"articleImage" withRootTag:self.reactTag];
    if (adImageView) {
        return (UIImageView *)adImageView;
    }
    return nil;
}

- (UIImageView *)authorImageView {
    UIView *authorImageView = [self.bridge.uiManager viewForNativeID:@"authorImage" withRootTag:self.reactTag];
    if (authorImageView) {
        return (UIImageView *)authorImageView;
    }
    return nil;
}

@end
