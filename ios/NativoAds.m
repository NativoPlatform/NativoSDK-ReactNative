//
//  NativoAds.m
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import "NativoAds.h"
#import "NativoAdsUtils.h"
#import "NativeAdTemplate.h"
#import "VideoAdTemplate.h"
#import "StandardDisplayAdTemplate.h"
#import "NtvSharedSectionDelegate.h"
#import "NativoLandingPageTemplate.h"
#import <React/UIView+React.h>
#import <React/RCTRootView.h>
#import <React/RCTRootViewDelegate.h>
#import <React/RCTDevLoadingView.h>
#import <React/RCTUIManager.h>


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
RCT_EXPORT_VIEW_PROPERTY(onAdRemoved, RCTBubblingEventBlock)


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
#if RCT_DEV
    [bridge moduleForClass:[RCTDevLoadingView class]];
#endif
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
@end

@implementation NativoAd

- (instancetype)init {
    self = [super init];
    return self;
}

- (void)didMoveToSuperview {
    if (self.sectionUrl && self.index && self.superview != nil) {
        @try {
            // prefetch ad using index path to dequeue from ads array if available
            // injectWithAdData will be called in sectionDelegate method 'didReceiveAd'
            [NtvSharedSectionDelegate setAdView:self forSectionUrl:self.sectionUrl atLocationIdentifier:self.index];
            [NativoSDK prefetchAdForSection:self.sectionUrl atLocationIdentifier:self.index options:nil];
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
                NSDictionary *appProperties = @{@"adTitle" : adData.title,
                                                @"adDescription" : adData.previewText,
                                                @"adAuthorName" : authorByLine,
                                                @"adDate" : adData.date };
                templateView = [[NativeAdTemplate alloc] initWithBridge:self.bridge
                                                             moduleName:self.nativeAdTemplate
                                                      initialProperties:appProperties];
            } else if (isVideoTemplate && self.videoAdTemplate) {
                NSDictionary *appProperties = @{@"adTitle" : adData.title,
                                                @"adDescription" : adData.previewText,
                                                @"adAuthorName" : authorByLine,
                                                @"adDate" : adData.date };
                templateView = [[VideoAdTemplate alloc] initWithBridge:self.bridge
                                                             moduleName:self.videoAdTemplate
                                                      initialProperties:appProperties];
            }
            else if (isStdDisplayTemplate && self.stdDisplayAdTemplate) {
                NSDictionary *appProperties = @{@"displayHeight" : @(adData.standardDisplaySize.height),
                                                @"displayWidth" : @(adData.standardDisplaySize.width)};
                templateView = [[StandardDisplayAdTemplate alloc] initWithBridge:self.bridge
                                                                      moduleName:self.stdDisplayAdTemplate
                                                               initialProperties:appProperties];
            }
            else {
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
            if (!adData.isAdContentAvailable) {
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
