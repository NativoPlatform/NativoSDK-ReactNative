//
//  NativoAdsUtils.h
//  ReactNativeNativoAds
//
//  Copyright © 2020 Nativo. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface NativoAdsUtils : NSObject

+ (UIView *)findClass:(Class)class inView:(UIView *)view;
+ (UIView *)findClass:(Class)class inSuperViews:(UIView *)view;

@end

NS_ASSUME_NONNULL_END
