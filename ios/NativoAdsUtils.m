//
//  NativoAdsUtils.m
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import "NativoAdsUtils.h"

@implementation NativoAdsUtils

+ (UIView *)findClass:(Class)class inView:(UIView *)view {
    //NSLog(@"View: %@ has #%lu subviews", view, (unsigned long)view.subviews.count);
    UIView *viewFound = nil;
    for (UIView *subView in view.subviews) {
        if ([subView isKindOfClass:class]) {
            viewFound = subView;
        } else {
            viewFound = [self findClass:class inView:subView];
        }
    }
    return viewFound;
}

+ (UIView *)findClass:(Class)class inSuperViews:(UIView *)view {
    UIView *viewFound = view;
    while (viewFound.superview != nil && ![viewFound.superview isKindOfClass:class]) {
        viewFound = viewFound.superview;
    }
    return viewFound.superview;
}

@end
