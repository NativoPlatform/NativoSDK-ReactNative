require "json"

package = JSON.parse(File.read(File.join(__dir__, "package.json")))

Pod::Spec.new do |s|
  s.name         = "react-native-nativo-ads"
  s.version      = package["version"]
  s.summary      = package["description"]
  s.description  = "This will allow you to monetize your app using Nativo's native, display, and video ads."
  s.homepage     = "https://github.com/github_account/react-native-nativo-ads"
  s.license      = "MIT"
  # s.license    = { :type => "MIT", :file => "FILE_LICENSE" }
  s.authors      = { "Nativo" => "sdksupport@nativo.com" }
  s.platforms    = { :ios => "9.0", :tvos => "10.0" }
  s.source       = { :git => "https://github.com/github_account/react-native-nativo-ads.git", :tag => "#{s.version}" }

  s.source_files = "ios/**/*.{h,m,swift}"
  s.requires_arc = true

  s.dependency "React"
  s.dependency "NativoSDK"
end
