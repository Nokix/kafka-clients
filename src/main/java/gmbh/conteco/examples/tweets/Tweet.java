package gmbh.conteco.examples.tweets;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tweet {
    private Long id;
    private String language;
    private String text;
}