//
//  FakeLandingPage.m
//  react-native-nativo-ads
//
//  Created by Matt Murray on 1/8/20.
//

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
