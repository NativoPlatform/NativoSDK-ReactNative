//
//  NativoLandingPageTemplate.h
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <NativoSDK/NativoSDK.h>
#import <React/RCTView.h>
#import <React/RCTBridge.h>

NS_ASSUME_NONNULL_BEGIN

/*
 Used only to trick the NativoSDK into sending section delegate method 'shouldDisplayLandingPage'
 */
@interface NativoLandingPageTemplate : UIViewController <NtvLandingPageInterface>

@property (nonatomic) WKWebView *webView;
@property (nonatomic) BOOL shouldScroll;
@property (nonatomic) RCTBubblingEventBlock onFinishLoading;
@property (nonatomic) RCTBubblingEventBlock onClickExternalLink;
@property (nonatomic) NtvAdData *adData;

@end

NS_ASSUME_NONNULL_END
