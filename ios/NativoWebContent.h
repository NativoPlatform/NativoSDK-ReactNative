
#import <React/RCTViewManager.h>
#import <NativoSDK/NativoSDK.h>
#import "NativoLandingPageTemplate.h"


@interface NativoWebContentManager : RCTViewManager
@end

@interface NativoWebContentView : UIView
@property (nonatomic) NtvContentWebView *nativoWebView;
@property (nonatomic) NativoLandingPageTemplate *templateProxy;
@property (nonatomic) NSString *sectionUrl;
@property (nonatomic) NSNumber *index;
@property (nonatomic) BOOL *shouldScroll;
@property (nonatomic) RCTBubblingEventBlock onFinishLoading;
@property (nonatomic) RCTBubblingEventBlock onClickExternalLink;
@end
