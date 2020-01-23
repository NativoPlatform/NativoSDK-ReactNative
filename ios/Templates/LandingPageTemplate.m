//
//  LandingPageTemplate.m
//  react-native-nativo-ads
//
//  Created by Matthew Murray on 11/12/19.
//

#import "LandingPageTemplate.h"
#import "NativoAdsUtils.h"
#import <React/RCTUtils.h>

@implementation LandingPageTemplate

- (WKWebView *)contentWKWebView {
    return self.webView;
}

//- (UIImageView *)previewImageView {
//    if (self.rootWebView) {
//        UIView *previewImageView = [self.bridge.uiManager viewForNativeID:@"articleImage" withRootTag:self.rootWebView.reactTag];
//        if (previewImageView) {
//            return (UIImageView *)previewImageView;
//        }
//    }
//    return nil;
//}
//
//- (UIImageView *)authorImageView {
//    if (self.rootWebView) {
//        UIView *authorImageView = [self.bridge.uiManager viewForNativeID:@"authorImage" withRootTag:self.rootWebView.reactTag];
//        if (authorImageView) {
//            return (UIImageView *)authorImageView;
//        }
//    }
//    return nil;
//}

- (void)didLoadContentWithAd:(nonnull NtvAdData *)adData {
    self.adData = adData;
}

- (BOOL)contentWebViewShouldScroll {
    return self.shouldScroll;
}

- (void)setContentWebViewHeight:(CGFloat)contentHeight {
    if (!self.shouldScroll) {
        if (self.onFinishLoading) {
            self.onFinishLoading(@{ @"contentHeight" : @(contentHeight) } );
        }
    }
}

- (void)handleExternalLink:(nonnull NSURL *)link {
    if (self.onClickExternalLink) {
        self.onClickExternalLink(@{ @"url" : link.absoluteString });
    }
}

- (void)contentWebViewDidFinishLoad {
    if (self.shouldScroll) {
        if (self.onFinishLoading) {
            self.onFinishLoading(@{ @"contentHeight" : @(self.webView.frame.size.height) });
        }
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
