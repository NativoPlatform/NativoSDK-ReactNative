//
//  VideoAdTemplate.m
//  react-native-nativo-ads
//
//  Created by Matthew Murray on 11/11/19.
//

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
    UIView *authorImageView = [self.bridge.uiManager viewForNativeID:@"authorImage" withRootTag:self.reactTag];
    if (authorImageView) {
        return (UIImageView *)authorImageView;
    }
    return nil;
}

- (nonnull NSString *)videoFillMode {
    return AVLayerVideoGravityResizeAspectFill;
}


@end
