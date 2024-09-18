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

#import "RCTNativoSDK.h"
#import "NtvSharedSectionDelegate.h"
#import "NativoAdsUtils.h"
#import <React/RCTLog.h>
#import <React/UIView+React.h>
#import <React/RCTUIManager.h>
#import <React/RCTUIManagerUtils.h>
#import <NativoSDK/NativoSDK.h>



@implementation RCTNativoSDK

RCT_EXPORT_MODULE(NativoSDK);

RCT_EXPORT_METHOD(enableDevLogs)
{
    [NativoSDK enableDevLogs];
}

RCT_EXPORT_METHOD(enableTestAdvertisements)
{
    [NativoSDK enableTestAdvertisements];
}

RCT_EXPORT_METHOD(enableTestAdvertisementsWithType:(nonnull NSNumber *)adType)
{
    [NativoSDK enableTestAdvertisementsWithAdType:[adType intValue]];
}

RCT_EXPORT_METHOD(prefetchAdForSection:(NSString *)section callback:(RCTResponseSenderBlock)senderBlock)
{
    [NativoSDK setSectionDelegate:[NtvSharedSectionDelegate sharedInstance] forSection:section];
    [NtvSharedSectionDelegate setPrefetchCallback:senderBlock forSectionUrl:section];
    [NativoSDK prefetchAdForSection:section options:nil];
}

RCT_EXPORT_METHOD(placeAdInWebView:(NSString *)section)
{
    if (self.bridge) {
        RCTExecuteOnMainQueue(^{
            UIView *window = [[UIApplication sharedApplication].delegate window];
            UIView *root = [NativoAdsUtils findClass:[RCTRootView class] inView:window];
            UIView *rootWebView = [self.bridge.uiManager viewForNativeID:@"nativoMoapAdView" withRootTag:root.reactTag];
            WKWebView *webview = (WKWebView *)[NativoAdsUtils findClass:[WKWebView class] inView:rootWebView];
            [NativoSDK placeAdInWebView:webview forSection:section];
        });
    }
}

RCT_EXPORT_METHOD(trackShareActionForAd:(NSString *)adID)
{
    if (adID) {
        NtvAdData *adData = [[NtvSharedSectionDelegate sharedInstance].adIDMap objectForKey:adID];
        TrackDidShareBlock trackBlock = nil;
        @try {
            trackBlock = [adData valueForKey:@"trackDidShareBlock"];
        } @catch (NSException *exception) {}
        if (trackBlock) {
            trackBlock(NtvSharePlatformOther);
        }
    }
}

RCT_EXPORT_METHOD(clearAdsInSection:(NSString *)sectionUrl)
{
    [NativoSDK clearAdsInSection:sectionUrl];
}

- (NSArray<NSString *> *)supportedEvents
{
  return @[];
}

+ (BOOL)requiresMainQueueSetup
{
    return YES;
}

- (NSDictionary *)constantsToExport
{
    NSDictionary *adTypes = @{ @"NATIVE" : @(NtvTestAdTypeNative),
                               @"DISPLAY" : @(NtvTestAdTypeDisplay),
                               @"STORY" : @(NtvTestAdTypeStory),
                               @"CLICK_VIDEO" : @(NtvTestAdTypeClickToPlayVideo),
                               @"SCROLL_VIDEO" : @(NtvTestAdTypeScrollToPlayVideo),
                               @"STANDARD_DISPLAY" : @(NtvTestAdTypeStandardDisplay),
                               @"DISPLAY_ADCHOICES" : @(NtvTestAdTypeAdChoicesDisplay),
                               @"CLICK_VIDEO_ADCHOICES" : @(NtvTestAdTypeAdChoicesVideo),
                               @"NO_FILL" : @(NtvTestAdTypeNoFill) };
    return @{ @"AdTypes" : adTypes };
};

@end
