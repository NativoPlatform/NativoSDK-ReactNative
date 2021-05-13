//
//  NtvSharedSectionDelegate.m
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import "NtvSharedSectionDelegate.h"
#import "NativoAds.h"
#import "NativoLandingPageTemplate.h"
#import <NativoSDK/NativoSDK.h>
#import <React/RCTLog.h>

@interface NtvSharedSectionDelegate ()
@property (nonatomic) NSMutableDictionary<NSString *, NSMutableDictionary<id, NativoAd*>*> *viewMap;
@property (nonatomic) NSMutableDictionary<NSString *, NSMutableArray<RCTResponseSenderBlock>*> *prefetchCallbackMap;
@end

@implementation NtvSharedSectionDelegate

+ (instancetype)sharedInstance {
    static dispatch_once_t once;
    static NtvSharedSectionDelegate *sharedDelegate;
    dispatch_once(&once, ^{
        sharedDelegate = [[NtvSharedSectionDelegate alloc] init];
        sharedDelegate.viewMap = [NSMutableDictionary dictionary];
        sharedDelegate.prefetchCallbackMap = [NSMutableDictionary dictionary];
        sharedDelegate.adIDMap = [NSMapTable mapTableWithKeyOptions:NSPointerFunctionsStrongMemory valueOptions:NSPointerFunctionsWeakMemory];
    });
    return sharedDelegate;
}

+ (void)setPrefetchCallback:(RCTResponseSenderBlock)senderBlock forSectionUrl:(NSString *)sectionUrl {
    if (senderBlock && sectionUrl) {
        NSMutableArray *sectionCallbacks = [NtvSharedSectionDelegate sharedInstance].prefetchCallbackMap[sectionUrl];
        if (!sectionCallbacks) {
            sectionCallbacks = [NSMutableArray array];
            [NtvSharedSectionDelegate sharedInstance].prefetchCallbackMap[sectionUrl] = sectionCallbacks;
        }
        [sectionCallbacks addObject:senderBlock];
    }
}

// Get NativoAd View
- (NativoAd *)getViewForAdData:(NtvAdData *)adData inSection:(NSString *)sectionUrl {
    NSMutableDictionary *sectionMap = [NtvSharedSectionDelegate sharedInstance].viewMap;
    NSDictionary *viewMap = sectionMap[sectionUrl];
    if (viewMap && adData) {
        NativoAd *adView = viewMap[adData.locationIdentifier];
        return adView;
    }
    return nil;
}

- (NativoAd *)getFirstViewInSection:(NSString *)sectionUrl {
    NSMutableDictionary *sectionMap = [NtvSharedSectionDelegate sharedInstance].viewMap;
    NSDictionary *viewMap = sectionMap[sectionUrl];
    for (id key in viewMap) {
        NativoAd *adView = viewMap[key];
        return adView;
    }
    return nil;
}

// Set NativoAd View
+ (void)setAdView:(NativoAd *)nativoAdView forSectionUrl:(NSString *)sectionUrl atLocationIdentifier:(id)locationId {
    [NativoSDK setSectionDelegate:[NtvSharedSectionDelegate sharedInstance] forSection:sectionUrl];
    NSMutableDictionary *sectionMap = [NtvSharedSectionDelegate sharedInstance].viewMap;
    if (sectionUrl && locationId) {
        NSMutableDictionary *viewMap = sectionMap[sectionUrl];
        if (viewMap) {
            viewMap[locationId] = nativoAdView;
        } else {
            viewMap = [@{ locationId : nativoAdView } mutableCopy];
            sectionMap[sectionUrl] = viewMap;
        }
    }
}

+ (void)clearAdViewAtLocationIdentifier:(id)locationId forSectionUrl:(NSString *)sectionUrl {
    NSMutableDictionary *sectionMap = [NtvSharedSectionDelegate sharedInstance].viewMap;
    if (sectionUrl && locationId) {
        NSMutableDictionary *viewMap = sectionMap[sectionUrl];
        [viewMap removeObjectForKey:locationId];
    }
}

- (void)section:(NSString *)sectionUrl needsPlaceAdInViewAtLocation:(id)identifier {
    
}

- (void)section:(NSString *)sectionUrl needsRemoveAdViewAtLocation:(id)identifier {
    NSMutableDictionary *sectionMap = [NtvSharedSectionDelegate sharedInstance].viewMap;
    NSDictionary *viewMap = sectionMap[sectionUrl];
    if (viewMap) {
        NativoAd *adView = viewMap[identifier];
        if (adView) {
            [adView collapseView];
            adView.onAdRemoved(@{ @"index": identifier, @"sectionUrl": sectionUrl });
        }
    }
}

- (void)section:(NSString *)sectionUrl needsDisplayLandingPage:(nullable UIViewController<NtvLandingPageInterface> *)sponsoredLandingPageViewController {
    NativoLandingPageTemplate *template = (NativoLandingPageTemplate *)sponsoredLandingPageViewController;
    NtvAdData *adData = template.adData;
    NativoAd *adView = [self getViewForAdData:adData inSection:sectionUrl];
    if (adView && adView.onNativeAdClick) {
        NSString *authorByLine = [NSString stringWithFormat:@"By %@", adData.authorName];
        NSMutableDictionary *props = [@{ @"adTitle" : adData.title,
                                         @"adDescription" : adData.previewText,
                                         @"adImgUrl" : adData.previewImageURL,
                                         @"adAuthorName" : authorByLine,
                                         @"adAuthorImgUrl" : adData.authorImageURL,
                                         @"adDate" : adData.date,
                                         @"adID" : adData.adUUID.UUIDString,
                                         @"index" : adData.locationIdentifier,
                                         @"sectionUrl" : sectionUrl } mutableCopy];
        NSString *shareUrl = nil;
        @try {
            shareUrl = [adData valueForKey:@"shareLink"];
        } @catch (NSException *exception) {}
        if (shareUrl) {
            props[@"adShareUrl"] = shareUrl;
        }
        adView.onNativeAdClick(props);
    }
}

- (void)section:(NSString *)sectionUrl needsDisplayClickoutURL:(NSURL *)url {
    NativoAd *adView = [self getFirstViewInSection:sectionUrl];
    if (adView && adView.onDisplayAdClick) {
        adView.onDisplayAdClick(@{ @"url" : url.absoluteString });
    }
}

- (void)section:(NSString *)sectionUrl didReceiveAd:(NtvAdData *)adData {
    [self.adIDMap setObject:adData forKey:adData.adUUID.UUIDString];
    NativoAd *adView = [self getViewForAdData:adData inSection:sectionUrl];
    if (adView) {
        [adView injectWithAdData:adData];
    }
    
    // Prefetch callback
    NSMutableArray *sectionCallbacks = [NtvSharedSectionDelegate sharedInstance].prefetchCallbackMap[sectionUrl];
    if (!adView && sectionCallbacks && sectionCallbacks.count > 0) {
        RCTResponseSenderBlock callback = sectionCallbacks[0];
        if (callback) {
            callback(@[[NSNull null], @(adData.isAdContentAvailable), sectionUrl]);
        }
        [sectionCallbacks removeObjectAtIndex:0];
    }
}

- (void)section:(NSString *)sectionUrl requestDidFailWithError:(nullable NSError *)error {
    id location = [error userInfo][@"locationId"];
    NativoAd *adView;
    if (location) {
        NSMutableDictionary *sectionMap = [NtvSharedSectionDelegate sharedInstance].viewMap;
        NSDictionary *viewMap = sectionMap[sectionUrl];
        if (viewMap) {
            adView = viewMap[location];
        }
    } else {
        adView = [self getFirstViewInSection:sectionUrl];
    }
    if (adView) {
        RCTLog(@"%@", error);
        if ([adView respondsToSelector:@selector(collapseView)]) {
            [adView collapseView];
        }
        if ([adView respondsToSelector:@selector(onAdRemoved)]) {
            adView.onAdRemoved(@{ @"index": location ? location : @(-1), @"sectionUrl": sectionUrl });
        }
    }
}

@end
