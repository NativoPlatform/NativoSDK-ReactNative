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

#import "VideoAdTemplate.h"
#import <React/UIView+React.h>
#import <React/RCTUIManager.h>
@import AVFoundation;

@implementation VideoAdTemplate

- (UIView *)videoView {
    UIView *videoView = [self.bridge.uiManager viewForNativeID:@"videoView" withRootTag:self.reactTag];
    return videoView;
}

- (UIImageView *)authorImageView {
    UIView *authorImageView = [self.bridge.uiManager viewForNativeID:@"adAuthorImage" withRootTag:self.reactTag];
    if (authorImageView) {
        return (UIImageView *)authorImageView;
    }
    return nil;
}

- (UIView *)adChoicesIconView {
    UIView *adChoicesIconView = [self.bridge.uiManager viewForNativeID:@"adChoicesImage" withRootTag:self.reactTag];
    if (adChoicesIconView) {
        return (UIView *)adChoicesIconView;
    }
    return nil;
}

- (UILabel *)titleLabel {
    return nil;
}

- (UILabel *)authorNameLabel {
    return nil;
}

- (nonnull NSString *)videoFillMode {
    return AVLayerVideoGravityResizeAspectFill;
}


@end
