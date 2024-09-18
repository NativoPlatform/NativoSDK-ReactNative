/**
 * Copyright 2024 Nativo
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     https://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

#import "NativoAdsUtils.h"

@implementation NativoAdsUtils

+ (UIView *)findClass:(Class)class inView:(UIView *)view {
    //NSLog(@"View: %@ has #%lu subviews", view, (unsigned long)view.subviews.count);
    UIView *viewFound = nil;
    for (UIView *subView in view.subviews) {
        if ([subView isKindOfClass:class]) {
            viewFound = subView;
        } else {
            viewFound = [self findClass:class inView:subView];
        }
    }
    return viewFound;
}

+ (UIView *)findClass:(Class)class inSuperViews:(UIView *)view {
    UIView *viewFound = view;
    while (viewFound.superview != nil && ![viewFound.superview isKindOfClass:class]) {
        viewFound = viewFound.superview;
    }
    return viewFound.superview;
}

@end
