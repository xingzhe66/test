package com.dcits.comet.gateway.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ConfigurationProperties(prefix ="transfer.sanner")
public class ScannerProperties {
    private List<String> packageNames;

    public List<String> getPackageNames() {
        return packageNames;
    }

    public void setPackageNames(List packageNames) {
        this.packageNames = packageNames;
    }
}
