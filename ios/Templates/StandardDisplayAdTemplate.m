/**
 * Copyright 2024 Nativo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
