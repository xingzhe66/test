package com.dcits.comet.gateway.property;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
//@ConfigurationProperties(prefix ="transfer.sanner")
@Setter
@Getter
public class CometYaml {

    private Gateway gateway;
    @Setter
    @Getter
    private class Gateway {
        private Transfer transfer;

        @Setter
        @Getter
        private class Transfer {
            private List<String> packageNames;
        }
    }
}
