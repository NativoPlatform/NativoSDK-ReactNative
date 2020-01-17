//
//  NativoAdsUtils.h
//  NativoAds
//
//  Created by Matthew Murray on 10/2/19.
//  Copyright © 2019 Facebook. All rights reserved.
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
