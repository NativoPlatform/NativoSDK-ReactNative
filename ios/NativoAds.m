//
//  NativoAds.m
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

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
RCT_EXPORT_VIEW_PROPERTY(enableDFPVersion, NSString)
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
@property (nonatomic) NSString *enableDFPVersion;
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
        [NtvSharedSectionDelegate clearAdViewAtLocationIdentifier:self.index forSectionUrl:self.sectionUrl];
    }
}

- (void)updateComponent {
    if (self.sectionUrl && self.index && self.superview != nil) {
        @try {
            // prefetch ad using index path to dequeue from ads array if available
            // injectWithAdData will be called in sectionDelegate method 'didReceiveAd'
            [NtvSharedSectionDelegate setAdView:self forSectionUrl:self.sectionUrl atLocationIdentifier:self.index];
            if (self.enableDFPVersion) {
                [NativoSDK enableDFPRequestsWithVersion:self.enableDFPVersion];
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
            BOOL isNativeTemplate = adData.adType == Native || adData.adType == Display;
            BOOL isVideoTemplate = adData.adType == ScrollToPlayVideo || adData.adType == ClickToPlayVideo;
            BOOL isStdDisplayTemplate = adData.adType == StandardDisplay;
            NSString *authorByLine = [NSString stringWithFormat:@"By %@", adData.authorName];
            if (isNativeTemplate && self.nativeAdTemplate) {
                NSMutableDictionary *appProperties = [@{@"adTitle" : adData.title,
                                                @"adDescription" : adData.previewText,
                                                @"adAuthorName" : authorByLine,
                                                @"adDate" : adData.date } mutableCopy];
                if (self.extraTemplateProps && self.extraTemplateProps.allKeys.count > 0) {
                    [appProperties addEntriesFromDictionary:self.extraTemplateProps];
                }
                templateView = [[NativeAdTemplate alloc] initWithBridge:self.bridge
                                                             moduleName:self.nativeAdTemplate
                                                      initialProperties:appProperties];
            } else if (isVideoTemplate && self.videoAdTemplate) {
                NSMutableDictionary *appProperties = [@{@"adTitle" : adData.title,
                                                       @"adDescription" : adData.previewText,
                                                       @"adAuthorName" : authorByLine,
                                                       @"adDate" : adData.date } mutableCopy];
                if (self.extraTemplateProps && self.extraTemplateProps.allKeys.count > 0) {
                    [appProperties addEntriesFromDictionary:self.extraTemplateProps];
                }
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
            templateView.frame = self.bounds;
            [self addSubview:templateView];
            
            
        } else {
            // No fill
            templateView = self;
        }
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            // Place ad in view
            if (!self.superview) { return; } // View was removed by owner. Abort.
            [NativoSDK placeAdInView:templateView atLocationIdentifier:self.index inContainer:self.superview forSection:self.sectionUrl options:@{ @"doNotClearSection" : @"1"}];
            if (adData.isAdContentAvailable) {
                self.onAdRendered(@{ @"index": self.index, @"sectionUrl": self.sectionUrl });
            } else {
                [self collapseView];
                self.onAdRemoved(@{ @"index": self.index, @"sectionUrl": self.sectionUrl });
            }
        });
    });
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
    [self.bridge.uiManager setSize:rootView.intrinsicContentSize forView:self];
}

@end
