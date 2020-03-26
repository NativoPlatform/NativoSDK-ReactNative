//
//  NativoAdsUtils.h
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NativoAdsUtils : NSObject

+ (UIScrollView *)getParentScrollViewForView:(UIView *)view;
+ (UIView *)findTopMostViewBeforeScrollViewForView:(UIView *)view;
+ (UIView *)findClass:(Class)class inView:(UIView *)view;
+ (UIView *)findClass:(Class)class inSuperViews:(UIView *)view;
+ (BOOL)view:(UIView *)subview isSubviewOfView:(UIView *)parentView;

@end

NS_ASSUME_NONNULL_END
