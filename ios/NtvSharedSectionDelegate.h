//
//  NtvSharedSectionDelegate.h
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <NativoSDK/NativoSDK.h>
#import <React/RCTBridgeModule.h>
#import "NativoAds.h"

NS_ASSUME_NONNULL_BEGIN

@interface NtvSharedSectionDelegate : NSObject <NtvSectionDelegate>

+ (instancetype)sharedInstance;
+ (void)setAdView:(NativoAd *)nativoAdView forSectionUrl:(NSString *)sectionUrl atLocationIdentifier:(id)locationId;
+ (void)clearAdViewAtLocationIdentifier:(id)locationId forSectionUrl:(NSString *)sectionUrl;
+ (void)setPrefetchCallback:(RCTResponseSenderBlock)senderBlock forSectionUrl:(NSString *)sectionUrl;

@property (nonatomic) NSMapTable<NSString *, NtvAdData *> *adIDMap;

@end

NS_ASSUME_NONNULL_END
