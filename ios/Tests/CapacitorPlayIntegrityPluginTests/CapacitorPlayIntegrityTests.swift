import XCTest
import Capacitor
@testable import CapacitorPlayIntegrityPlugin

class CapacitorPlayIntegrityTests: XCTestCase {

    // The template's echo test referred to a module and an API this plugin never had, so the target did not compile.
    func testRequestIntegrityTokenResolvesWithAnEmptyToken() {
        let plugin = CapacitorPlayIntegrityPlugin()
        var token: String?
        let call = CAPPluginCall(callbackId: "test", methodName: "requestIntegrityToken", options: ["nonce": "nonce"], success: { result, _ in
            token = result.data?["token"] as? String
        }, error: { _ in
            XCTFail("requestIntegrityToken should not reject on iOS")
        })

        plugin.requestIntegrityToken(call)

        XCTAssertEqual("", token)
    }
}
