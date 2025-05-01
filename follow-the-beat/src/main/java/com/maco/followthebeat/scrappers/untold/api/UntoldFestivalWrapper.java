package com.maco.followthebeat.scrappers.untold.api;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public class UntoldFestivalWrapper {
    private UntoldFestivalResponse artists; // artists is the actual payload

}
