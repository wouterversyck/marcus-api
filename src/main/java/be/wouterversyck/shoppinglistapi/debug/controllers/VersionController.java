package be.wouterversyck.shoppinglistapi.debug.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("public/version")
public class VersionController {

    @GetMapping
    public String versionEndpoint() {
        return getClass().getPackage().getImplementationVersion() + "testest";
    }
}
