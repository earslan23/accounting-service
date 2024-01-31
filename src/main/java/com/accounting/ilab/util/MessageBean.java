package com.accounting.ilab.util;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@Getter
@Setter
public class MessageBean {
    private Locale locale;
}
