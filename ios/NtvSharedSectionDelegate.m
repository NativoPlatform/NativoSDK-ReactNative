//
//  RCTNtvSectionDelegate.m
//  NativoAds
//
//  Created by Matthew Murray on 10/23/19.
//  Copyright © 2019 Facebook. All rights reserved.
//

#import "NtvSharedSectionDelegate.h"
#import "NativoAds.h"
#import "NativoLandingPageTemplate.h"
#import <NativoSDK/NativoSDK.h>

@interface NtvSharedSectionDelegate ()
@property (nonatomic) NSMutableDictionary<NSString *, NSMutableDictionary<id, NativoAd*>*> *viewMap;
@end

@implementation NtvSharedSectionDelegate

+ (instancetype)sharedInstance {
    static dispatch_once_t once;
    static NtvSharedSectionDelegate *sharedDelegate;
    dispatch_once(&once, ^{
        sharedDelegate = [[NtvSharedSectionDelegate alloc] init];
        sharedDelegate.viewMap = [NSMutableDictionary dictionary];
    });
    return sharedDelegate;
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

- (void)section:(NSString *)sectionUrl needsReloadDatasourceAtLocationIdentifier:(id)identifier forReason:(NSString *)reason {
    NSLog(@"%@ %@", sectionUrl, reason);
    // TODO: call needsRemoveAd()
}

- (void)section:(NSString *)sectionUrl needsDisplayLandingPage:(nullable UIViewController<NtvLandingPageInterface> *)sponsoredLandingPageViewController {
    
    NSLog(@"%@ Attempting to display Nativo Landing Page", sectionUrl);
    NativoAd *adView = [self getFirstViewInSection:sectionUrl];
    NativoLandingPageTemplate *template = (NativoLandingPageTemplate *)sponsoredLandingPageViewController;
    NtvAdData *adData = template.adData;
    if (adView && adView.onNativeAdClick) {
        adView.onNativeAdClick(@{ @"title" : adData.title,
                                @"description" : adData.previewText,
                                @"authorName" : adData.authorName,
                                @"authorImgUrl" : adData.authorImageURL,
                                @"date" : adData.date,
                                @"index" : adData.locationIdentifier,
                                @"sectionUrl" : sectionUrl
        });
    }
}

- (void)section:(NSString *)sectionUrl needsDisplayClickoutURL:(NSURL *)url {
    NSLog(@"%@ Attempting to display Nativo Clickout URL: %@", sectionUrl, url);
    NativoAd *adView = [self getFirstViewInSection:sectionUrl];
    if (adView && adView.onDisplayAdClick) {
        adView.onDisplayAdClick(@{ @"url" : url.absoluteString });
    }
}

- (void)section:(NSString *)sectionUrl didReceiveAd:(NtvAdData *)adData {
    NSLog(@"%@ Did recieve Ad!", sectionUrl);
    NativoAd *adView = [self getViewForAdData:adData inSection:sectionUrl];
    if (adView) {
        [adView injectWithAdData:adData];
    }
}

- (void)section:(NSString *)sectionUrl requestDidFailWithError:(nullable NSError *)error {
    NSLog(@"%@ Ad did fail with error: %@", sectionUrl, error);
}

@end
