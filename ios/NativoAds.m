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

#import "NativoAds.h"
#import "NativeAdTemplate.h"
#import "VideoAdTemplate.h"
#import "StandardDisplayAdTemplate.h"
#import "NtvSharedSectionDelegate.h"
#import "NativoLandingPageTemplate.h"
#import <React/UIView+React.h>
#import <React/RCTRootView.h>
#import <React/RCTRootViewDelegate.h>
#import <React/RCTUIManager.h>
#import <React/RCTLog.h>
#import <objc/runtime.h>


@interface NativoAdManager ()
@end

@implementation NativoAdManager

RCT_EXPORT_MODULE(NativoAd)
RCT_EXPORT_VIEW_PROPERTY(sectionUrl, NSString)
RCT_EXPORT_VIEW_PROPERTY(index, NSNumber)
RCT_EXPORT_VIEW_PROPERTY(nativeAdTemplate, NSString)
RCT_EXPORT_VIEW_PROPERTY(videoAdTemplate, NSString)
RCT_EXPORT_VIEW_PROPERTY(stdDisplayAdTemplate, NSString)
RCT_EXPORT_VIEW_PROPERTY(onNativeAdClick, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onDisplayAdClick, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdRendered, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAdRemoved, RCTBubblingEventBlock)
RCT_EXPORT_VIEW_PROPERTY(enableGAMVersion, NSString)
RCT_EXPORT_VIEW_PROPERTY(extraTemplateProps, NSDictionary)



- (instancetype)init {
    self = [super init];
    [NativoSDK registerClass:[NativeAdTemplate class] forAdTemplateType:NtvAdTemplateTypeNative];
    [NativoSDK registerClass:[VideoAdTemplate class] forAdTemplateType:NtvAdTemplateTypeVideo];
    [NativoSDK registerClass:[StandardDisplayAdTemplate class] forAdTemplateType:NtvAdTemplateTypeStandardDisplay];
    [NativoSDK registerClass:[NativoLandingPageTemplate class] forAdTemplateType:NtvAdTemplateTypeLandingPage];
    return self;
}

- (UIView *)view
{
    NativoAd *nativoAd = [[NativoAd alloc] init];
    nativoAd.bridge = self.bridge;
    return nativoAd;
}

- (void)setBridge:(RCTBridge *)bridge {
    super.bridge = bridge;
}

+ (BOOL)requiresMainQueueSetup {
    return YES;
}

@end


# pragma mark - Nativo Ad

@interface NativoAd () <RCTRootViewDelegate>
@property (nonatomic) NtvAdData *adData;
@property (nonatomic) NSString *sectionUrl;
@property (nonatomic) NSNumber *index;
@property (nonatomic) NSString *nativeAdTemplate;
@property (nonatomic) NSString *videoAdTemplate;
@property (nonatomic) NSString *stdDisplayAdTemplate;
@property (nonatomic) NSString *enableGAMVersion;
@property (nonatomic) NSDictionary *extraTemplateProps;
@end

@implementation NativoAd

- (instancetype)init {
    self = [super init];
    return self;
}

- (void)setIndex:(NSNumber *)index {
    BOOL shouldUpdate = (BOOL)_index;
    _index = index;
    if (shouldUpdate) {
        [self updateComponent];
    }
}

- (void)setSectionUrl:(NSString *)sectionUrl {
    BOOL shouldUpdate = (BOOL)_sectionUrl;
    _sectionUrl = sectionUrl;
    if (shouldUpdate) {
        [self updateComponent];
    }
}

- (void)setNativeAdTemplate:(NSString *)nativeAdTemplate {
    BOOL shouldUpdate = (BOOL)_nativeAdTemplate;
    _nativeAdTemplate = nativeAdTemplate;
    if (shouldUpdate) {
        [self updateComponent];
    }
}

- (void)setVideoAdTemplate:(NSString *)videoAdTemplate {
    BOOL shouldUpdate = (BOOL)_videoAdTemplate;
    _videoAdTemplate = videoAdTemplate;
    if (shouldUpdate) {
        [self updateComponent];
    }
}

- (void)setStdDisplayAdTemplate:(NSString *)stdDisplayAdTemplate {
    BOOL shouldUpdate = (BOOL)_stdDisplayAdTemplate;
    _stdDisplayAdTemplate = stdDisplayAdTemplate;
    if (shouldUpdate) {
        [self updateComponent];
    }
}

- (void)setExtraTemplateProps:(NSDictionary *)extraTemplateProps {
    BOOL shouldUpdate = (BOOL)_extraTemplateProps;
    _extraTemplateProps = extraTemplateProps;
    if (shouldUpdate) {
        [self updateComponent];
    }
}

- (void)didMoveToSuperview {
    [self updateComponent];
}

- (void)willMoveToSuperview:(UIView *)newSuperview {
    if (newSuperview == nil) {
        // In order to free memory when ad is removed
        [NtvSharedSectionDelegate clearAdViewAtLocationIdentifier:self.index forSectionUrl:self.sectionUrl];
    }
}

- (void)updateComponent {
    if (self.sectionUrl && self.index && self.superview != nil) {
        @try {
            // prefetch ad using index path to dequeue from ads array if available
            // injectWithAdData will be called in sectionDelegate method 'didReceiveAd'
            [NtvSharedSectionDelegate setAdView:self forSectionUrl:self.sectionUrl atLocationIdentifier:self.index];
            if (self.enableGAMVersion) {
                [NativoSDK enableGAMRequestsWithVersion:self.enableGAMVersion];
                NtvAdData *adData = [NativoSDK getCachedAdAtLocationIdentifier:self.index forSection:self.sectionUrl];
                if (adData) {
                    [self injectWithAdData:adData];
                }
            } else {
                [NativoSDK prefetchAdForSection:self.sectionUrl atLocationIdentifier:self.index options:nil];
            }
        } @catch (NSException *exception) {
            NSLog(@"Failed to inject NativoAd: %@", exception);
        }
    }
}

- (void)injectWithAdData:(NtvAdData *)adData {
    self.adData = adData;
    
    // Get main thread
    dispatch_async(dispatch_get_main_queue(), ^{
        UIView *templateView;
        if (adData.isAdContentAvailable) {
            BOOL isNativeTemplate = adData.adType == NtvAdTypeNative || adData.adType == NtvAdTypeDisplay || adData.adType == NtvAdTypeStory;
            BOOL isVideoTemplate = adData.adType == NtvAdTypeScrollToPlayVideo || adData.adType == NtvAdTypeClickToPlayVideo;
            BOOL isStdDisplayTemplate = adData.adType == NtvAdTypeStandardDisplay;
            
            if (isNativeTemplate && self.nativeAdTemplate) {
                NSDictionary *appProperties = [self getAppPropertiesFromAdData:adData withExtra:self.extraTemplateProps];
                templateView = [[NativeAdTemplate alloc] initWithBridge:self.bridge
                                                             moduleName:self.nativeAdTemplate
                                                      initialProperties:appProperties];
            } else if (isVideoTemplate && self.videoAdTemplate) {
                NSDictionary *appProperties = [self getAppPropertiesFromAdData:adData withExtra:self.extraTemplateProps];
                templateView = [[VideoAdTemplate alloc] initWithBridge:self.bridge
                                                             moduleName:self.videoAdTemplate
                                                      initialProperties:appProperties];
            }
            else if (isStdDisplayTemplate && self.stdDisplayAdTemplate) {
                NSMutableDictionary *appProperties = [@{@"displayHeight" : @(adData.standardDisplaySize.height),
                                                @"displayWidth" : @(adData.standardDisplaySize.width)} mutableCopy];
                if (self.extraTemplateProps && self.extraTemplateProps.allKeys.count > 0) {
                    [appProperties addEntriesFromDictionary:self.extraTemplateProps];
                }
                templateView = [[StandardDisplayAdTemplate alloc] initWithBridge:self.bridge
                                                                      moduleName:self.stdDisplayAdTemplate
                                                               initialProperties:appProperties];
            }
            else {
                NSLog(@"NativoSDK: No template given for ad type: %lu", (unsigned long)adData.adType);
                [self collapseView];
                self.onAdRemoved(@{ @"index": self.index, @"sectionUrl": self.sectionUrl });
                return;
            }

            // Inject template
            RCTRootView *rootTemplate = (RCTRootView *)templateView;
            rootTemplate.delegate = self;
            rootTemplate.sizeFlexibility = RCTRootViewSizeFlexibilityHeight;
            UIView *container = self.subviews.count > 0 ? self.subviews[0] : self;
            templateView.frame = container.bounds;
            [container addSubview:templateView];
            
        } else {
            // No fill
            templateView = self;
        }
        
        // In tests, placeAdInView() did not work without sublte timeout before injecting
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            // Place ad in view
            if (!self.superview) { return; } // View was removed by owner. Abort.
            if (adData.isAdContentAvailable) {
                [NativoSDK placeAdInView:templateView atLocationIdentifier:self.index inContainer:self.superview forSection:self.sectionUrl options:@{ @"doNotClearSection" : @"1"}];
                self.onAdRendered(@{ @"index": self.index, @"sectionUrl": self.sectionUrl });
            } else {
                [self collapseView];
                self.onAdRemoved(@{ @"index": self.index, @"sectionUrl": self.sectionUrl });
            }
        });
    });
}

- (NSMutableDictionary *)getAppPropertiesFromAdData:(NtvAdData *)adData withExtra:(NSDictionary *)extraProps {
    
    // Configure properties dictionary
    NSString *authorByLine = [NSString stringWithFormat:@"By %@", adData.authorName];
    NSMutableDictionary *appProperties = [@{@"adTitle" : adData.title,
                                            @"adDescription" : adData.previewText,
                                            @"adAuthorName" : authorByLine,
                                            @"adDate" : @(adData.date.timeIntervalSince1970 * 1000.0) } mutableCopy];
    
    // Set extra template props
    if (self.extraTemplateProps && self.extraTemplateProps.allKeys.count > 0) {
        [appProperties addEntriesFromDictionary:self.extraTemplateProps];
    }
    return appProperties;
}

- (void)collapseView {
    if (self.bridge) {
        RCTExecuteOnMainQueue(^{
            [self setHidden:YES];
            for (UIView* view in self.subviews) {
                [view setHidden:YES];
            }
            [self.bridge.uiManager setSize:CGSizeZero forView:self];
        });
    }
}

#pragma mark - RCTRootViewDelegate

- (void)rootViewDidChangeIntrinsicSize:(RCTRootView *)rootView {
    // Required to force size update on JS side
    CGRect newFrame = rootView.frame;
    newFrame.size = rootView.intrinsicContentSize;
    rootView.frame = newFrame;
    UIView *container = self.subviews.count > 0 ? self.subviews[0] : self;
    [self.bridge.uiManager setSize:rootView.intrinsicContentSize forView:container];
}

@end

@implementation NtvAdData (NtvUUID)
@dynamic adUUID;
- (NSUUID *)adUUID {
    NSUUID *_adUUID = objc_getAssociatedObject(self, @selector(adUUID));
    if (!_adUUID) {
        _adUUID = [NSUUID new];
        objc_setAssociatedObject(self, @selector(adUUID), _adUUID, OBJC_ASSOCIATION_RETAIN);
    }
    return _adUUID;
}
@end
