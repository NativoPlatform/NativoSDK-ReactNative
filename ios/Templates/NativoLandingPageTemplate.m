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

#import "NativoLandingPageTemplate.h"
#import <React/RCTUtils.h>

@interface NativoLandingPageTemplate ()

@end

@implementation NativoLandingPageTemplate

- (WKWebView *)contentWKWebView {
    return self.webView;
}

- (void)didLoadContentWithAd:(nonnull NtvAdData *)adData {
    self.adData = adData;
}

- (BOOL)contentWebViewShouldScroll {
    return self.shouldScroll;
}

- (void)handleExternalLink:(nonnull NSURL *)link {
    if (self.onClickExternalLink) {
        self.onClickExternalLink(@{ @"url" : link.absoluteString });
    }
}

- (void)contentWebViewDidFinishLoad {
    if (self.onFinishLoading) {
        float contentHeight = [self.webView.subviews[0] contentSize].height + 2.0f;
        self.onFinishLoading(@{ @"contentHeight" : @(contentHeight) });
    }
}

- (void)contentWebViewDidFailLoadWithError:(nullable NSError *)error {
    if (self.onFinishLoading) {
        NSDictionary *rctError = RCTMakeError(error.description, error, nil);
        self.onFinishLoading(@{ @"error" : rctError,
                        @"contentHeight" : @(self.webView.frame.size.height) });
    }
}

@end
