package org.example.bootreloadable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AppBean {
    private String key;
    private String secret;

    public void reload() {

    }
}
