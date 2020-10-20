# NRWithChunkedResponses

The purpose of this project is to demonstrate how NewRelic, when using OkHttp3, doesn't properly upload response bodies.

See [this forum post](https://discuss.newrelic.com/t/chunked-responses-dont-get-recorded-with-okhttp3/118502) for more details

## Configuration

Before building, modify `configure.gradle` in the root project directory to include your NewRelic application token. This token will be injected into as `BuildConfig.NEW_RELIC_APPLICATION_TOKEN` at compile time.

## Reproduce Steps

0. Configure the project with your NewRelic application token
1. Build the app with `./gradlew assembleDebug` or through Android Studio 4.0+
2. Install the app on an emulator or physical device
3. Open the app and tap on either "Test with Mock Server" or "Test with Real Server"
3a. In Logcat, notice either option will return a response with header `Transfer-Encoding: chunked` and body `{"error":"invalid_request"}`
3b. In Logcat, notice that the NewRelic agent will report `com.newrelic.android: OkHttp3TransactionStateUtil: Missing body or content length`
4. Use your NewRelic Insights query builder with a statement like `FROM MobileRequestError SELECT device,requestUrl,responseBody`
4a. Locate your app, device, and requestUrl (either `localhost` or `signin.mindbodyonline.com` for this project)
4b. Notice the `responseBody` column is empty/null instead of `{"error":"invalid_request"}`