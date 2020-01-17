//
//  StandardDisplayAdTemplate.m
//  react-native-nativo-ads
//
//  Created by Matthew Murray on 12/10/19.
//

#import "StandardDisplayAdTemplate.h"
#import "NativoAdsUtils.h"
#import <objc/runtime.h>
#import <React/RCTUtils.h>

#define CONTAINER_TAG @"nativoDisplayContainer"

@implementation StandardDisplayAdTemplate

- (NtvContentWebView *)contentWebView {
    if (!_contentWebView) {
        UIView *webview = [NativoAdsUtils findClass:[NtvContentWebView class] inView:self];
        _contentWebView = (NtvContentWebView *)webview;
    }
    return _contentWebView;
}

- (void)didFinishNavigation:(null_unspecified WKNavigation *)navigation {
    //NSLog(@"DidFinishNavigation");
    if (self.onFinishLoading) {
        self.onFinishLoading(@{});
    }
}

- (void)didFailNavigation:(null_unspecified WKNavigation *)navigation withError:(nonnull NSError *)error {
    //NSLog(@"Did fail: %@", error);
    if (self.onFinishLoading) {
        NSDictionary *rctError = RCTMakeError(error.description, error, nil);
        self.onFinishLoading(@{ @"error" : rctError });
    }
}

@end
