package gmbh.conteco.tweets;

public class Tweet {
    public Tweet(Long id, String language, String text) {
        this.id = id;
        this.language = language;
        this.text = text;
    }

    private Long id;

    private String language;
    private String text;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "Tweet{" +
                "id=" + id +
                ", language='" + language + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}
