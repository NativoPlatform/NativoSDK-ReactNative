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

#import "NativoWebContent.h"
#import <NativoSDK/NativoSDK.h>
#import <React/RCTLog.h>

@implementation NativoWebContentManager

RCT_EXPORT_MODULE(NativoWebContent)
RCT_EXPORT_VIEW_PROPERTY(sectionUrl, NSString)
RCT_EXPORT_VIEW_PROPERTY(index, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(shouldScroll, BOOL)
RCT_EXPORT_VIEW_PROPERTY(onFinishLoading, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onClickExternalLink, RCTBubblingEventBlock)

- (UIView *)view
{
    NativoWebContentView *webContentView = [[NativoWebContentView alloc] init];
    return webContentView;
}

@end


@implementation NativoWebContentView

- (void)didMoveToSuperview {
    
    if (!self.nativoWebView) {
        self.nativoWebView = [[NtvContentWebView alloc] initWithFrame:self.bounds configuration:[[WKWebViewConfiguration alloc] init]];
        [self addSubview:self.nativoWebView];
        self.nativoWebView.translatesAutoresizingMaskIntoConstraints = NO;
        [self.nativoWebView.topAnchor constraintEqualToAnchor:self.topAnchor].active = YES;
        [self.nativoWebView.bottomAnchor constraintEqualToAnchor:self.bottomAnchor].active = YES;
        [self.nativoWebView.leftAnchor constraintEqualToAnchor:self.leftAnchor].active = YES;
        [self.nativoWebView.rightAnchor constraintEqualToAnchor:self.rightAnchor].active = YES;
    }
    
    // This part used only for landing pages
    if (self.sectionUrl && self.index) {
        self.templateProxy = [[NativoLandingPageTemplate alloc] init];
        self.templateProxy.webView = self.nativoWebView;
        self.templateProxy.shouldScroll = self.shouldScroll;
        self.templateProxy.onClickExternalLink = self.onClickExternalLink;
        self.templateProxy.onFinishLoading = self.onFinishLoading;
        [NativoSDK injectSponsoredLandingPageViewController:self.templateProxy forSection:self.sectionUrl withAdAtLocationIdentifier:self.index];
    }
}

@end
