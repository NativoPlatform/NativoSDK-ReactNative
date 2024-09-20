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

#import <React/RCTViewManager.h>
#import <NativoSDK/NativoSDK.h>


@interface NativoAdManager : RCTViewManager
@end


@interface NativoAd: UIView

@property (nonatomic) RCTBridge *bridge;
@property (nonatomic) RCTBubblingEventBlock onDisplayAdClick;
@property (nonatomic) RCTBubblingEventBlock onNativeAdClick;
@property (nonatomic) RCTBubblingEventBlock onAdRendered;
@property (nonatomic) RCTBubblingEventBlock onAdRemoved;
- (void)injectWithAdData:(NtvAdData *)adData;
- (void)collapseView;

@end

@interface NtvAdData (NtvUUID)
@property (nonatomic, readonly) NSUUID *adUUID;
@end
